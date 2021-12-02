package database;

import model.Movie;
import model.RentedMovie;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Maintains the repository of rented movies
 */
public class RentedRepository implements DatabaseAccess {

    /**
     * the database of movies out for rent
     */
    private final List<RentedMovie> rentedMovies;

    /**
     * the movie repository
     */
    private final MovieRepository movieRepository;

    /**
     * singleton instance
     */
    private static RentedRepository rentedRepositoryInstance = null;

    /**
     * configs
     */
    private static final String RENTED_FILE_PATH = "/src/main/resources/rented.csv";
    private static final String rentedPath = System.getProperty("user.dir") + RENTED_FILE_PATH;

    private RentedRepository() {
        rentedMovies = new ArrayList<>();
        movieRepository = MovieRepository.getInstance();
        loadCSV();
    }

    /**
     * @return get the singleton instance of this class
     */
    public static RentedRepository getInstance() {
        if (rentedRepositoryInstance == null) {
            rentedRepositoryInstance = new RentedRepository();
        }
        return rentedRepositoryInstance;
    }

    @Override
    public void updateCSV() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(rentedPath, false),
                CSVFormat.RFC4180.withDelimiter(',')
                        .withHeader("orderNumber",
                                "barcode"
                        ))) {
            for (RentedMovie r : rentedMovies) {
                printer.printRecord(r.getOrderId(), r.getBarcode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadCSV() {
        try {
            CSVParser parser = new CSVParser(new FileReader(RentedRepository.rentedPath), CSVFormat.RFC4180
                    .withDelimiter(',')
                    .withHeader(
                            "orderNumber",
                            "barcode"
                    ));
            List<CSVRecord> records = parser.getRecords();
            for (int i = 1; i < records.size(); i++) {
                int orderNo = Integer.parseInt(records.get(i).get("orderNumber"));
                String barcode = records.get(i).get("barcode");
                rentedMovies.add(new RentedMovie(orderNo, barcode));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * store the rented movies in this database
     * @param orderNumber the order number to associate with the rented movies
     * @param movies the movies to rent
     */
    public void storeMovies(int orderNumber, Map<Movie,Integer> movies) {
        for (Map.Entry<Movie,Integer> entry : movies.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                rentedMovies.add(new RentedMovie(orderNumber, entry.getKey().getBarcode()));
            }
        }
        updateCSV();
    }

    /**
     * return the movies to the movie repository
     * @param orderNumber the order number to return
     */
    public void returnMovies(int orderNumber) {
        for (RentedMovie movie : rentedMovies) {
            if (movie.getOrderId() == orderNumber) {
                movieRepository.returnMovie(movie.getBarcode());
            }
        }
        rentedMovies.removeIf(r -> r.getOrderId() == orderNumber);
        updateCSV();
    }

    public int countMoviesInOrder(int orderNumber) {
        int count = 0;
        for (RentedMovie r : rentedMovies) {
            if (r.getOrderId() == orderNumber) {
                count++;
            }
        }
        return count;
    }

}
