package model.movie;

import database.MovieRepository;

import java.util.Map;

public class MovieServiceImpl implements MovieService {

    MovieRepository movieRepository;

    public MovieServiceImpl() {
        movieRepository = new MovieRepository();
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
    public boolean addMovie(Movie movie) {
        return movieRepository.addMovie(movie);
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
