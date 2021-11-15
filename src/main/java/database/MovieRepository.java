package database;

import model.Movie;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    public List<Movie> movies = new ArrayList<>();
    private static final String MOVIE_FILE_PATH = "/src/main/resources/movies.csv";
    private static final String path = System.getProperty("user.dir") + MOVIE_FILE_PATH;

    public MovieRepository() {
        load(path);
    }

    public void load (String path) {
        try {
            CSVParser parser = new CSVParser(new FileReader(path), CSVFormat.RFC4180
                    .withDelimiter(',')
                    .withHeader("barcode", "title", "description", "genre", "releaseDate",
                            "quantity", "cost"));
            List<CSVRecord> records = parser.getRecords();
            for (int i = 1; i < records.size(); i++) {
                Movie movie = new Movie();
                movie.setBarcode(Integer.parseInt(records.get(i).get("barcode")));
                movie.setTitle(records.get(i).get("title"));
                movie.setDescription(records.get(i).get("description"));
                movie.setCost(Double.parseDouble(records.get(i).get("cost")));
                movie.setQuantity(Integer.parseInt(records.get(i).get("quantity")));
                movie.setReleaseDate(Date.valueOf(records.get(i).get("releaseDate")));
                movie.setGenre(records.get(i).get("genre"));
                movies.add(movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(path, false),
                CSVFormat.RFC4180
                        .withDelimiter(',')
                        .withHeader(
                                "barcode",
                                "title",
                                "description",
                                "genre",
                                "releaseDate",
                                "quantity",
                                "cost"
                        ))) {
            for (Movie m : movies) {
                printer.printRecord(m.getBarcode(), m.getTitle(), m.getDescription(), m.getGenre(), m.getReleaseDate(),
                        m.getQuantity(), m.getCost());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Movie> findMovieByTitle(String movieTitle) {
        List<Movie> titleMatches = new ArrayList<>();
        for (Movie m : movies) {
            if (m.getTitle().equals(movieTitle)) {
                titleMatches.add(m);
            }
        }
        return titleMatches;
    }

    public List<Movie> getMoviesByCategory(String genre) {
        List<Movie> genreMatches = new ArrayList<>();
        for (Movie m : movies) {
            if (m.getGenre().equals(genre)) {
                genreMatches.add(m);
            }
        }
        return genreMatches;
    }

    public List<Movie> getAllMovies() {
        return this.movies;
    }

}
