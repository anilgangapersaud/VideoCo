package model;

import java.util.List;

public interface MovieService {

    List<Movie> findMovieByTitle(String movieTitle);

    List<Movie> getMoviesByCategory(String genre);

    List<Movie> getAllMovies();

    boolean addMovie(Movie movie);

    boolean deleteMovie(int barcode);

    boolean updateMovie(Movie movie);

    Movie getMovie(int barcode);
}
