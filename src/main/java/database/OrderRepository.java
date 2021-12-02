package database;

import model.Model;
import model.Order;
import model.User;
import model.payments.CreditCard;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The repository to manage orders and common operations on orders
 */
public class OrderRepository implements DatabaseAccess {

    /**
     * Periodically checks for overdue orders in the database and notifies users for late fees
     */
    public static class CheckOverdueOrders extends TimerTask {

        public void run() {
            System.out.println("Running daily check for overdue orders...");
            List<Order> orders = orderRepositoryInstance.getAllOrders();
            for (Order o : orders) {
                String dueDate = o.getDueDate();
                try {
                    Date dueDateP = new SimpleDateFormat("yyyy/MM/dd").parse(dueDate);
                    Date todaysDate = Calendar.getInstance().getTime();
                    if (dueDateP.before(todaysDate)) {
                        o.setOverdue(true);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            for (Order o : orders) {
                if (o.getOrderStatus().equals("DELIVERED") && o.getOverdue()) {
                    // charge the users credit card $1.00 for each movie
                    CreditCard c = Model.getBillingService().getCreditCard(o.getUsername());
                    int totalMovies = orderRepositoryInstance.rentedRepository.countMoviesInOrder(o.getOrderId());
                    double charge = 1.00D * totalMovies;
                    c.charge(charge);
                    System.out.println("Charging " + o.getUsername() + " " + charge + " for an overdue order");
                }
            }
        }
    }

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

    private Set<User> observers;

    private OrderRepository() {
        orderDatabase = new HashMap<>();
        observers = new HashSet<>();
        rentedRepository = RentedRepository.getInstance();
        load();

        Timer timer = new Timer();
        CheckOverdueOrders overdueOrders = new CheckOverdueOrders();
        timer.schedule(overdueOrders, Calendar.getInstance().getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
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
    public void load() {
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
    public void update() {
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
            orderDatabase.remove(orderNumber);
            rentedRepository.returnMovies(orderNumber);
            update();
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
        update();
    }

    /**
     * create an order
     * @param o the order to create
     */
    public void createOrder(Order o) {
        orderDatabase.put(o.getOrderId(), o);
        rentedRepository.storeMovies(o.getOrderId(), o.getMovies());
        update();
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
            o.setOrderStatus("RETURNED");
            o.setOverdue(false);
            orderDatabase.replace(orderNumber, o);
            return true;
        }
    }

}

