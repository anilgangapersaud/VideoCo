import model.movie.MovieService;
import model.movie.MovieServiceImpl;
import model.user.UserService;
import model.user.UserServiceImpl;
import view.StoreFront;

public class App {

    private static UserService userService = new UserServiceImpl();
    private static MovieService movieService = new MovieServiceImpl();

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
