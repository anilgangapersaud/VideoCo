package model;

import database.MovieRepository;

import java.util.List;

public class MovieServiceImpl implements MovieService {

    MovieRepository movieRepository;

    MovieServiceImpl() {
        movieRepository = new MovieRepository();
    }

    @Override
    public List<Movie> findMovieByTitle(String movieTitle) {
        return movieRepository.findMovieByTitle(movieTitle);
    }

    @Override
    public List<Movie> getMoviesByCategory(String genre) {
        return movieRepository.getMoviesByCategory(genre);
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.getAllMovies();
    }

    @Override
    public boolean addMovie(Movie movie) {
        return false;
    }

    @Override
    public boolean deleteMovie(int barcode) {
        return false;
    }

    @Override
    public boolean updateMovie(Movie movie) {
        return false;
    }

    @Override
    public Movie getMovie(int barcode) {
        return null;
    }
}
