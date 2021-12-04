package database;

import model.*;
import model.payments.CreditCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserRepositoryTest {

    private static OrderRepository testOrderRepository;
    private static MovieRepository testMovieRepository;
    private static UserRepository testUserRepository;
    private static BillingRepository testBillingRepository;
    private static AddressRepository testAddressRepository;
    private static RentedRepository testRentedRepository;

    @BeforeEach
    void setup() {
        testMovieRepository = MovieRepository.getInstance();
        testUserRepository = UserRepository.getInstance();
        testOrderRepository = OrderRepository.getInstance();
        testBillingRepository = BillingRepository.getInstance();
        testAddressRepository = AddressRepository.getInstance();
        testRentedRepository = RentedRepository.getInstance();
    }

    @AfterEach
    void teardown() {
        testAddressRepository.clearCSV();
        testMovieRepository.clearCSV();
        testUserRepository.clearCSV();
        testOrderRepository.clearCSV();
        testBillingRepository.clearCSV();
        testRentedRepository.clearCSV();
    }

    @Test
    void testChangeUsernameSuccess() {
        User u = new User();
        u.setUsername("username1");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);
        testUserRepository.register(u);
        testUserRepository.login("username1", "password");
        boolean result = testUserRepository.changeUsername("username123", "username1");
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

        testUserRepository.register(u);

        testUserRepository.login("username3", "password");

        boolean result = testUserRepository.changeUsername("username3", "username3");
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

        testAddressRepository.saveAddress(a);
        testUserRepository.register(u);
        testBillingRepository.saveCreditCard(c);

        Movie m = new Movie();
        m.setBarcode("1");
        m.setTitle("test");
        m.setPrice(10);
        m.setGenre("Kids");
        m.setReleaseDate("01/01/01");

        testMovieRepository.addMovie(m,5);

        Cart cart = new Cart();
        cart.addMovieToCart(m, 2);
        cart.setUsername("username12");

        testOrderRepository.createOrder(cart, c);

        testUserRepository.login("username12", "password");

        boolean result = testUserRepository.changeUsername("username123", "username12");
        assertThat(result).isTrue();

        Address result1 = testAddressRepository.getAddress("username123");
        CreditCard result2 = testBillingRepository.getCreditCard("username123");
        List<Order> result3 = testOrderRepository.getOrdersByCustomer("username123");

        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result3.size()).isEqualTo(1);
    }

    @Test
    void testChangeUsernameFailedNoUser() {
        boolean result = testUserRepository.changeUsername("idontexist", "idontexisteither");
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

        testUserRepository.register(u);

        testUserRepository.login("username54", "password");

        boolean result = testUserRepository.changePassword("password123", "username54");
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

        testUserRepository.register(u);

        testUserRepository.login("username67", "password");

        boolean result = testUserRepository.changePassword("", "username67");
        assertThat(result).isFalse();
    }

    @Test
    void testChangePasswordFailedNoUser() {
        testUserRepository.changePassword("newPass", "nonexistant");
    }

    @Test
    void testChangeEmailSuccess() {
        User u = new User();
        u.setUsername("username83");
        u.setPassword("password");
        u.setEmailAddress("username83");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        testUserRepository.register(u);

        testUserRepository.login("username83", "password");

        boolean result = testUserRepository.changeEmail("a.gangapersaud@gmail.com", "username83");
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

        testUserRepository.register(u);

        boolean result = testUserRepository.changeEmail("", "username345");
        assertThat(result).isFalse();
    }

    @Test
    void testChangeEmailFailedNoUser() {
        testUserRepository.changeEmail("newEmail", "nouser");
    }


    @Test
    void testDeleteUser() {
        User u = new User();
        u.setUsername("username3454");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        testUserRepository.register(u);

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

        testAddressRepository.saveAddress(a);
        testUserRepository.register(u);
        testBillingRepository.saveCreditCard(c);

        Movie m = new Movie();
        m.setBarcode("1");
        m.setTitle("test");
        m.setPrice(10);
        m.setGenre("Kids");
        m.setReleaseDate("01/01/01");

        testMovieRepository.addMovie(m,5);

        Cart cart = new Cart();
        cart.addMovieToCart(m, 2);
        cart.setUsername("username3454");

        testOrderRepository.createOrder(cart, c);

        testUserRepository.deleteUser("username3454");
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