package database;

import model.Order;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import scheduledtasks.CheckOverdueOrders;
import scheduledtasks.DeliverOrders;
import scheduledtasks.ShipOrders;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The repository to manage orders and common operations on orders
 */
public class OrderRepository implements DatabaseAccess, Subject {

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
    private static OrderRepository orderRepositoryInstance = null;

    /**
     * maintains the movies currently out for rent
     */
    private final RentedRepository rentedRepository;

    private final BillingRepository billingRepository;

    private final List<Observer> observers;

    private OrderRepository() {
        orderDatabase = new HashMap<>();
        rentedRepository = RentedRepository.getInstance();
        billingRepository = BillingRepository.getInstance();
        observers = new ArrayList<>();
        loadCSV();

        Timer timer = new Timer();
        timer.schedule(new CheckOverdueOrders(), Calendar.getInstance().getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        timer.schedule(new DeliverOrders(), Calendar.getInstance().getTime(), 600000);
        timer.schedule(new ShipOrders(), Calendar.getInstance().getTime(), 900000);
    }

    /**
     * @return the singleton instance of this class
     */
    public static OrderRepository getInstance() {
        if (orderRepositoryInstance == null) {
            orderRepositoryInstance = new OrderRepository();
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
            billingRepository.refundCustomer(o.getUsername(), rentedRepository.getOrderTotal(orderNumber));
            orderDatabase.remove(orderNumber);
            rentedRepository.returnMovies(orderNumber);
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
     * @param o the order to create
     */
    public void createOrder(Order o) {
        orderDatabase.put(o.getOrderId(), o);
        rentedRepository.storeMovies(o.getOrderId(), o.getMovies());
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
            rentedRepository.returnMovies(orderNumber);
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
}

