package database;

import model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class OrderRepository implements DatabaseAccess, Subject {


    private final Map<Integer, Order> orderDatabase;

    private final String ORDER_CSV_PATH;

    private volatile static OrderRepository orderRepositoryInstance;

    private final List<Observer> observers;

    private OrderRepository(String path) {
        ORDER_CSV_PATH = path;
        clearCSV();
        orderDatabase = new HashMap<>();
        observers = new ArrayList<>();
        loadCSV();
    }

    public static OrderRepository getInstance(String path) {
        if (orderRepositoryInstance == null) {
            synchronized (OrderRepository.class) {
                if (orderRepositoryInstance == null) {
                    orderRepositoryInstance = new OrderRepository(path);
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

    public void cancelOrder(int orderNumber) {
        orderDatabase.remove(orderNumber);
        updateCSV();
    }

    public void changeOrderStatus(int orderNumber, String status) {
        Order o = orderDatabase.get(orderNumber);
        o.setOrderStatus(status);
        orderDatabase.replace(orderNumber, o);
        updateCSV();
    }

    public void createOrder(Order o) {
        orderDatabase.put(o.getOrderId(), o);
        updateCSV();
    }

    public void deleteOrder(int orderNumber) {
        orderDatabase.remove(orderNumber);
        updateCSV();
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

    public boolean returnOrder(Order order) {
        orderDatabase.replace(order.getOrderId(), order);
        updateCSV();
        return true;
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
}

