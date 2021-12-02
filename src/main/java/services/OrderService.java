package services;

import model.Address;
import model.Cart;
import model.Order;

import java.util.List;

public interface OrderService {

    boolean cancelOrder(int orderNumber);

    void changeOrderStatus(int orderNumber, String status);

    boolean createOrder(Cart cart, PaymentService paymentMethod, Address shipping);

    List<Order> getAllOrders();

    List<Order> getOrdersByCustomer(String username);

    boolean returnMovies(int orderNumber);

}
