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

/**
 * Maintain the movie database and provide common operations on movies
 */
public class MovieRepository implements DatabaseAccess, Subject {

    /**
     * Maintains the current list of movies in the system
     */
    private final Map<Movie, Integer> movieDatabase;

    /**
     * Barcode to movie mapping
     */
    private final Map<String, Movie> barcodeToMovieMap;

    /**
     * Singleton instance
     */
    private volatile static MovieRepository movieRepositoryInstance;

    private final List<Observer> observers;

    /**
     * Configurations for the csv file
     */
    private static final String MOVIE_FILE_PATH = "/src/main/resources/movies.csv";
    private static final String path = System.getProperty("user.dir") + MOVIE_FILE_PATH;

    /**
     * Construct a MovieRepository class
     */
    private MovieRepository() {
        movieDatabase = new HashMap<>();
        barcodeToMovieMap = new HashMap<>();
        observers = new ArrayList<>();
        loadCSV();
    }

    /**
     * Return the singleton instance of this class
     * @return the single MovieRepository
     */
    public static MovieRepository getInstance() {
        if (movieRepositoryInstance == null) {
            synchronized (MovieRepository.class) {
                if (movieRepositoryInstance == null) {
                    movieRepositoryInstance = new MovieRepository();
                }
            }
        }
        return movieRepositoryInstance;
    }

    @Override
    public void loadCSV() {
        try {
            CSVParser parser = new CSVParser(new FileReader(MovieRepository.path), CSVFormat.RFC4180
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
    public void updateCSV() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(path, false),
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

    /**
     * Add a movie to the database
     * @param movie the movie to add
     * @return {@code true} if the movie was added successfully, {@code false} otherwise
     */
    public boolean addMovie(Movie movie, Integer quantity) {
        if (validateMovieAdd(movie)) {
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

    /**
     * Delete movies from the database
     * @param barcodes the ids of movies to delete
     */
    public void deleteMovies(List<String> barcodes) {
        for (String barcode : barcodes) {
            Movie m = barcodeToMovieMap.get(barcode);
            movieDatabase.remove(m);
        }
        updateCSV();
    }

    /**
     * Get all movies with matching titles from the database
     * @param movieTitle the title to search
     * @return a list of movies matching the title search
     */
    public Map<Movie,Integer> findMovieByTitle(String movieTitle) {
        Map<Movie,Integer> titleMatches = new HashMap<>();
        for (Map.Entry<Movie, Integer> entry : movieDatabase.entrySet()) {
            Movie m = entry.getKey();
            if (m.getTitle().toLowerCase().matches(".*" + movieTitle.toLowerCase() + ".*")) {
                titleMatches.put(entry.getKey(), entry.getValue());
            }
        }
        return titleMatches;
    }

    /**
     * @return a list of all movies in the database
     */
    public Map<Movie,Integer> getAllMovies() {
        return movieDatabase;
    }

    /**
     * Get all movies with matching category from the database
     * @param genre the genre to match
     * @return a list of movies matching the category search
     */
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

    /**
     * Get movie by barcode
     * @param barcode the barcode id of the movie
     * @return the movie
     */
    public Movie getMovie(String barcode) {
        return barcodeToMovieMap.get(barcode);
    }

    /**
     * Given a map of movies and quantities to rent, update the database accordingly
     * @param movies the movies to rent
     * @return true if successful, false otherwise
     */
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

    /**
     * return a single movie to the database given the barcode of the movie
     * @param barcode the barcode of movie to return
     */
    public void returnMovie(String barcode) {
        Movie m = barcodeToMovieMap.get(barcode);
        movieDatabase.replace(m, movieDatabase.get(m) + 1);
        updateCSV();
    }

    /**
     * Remove stock for a particular movie
     * @param barcode the id of the movie to remove stock
     */
    public void removeStock(String barcode) {
        Movie m = barcodeToMovieMap.get(barcode);
        if (movieDatabase.get(m) <= 0) {
            return;
        } else {
            movieDatabase.replace(m, movieDatabase.get(m)-1);
        }
        updateCSV();
    }

    /**
     * Update a movie in the database
     * @param movie the movie to update
     * @return true if successful, false otherwise
     */
    public boolean updateMovie(Movie movie) {
        if (validateMovieUpdate(movie)) {
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

    private boolean validateMovieAdd(Movie m) {
        if (barcodeToMovieMap.containsKey(m.getBarcode())) {
            return false;
        } else {
            return validateMovieUpdate(m);
        }
    }

    private boolean validateMovieUpdate(Movie m) {
        return !m.getTitle().equals("") && m.getPrice() >= 0.00D;
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
}
