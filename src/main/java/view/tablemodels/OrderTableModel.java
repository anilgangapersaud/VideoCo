package view.tablemodels;

import database.Observer;
import database.OrderRepository;
import model.Model;
import model.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.util.List;

public class OrderTableModel extends DefaultTableModel implements Observer {

    private final JTable view;
    private final JComboBox<String> orderStatus;

    public OrderTableModel(JTable table, JComboBox<String> orderStatus) {
        view = table;
        this.orderStatus = orderStatus;
        OrderRepository.getInstance().registerObserver(this);
        List<Order> orders;
        if (Model.getUserService().getLoggedInUser().isAdmin()) {
            orders = Model.getOrderService().getAllOrders();
        } else {
            orders = Model.getOrderService().getOrdersByCustomer(Model.getUserService().getLoggedInUser().getUsername());
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
        table.setModel(this);
        TableColumn statusColumn = table.getColumnModel().getColumn(1);
        statusColumn.setCellEditor(new DefaultCellEditor(orderStatus));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 1 && Model.getUserService().getLoggedInUser().isAdmin();
    }

    @Override
    public void update() {
        List<Order> orders;
        if (Model.getUserService().getLoggedInUser().isAdmin()) {
            orders = Model.getOrderService().getAllOrders();
        } else {
            orders = Model.getOrderService().getOrdersByCustomer(Model.getUserService().getLoggedInUser().getUsername());
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
        TableColumn statusColumn = view.getColumnModel().getColumn(1);
        statusColumn.setCellEditor(new DefaultCellEditor(orderStatus));
    }
}
