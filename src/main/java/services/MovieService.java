package services;

import model.Movie;

import java.util.List;
import java.util.Map;

/**
 * MovieService interface for common operations on movies
 */
public interface MovieService {

    /**
     * Adds a movie to the database
     * @param movie the movie to add
     * @return {@code true} if the movie was added successfully, {@code false} otherwise
     */
    boolean addMovie(Movie movie, Integer quantity);

    /**
     * Increase the stock of a movie by one
     * @param barcode the movies barcode
     */
    void addStock(String barcode);

    /**
     * Delete movies from the database
     * @param barcodes the barcodes of movies to remove
     */
    void deleteMovies(List<String> barcodes);

    /**
     * Find movie by title
     * @param movieTitle title of movie to search
     * @return a list of movies that match the title
     */
    Map<Movie,Integer> findMovieByTitle(String movieTitle);

    /**
     * Get all movies in the system
     * @return a list of all movies
     */
    Map<Movie,Integer> getAllMovies();

    /**
     * Get movies by category
     * @param genre the category to search
     * @return a list of movies that match the category
     */
    Map<Movie,Integer> getMoviesByCategory(String genre);

    /**
     * Get movie by barcode
     * @param barcode the barcode
     * @return the movie associated with the barcode
     */
    Movie getMovie(String barcode);

    /**
     * Decrease the stock of a movie by one
     * @param barcode
     */
    void removeStock(String barcode);

    /**
     * Updates a movie
     * @param movie the movie to update
     * @return true if successful, false otherwise
     */
    boolean updateMovie(Movie movie);

}
