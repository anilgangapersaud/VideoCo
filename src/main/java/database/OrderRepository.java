package database;

import model.Address;
import model.Cart;
import model.Movie;
import model.Order;
import model.payments.CreditCard;
import model.payments.LoyaltyPoints;
import model.payments.PaymentService;
import model.payments.PaymentVisitor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The repository to manage orders and common operations on orders
 */
public class OrderRepository implements DatabaseAccess, Subject, PaymentVisitor {

    /**
     * the order database
     */
    private final Map<Integer, Order> orderDatabase;

    /**
     * configs
     */
    private static final String ORDER_FILE_PATH = "/src/main/resources/orders.csv";
    private static final String orderPath = System.getProperty("user.dir") + ORDER_FILE_PATH;

    /**
     * singleton instance
     */
    private volatile static OrderRepository orderRepositoryInstance;

    private final List<Observer> observers;

    private OrderRepository() {
        orderDatabase = new HashMap<>();
        observers = new ArrayList<>();
        loadCSV();
    }

    /**
     * @return the singleton instance of this class
     */
    public static OrderRepository getInstance() {
        if (orderRepositoryInstance == null) {
            synchronized (OrderRepository.class) {
                if (orderRepositoryInstance == null) {
                    orderRepositoryInstance = new OrderRepository();
                }
            }
        }
        return orderRepositoryInstance;
    }

