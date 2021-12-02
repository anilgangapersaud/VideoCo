package services;

import database.MovieRepository;
import database.OrderRepository;
import database.UserRepository;
import model.Address;
import model.Cart;
import model.Order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl() {
        orderRepository = OrderRepository.getInstance();
        movieRepository = MovieRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }

    private String getDate() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        return df.format(today.getTime());
    }

    private String getDueDate() {
        Calendar calender = Calendar.getInstance();
        calender.add(Calendar.DATE, 14);
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        return df.format(calender.getTime());
    }

    @Override
    public boolean createOrder(Cart cart, PaymentService paymentMethod, Address shipping) {
        if (paymentMethod.pay(cart.getMoviesInCart())) {
            // remove the stock from the movie repository
            if (movieRepository.rentMovies(cart.getMoviesInCart())) {
                // give the customer a loyalty point
                userRepository.awardLoyaltyPoint(shipping.getUsername());
                // create the order
                Order o = new Order();
                o.setOrderId(orderRepository.getTotalOrders());
                o.setOrderDate(getDate());
                o.setOrderStatus("PROCESSED");
                o.setUsername(shipping.getUsername());
                o.setOverdue(false);
                o.setDueDate(getDueDate());
                o.setMovies(cart.getMoviesInCart());
                orderRepository.createOrder(o);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public List<Order> getOrdersByCustomer(String username) {
        return orderRepository.getOrdersByCustomer(username);
    }

    @Override
    public boolean cancelOrder(int orderNumber) {
        return orderRepository.cancelOrder(orderNumber);
    }

    @Override
    public boolean returnMovies(int orderNumber) {
        return orderRepository.returnOrder(orderNumber);
    }
}
