import services.MovieService;
import services.MovieServiceImpl;
import services.UserService;
import services.UserServiceImpl;
import view.StoreFront;

public class App {

    //TODO: abstract logic from views into controllers

    private static final UserService userService = new UserServiceImpl();
    private static final MovieService movieService = new MovieServiceImpl();

    public App() {
        initializeApp();
    }

    public static void main(String[] args) {
        new App();
    }

    private void initializeApp() {
        new StoreFront();
    }

    public static UserService getUserService() {
        return userService;
    }

    public static MovieService getMovieService() {return movieService; }

}
