package view;

import model.Movie;
import services.*;
import view.cards.StoreFrontCards;

import javax.swing.*;
import java.awt.*;

public class StoreFront extends JFrame {

    private static final String VIDEOCO_LOGO_PATH = System.getProperty("user.dir") + "/src/main/resources/videoco_logo.jpg";
    private static final String MOVIE_CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/movies.csv";
    private static final String ORDER_CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/orders.csv";
    private static final String ADMIN_CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/admins.csv";
    private static final String USER_CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/users.csv";
    private static final String ADDRESS_CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/addresses.csv";
    private static final String BILLING_CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/billing.csv";
    private static final String RENTED_CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/rented.csv";

    private static final String appName = "VideoCo Inc.";
    private static final int windowWidth = 1000;
    private static final int windowHeight = 500;

    public StoreFront() {
        MovieServiceImpl.setCsvPath(MOVIE_CSV_PATH);
        UserServiceImpl.setCsvPath(ADMIN_CSV_PATH, USER_CSV_PATH);
        AddressServiceImpl.setCsvPath(ADDRESS_CSV_PATH);
        BillingServiceImpl.setCsvPath(BILLING_CSV_PATH);
        RentedServiceImpl.setCsvPath(RENTED_CSV_PATH);
        OrderServiceImpl.setCsvPath(ORDER_CSV_PATH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(windowWidth, windowHeight);
        setResizable(false);
        setTitle(appName);
        getContentPane().setBackground(Color.WHITE);
        add(new StoreFrontCards(), BorderLayout.CENTER);
        Image icon = Toolkit.getDefaultToolkit().getImage(VIDEOCO_LOGO_PATH);
        setIconImage(icon);
        setVisible(true);
    }

    public static MovieServiceImpl getMovieService() {
        return MovieServiceImpl.getInstance();
    }
    public static RentedServiceImpl getRentedService() {
        return RentedServiceImpl.getInstance();
    }

    public static UserServiceImpl getUserService() { return UserServiceImpl.getInstance(); }

    public static BillingServiceImpl getBillingService() { return BillingServiceImpl.getInstance(); }

    public static AddressServiceImpl getAddressService() { return AddressServiceImpl.getInstance(); }

    public static OrderServiceImpl getOrderService() { return OrderServiceImpl.getInstance(); }

}
