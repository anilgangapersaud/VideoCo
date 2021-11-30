package database;

import model.movie.Movie;
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
public class MovieRepository {

    /**
     * Maintains the current list of movies in the system
     */
    private final Map<Movie, Integer> movieDatabase;

    /**
     * Configurations for the csv file
     */
    private static final String MOVIE_FILE_PATH = "/src/main/resources/movies.csv";
    private static final String path = System.getProperty("user.dir") + MOVIE_FILE_PATH;

    /**
     * Construct a MovieRepository class
     */
    public MovieRepository() {
        movieDatabase = new HashMap<>();
        load();
    }

    /**
     * Load the data from csv to list and map data structures
     */
    private void load() {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the data in the csv file
     */
    public void update() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * Get all movies with matching category from the database
     * @param genre the genre to match
     * @return a list of movies matching the category search
     */
    public Map<Movie,Integer> getMoviesByCategory(String genre) {
        Map<Movie,Integer> genreMatches = new HashMap<>();
        for (Map.Entry<Movie,Integer> entry : movieDatabase.entrySet()) {
            Movie m = entry.getKey();
            if (m.getGenre().equals(genre)) {
                genreMatches.put(entry.getKey(), entry.getValue());
            }
        }
        return genreMatches;
    }

    /**
     * @return a list of all movies in the database
     */
    public Map<Movie,Integer> getAllMovies() {
        return new HashMap<>(movieDatabase);
    }

    /**
     * Add a movie to the database
     * @param movie the movie to add
     * @return {@code true} if the movie was added successfully, {@code false} otherwise
     */
    public boolean addMovie(Movie movie) {
        if (validateMovie(movie)) {
            if (movieDatabase.containsKey(movie)) {
                movieDatabase.replace(movie,movieDatabase.get(movie)+1);
            } else {
                movieDatabase.put(movie, 1);
            }
            update();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Validates the movie object
     */
    private boolean validateMovie(Movie m) {
        return m.getBarcode() != null && !m.getBarcode().equals("") && m.getPrice() >= 0.00D;
    }
}