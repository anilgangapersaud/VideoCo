package database;

import model.Movie;
import model.Order;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderRepositoryTest {

    private OrderRepository underTest;

    @BeforeEach
    void setup() {
        underTest = OrderRepository.getInstance(TestConfigs.ORDER_CSV__TEST_PATH);
    }

    @Test
    void testCancelOrder() {
        underTest.cancelOrder(23);
    }

    @Test
    void testChangeOrderStatusFailed() {
        underTest.changeOrderStatus(24, "PROCESSED");
    }

    @Test
    void testChangeOrderStatusSuccess() {
        Order o = new Order();
        o.setOrderStatus("PROCESSED");
        o.setUsername("username");
        o.setOrderId(23);
        o.setOverdue(false);
        o.setDueDate("12/12/12");
        o.setMovies(new HashMap<Movie,Integer>());
        underTest.createOrder(o);

        underTest.changeOrderStatus(23, "DELIVERED");
    }

    @Test
    void testDeleteOrder() {
        underTest.deleteOrder(23);
    }

    @Test
    void testGetAllOrders() {
        Order o = new Order();
        o.setOrderStatus("PROCESSED");
        o.setUsername("username");
        o.setOrderId(23);
        o.setOverdue(false);
        o.setDueDate("12/12/12");
        o.setMovies(new HashMap<Movie,Integer>());
        underTest.createOrder(o);

        List<Order> expected = new ArrayList<>();
        expected.add(o);

        assertThat(expected).isEqualTo(underTest.getAllOrders());
    }

    @Test
    void testGetOrdersByCustomer() {
        Order o = new Order();
        o.setOrderStatus("PROCESSED");
        o.setUsername("username");
        o.setOrderId(23);
        o.setOverdue(false);
        o.setDueDate("12/12/12");
        o.setMovies(new HashMap<Movie,Integer>());
        underTest.createOrder(o);

        List<Order> expected = new ArrayList<>();
        expected.add(o);

        assertThat(expected).isEqualTo(underTest.getOrdersByCustomer("username"));
    }

    @Test
    void testGetOrder() {
        Order o = underTest.getOrder(99);
        assertThat(o).isNull();
    }

    @Test
    void testGetTotalOrders() {
        underTest.getTotalOrders();
    }

    @Test
    void testUpdateOrderSuccess() {
        Order o = new Order();
        o.setOrderStatus("PROCESSED");
        o.setUsername("username");
        o.setOrderId(23);
        o.setOverdue(false);
        o.setOrderDate("12/12/12");
        o.setDueDate("12/12/12");
        o.setMovies(new HashMap<Movie,Integer>());
        underTest.createOrder(o);

        o.setOrderStatus("DELIVERED");

        boolean result = underTest.updateOrder(23, o);
        assertThat(result).isTrue();
    }

    @Test
    void testUpdateOrderFailed() {
        Order o = new Order();
        o.setOrderStatus("PROCESSED");
        o.setUsername("username");
        o.setOrderId(23);
        o.setOverdue(false);
        o.setDueDate("12/12/12");
        o.setMovies(new HashMap<Movie,Integer>());
        underTest.createOrder(o);

        o.setOrderStatus("DELIVERED");

        boolean result = underTest.updateOrder(23, o);
        assertThat(result).isFalse();
    }

    @Test
    void testReturnOrder() {
        underTest.returnOrder(new Order());
    }

}