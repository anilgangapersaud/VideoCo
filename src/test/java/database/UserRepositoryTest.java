package database;

import model.*;
import model.payments.CreditCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.*;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserRepositoryTest {

    private static OrderServiceImpl testOrderService;
    private static MovieServiceImpl testMovieService;
    private static UserServiceImpl testUserService;
    private static BillingServiceImpl testBillingService;
    private static AddressServiceImpl testAddressService;
    private static RentedServiceImpl testRentedService;


    @BeforeEach
    void setup() {
        MovieServiceImpl.setCsvPath(TestConfigs.MOVIE_CSV_TEST_PATH);
        UserServiceImpl.setCsvPath(TestConfigs.ADMIN_CSV_TEST_PATH, TestConfigs.USER_CSV_TEST_PATH);
        OrderServiceImpl.setCsvPath(TestConfigs.ORDER_CSV__TEST_PATH);
        BillingServiceImpl.setCsvPath(TestConfigs.BILLING_CSV_TEST_PATH);
        AddressServiceImpl.setCsvPath(TestConfigs.ADDRESS_CSV_TEST_PATH);
        RentedServiceImpl.setCsvPath(TestConfigs.RENTED_CSV_TEST_PATH);

        testMovieService = MovieServiceImpl.getInstance();
        testUserService = UserServiceImpl.getInstance();
        testOrderService = OrderServiceImpl.getInstance();
        testBillingService = BillingServiceImpl.getInstance();
        testAddressService = AddressServiceImpl.getInstance();
        testRentedService = RentedServiceImpl.getInstance();
    }

    @AfterEach
    void teardown() {
    }

    @Test
    void testChangeUsernameSuccess() {
        User u = new User();
        u.setUsername("username1");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);
        testUserService.register(u);
        testUserService.login("username1", "password");
        boolean result = testUserService.changeUsername("username123", "username1");
        assertThat(result).isTrue();
    }

    @Test
    void testChangeUsernameFailedNonUnique() {
        User u = new User();
        u.setUsername("username3");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        testUserService.register(u);

        testUserService.login("username3", "password");

        boolean result = testUserService.changeUsername("username3", "username3");
        assertThat(result).isFalse();
    }

    @Test
    void testChangeUsernameWithAddressAndBillingAndOrders() {
        User u = new User();
        u.setUsername("username12");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        Address a = new Address();
        a.setUsername("username12");
        a.setCity("toronto");
        a.setLineAddress("driftowod");
        a.setProvince("ontario");
        a.setPostalCode("34298");

        CreditCard c = new CreditCard();
        c.setUsername("username12");
        c.setCardNumber("42938429");
        c.setBalance(0);
        c.setCsv("324");
        c.setExpiry("01/13");

        testAddressService.saveAddress(a);
        testUserService.register(u);
        testBillingService.saveCreditCard(c);

        Movie m = new Movie();
        m.setBarcode("1");
        m.setTitle("test");
        m.setPrice(10);
        m.setGenre("Kids");
        m.setReleaseDate("01/01/01");

        testMovieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.addMovieToCart(m, 2);
        cart.setUsername("username12");

        testOrderService.createOrder(cart, c);

        testUserService.login("username12", "password");

        boolean result = testUserService.changeUsername("username123", "username12");
        assertThat(result).isTrue();

        Address result1 = testAddressService.getAddress("username123");
        CreditCard result2 = testBillingService.getCreditCard("username123");
        List<Order> result3 = testOrderService.getOrdersByCustomer("username123");

        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result3.size()).isEqualTo(1);
    }

    @Test
    void testChangeUsernameFailedNoUser() {
        boolean result = testUserService.changeUsername("idontexist", "idontexisteither");
        assertThat(result).isEqualTo(false);
    }

    @Test
    void testChangePasswordSuccess() {
        User u = new User();
        u.setUsername("username54");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        testUserService.register(u);

        testUserService.login("username54", "password");

        boolean result = testUserService.changePassword("password123", "username54");
        assertThat(result).isTrue();
    }

    @Test
    void testChangePasswordFailedInvalidPassword() {
        User u = new User();
        u.setUsername("username67");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        testUserService.register(u);

        testUserService.login("username67", "password");

        boolean result = testUserService.changePassword("", "username67");
        assertThat(result).isFalse();
    }

    @Test
    void testChangePasswordFailedNoUser() {
        testUserService.changePassword("newPass", "nonexistant");
    }

    @Test
    void testChangeEmailSuccess() {
        User u = new User();
        u.setUsername("username83");
        u.setPassword("password");
        u.setEmailAddress("username83");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        testUserService.register(u);

        testUserService.login("username83", "password");

        boolean result = testUserService.changeEmail("a.gangapersaud@gmail.com", "username83");
        assertThat(result).isTrue();
    }

    @Test
    void testChangeEmailFailedInvalidEmail() {
        User u = new User();
        u.setUsername("username345");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        testUserService.register(u);

        boolean result = testUserService.changeEmail("", "username345");
        assertThat(result).isFalse();
    }

    @Test
    void testChangeEmailFailedNoUser() {
        testUserService.changeEmail("newEmail", "nouser");
    }


    @Test
    void testDeleteUser() {
        User u = new User();
        u.setUsername("username3454");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        testUserService.register(u);

        Address a = new Address();
        a.setUsername("username3454");
        a.setCity("toronto");
        a.setLineAddress("driftowod");
        a.setProvince("ontario");
        a.setPostalCode("34298");

        CreditCard c = new CreditCard();
        c.setUsername("username3454");
        c.setCardNumber("42938429");
        c.setBalance(0);
        c.setCsv("324");
        c.setExpiry("01/13");

        testAddressService.saveAddress(a);
        testUserService.register(u);
        testBillingService.saveCreditCard(c);

        Movie m = new Movie();
        m.setBarcode("1");
        m.setTitle("test");
        m.setPrice(10);
        m.setGenre("Kids");
        m.setReleaseDate("01/01/01");

        testMovieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.addMovieToCart(m, 2);
        cart.setUsername("username3454");

        testOrderService.createOrder(cart, c);

        testUserService.deleteUser("username3454");
    }

    @Test
    void getAllCustomers() {
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