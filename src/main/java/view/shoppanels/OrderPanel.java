package view.shoppanels;

import controllers.OrderController;
import view.StoreFront;
import view.tablemodels.OrderTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class OrderPanel extends JPanel {

    private final JTable table;

    private final JComboBox<String> orderStatus;

    private final String[] statuses = {
            "PROCESSED", "SHIPPED", "DELIVERED", "COMPLETED",
    };

    public OrderPanel() {
        setLayout(new BorderLayout(20, 10));
        OrderController orderController = new OrderController(this);

        JPanel northBar = new JPanel();

        if (!StoreFront.getUserService().getLoggedInUser().isAdmin()) {
            JButton cancelOrder = new JButton("Cancel Order");
            cancelOrder.addActionListener(orderController);
            cancelOrder.setActionCommand("cancel");

            JButton returnMovies = new JButton("Return Movies");
            returnMovies.setActionCommand("return");
            returnMovies.addActionListener(orderController);

            northBar.add(cancelOrder);
            northBar.add(returnMovies);
        }

        orderStatus = new JComboBox<>(statuses);
        orderStatus.addActionListener(orderController);

        table = new JTable();
        OrderTableModel otm = new OrderTableModel(table, orderStatus);

        TableColumn statusColumn = table.getColumnModel().getColumn(1);
        statusColumn.setCellEditor(new DefaultCellEditor(orderStatus));
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);
        add(northBar, BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    public void displayErrorMessage(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public JTable getTable() {
        return table;
    }

    public JComboBox<String> getOrderStatus() {
        return orderStatus;
    }

}
