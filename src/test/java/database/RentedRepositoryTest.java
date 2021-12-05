package database;

import model.Movie;
import model.Order;
import model.RentedMovie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class RentedRepositoryTest {

    private static RentedRepository underTest;

    @BeforeAll
    static void setup2() {
        underTest = RentedRepository.getInstance(TestConfigs.RENTED_CSV_TEST_PATH);
        underTest.clearCSV();
    }

    @AfterEach
    void teardown(){
        underTest.clearCSV();
    }

    @Test
    void storeMovies() {
        Movie m = new Movie();
        m.setReleaseDate("13/131/3");
        m.setGenre("Kids");
        m.setPrice(2);
        m.setTitle("Pokemon");
        m.setBarcode("21");

        Map<Movie,Integer> movies = new HashMap<>();
        movies.put(m,5);

        Order order = new Order();
        order.setMovies(movies);
        order.setOrderStatus("DELIVERED");
        order.setOrderDate("/121/21");
        order.setOrderId(23);
        order.setUsername("username");
        order.setDueDate("11/2214/1");
        order.setOverdue(false);

        underTest.storeMovies(order);
    }

    @Test
    void getAllRentedMovies() {
        Movie m = new Movie();
        m.setReleaseDate("13/131/3");
        m.setGenre("Kids");
        m.setPrice(2);
        m.setTitle("Pokemon");
        m.setBarcode("21");

        Map<Movie,Integer> movies = new HashMap<>();
        movies.put(m,5);

        Order order = new Order();
        order.setMovies(movies);
        order.setOrderStatus("DELIVERED");
        order.setOrderDate("/121/21");
        order.setOrderId(23);
        order.setUsername("username");
        order.setDueDate("11/2214/1");
        order.setOverdue(false);

        underTest.storeMovies(order);

        List<RentedMovie> expected = new ArrayList<>();
        expected.add(new RentedMovie(23,"21"));
        expected.add(new RentedMovie(23, "21"));
        expected.add(new RentedMovie(23, "21"));
        expected.add(new RentedMovie(23, "21"));
        expected.add(new RentedMovie(23, "21"));


        assertThat(underTest.getAllRentedMovies()).isEqualTo(expected);
    }

    @Test
    void countMoviesInOrder() {
        Movie m = new Movie();
        m.setReleaseDate("13/131/3");
        m.setGenre("Kids");
        m.setPrice(2);
        m.setTitle("Pokemon");
        m.setBarcode("21");

        Map<Movie,Integer> movies = new HashMap<>();
        movies.put(m,5);

        Order order = new Order();
        order.setMovies(movies);
        order.setOrderStatus("DELIVERED");
        order.setOrderDate("/121/21");
        order.setOrderId(23);
        order.setUsername("username");
        order.setDueDate("11/2214/1");
        order.setOverdue(false);

        underTest.storeMovies(order);

        assertThat(underTest.countMoviesInOrder(23)).isEqualTo(5);
    }
}