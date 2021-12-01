package services;

import model.Address;
import model.Cart;

public interface OrderService {

    boolean createOrder(Cart cart, PaymentService paymentMethod, Address shipping);

}
