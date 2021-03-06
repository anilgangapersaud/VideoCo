package services;

import database.Observer;
import database.OrderRepository;
import model.Cart;
import model.Movie;
import model.Order;
import model.User;
import model.payments.CreditCard;
import model.payments.LoyaltyPoints;
import model.payments.PaymentService;
import model.payments.PaymentVisitor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class OrderService implements PaymentVisitor {

    private final OrderRepository orderRepository;

    private static String ORDER_CSV_PATH;

    private volatile static OrderService instance;

    private OrderService() {
        orderRepository = OrderRepository.getInstance(ORDER_CSV_PATH);
    }

    public static OrderService getInstance() {
        if (instance == null) {
            synchronized (OrderService.class) {
                if (instance == null) {
                    instance = new OrderService();
                }
            }
        }
        return instance;
    }

    public static void setCsvPath(String path) {
        ORDER_CSV_PATH = path;
    }

    public boolean cancelOrder(int orderNumber) {
        Order o = orderRepository.getOrder(orderNumber);
        if (o == null || !o.getOrderStatus().equals("PROCESSED")) {
            return false;
        } else {
            orderRepository.cancelOrder(orderNumber);
            getBillingService().refundCustomer(o.getUsername(), getRentedService().getOrderTotal(orderNumber));
            getRentedService().returnMovies(orderNumber);
            return true;
        }
    }

    public Order createOrder(Cart cart, PaymentService paymentService) {
        User u = getUserService().getLoggedInUser();
        String username;
        if (u != null) {
            username = u.getUsername();
        } else {
            username = cart.getUsername();
        }
        if (getAddressService().getAddress(username) != null) {
            if (paymentService.acceptPayment(this, cart)) {
                if (getMovieService().rentMovies(cart.getMoviesInCart())) {
                    Order o = new Order();
                    o.setUsername(username);
                    o.setOrderId(orderRepository.getTotalOrders());
                    o.setOrderDate(getDate());
                    o.setDueDate(getDueDate());
                    o.setOrderStatus("PROCESSED");
                    o.setMovies(cart.getMoviesInCart());
                    orderRepository.createOrder(o);
                    getRentedService().storeMovies(o);
                    if (u != null) {
                        getUserService().awardLoyaltyPoint(username);
                    }
                    return o;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void changeOrderStatus(int orderNumber, String status) {
        Order o = orderRepository.getOrder(orderNumber);
        if (o != null) {
            o.setOrderStatus(status);
        }
        orderRepository.updateOrder(orderNumber, o);
    }

    public void deleteOrder(int orderNumber) {
        Order o = orderRepository.getOrder(orderNumber);
        if (o != null) {
            getRentedService().returnMovies(orderNumber);
            orderRepository.deleteOrder(orderNumber);
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public List<Order> getOrdersByCustomer(String username) {
        return orderRepository.getOrdersByCustomer(username);
    }

    public Order getOrder(int orderNumber) {
        return orderRepository.getOrder(orderNumber);
    }

    public boolean returnOrder(int orderNumber) {
        Order o = orderRepository.getOrder(orderNumber);
        if (o == null || !o.getOrderStatus().equals("DELIVERED")) {
            return false;
        } else {
            o.setDueDate("");
            o.setOrderStatus("COMPLETED");
            o.setOverdue(false);
            orderRepository.returnOrder(o);
            getRentedService().returnMovies(orderNumber);
            return true;
        }
    }

    public void updateOrder(int orderNumber, Order o) {
        orderRepository.updateOrder(orderNumber, o);
    }

    private String getDate() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(today.getTime());
    }

    private String getDueDate() {
        Calendar calender = Calendar.getInstance();
        calender.add(Calendar.DATE, 14);
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(calender.getTime());
    }

    @Override
    public boolean visitLoyaltyPoints(LoyaltyPoints loyaltyPoints, Cart cart) {
        int totalMovies = 0;
        for (Map.Entry<Movie,Integer> entry : cart.getMoviesInCart().entrySet()) {
            totalMovies += entry.getValue();
        }
        if (loyaltyPoints.getLoyaltyPoints() >= totalMovies * 10) {
            int deduction = totalMovies * 10;
            User u = getUserService().getUser(cart.getUsername());
            u.setLoyaltyPoints(loyaltyPoints.getLoyaltyPoints() - deduction);
            getUserService().updateUser(u);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean visitCreditCard(CreditCard creditCard, Cart cart) {
        for (Map.Entry<Movie, Integer> entry : cart.getMoviesInCart().entrySet()) {
            creditCard.charge(entry.getKey().getPrice() * entry.getValue());
            getBillingService().updateCreditCard(creditCard);
        }
        return true;
    }

    public void registerObserver(Observer o) {
        orderRepository.registerObserver(o);
    }

    public void removeObserver(Observer o) {
        orderRepository.removeObserver(o);
    }


    private BillingService getBillingService() {
        return BillingService.getInstance();
    }

    private RentedService getRentedService() {
        return RentedService.getInstance();
    }

    private UserService getUserService() {
        return UserService.getInstance();
    }

    private AddressService getAddressService() {
        return AddressService.getInstance();
    }

    private MovieService getMovieService() {
        return MovieService.getInstance();
    }
}
