package database;

import model.*;
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

public class OrderRepository implements DatabaseAccess, Subject, PaymentVisitor {


    private final Map<Integer, Order> orderDatabase;

    private static final String ORDER_CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/orders.csv";

    private volatile static OrderRepository orderRepositoryInstance;

    private final List<Observer> observers;

    private OrderRepository() {
        clearCSV();
        orderDatabase = new HashMap<>();
        observers = new ArrayList<>();
        loadCSV();
    }

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
            CSVParser parser = new CSVParser(new FileReader(ORDER_CSV_PATH), CSVFormat.RFC4180
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
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(ORDER_CSV_PATH, false),
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

    @Override
    public void clearCSV() {
        try {
            FileWriter fw = new FileWriter(ORDER_CSV_PATH, false);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean cancelOrder(int orderNumber) {
        if (orderDatabase.containsKey(orderNumber)) {
            Order o = orderDatabase.get(orderNumber);
            if (o == null || !o.getOrderStatus().equals("PROCESSED")) {
                return false;
            } else {
                getBillingRepository().refundCustomer(o.getUsername(), getRentedRepository().getOrderTotal(orderNumber));
                orderDatabase.remove(orderNumber);
                getRentedRepository().returnMovies(orderNumber);
                updateCSV();
                return true;
            }
        }
        return false;
    }

    public void changeOrderStatus(int orderNumber, String status) {
        Order o = orderDatabase.get(orderNumber);
        o.setOrderStatus(status);
        orderDatabase.replace(orderNumber, o);
        updateCSV();
    }

    public Order createOrder(Cart cart, PaymentService paymentMethod) {
        User u = getUserRepository().getLoggedInUser();
        String username;
        if (u != null) {
            username = u.getUsername();
        }  else {
            username = cart.getUsername();
        }
        if (getAddressRepository().checkAddressExists(username)) {
            if (paymentMethod.acceptPayment(this, cart)) {
                if (getMovieRepository().rentMovies(cart.getMoviesInCart())) {
                    Order o = new Order();
                    o.setUsername(username);
                    o.setOrderId(getTotalOrders());
                    o.setOrderDate(getDate());
                    o.setOrderStatus("PROCESSED");
                    o.setOverdue(false);
                    o.setDueDate(getDueDate());
                    o.setMovies(cart.getMoviesInCart());
                    orderDatabase.put(o.getOrderId(), o);
                    getRentedRepository().storeMovies(o.getOrderId(), o.getMovies());
                    // award a loyalty point
                    getUserRepository().awardLoyaltyPoint(username);
                    updateCSV();
                    return o;
                }
            }
        }
        return null;
    }

    public void deleteOrder(int orderNumber) {
        if (orderDatabase.containsKey(orderNumber)) {
            getRentedRepository().returnMovies(orderNumber);
            orderDatabase.remove(orderNumber);
            updateCSV();
        }
    }

    public List<Order> getAllOrders() {
        List<Order> allOrders = new ArrayList<>();
        for (Map.Entry<Integer, Order> entry : orderDatabase.entrySet()) {
            allOrders.add(entry.getValue());
        }
        return allOrders;
    }

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

    public int getTotalOrders() {
        return orderDatabase.size();
    }

    public boolean updateOrder(int orderNumber, Order o) {
        if (validateOrder(o) && orderDatabase.containsKey(orderNumber)) {
            orderDatabase.replace(orderNumber, o);
            updateCSV();
            return true;
        } else {
            return false;
        }
    }

    public boolean returnOrder(int orderNumber) {
        if (orderDatabase.containsKey(orderNumber)) {
            Order o = orderDatabase.get(orderNumber);
            if (o == null || !o.getOrderStatus().equals("DELIVERED")) {
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
        } else {
            return false;
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
    public boolean visitLoyaltyPoints(LoyaltyPoints loyaltyPoints, Cart cart) {
        int totalMovies = 0;
        for (Map.Entry<Movie,Integer> entry : cart.getMoviesInCart().entrySet()) {
            totalMovies += entry.getValue();
        }
        if (loyaltyPoints.getLoyaltyPoints() >= totalMovies * 10) {
            int deduction = totalMovies * 10;
            User u = getUserRepository().getUser(cart.getUsername());
            u.setLoyaltyPoints(loyaltyPoints.getLoyaltyPoints() - deduction);
            getUserRepository().updateUser(u);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean visitCreditCard(CreditCard creditCard, Cart cart) {
        for (Map.Entry<Movie, Integer> entry : cart.getMoviesInCart().entrySet()) {
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

    private boolean validateOrder(Order o) {
        if (o == null) {
            return false;
        } else {
            if (o.getOrderId() < 0 || o.getOrderStatus() == null
            || o.getOrderDate() == null || o.getDueDate() == null || o.getUsername() == null
        || o.getMovies() == null) {
                return false;
            } else {
                return !o.getOrderStatus().equals("") && !o.getOrderDate().equals("")
                        && !o.getUsername().equals("") && !o.getDueDate().equals("");
            }
        }
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

    private AddressRepository getAddressRepository() { return AddressRepository.getInstance(); }

}

