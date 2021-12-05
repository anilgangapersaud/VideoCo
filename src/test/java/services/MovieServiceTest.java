package services;

import database.TestConfigs;
import model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setup() {
        MovieService.setCsvPath(TestConfigs.MOVIE_CSV_TEST_PATH);
        movieService = MovieService.getInstance();
    }

    @Test
    void testAddMovie() {
        Movie m = new Movie();
        m.setBarcode("123");
        m.setTitle("Pokemon");
        m.setPrice(20);
        m.setGenre("Kids");
        m.setReleaseDate("01/10/10");
        movieService.addMovie(m,10);
    }

    @Test
    void testDeleteMovie() {
        Movie m = new Movie();
        m.setBarcode("123");
        m.setTitle("Pokemon");
        m.setPrice(20);
        m.setGenre("Kids");
        m.setReleaseDate("01/10/10");
        movieService.addMovie(m,10);
        movieService.deleteMovie(m.getBarcode());
    }

    @Test
    void testGetMovieByTitle() {
        Movie m = new Movie();
        m.setBarcode("123");
        m.setTitle("Pokemon");
        m.setPrice(20);
        m.setGenre("Kids");
        m.setReleaseDate("01/10/10");
        movieService.addMovie(m,10);

        Map<Movie,Integer> expected = new HashMap<Movie,Integer>();
        expected.put(m,10);

        assertThat(expected).isEqualTo(movieService.getMovieByTitle("Pok"));
    }

    @Test
    void testGetAllMovies() {
        Movie m = new Movie();
        m.setBarcode("123");
        m.setTitle("Pokemon");
        m.setPrice(20);
        m.setGenre("Kids");
        m.setReleaseDate("01/10/10");
        movieService.addMovie(m,10);

        Map<Movie,Integer> expected = new HashMap<Movie,Integer>();
        expected.put(m,10);

        assertThat(expected).isEqualTo(movieService.getAllMovies());
    }

    @Test
    void testGetMoviesByCategory() {
        Movie m = new Movie();
        m.setBarcode("123");
        m.setTitle("Pokemon");
        m.setPrice(20);
        m.setGenre("Kids");
        m.setReleaseDate("01/10/10");
        movieService.addMovie(m,10);

        Map<Movie,Integer> expected = new HashMap<Movie,Integer>();
        expected.put(m,10);

        assertThat(expected).isEqualTo(movieService.getMoviesByCategory("Kids"));
    }

    @Test
    void testGetMovie() {
        Movie m = new Movie();
        m.setBarcode("123");
        m.setTitle("Pokemon");
        m.setPrice(20);
        m.setGenre("Kids");
        m.setReleaseDate("01/10/10");
        movieService.addMovie(m,10);

        assertThat(m).isEqualTo(movieService.getMovie("123"));
    }

    @Test
    void testGetStockForMovie() {
        Movie m = new Movie();
        m.setBarcode("123");
        m.setTitle("Pokemon");
        m.setPrice(20);
        m.setGenre("Kids");
        m.setReleaseDate("01/10/10");
        movieService.addMovie(m,10);

        assertThat(movieService.getStockForMovie("123")).isEqualTo(10);
    }

    @Test
    void testRentMovies() {
        Movie m = new Movie();
        m.setBarcode("123");
        m.setTitle("Pokemon");
        m.setPrice(20);
        m.setGenre("Kids");
        m.setReleaseDate("01/10/10");
        movieService.addMovie(m,10);

        Map<Movie,Integer> rent = new HashMap<>();
        rent.put(m,7);
        boolean result = movieService.rentMovies(rent);
        assertThat(result).isTrue();
    }

    @Test
    void testRentMoviesFail() {
        Movie m = new Movie();
        m.setBarcode("123");
        m.setTitle("Pokemon");
        m.setPrice(20);
        m.setGenre("Kids");
        m.setReleaseDate("01/10/10");
        movieService.addMovie(m,10);

        Map<Movie,Integer> rent = new HashMap<>();
        rent.put(m,20);
        boolean result = movieService.rentMovies(rent);
        assertThat(result).isFalse();
    }

    @Test
    void testReturnMovie() {
        Movie m = new Movie();
        m.setBarcode("123");
        m.setTitle("Pokemon");
        m.setPrice(20);
        m.setGenre("Kids");
        m.setReleaseDate("01/10/10");
        movieService.addMovie(m,10);
        movieService.returnMovie("123");
        assertThat(movieService.getStockForMovie("123")).isEqualTo(11);
    }

    @Test
    void removeStock() {
        Movie m = new Movie();
        m.setBarcode("123");
        m.setTitle("Pokemon");
        m.setPrice(20);
        m.setGenre("Kids");
        m.setReleaseDate("01/10/10");
        movieService.addMovie(m,10);
        movieService.removeStock("123");
        assertThat(movieService.getStockForMovie("123")).isEqualTo(9);
    }

    @Test
    void testUpdateMovie() {
        Movie m = new Movie();
        m.setBarcode("123");
        m.setTitle("Pokemon");
        m.setPrice(20);
        m.setGenre("Kids");
        m.setReleaseDate("01/10/10");

        movieService.addMovie(m, 5);
        m.setGenre("Horror");

        movieService.updateMovie(m);

        assertThat(m).isEqualTo(movieService.getMovie("123"));
    }
}