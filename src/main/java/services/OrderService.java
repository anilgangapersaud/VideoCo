package services;

import model.Address;
import model.Cart;
import model.Order;

import java.util.List;

public interface OrderService {

    boolean cancelOrder(int orderNumber);

    boolean createOrder(Cart cart, PaymentService paymentMethod, Address shipping);

    List<Order> getOrdersByCustomer(String username);

    boolean returnMovies(int orderNumber);

}
