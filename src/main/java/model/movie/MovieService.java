package model.movie;

import java.util.List;

/**
 * MovieService interface for common operations on movies
 */
public interface MovieService {

    /**
     * Find movie by title
     * @param movieTitle title of movie to search
     * @return a list of movies that match the title
     */
    List<Movie> findMovieByTitle(String movieTitle);

    /**
     * Get movies by category
     * @param genre the category to search
     * @return a list of movies that match the category
     */
    List<Movie> getMoviesByCategory(String genre);

    /**
     * Get all movies in the system
     * @return a list of all movies
     */
    List<Movie> getAllMovies();

    /**
     * Adds a movie to the database
     * @param movie the movie to add
     * @return {@code true} if the movie was added successfully, {@code false} otherwise
     */
    boolean addMovie(Movie movie);

    boolean deleteMovie(int barcode);

    boolean updateMovie(Movie movie);

    Movie getMovie(int barcode);
}
