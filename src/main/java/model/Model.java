package model;

import model.movie.MovieService;
import model.movie.MovieServiceImpl;
import model.user.UserService;
import model.user.UserServiceImpl;

public class Model {

    private final static UserService userService;
    private final static MovieService movieService;

    static {
        userService = new UserServiceImpl();
        movieService = new MovieServiceImpl();
    }

    public static UserService getUserService() {
        return userService;
    }

    public static MovieService getMovieService() {
        return movieService;
    }
}
