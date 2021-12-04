package services;

import database.MovieRepository;
import database.Observer;
import model.Movie;

import java.util.Map;

public class MovieServiceImpl {

    private static String MOVIE_CSV_PATH;

    private volatile static MovieServiceImpl instance;

    private final MovieRepository movieRepository;

    private MovieServiceImpl() {
        movieRepository = MovieRepository.getInstance(MOVIE_CSV_PATH);
    }

    public static MovieServiceImpl getInstance() {
        if (instance == null) {
            synchronized (MovieServiceImpl.class) {
                if (instance == null) {
                    instance = new MovieServiceImpl();
                }
            }
        }
        return instance;
    }

    public static void setCsvPath(String path) {
        MOVIE_CSV_PATH = path;
    }

    public boolean addMovie(Movie movie, Integer quantity) {
        return movieRepository.addMovie(movie, quantity);
    }

    public void deleteMovie(String barcode) {
        movieRepository.deleteMovie(barcode);
    }

    public Map<Movie,Integer> getMovieByTitle(String movieTitle) {
        return movieRepository.getMovieByTitle(movieTitle);
    }

    public Map<Movie,Integer> getAllMovies() {
        return movieRepository.getAllMovies();
    }

    public Map<Movie,Integer> getMoviesByCategory(String genre) {
        return movieRepository.getMoviesByCategory(genre);
    }

    public Movie getMovie(String barcode) {
        return movieRepository.getMovie(barcode);
    }

    public int getStockForMovie(String barcode) {
        return movieRepository.getStockForMovie(barcode);
    }

    public boolean rentMovies(Map<Movie,Integer> movies) {
        return movieRepository.rentMovies(movies);
    }

    public void returnMovie(String barcode) {
        movieRepository.returnMovie(barcode);
    }

    public void removeStock(String barcode) {
        movieRepository.removeStock(barcode);
    }

    public boolean updateMovie(Movie movie) {
        return movieRepository.updateMovie(movie);
    }

    public void registerObserver(Observer o) {
        movieRepository.registerObserver(o);
    }

    public void removeObserver(Observer o) {
        movieRepository.removeObserver(o);
    }

}
