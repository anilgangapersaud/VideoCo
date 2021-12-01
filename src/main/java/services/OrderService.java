package services;

import model.Address;
import model.Cart;
import model.Order;

import java.util.List;

public interface OrderService {

    boolean createOrder(Cart cart, PaymentService paymentMethod, Address shipping);

    List<Order> getOrdersByCustomer(String username);

    boolean cancelOrder(int orderNumber);
}
