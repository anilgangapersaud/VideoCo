package database;

import model.Movie;
import model.Order;
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

public class RentedRepository implements DatabaseAccess {

    private final List<RentedMovie> rentedMovies;

    private volatile static RentedRepository rentedRepositoryInstance;

    private final String RENTED_CSV_PATH;

    private RentedRepository(String path) {
        RENTED_CSV_PATH = path;
        clearCSV();
        rentedMovies = new ArrayList<>();
        loadCSV();
    }

    public static RentedRepository getInstance(String path) {
        if (rentedRepositoryInstance == null) {
            synchronized (RentedRepository.class) {
                if (rentedRepositoryInstance == null) {
                    rentedRepositoryInstance = new RentedRepository(path);
                }
            }
        }
        return rentedRepositoryInstance;
    }

    @Override
    public synchronized void updateCSV() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(RENTED_CSV_PATH, false),
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
    public synchronized void loadCSV() {
        try {
            CSVParser parser = new CSVParser(new FileReader(RENTED_CSV_PATH), CSVFormat.RFC4180
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

    @Override
    public synchronized void clearCSV() {
        try {
            FileWriter fw = new FileWriter(RENTED_CSV_PATH, false);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void storeMovies(Order order) {
        for (Map.Entry<Movie,Integer> entry : order.getMovies().entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                rentedMovies.add(new RentedMovie(order.getOrderId(), entry.getKey().getBarcode()));
            }
        }
        updateCSV();
    }

    public List<RentedMovie> getAllRentedMovies() {
        return rentedMovies;
    }

    public void deleteRentedMoviesFromOrder(int orderNumber) {
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
