package view;

import model.movie.MovieService;
import model.movie.MovieServiceImpl;
import model.user.UserService;
import model.user.UserServiceImpl;

import javax.swing.*;
import java.awt.*;

public class App {

    private static final String appName = "VideoCo Inc.";
    private static final int windowWidth = 1000;
    private static final int windowHeight = 500;
    private static UserService userService = new UserServiceImpl();
    private static MovieService movieService = new MovieServiceImpl();

    public App() {
        initializeApp();
    }

    public static void main(String[] args) {
        new App();
    }

    private void initializeApp() {
        JFrame mainframe = new JFrame(appName);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setSize(windowWidth, windowHeight);
        mainframe.setLayout(new BorderLayout());
        mainframe.add(new LoginPanel(), BorderLayout.SOUTH);
        mainframe.setBackground(Color.black);
        mainframe.setVisible(true);
    }

    public static UserService getUserService() {
        return userService;
    }

    public static MovieService getMovieService() {return movieService; }

}
