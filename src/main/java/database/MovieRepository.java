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
    private final List<Movie> movies;

    /**
     * A quick way to check if a movie exists in the system by barcode mapping
     */
    private final Map<String, Movie> movieMap;

    /**
     * Configurations for the csv file
     */
    private static final String MOVIE_FILE_PATH = "/src/main/resources/movies.csv";
    private static final String path = System.getProperty("user.dir") + MOVIE_FILE_PATH;

    /**
     * Construct a MovieRepository class
     */
    public MovieRepository() {
        movies = new ArrayList<>();
        movieMap = new HashMap<>();
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
                Movie movie = new Movie();
                movie.setBarcode(records.get(i).get("barcode"));
                movie.setTitle(records.get(i).get("title"));
                movie.setCost(Double.parseDouble(records.get(i).get("cost")));
                movie.setQuantity(Integer.parseInt(records.get(i).get("quantity")));
                movie.setReleaseDate(records.get(i).get("releaseDate"));
                movie.setGenre(records.get(i).get("genre"));
                movies.add(movie);
                movieMap.put(movie.getBarcode(), movie);
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
            for (Movie m : movies) {
                printer.printRecord(m.getBarcode(), m.getTitle(), m.getGenre(), m.getReleaseDate(),
                        m.getQuantity(), m.getCost());
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
    public List<Movie> findMovieByTitle(String movieTitle) {
        List<Movie> titleMatches = new ArrayList<>();
        for (Movie m : movies) {
            if (m.getTitle().toLowerCase().matches(".*" + movieTitle.toLowerCase() + ".*")) {
                titleMatches.add(m);
            }
        }
        return titleMatches;
    }

    /**
     * Get all movies with matching category from the database
     * @param genre the genre to match
     * @return a list of movies matching the category search
     */
    public List<Movie> getMoviesByCategory(String genre) {
        List<Movie> genreMatches = new ArrayList<>();
        for (Movie m : movies) {
            if (m.getGenre().equals(genre)) {
                genreMatches.add(m);
            }
        }
        return genreMatches;
    }

    /**
     * @return a list of all movies in the database
     */
    public List<Movie> getAllMovies() {
        return this.movies;
    }

    /**
     * Add a movie to the database
     * @param movie the movie to add
     * @return {@code true} if the movie was added successfully, {@code false} otherwise
     */
    public boolean addMovie(Movie movie) {
        if (validateMovie(movie)) {
            movies.add(movie);
            movieMap.put(movie.getBarcode(), movie);
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
        return m.getBarcode() != null && !m.getBarcode().equals("") && m.getQuantity() >= 0 && m.getCost() >= 0.00D;
    }
}
