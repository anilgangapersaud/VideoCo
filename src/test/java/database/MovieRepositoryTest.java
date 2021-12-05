package database;

import model.Movie;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MovieRepositoryTest {

    private static MovieRepository underTest;

    @BeforeEach
    void setup() {
        underTest = MovieRepository.getInstance(TestConfigs.MOVIE_CSV_TEST_PATH);
    }

    @AfterEach
    void teardown() {
    }

    @Test
    void testAddNewMovie() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("111");
        newMovie.setTitle("Pokemon");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);

        boolean result = underTest.addMovie(newMovie,1);

        assertThat(result).isEqualTo(true);

        Movie m = underTest.getMovie("111");

        assertThat(m).isEqualTo(newMovie);

        underTest.deleteMovie("111");
    }

    @Test
    void testAddExistingMovie() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("222");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);

        underTest.addMovie(newMovie,1);

        underTest.addMovie(newMovie, 2);

        int result = underTest.getStockForMovie("222");
        int expected = 3;

        assertThat(result).isEqualTo(expected);

        underTest.deleteMovie("222");
    }

    @Test
    void testDeleteMovies() {
        Movie newMovie2 = new Movie();
        newMovie2.setBarcode("333");
        newMovie2.setTitle("Test");
        newMovie2.setGenre("Kids");
        newMovie2.setReleaseDate("01/01/01");
        newMovie2.setPrice(9.99);

        Movie newMovie3 = new Movie();
        newMovie3.setBarcode("444");
        newMovie3.setTitle("Test");
        newMovie3.setGenre("Kids");
        newMovie3.setReleaseDate("01/01/01");
        newMovie3.setPrice(9.99);

        Movie newMovie = new Movie();
        newMovie.setBarcode("555");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);

        underTest.addMovie(newMovie,1);
        underTest.addMovie(newMovie2,1);
        underTest.addMovie(newMovie3,1);

        List<String> barcodes = new ArrayList<>();
        barcodes.add("333");
        barcodes.add("444");
        barcodes.add("555");

        for (String barcode : barcodes) {
            underTest.deleteMovie(barcode);
        }

        int stock1 = underTest.getStockForMovie("333");
        int stock2 = underTest.getStockForMovie("444");
        int stock3 = underTest.getStockForMovie("555");

        assertThat(stock1).isEqualTo(0);
        assertThat(stock2).isEqualTo(0);
        assertThat(stock3).isEqualTo(0);
    }

    @Test
    void testFindMovieByTitle() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("666");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);

        underTest.addMovie(newMovie,2);

        Map<Movie,Integer> expected = new HashMap<>();
        expected.put(newMovie,2);

        Map<Movie,Integer> result = underTest.getMovieByTitle("te");

        assertThat(result).isEqualTo(expected);

        underTest.deleteMovie("666");
    }

    @Test
    void testGetAllMovies() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("777");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);

        underTest.addMovie(newMovie,1);

        Map<Movie,Integer> expected = new HashMap<>();
        expected.put(newMovie,1);

        Map<Movie,Integer> result = underTest.getAllMovies();

        assertThat(expected).isEqualTo(result);

        underTest.deleteMovie("777");
    }

    @Test
    void testGetMoviesByCategory() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("99");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);

        underTest.addMovie(newMovie,1);

        Map<Movie,Integer> expected = new HashMap<>();

        expected.put(newMovie,1);

        Map<Movie,Integer> result = underTest.getMoviesByCategory("Kids");

        assertThat(result).isEqualTo(expected);

        underTest.deleteMovie("99");
    }

    @Test
    void testGetMovie() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("24");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);

        underTest.addMovie(newMovie,1);

        Movie m = underTest.getMovie("24");
        assertThat(m).isEqualTo(newMovie);

        underTest.deleteMovie(newMovie.getBarcode());
    }

    @Test
    void testGetStockForMovie() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("59");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);

        int expectedStock = 3;
        underTest.addMovie(newMovie,expectedStock);

        int result = underTest.getStockForMovie("59");
        assertThat(result).isEqualTo(expectedStock);

        underTest.deleteMovie("59");
    }

    @Test
    void testRentMoviesSuccess() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("5445");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);

        Movie newMovie2 = new Movie();
        newMovie2.setBarcode("47454");
        newMovie2.setTitle("Test");
        newMovie2.setGenre("Kids");
        newMovie2.setReleaseDate("01/01/01");
        newMovie2.setPrice(9.99);
        underTest.addMovie(newMovie,2);
        underTest.addMovie(newMovie2,2);

        Map<Movie,Integer> map = new HashMap<>();
        map.put(newMovie,2);
        map.put(newMovie2,2);

        boolean result = underTest.rentMovies(map);

        assertThat(result).isEqualTo(true);

        int expectedStock1 = 0;
        int expectedStock2 = 0;

        int resultStock1 = underTest.getStockForMovie("5445");
        int resultStock2 = underTest.getStockForMovie("47454");

        assertThat(resultStock1).isEqualTo(expectedStock1);
        assertThat(resultStock2).isEqualTo(expectedStock2);

        underTest.deleteMovie("5445");
        underTest.deleteMovie("47454");
    }

    @Test
    void testRentMoviesFailure() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("234");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);

        Movie newMovie2 = new Movie();
        newMovie2.setBarcode("436");
        newMovie2.setTitle("Test");
        newMovie2.setGenre("Kids");
        newMovie2.setReleaseDate("01/01/01");
        newMovie2.setPrice(9.99);
        underTest.addMovie(newMovie,2);
        underTest.addMovie(newMovie2,2);

        Map<Movie,Integer> map = new HashMap<>();
        map.put(newMovie,2);
        map.put(newMovie2,3);

        boolean result = underTest.rentMovies(map);
        assertThat(result).isEqualTo(false);

        underTest.deleteMovie("234");
        underTest.deleteMovie("436");
    }

    @Test
    void testRentMoviesFailureNoMovie() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("93");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);

        Movie newMovie2 = new Movie();
        newMovie2.setBarcode("68");
        newMovie2.setTitle("Test");
        newMovie2.setGenre("Kids");
        newMovie2.setReleaseDate("01/01/01");
        newMovie2.setPrice(9.99);
        underTest.addMovie(newMovie2,2);

        Map<Movie,Integer> map = new HashMap<>();
        map.put(newMovie,2);
        map.put(newMovie2,3);

        boolean result = underTest.rentMovies(map);
        assertThat(result).isEqualTo(false);

        underTest.deleteMovie("68");
    }

    @Test
    void testReturnMovie() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("97");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);
        underTest.addMovie(newMovie,1);

        Map<Movie,Integer> rent = new HashMap<>();
        rent.put(newMovie,1);
        underTest.rentMovies(rent);

        assertThat(underTest.getStockForMovie("97")).isEqualTo(0);

        underTest.returnMovie("97");

        assertThat(underTest.getStockForMovie("97")).isEqualTo(1);

        underTest.deleteMovie("97");
    }

    @Test
    void testRemoveStock() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("42");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);
        underTest.addMovie(newMovie,1);

        assertThat(underTest.getStockForMovie("42")).isEqualTo(1);

        underTest.removeStock("42");

        assertThat(underTest.getStockForMovie("42")).isEqualTo(0);

        underTest.removeStock("42");

        assertThat(underTest.getStockForMovie("42")).isEqualTo(0);

        underTest.deleteMovie("42");
    }

    @Test
    void testUpdateMovie() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("42");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);
        underTest.addMovie(newMovie,1);

        Movie updatedMovie = new Movie();
        updatedMovie.setBarcode("42");
        updatedMovie.setTitle("Test2");
        updatedMovie.setGenre("Kids");
        updatedMovie.setReleaseDate("01/01/01");
        updatedMovie.setPrice(9.99);

        boolean result = underTest.updateMovie(updatedMovie);
        assertThat(result).isEqualTo(true);

        underTest.deleteMovie("42");
    }

    @Test
    void testUpdateMovieNull() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("42");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);
        underTest.addMovie(newMovie,1);

        boolean result = underTest.updateMovie(null);

        assertThat(result).isEqualTo(false);

        underTest.deleteMovie("42");
    }

    @Test
    void testUpdateMovieNullFields() {
        Movie newMovie = new Movie();
        newMovie.setBarcode("42");
        newMovie.setTitle("Test");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);
        underTest.addMovie(newMovie,1);

        Movie updateMovie = new Movie();
        newMovie.setBarcode("42");
        newMovie.setGenre("Kids");
        newMovie.setReleaseDate("01/01/01");
        newMovie.setPrice(9.99);

        boolean result = underTest.updateMovie(updateMovie);

        assertThat(result).isEqualTo(false);

        underTest.deleteMovie("42");
    }

}