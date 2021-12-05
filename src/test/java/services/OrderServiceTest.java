package services;

import database.TestConfigs;
import model.*;
import model.payments.CreditCard;
import model.payments.LoyaltyPoints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderServiceTest {

    private OrderService orderService;
    private UserService userService;
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
    void testCancelOrder() {
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

        billingService.saveCreditCard(c);
        addressService.saveAddress(a);
        movieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.setUsername("username");
        cart.addMovieToCart(m,5);

        Order o = orderService.createOrder(cart,c);
        boolean result = orderService.cancelOrder(o.getOrderId());
        assertThat(result).isTrue();
    }

    @Test
    void testCreateOrder() {
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

        billingService.saveCreditCard(c);
        addressService.saveAddress(a);
        movieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.setUsername("username");
        cart.addMovieToCart(m,5);

        assertThat(orderService.createOrder(cart,c)).isNotNull();
    }

    @Test
    void testChangeOrderStatus() {
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

        billingService.saveCreditCard(c);
        addressService.saveAddress(a);
        movieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.setUsername("username");
        cart.addMovieToCart(m,5);

        Order o = orderService.createOrder(cart,c);
        orderService.changeOrderStatus(o.getOrderId(), "DELIVERED");
    }

    @Test
    void testDeleteOrder() {
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

        billingService.saveCreditCard(c);
        addressService.saveAddress(a);
        movieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.setUsername("username");
        cart.addMovieToCart(m,5);

        Order o = orderService.createOrder(cart,c);
        orderService.deleteOrder(o.getOrderId());
    }

    @Test
    void testGetAllOrders() {
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

        billingService.saveCreditCard(c);
        addressService.saveAddress(a);
        movieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.setUsername("username");
        cart.addMovieToCart(m,5);

        Order o = orderService.createOrder(cart,c);

        List<Order> orders = new ArrayList<>();
        orders.add(o);

        assertThat(orders).isEqualTo(orderService.getAllOrders());
    }

    @Test
    void testGetOrdersByCustomer() {
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

        billingService.saveCreditCard(c);
        addressService.saveAddress(a);
        movieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.setUsername("username");
        cart.addMovieToCart(m,5);

        Order o = orderService.createOrder(cart,c);

        List<Order> orders = new ArrayList<Order>();
        orders.add(o);

        assertThat(orderService.getOrdersByCustomer("username")).isEqualTo(orders);
    }

    @Test
    void testGetOrder() {
        orderService.getOrder(23);
    }


    @Test
    void testReturnOrder() {
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

        billingService.saveCreditCard(c);
        addressService.saveAddress(a);
        movieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.setUsername("username");
        cart.addMovieToCart(m,5);

        Order o = orderService.createOrder(cart,c);
        orderService.changeOrderStatus(o.getOrderId(), "DELIVERED");
        boolean returnResult = orderService.returnOrder(o.getOrderId());
        assertThat(returnResult).isTrue();
    }

    @Test
    void updateOrder() {
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

        billingService.saveCreditCard(c);
        addressService.saveAddress(a);
        movieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.setUsername("username");
        cart.addMovieToCart(m,5);

        Order o = orderService.createOrder(cart,c);
        o.setOrderStatus("SHIPPED");
        orderService.updateOrder(o.getOrderId(), o);
    }

    @Test
    void createOrderLoyaltyPoints() {
        User u = new User();
        u.setLoyaltyPoints(50);
        u.setPassword("password");
        u.setUsername("username");
        u.setAccountType("customer");
        u.setEmailAddress("a.gangapersaud@gmail.com");

        LoyaltyPoints lp = new LoyaltyPoints(50);

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

        userService.register(u);
        addressService.saveAddress(a);
        movieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.setUsername("username");
        cart.addMovieToCart(m,5);

        Order o = orderService.createOrder(cart,lp);
        assertThat(o).isNotNull();
    }

    @Test
    void createOrderLoyaltyPointsFail() {
        User u = new User();
        u.setLoyaltyPoints(25);
        u.setPassword("password");
        u.setUsername("username");
        u.setAccountType("customer");
        u.setEmailAddress("a.gangapersaud@gmail.com");

        LoyaltyPoints lp = new LoyaltyPoints(25);

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

        userService.register(u);
        addressService.saveAddress(a);
        movieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.setUsername("username");
        cart.addMovieToCart(m,5);

        Order o = orderService.createOrder(cart,lp);
        assertThat(o).isNull();
    }

}