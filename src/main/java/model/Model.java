package model;

import model.movie.MovieService;
import model.movie.MovieServiceImpl;
import model.order.OrderService;
import model.order.OrderServiceImpl;
import model.user.*;

public class Model {

    private final static UserService userService;
    private final static MovieService movieService;
    private final static AddressService addressService;
    private final static OrderService orderService;

    static {
        userService = new UserServiceImpl();
        movieService = new MovieServiceImpl();
        addressService = new AddressServiceImpl();
        orderService = new OrderServiceImpl();
    }

    public static UserService getUserService() {
        return userService;
    }

    public static MovieService getMovieService() {
        return movieService;
    }

    public static AddressService getAddressService() { return addressService; }

    public static OrderService getOrderService() { return orderService; }
}
