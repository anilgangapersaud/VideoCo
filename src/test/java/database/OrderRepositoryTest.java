package database;

import model.*;
import model.Order;
import model.payments.CreditCard;
import model.payments.LoyaltyPoints;
import org.junit.jupiter.api.*;
import services.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderRepositoryTest {

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
    void testCancelOrderSuccess() {
        Order order = createOrderCreditCard("username1");
        boolean cancelResult = testOrderService.cancelOrder(order.getOrderId());
        assertThat(cancelResult).isTrue();
    }

    @Test
    void testCancelOrderStatusNotProcessed() {
        Order order = createOrderCreditCard("username2");
        testOrderService.changeOrderStatus(order.getOrderId(), "DELIVERED");
        boolean cancelResult = testOrderService.cancelOrder(order.getOrderId());
        assertThat(cancelResult).isFalse();
    }

    @Test
    void testCancelOrderNotExist() {
        boolean result = testOrderService.cancelOrder(24);
        assertThat(result).isFalse();
    }

    @Test
    void testCreateOrderSuccess() {
        Order order = createOrderCreditCard("username3");

        assertThat(order).isNotNull();
        assertThat(order.getOrderId()).isNotNull();
        assertThat(order.getOrderDate()).isNotNull();
        assertThat(order.getDueDate()).isNotNull();
        assertThat(order.getOrderStatus()).isNotNull();
        assertThat(order.getUsername()).isNotNull();
        assertThat(order.getMovies()).isNotNull();
    }

    @Test
    void testCreateOrderNoAddressFailed() {
        Movie m = new Movie();
        m.setBarcode("1");
        m.setTitle("test");
        m.setPrice(10);
        m.setGenre("Kids");
        m.setReleaseDate("01/01/01");

        testMovieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.addMovieToCart(m, 2);
        cart.setUsername("username5");

        CreditCard c = new CreditCard();
        c.setUsername("username5");
        c.setCardNumber("123");
        c.setExpiry("01/13");
        c.setCsv("123");
        c.setBalance(0);

        User u = new User();
        u.setUsername("username5");
        u.setPassword("username5");
        u.setEmailAddress("");

        boolean saveGuestResult = testUserService.saveGuestAccount(u);
        assertThat(saveGuestResult).isEqualTo(true);

        boolean saveBillingResult = testBillingService.saveCreditCard(c);
        assertThat(saveBillingResult).isEqualTo(true);

        Order order = testOrderService.createOrder(cart, c);
        assertThat(order).isNull();
    }

    @Test
    void testDeleteNotExistingOrder() {
        testOrderService.deleteOrder(35);
    }

    @Test
    void testDeleteExistingOrder() {
        Order order = createOrderCreditCard("username6");
        testOrderService.deleteOrder(order.getOrderId());
    }

    @Test
    @Disabled
    void testGetAllOrders() {
        Order order = createOrderCreditCard("username6");
        List<Order> expected = new ArrayList<>();
        expected.add(order);
        List<Order> result = testOrderService.getAllOrders();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testGetOrdersByCustomer() {
        Order o1 = createOrderCreditCard("username7");
        createOrderCreditCard("username1");
        createOrderCreditCard("username2");

        List<Order> ordersByCustomer = new ArrayList<>();
        ordersByCustomer.add(o1);

        List<Order> result = testOrderService.getOrdersByCustomer("username7");

        assertThat(result).isEqualTo(ordersByCustomer);
    }

    @Test
    void testGetOrder() {
        Order expected = createOrderCreditCard("username8");
        Order result = testOrderService.getOrder(expected.getOrderId());
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @Disabled
    void getTotalOrders() {
        createOrderCreditCard("username9");
        createOrderCreditCard("username2");
        createOrderCreditCard("username3");

        assertThat(testOrderService.getTotalOrders()).isEqualTo(3);
    }

    @Test
    void testUpdateOrder() {
        Order o = createOrderCreditCard("username10");
        o.setOrderStatus("DELIVERED");
        boolean updateResult = testOrderService.updateOrder(o.getOrderId(), o);
        assertThat(updateResult).isTrue();
    }

    @Test
    void testUpdateOrderFailed() {
        Order o = createOrderCreditCard("username11");
        o.setOrderStatus("");
        boolean updateResult = testOrderService.updateOrder(o.getOrderId(), o);
        assertThat(updateResult).isFalse();
    }

    @Test
    void testReturnOrderSuccess() {
        Order o = createOrderCreditCard("username12");
        o.setOrderStatus("DELIVERED");
        boolean returnResult = testOrderService.returnOrder(o.getOrderId());
        assertThat(returnResult).isTrue();
    }

    @Test
    void testReturnOrderFailed() {
        Order o = createOrderCreditCard("username13");
        boolean returnResult = testOrderService.returnOrder(o.getOrderId());
        assertThat(returnResult).isFalse();
    }

    @Test
    void testReturnOrderFailedNoOrder() {
        boolean returnResult = testOrderService.returnOrder(320);
        assertThat(returnResult).isFalse();
    }

    public static Order createOrderCreditCard(String username) {
        Movie m = new Movie();
        m.setBarcode("1");
        m.setTitle("test");
        m.setPrice(10);
        m.setGenre("Kids");
        m.setReleaseDate("01/01/01");

        testMovieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.addMovieToCart(m, 2);
        cart.setUsername(username);

        CreditCard c = new CreditCard();
        c.setUsername(username);
        c.setCardNumber("123");
        c.setExpiry("01/13");
        c.setCsv("123");
        c.setBalance(0);

        Address a = new Address();
        a.setUsername(username);
        a.setLineAddress("3439820493");
        a.setProvince("ONtario");
        a.setPostalCode("m3n2");
        a.setCity("toronto");

        User u = new User();
        u.setUsername(username);
        u.setPassword(username);
        u.setEmailAddress("");

        boolean saveAddressResult = testAddressService.saveAddress(a);
        assertThat(saveAddressResult).isEqualTo(true);

        boolean saveGuestResult = testUserService.saveGuestAccount(u);
        assertThat(saveGuestResult).isEqualTo(true);

        boolean saveBillingResult = testBillingService.saveCreditCard(c);
        assertThat(saveBillingResult).isEqualTo(true);
        return testOrderService.createOrder(cart, c);
    }

    @Test
    void testCreateOrderWithLoyaltyPoints() {
        Order o = createOrderLoyaltyPoints("username");
        assertThat(o).isNotNull();
    }

    @Test
    void testCreateOrderFailedWithLoyaltyPoints() {
        Movie m = new Movie();
        m.setBarcode("1");
        m.setTitle("test");
        m.setPrice(10);
        m.setGenre("Kids");
        m.setReleaseDate("01/01/01");

        testMovieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.addMovieToCart(m, 2);
        cart.setUsername("username23");

        LoyaltyPoints lp = new LoyaltyPoints(10);

        Address a = new Address();
        a.setUsername("username23");
        a.setLineAddress("3439820493");
        a.setProvince("ONtario");
        a.setPostalCode("m3n2");
        a.setCity("toronto");

        User u = new User();
        u.setUsername("username23");
        u.setPassword("userrname23");
        u.setEmailAddress("");

        boolean saveAddressResult = testAddressService.saveAddress(a);
        assertThat(saveAddressResult).isEqualTo(true);

        boolean saveGuestResult = testUserService.saveGuestAccount(u);
        assertThat(saveGuestResult).isEqualTo(true);

        Order o = testOrderService.createOrder(cart, lp);
        assertThat(o).isNull();
    }

    private Order createOrderLoyaltyPoints(String username) {
        Movie m = new Movie();
        m.setBarcode("1");
        m.setTitle("test");
        m.setPrice(10);
        m.setGenre("Kids");
        m.setReleaseDate("01/01/01");

        testMovieService.addMovie(m,5);

        Cart cart = new Cart();
        cart.addMovieToCart(m, 2);
        cart.setUsername(username);

        LoyaltyPoints lp = new LoyaltyPoints(20);

        Address a = new Address();
        a.setUsername(username);
        a.setLineAddress("3439820493");
        a.setProvince("ONtario");
        a.setPostalCode("m3n2");
        a.setCity("toronto");

        User u = new User();
        u.setUsername(username);
        u.setPassword(username);
        u.setEmailAddress("");

        boolean saveAddressResult = testAddressService.saveAddress(a);
        assertThat(saveAddressResult).isEqualTo(true);

        boolean saveGuestResult = testUserService.saveGuestAccount(u);
        assertThat(saveGuestResult).isEqualTo(true);

        return testOrderService.createOrder(cart, lp);
    }
}