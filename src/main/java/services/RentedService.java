package services;

import database.RentedRepository;
import model.Movie;
import model.Order;
import model.RentedMovie;

import java.util.List;

public class RentedService {

    private final RentedRepository rentedRepository;

    private static String RENTED_CSV_PATH;

    private volatile static RentedService instance;

    public RentedService() {
        rentedRepository = RentedRepository.getInstance(RENTED_CSV_PATH);
    }

    public static RentedService getInstance() {
        if (instance == null) {
            synchronized (RentedService.class) {
                if (instance == null) {
                    instance = new RentedService();
                }
            }
        }
        return instance;
    }

    public static void setCsvPath(String path) {
        RENTED_CSV_PATH = path;
    }

    public int countMoviesInOrder(int orderNumber) {
        return rentedRepository.countMoviesInOrder(orderNumber);
    }

    public List<RentedMovie> getAllRentedMovies() {
        return rentedRepository.getAllRentedMovies();
    }

    public double getOrderTotal(int orderNumber) {
        double total = 0;
        for (RentedMovie r : rentedRepository.getAllRentedMovies()) {
            if (orderNumber == r.getOrderId()) {
                String barcode = r.getBarcode();
                Movie m = getMovieService().getMovie(barcode);
                total += m.getPrice();
            }
        }
        return total;
    }

    public void storeMovies(Order order) {
        rentedRepository.storeMovies(order);
    }

    public void returnMovies(int orderNumber) {
        List<RentedMovie> rentedMovies = getAllRentedMovies();
        for (RentedMovie movie : rentedMovies) {
            if (movie.getOrderId() == orderNumber) {
                getMovieService().returnMovie(movie.getBarcode());
            }
        }
        rentedRepository.deleteRentedMoviesFromOrder(orderNumber);
    }


    private MovieService getMovieService() {
        return MovieService.getInstance();
    }
}
