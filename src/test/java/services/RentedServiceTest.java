package services;

import database.TestConfigs;
import model.Movie;
import model.Order;
import model.RentedMovie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RentedServiceTest {

    private RentedService rentedService;
    private MovieService movieService;

    @BeforeEach
    void setup() {
        MovieService.setCsvPath(TestConfigs.MOVIE_CSV_TEST_PATH);
        movieService = MovieService.getInstance();

        RentedService.setCsvPath(TestConfigs.RENTED_CSV_TEST_PATH);
        rentedService = RentedService.getInstance();
    }

    @Test
    void countMoviesInOrder() {
        rentedService.countMoviesInOrder(23);
    }

    @Test
    void getAllRentedMovies() {
        List<RentedMovie> rentedMovieList = rentedService.getAllRentedMovies();
        assertThat(rentedMovieList.size()).isEqualTo(0);
    }

    @Test
    void getOrderTotal() {
        Movie m = new Movie();
        m.setGenre("kids");
        m.setReleaseDate("01/0/1");
        m.setPrice(20);
        m.setTitle("Pokemon");
        m.setBarcode("123");

        movieService.addMovie(m,6);

        Map<Movie,Integer> map = new HashMap<>();

        map.put(m,6);

        Order o = new Order();
        o.setOverdue(false);
        o.setMovies(map);
        o.setDueDate("01/1/01");
        o.setOrderStatus("DELIVERED");
        o.setOrderDate("90/1313/");
        o.setOrderId(1);

        rentedService.storeMovies(o);

        assertThat(rentedService.getOrderTotal(1)).isEqualTo(20*6);
    }

    @Test
    void storeMovies() {
        Movie m = new Movie();
        m.setGenre("kids");
        m.setReleaseDate("01/0/1");
        m.setPrice(20);
        m.setTitle("Pokemon");
        m.setBarcode("123");

        movieService.addMovie(m,6);

        Map<Movie,Integer> map = new HashMap<>();

        map.put(m,6);

        Order o = new Order();
        o.setOverdue(false);
        o.setMovies(map);
        o.setDueDate("01/1/01");
        o.setOrderStatus("DELIVERED");
        o.setOrderDate("90/1313/");

        rentedService.storeMovies(o);
    }

    @Test
    void returnMovies() {
        Movie m = new Movie();
        m.setGenre("kids");
        m.setReleaseDate("01/0/1");
        m.setPrice(20);
        m.setTitle("Pokemon");
        m.setBarcode("123");

        movieService.addMovie(m,6);

        Map<Movie,Integer> map = new HashMap<>();

        map.put(m,6);

        Order o = new Order();
        o.setOverdue(false);
        o.setMovies(map);
        o.setDueDate("01/1/01");
        o.setOrderStatus("DELIVERED");
        o.setOrderDate("90/1313/");
        o.setOrderId(1);

        rentedService.storeMovies(o);

        rentedService.returnMovies(1);
    }
}