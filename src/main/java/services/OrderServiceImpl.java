package services;

import database.OrderRepository;
import model.Address;
import model.Cart;

public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    public OrderServiceImpl() {
        orderRepository = new OrderRepository();
    }

    @Override
    public boolean createOrder(Cart cart, PaymentService paymentMethod, Address shipping) {
        boolean paymentAccepted = paymentMethod.pay(cart.getMoviesInCart());
        if (paymentAccepted) {
            return true;
        } else {
            return false;
        }
    }
}