    @Override
    public void loadCSV() {
        try {
            CSVParser parser = new CSVParser(new FileReader(OrderRepository.orderPath), CSVFormat.RFC4180
                    .withDelimiter(',')
                    .withHeader(
                            "orderNumber",
                            "username",
                            "orderStatus",
                            "orderDate",
                            "dueDate",
                            "isOverdue"
                    ));
            List<CSVRecord> records = parser.getRecords();
            for (int i = 1; i < records.size(); i++) {
                Order order = new Order();
                order.setOrderId(Integer.parseInt(records.get(i).get("orderNumber")));
                order.setUsername(records.get(i).get("username"));
                order.setOrderStatus(records.get(i).get("orderStatus"));
                order.setOrderDate(records.get(i).get("orderDate"));
                order.setDueDate(records.get(i).get("dueDate"));
                order.setOverdue(Boolean.parseBoolean(records.get(i).get("isOverdue")));
                orderDatabase.put(order.getOrderId(), order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCSV() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(orderPath, false),
                CSVFormat.RFC4180.withDelimiter(',')
                        .withHeader("orderNumber",
                                    "username",
                                    "orderStatus",
                                    "orderDate",
                                    "dueDate",
                                    "isOverdue"
                                ))) {
            for (Map.Entry<Integer, Order> entry : orderDatabase.entrySet()) {
                Order o = entry.getValue();
                printer.printRecord(o.getOrderId(), o.getUsername(), o.getOrderStatus(),
                        o.getOrderDate(), o.getDueDate(), o.getOverdue());
            }
            notifyObservers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * cancel an order
     * @param orderNumber the order number of the order to be canceled
     * @return true if successful, false otherwise
     */
    public boolean cancelOrder(int orderNumber) {
        Order o = orderDatabase.get(orderNumber);
        if (o == null) {
            return false;
        }
        if (!o.getOrderStatus().equals("PROCESSED")) {
            return false;
        } else {
            getBillingRepository().refundCustomer(o.getUsername(), getRentedRepository().getOrderTotal(orderNumber));
            orderDatabase.remove(orderNumber);
            getRentedRepository().returnMovies(orderNumber);
            updateCSV();
            return true;
        }
    }

    /**
     * Update an order's status
     * @param orderNumber the order number to update
     * @param status the new order status
     */
    public void changeOrderStatus(int orderNumber, String status) {
        Order o = orderDatabase.get(orderNumber);
        o.setOrderStatus(status);
        orderDatabase.replace(orderNumber, o);
        updateCSV();
    }

    /**
     * create an order
     */
    public boolean createOrder(Cart cart, PaymentService paymentMethod, Address shipping) {
        if (paymentMethod.acceptPayment(this, cart.getMoviesInCart())) {
            if (getMovieRepository().rentMovies(cart.getMoviesInCart())) {
                getUserRepository().awardLoyaltyPoint(shipping.getUsername());
                Order o = new Order();
                o.setOrderId(getTotalOrders());
                o.setOrderDate(getDate());
                o.setOrderStatus("PROCESSED");
                o.setUsername(shipping.getUsername());
                o.setOverdue(false);
                o.setDueDate(getDueDate());
                o.setMovies(cart.getMoviesInCart());
                orderDatabase.put(o.getOrderId(), o);
                getRentedRepository().storeMovies(o.getOrderId(), o.getMovies());
                updateCSV();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Order createOrder(Map<Movie,Integer> movies, PaymentService paymentMethod, Address shipping) {
        if (paymentMethod.acceptPayment(this, movies)) {
            if (getMovieRepository().rentMovies(movies)) {
                Order o = new Order();
                o.setOrderId(getTotalOrders());
                o.setOrderDate(getDate());
                o.setOrderStatus("PROCESSED");
                o.setUsername(shipping.getUsername());
                o.setOverdue(false);
                o.setDueDate(getDueDate());
                o.setMovies(movies);
                orderDatabase.put(o.getOrderId(), o);
                getRentedRepository().storeMovies(o.getOrderId(), o.getMovies());
                updateCSV();
                return o;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    public void createOrder(Order o) {
        orderDatabase.put(o.getOrderId(), o);
        getRentedRepository().storeMovies(o.getOrderId(), o.getMovies());
        updateCSV();
    }

    public void deleteOrder(int orderNumber) {
        getRentedRepository().returnMovies(orderNumber);
        orderDatabase.remove(orderNumber);
        updateCSV();
    }

    /**
     * @return all orders in the system
     */
    public List<Order> getAllOrders() {
        List<Order> allOrders = new ArrayList<>();
        for (Map.Entry<Integer, Order> entry : orderDatabase.entrySet()) {
            allOrders.add(entry.getValue());
        }
        return allOrders;
    }

    /**
     * get all orders for a specific user
     * @param username the user
     * @return a list of orders connected to this customer
     */
    public List<Order> getOrdersByCustomer(String username) {
        List<Order> orders = new ArrayList<>();
        for (Map.Entry<Integer, Order> entry : orderDatabase.entrySet()) {
            Order o = entry.getValue();
            if (o.getUsername().equals(username)) {
                orders.add(entry.getValue());
            }
        }
        return orders;
    }

    public Order getOrder(int orderNumber) {
        return orderDatabase.get(orderNumber);
    }
    /**
     * get the total number of orders in the system
     * @return total orders
     */
    public int getTotalOrders() {
        return orderDatabase.size();
    }

    /**
     * Return movies to the system
     * @param orderNumber the order to return
     * @return true if successful, false otherwise
     */
    public boolean returnOrder(int orderNumber) {
        Order o = orderDatabase.get(orderNumber);
        if (o == null) {
            return false;
        }
        if (!o.getOrderStatus().equals("DELIVERED")) {
            return false;
        } else {
            getRentedRepository().returnMovies(orderNumber);
            o.setDueDate("");
            o.setOrderStatus("COMPLETED");
            o.setOverdue(false);
            orderDatabase.replace(orderNumber, o);
            updateCSV();
            return true;
        }
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

    @Override
    public boolean visitLoyaltyPoints(LoyaltyPoints loyaltyPoints, Map<Movie,Integer> movies) {
        int totalMovies = 0;
        for (Map.Entry<Movie,Integer> entry : movies.entrySet()) {
            totalMovies += entry.getValue();
        }
        if (loyaltyPoints.getLoyaltyPoints() >= totalMovies * 10) {
            int deduction = totalMovies * 10;
            getUserRepository().getLoggedInUser().setLoyaltyPoints(loyaltyPoints.getLoyaltyPoints() - deduction);
            getUserRepository().updateUser(getUserRepository().getLoggedInUser());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean visitCreditCard(CreditCard creditCard, Map<Movie, Integer> movies) {
        for (Map.Entry<Movie, Integer> entry : movies.entrySet()) {
            creditCard.charge(entry.getKey().getPrice() * entry.getValue());
            getBillingRepository().updateCreditCard(creditCard);
        }
        return true;
    }


    private String getDueDate() {
        Calendar calender = Calendar.getInstance();
        calender.add(Calendar.DATE, 14);
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(calender.getTime());
    }

    private String getDate() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(today.getTime());
    }

    private BillingRepository getBillingRepository() {
        return BillingRepository.getInstance();
    }

    private RentedRepository getRentedRepository() {
        return RentedRepository.getInstance();
    }

    private MovieRepository getMovieRepository() {
        return MovieRepository.getInstance();
    }

    private UserRepository getUserRepository() {
        return UserRepository.getInstance();
    }

}

