package services;

import model.Movie;

import java.util.Map;

/**
 * MovieService interface for common operations on movies
 */
public interface MovieService {

    /**
     * Find movie by title
     * @param movieTitle title of movie to search
     * @return a list of movies that match the title
     */
    Map<Movie,Integer> findMovieByTitle(String movieTitle);

    /**
     * Get movies by category
     * @param genre the category to search
     * @return a list of movies that match the category
     */
    Map<Movie,Integer> getMoviesByCategory(String genre);

    /**
     * Get all movies in the system
     * @return a list of all movies
     */
    Map<Movie,Integer> getAllMovies();

    /**
     * Adds a movie to the database
     * @param movie the movie to add
     * @return {@code true} if the movie was added successfully, {@code false} otherwise
     */
    boolean addMovie(Movie movie);
}
