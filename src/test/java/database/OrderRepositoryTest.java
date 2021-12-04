package database;

import model.*;
import model.Order;
import model.payments.CreditCard;
import model.payments.LoyaltyPoints;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderRepositoryTest {

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
    void testCancelOrderSuccess() {
        Order order = createOrderCreditCard("username1");
        boolean cancelResult = testOrderRepository.cancelOrder(order.getOrderId());
        assertThat(cancelResult).isTrue();
    }

    @Test
    void testCancelOrderStatusNotProcessed() {
        Order order = createOrderCreditCard("username2");
        testOrderRepository.changeOrderStatus(order.getOrderId(), "DELIVERED");
        boolean cancelResult = testOrderRepository.cancelOrder(order.getOrderId());
        assertThat(cancelResult).isFalse();
    }

    @Test
    void testCancelOrderNotExist() {
        boolean result = testOrderRepository.cancelOrder(24);
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

        testMovieRepository.addMovie(m,5);

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

        boolean saveGuestResult = testUserRepository.saveGuestAccount(u);
        assertThat(saveGuestResult).isEqualTo(true);

        boolean saveBillingResult = testBillingRepository.saveCreditCard(c);
        assertThat(saveBillingResult).isEqualTo(true);

        Order order = testOrderRepository.createOrder(cart, c);
        assertThat(order).isNull();
    }

    @Test
    void testDeleteNotExistingOrder() {
        testOrderRepository.deleteOrder(35);
    }

    @Test
    void testDeleteExistingOrder() {
        Order order = createOrderCreditCard("username6");
        testOrderRepository.deleteOrder(order.getOrderId());
    }

    @Test
    @Disabled
    void testGetAllOrders() {
        Order order = createOrderCreditCard("username6");
        List<Order> expected = new ArrayList<>();
        expected.add(order);
        List<Order> result = testOrderRepository.getAllOrders();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testGetOrdersByCustomer() {
        Order o1 = createOrderCreditCard("username7");
        createOrderCreditCard("username1");
        createOrderCreditCard("username2");

        List<Order> ordersByCustomer = new ArrayList<>();
        ordersByCustomer.add(o1);

        List<Order> result = testOrderRepository.getOrdersByCustomer("username7");

        assertThat(result).isEqualTo(ordersByCustomer);
    }

    @Test
    void testGetOrder() {
        Order expected = createOrderCreditCard("username8");
        Order result = testOrderRepository.getOrder(expected.getOrderId());
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @Disabled
    void getTotalOrders() {
        createOrderCreditCard("username9");
        createOrderCreditCard("username2");
        createOrderCreditCard("username3");

        assertThat(testOrderRepository.getTotalOrders()).isEqualTo(3);
    }

    @Test
    void testUpdateOrder() {
        Order o = createOrderCreditCard("username10");
        o.setOrderStatus("DELIVERED");
        boolean updateResult = testOrderRepository.updateOrder(o.getOrderId(), o);
        assertThat(updateResult).isTrue();
    }

    @Test
    void testUpdateOrderFailed() {
        Order o = createOrderCreditCard("username11");
        o.setOrderStatus("");
        boolean updateResult = testOrderRepository.updateOrder(o.getOrderId(), o);
        assertThat(updateResult).isFalse();
    }

    @Test
    void testReturnOrderSuccess() {
        Order o = createOrderCreditCard("username12");
        o.setOrderStatus("DELIVERED");
        boolean returnResult = testOrderRepository.returnOrder(o.getOrderId());
        assertThat(returnResult).isTrue();
    }

    @Test
    void testReturnOrderFailed() {
        Order o = createOrderCreditCard("username13");
        boolean returnResult = testOrderRepository.returnOrder(o.getOrderId());
        assertThat(returnResult).isFalse();
    }

    @Test
    void testReturnOrderFailedNoOrder() {
        boolean returnResult = testOrderRepository.returnOrder(320);
        assertThat(returnResult).isFalse();
    }

    public static Order createOrderCreditCard(String username) {
        Movie m = new Movie();
        m.setBarcode("1");
        m.setTitle("test");
        m.setPrice(10);
        m.setGenre("Kids");
        m.setReleaseDate("01/01/01");

        testMovieRepository.addMovie(m,5);

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

        boolean saveAddressResult = testAddressRepository.saveAddress(a);
        assertThat(saveAddressResult).isEqualTo(true);

        boolean saveGuestResult = testUserRepository.saveGuestAccount(u);
        assertThat(saveGuestResult).isEqualTo(true);

        boolean saveBillingResult = testBillingRepository.saveCreditCard(c);
        assertThat(saveBillingResult).isEqualTo(true);
        return testOrderRepository.createOrder(cart, c);
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

        testMovieRepository.addMovie(m,5);

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

        boolean saveAddressResult = testAddressRepository.saveAddress(a);
        assertThat(saveAddressResult).isEqualTo(true);

        boolean saveGuestResult = testUserRepository.saveGuestAccount(u);
        assertThat(saveGuestResult).isEqualTo(true);

        Order o = testOrderRepository.createOrder(cart, lp);
        assertThat(o).isNull();
    }

    private Order createOrderLoyaltyPoints(String username) {
        Movie m = new Movie();
        m.setBarcode("1");
        m.setTitle("test");
        m.setPrice(10);
        m.setGenre("Kids");
        m.setReleaseDate("01/01/01");

        testMovieRepository.addMovie(m,5);

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

        boolean saveAddressResult = testAddressRepository.saveAddress(a);
        assertThat(saveAddressResult).isEqualTo(true);

        boolean saveGuestResult = testUserRepository.saveGuestAccount(u);
        assertThat(saveGuestResult).isEqualTo(true);

        return testOrderRepository.createOrder(cart, lp);
    }
}