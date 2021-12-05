package database;

import model.Movie;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MovieRepository implements DatabaseAccess, Subject {

    private final Map<Movie, Integer> movieDatabase;

    private final Map<String, Movie> barcodeToMovieMap;

    private volatile static MovieRepository movieRepositoryInstance;

    private final List<Observer> observers;

    private final String MOVIE_CSV_PATH;

    private MovieRepository(String path) {
        MOVIE_CSV_PATH = path;
        clearCSV();
        movieDatabase = new HashMap<>();
        barcodeToMovieMap = new HashMap<>();
        observers = new ArrayList<>();
        loadCSV();
    }

    public static MovieRepository getInstance(String path) {
        if (movieRepositoryInstance == null) {
            synchronized (MovieRepository.class) {
                if (movieRepositoryInstance == null) {
                    movieRepositoryInstance = new MovieRepository(path);
                }
            }
        }
        return movieRepositoryInstance;
    }

    @Override
    public synchronized void loadCSV() {
        try {
            CSVParser parser = new CSVParser(new FileReader(MOVIE_CSV_PATH), CSVFormat.RFC4180
                    .withDelimiter(',')
                    .withHeader("barcode", "title", "genre", "releaseDate",
                            "quantity", "cost"));
            List<CSVRecord> records = parser.getRecords();
            for (int i = 1; i < records.size(); i++) {
                int quantity = Integer.parseInt(records.get(i).get("quantity"));
                Movie movie = new Movie();
                movie.setBarcode(records.get(i).get("barcode"));
                movie.setTitle(records.get(i).get("title"));
                movie.setPrice(Double.parseDouble(records.get(i).get("cost")));
                movie.setReleaseDate(records.get(i).get("releaseDate"));
                movie.setGenre(records.get(i).get("genre"));
                movieDatabase.put(movie, quantity);
                barcodeToMovieMap.put(movie.getBarcode(), movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void updateCSV() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(MOVIE_CSV_PATH, false),
                CSVFormat.RFC4180
                        .withDelimiter(',')
                        .withHeader(
                                "barcode",
                                "title",
                                "genre",
                                "releaseDate",
                                "quantity",
                                "cost"
                        ))) {
            for (Map.Entry<Movie,Integer> entry : movieDatabase.entrySet()) {
                Movie movie = entry.getKey();
                printer.printRecord(movie.getBarcode(), movie.getTitle(), movie.getGenre(), movie.getReleaseDate(), entry.getValue(), movie.getPrice());
            }
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void clearCSV() {
        try {
            FileWriter fw = new FileWriter(MOVIE_CSV_PATH, false);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean addMovie(Movie movie, Integer quantity) {
        if (validateMovie(movie)) {
            if (movieDatabase.containsKey(movie)) {
                movieDatabase.replace(movie,movieDatabase.get(movie)+quantity);
            } else {
                movieDatabase.put(movie, quantity);
                barcodeToMovieMap.put(movie.getBarcode(), movie);
            }
            updateCSV();
            return true;
        } else {
            return false;
        }
    }

    public void deleteMovie(String barcode) {
        Movie m = barcodeToMovieMap.get(barcode);
        barcodeToMovieMap.remove(barcode);
        movieDatabase.remove(m);
        updateCSV();
    }

    public Map<Movie,Integer> getMovieByTitle(String movieTitle) {
        Map<Movie,Integer> titleMatches = new HashMap<>();
        for (Map.Entry<Movie, Integer> entry : movieDatabase.entrySet()) {
            Movie m = entry.getKey();
            if (m.getTitle().toLowerCase().matches(".*" + movieTitle.toLowerCase() + ".*")) {
                titleMatches.put(entry.getKey(), entry.getValue());
            }
        }
        return titleMatches;
    }

    public Map<Movie,Integer> getAllMovies() {
        return movieDatabase;
    }

    public Map<Movie,Integer> getMoviesByCategory(String genre) {
        Map<Movie,Integer> genreMatches = new HashMap<>();
        for (Map.Entry<Movie,Integer> entry : movieDatabase.entrySet()) {
            Movie m = entry.getKey();
            if (m.getGenre().equalsIgnoreCase(genre)) {
                genreMatches.put(entry.getKey(), entry.getValue());
            }
        }
        return genreMatches;
    }

    public Movie getMovie(String barcode) {
        return barcodeToMovieMap.get(barcode);
    }

    public int getStockForMovie(String barcode) {
        Movie m = barcodeToMovieMap.get(barcode);
        if (m == null) {
            return 0;
        } else {
            return movieDatabase.get(m);
        }
    }

    public boolean rentMovies(Map<Movie,Integer> movies) {
        for (Map.Entry<Movie,Integer> entry : movies.entrySet()) {
            if (movieDatabase.containsKey(entry.getKey())) {
                if (entry.getValue() <= movieDatabase.get(entry.getKey())) {
                    movieDatabase.replace(entry.getKey(), movieDatabase.get(entry.getKey()) - entry.getValue());
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        updateCSV();
        return true;
    }

    public void returnMovie(String barcode) {
        Movie m = barcodeToMovieMap.get(barcode);
        movieDatabase.replace(m, movieDatabase.get(m) + 1);
        updateCSV();
    }

    public void removeStock(String barcode) {
        Movie m = barcodeToMovieMap.get(barcode);
        if (movieDatabase.get(m) <= 0) {
            return;
        } else {
            movieDatabase.replace(m, movieDatabase.get(m)-1);
        }
        updateCSV();
    }

    public boolean updateMovie(Movie movie) {
        if (validateMovie(movie) && barcodeToMovieMap.containsKey(movie.getBarcode())) {
            Movie oldMovie = barcodeToMovieMap.get(movie.getBarcode());
            Integer oldMovieQuantity = movieDatabase.get(oldMovie);
            movieDatabase.remove(oldMovie);
            movieDatabase.put(movie, oldMovieQuantity);
            barcodeToMovieMap.replace(movie.getBarcode(), movie);
            updateCSV();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

    private boolean validateMovie(Movie m) {
        if (m == null) {
            return false;
        } else {
            if (m.getBarcode() != null && m.getTitle() != null && m.getPrice() >= 0 && m.getGenre() != null && m.getReleaseDate() != null) {
                return !m.getGenre().equals("") && !m.getTitle().equals("") && !m.getBarcode().equals("") && !m.getReleaseDate().equals("");
            } else {
                return false;
            }
        }
    }
}
