package model;

import services.*;

public class Model {

    private final static UserService userService;
    private final static MovieService movieService;
    private final static AddressService addressService;
    private final static OrderService orderService;
    private final static BillingService billingService;
    private final static RentedService rentedService;

    static {
        userService = new UserServiceImpl();
        movieService = new MovieServiceImpl();
        addressService = new AddressServiceImpl();
        orderService = new OrderServiceImpl();
        billingService = new BillingServiceImpl();
        rentedService = new RentedServiceImpl();
    }

    public static UserService getUserService() {
        return userService;
    }

    public static MovieService getMovieService() {return movieService;}

    public static AddressService getAddressService() { return addressService; }

    public static OrderService getOrderService() { return orderService; }

    public static BillingService getBillingService() { return billingService; }

    public static RentedService getRentedService() { return rentedService; }
}
