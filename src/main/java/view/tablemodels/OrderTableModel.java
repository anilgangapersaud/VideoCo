package view.tablemodels;

import database.Observer;
import model.Order;
import services.OrderService;
import services.UserService;
import view.StoreFront;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.util.List;

public class OrderTableModel extends DefaultTableModel implements Observer {

    private final JTable view;
    private final JComboBox<String> orderStatus;
    private final OrderService orderService;
    private final UserService userService;

    public OrderTableModel(JTable table, JComboBox<String> orderStatus) {
        view = table;
        orderService = StoreFront.getOrderService();
        userService = StoreFront.getUserService();
        this.orderStatus = orderStatus;
        subscribe();
        updateTable();
    }

    private void subscribe() {
        StoreFront.getOrderService().registerObserver(this);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 1 && userService.getLoggedInUser().isAdmin();
    }

    private void updateTable() {
        List<Order> orders;
        if (userService.getLoggedInUser().isAdmin()) {
            orders = orderService.getAllOrders();
        } else {
            orders = orderService.getOrdersByCustomer(userService.getLoggedInUser().getUsername());
        }
        String[][] data = new String[orders.size()][5];
        String[] column = {"NUMBER","STATUS","DATE","DUEDATE","OVERDUE"};
        int i = 0;
        for (Order o : orders) {
            data[i][0] = String.valueOf(o.getOrderId());
            data[i][1] = o.getOrderStatus();
            data[i][2] = o.getOrderDate();
            data[i][3] = o.getDueDate();
            data[i][4] = String.valueOf(o.getOverdue());
            i++;
        }
        setDataVector(data,column);
        if (userService.getLoggedInUser().isAdmin()) {
            TableColumn statusColumn = view.getColumnModel().getColumn(1);
            statusColumn.setCellEditor(new DefaultCellEditor(orderStatus));
        }
    }

    @Override
    public void update() {
        updateTable();
    }
}
