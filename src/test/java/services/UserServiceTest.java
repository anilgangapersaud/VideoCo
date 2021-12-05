package services;

import database.TestConfigs;
import model.Address;
import model.Cart;
import model.Movie;
import model.User;
import model.payments.CreditCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private OrderService orderService;
    private AddressService addressService;
    private BillingService billingService;
    private MovieService movieService;
    private RentedService rentedService;


    @BeforeEach
    void setup() {
        UserService.setCsvPath(TestConfigs.ADMIN_CSV_TEST_PATH, TestConfigs.USER_CSV_TEST_PATH);
        userService = UserService.getInstance();

        OrderService.setCsvPath(TestConfigs.ORDER_CSV__TEST_PATH);
        orderService = OrderService.getInstance();

        AddressService.setCsvPath(TestConfigs.ADDRESS_CSV_TEST_PATH);
        addressService = AddressService.getInstance();

        BillingService.setCsvPath(TestConfigs.BILLING_CSV_TEST_PATH);
        billingService = BillingService.getInstance();

        MovieService.setCsvPath(TestConfigs.MOVIE_CSV_TEST_PATH);
        movieService = MovieService.getInstance();

        RentedService.setCsvPath(TestConfigs.RENTED_CSV_TEST_PATH);
        rentedService = RentedService.getInstance();
    }

    @Test
    void changeUsername() {
        User u = new User();
        u.setLoyaltyPoints(50);
        u.setPassword("password");
        u.setUsername("username");
        u.setAccountType("customer");
        u.setEmailAddress("a.gangapersaud@gmail.com");

        Movie m = new Movie();
        m.setReleaseDate("13/131/3");
        m.setGenre("Kids");
        m.setPrice(2);
        m.setTitle("Pokemon");
        m.setBarcode("21");

        Address a = new Address();
        a.setUsername("username");
        a.setCity("toronto");
        a.setProvince("ontario");
        a.setPostalCode("394823");
        a.setLineAddress("110 drift");

        CreditCard c = new CreditCard();
        c.setUsername("username");
        c.setBalance(0);
        c.setCardNumber("392482934");
        c.setExpiry("01/13");
        c.setCsv("23492834");

        userService.register(u);
        billingService.saveCreditCard(c);
        addressService.saveAddress(a);
        movieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.setUsername("username");
        cart.addMovieToCart(m,5);

        orderService.createOrder(cart,c);

        userService.changeUsername("username3", "username");
    }

    @Test
    void changePassword() {
    }

    @Test
    void changeEmail() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getAllCustomers() {
    }

    @Test
    void getLoggedInUser() {
    }

    @Test
    void getUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void isAdmin() {
    }

    @Test
    void register() {
    }

    @Test
    void saveGuestAccount() {
    }

    @Test
    void login() {
    }
}