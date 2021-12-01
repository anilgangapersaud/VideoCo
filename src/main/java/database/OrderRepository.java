package database;

import model.Order;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository implements DatabaseAccess {

    private final Map<Integer, Order> orderDatabase;

    private static final String ORDER_FILE_PATH = "/src/main/resources/orders.csv";
    private static final String orderPath = System.getProperty("user.dir") + ORDER_FILE_PATH;

    public OrderRepository() {
        orderDatabase = new HashMap<>();
        load();
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
}

