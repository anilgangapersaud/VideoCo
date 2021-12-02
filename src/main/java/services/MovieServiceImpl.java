package services;

import database.MovieRepository;
import model.Movie;

import java.util.List;
import java.util.Map;

public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl() {
        movieRepository = MovieRepository.getInstance();
    }

    @Override
    public Map<Movie,Integer> findMovieByTitle(String movieTitle) {
        return movieRepository.findMovieByTitle(movieTitle);
    }

    @Override
    public Map<Movie,Integer> getMoviesByCategory(String genre) {
        return movieRepository.getMoviesByCategory(genre);
    }

    @Override
    public Map<Movie,Integer> getAllMovies() {
        return movieRepository.getAllMovies();
    }

    @Override
    public boolean addMovie(Movie movie, Integer quantity) {
        return movieRepository.addMovie(movie, quantity);
    }

    @Override
    public boolean updateMovie(Movie movie) {
        return movieRepository.updateMovie(movie);
    }

    @Override
    public void deleteMovies(List<String> barcodes) {
        movieRepository.deleteMovies(barcodes);
    }

    @Override
    public Movie getMovie(String barcode) {
        return movieRepository.getMovie(barcode);
    }

    @Override
    public void addStock(String barcode) {
        movieRepository.returnMovie(barcode);
    }

    @Override
    public void removeStock(String barcode) {
        movieRepository.removeStock(barcode);
    }
}
