package database;

import model.Order;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The repository to manage orders and common operations on orders
 */
public class OrderRepository implements DatabaseAccess {

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
    private RentedRepository rentedRepository;

    private OrderRepository() {
        orderDatabase = new HashMap<>();
        rentedRepository = RentedRepository.getInstance();
        load();
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
     * create an order
     * @param o the order to create
     */
    public void createOrder(Order o) {
        orderDatabase.put(o.getOrderId(), o);
        rentedRepository.storeMovies(o.getOrderId(), o.getMovies());
        update();
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
        if (o.getOrderStatus().equals("DELIVERED")) {
            return false;
        } else {
            orderDatabase.remove(orderNumber);
            rentedRepository.returnMovies(orderNumber);
            update();
            return true;
        }
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
}

