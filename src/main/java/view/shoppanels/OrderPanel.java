package view.shoppanels;

import model.Model;
import model.Order;
import view.cards.ShopCards;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OrderPanel extends JPanel implements ActionListener {

    private final ShopCards cards;

    private final JTable table;

    private JComboBox<String> orderStatus = null;

    private final String[] statuses = {
            "PROCESSED", "DELIVERED", "RETURNED", "SHIPPED"
    };

    public OrderPanel(ShopCards cards) {
        this.cards = cards;
        setLayout(new BorderLayout(20, 10));

        // north bar
        JPanel northbar = new JPanel();

        if (!Model.getUserService().getLoggedInUser().isAdmin()) {
            // customer menu bar
            JButton cancelOrder = new JButton("Cancel Order");
            cancelOrder.addActionListener(this);
            cancelOrder.setActionCommand("cancel");

            JButton returnMovies = new JButton("Return Movies");
            returnMovies.setActionCommand("return");
            returnMovies.addActionListener(this);

            northbar.add(cancelOrder);
            northbar.add(returnMovies);
        } else {
            // admin menu bar
        }

        table = new JTable();
        updateViews();
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);
        add(northbar, BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateViews() {
        updateTable();
    }

    public void updateTable() {
        List<Order> orders;
        DefaultTableModel tmodel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 1  && Model.getUserService().getLoggedInUser().isAdmin()) {
                    return true;
                } else {
                    return false;
                }
            }
        };
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

        tmodel.setDataVector(data,column);
        table.setModel(tmodel);
        TableColumn statusColumn = table.getColumnModel().getColumn(1);
        orderStatus = new JComboBox<>(statuses);
        orderStatus.addActionListener(this);
        statusColumn.setCellEditor(new DefaultCellEditor(orderStatus));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("cancel")) {
            int[] selected = table.getSelectedRows();
            if (selected.length != 1) {
                JOptionPane.showMessageDialog(this, "Select an order to cancel","Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int orderNumber = Integer.parseInt((String)table.getValueAt(selected[0],0));
                boolean result = Model.getOrderService().cancelOrder(orderNumber);
                if (result) {
                    JOptionPane.showMessageDialog(this, "Cancelled Order");
                    updateViews();
                } else {
                    JOptionPane.showMessageDialog(this, "Error cancelling order","Error", JOptionPane.ERROR_MESSAGE);}
                }
        } else if (e.getActionCommand().equals("return")) {
            int[] selected = table.getSelectedRows();
            if (selected.length != 1) {
                JOptionPane.showMessageDialog(this, "Select 1 order", "Error", JOptionPane.ERROR);
            } else {
                int orderNumber = Integer.parseInt((String)table.getValueAt(selected[0],0));
                boolean result = Model.getOrderService().returnMovies(orderNumber);
                if (result) {
                    JOptionPane.showMessageDialog(this, "Thanks for shopping with VideoCo!");
                    updateViews();
                } else {
                    JOptionPane.showMessageDialog(this, "Error returning movies. Status must be DELIVERED", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getActionCommand().equals("comboBoxChanged")) {
            // get the new status of and update the order number
            String status = (String)orderStatus.getSelectedItem();
            int[] selected = table.getSelectedRows();
            if (selected.length == 1) {
                int orderNumber =  Integer.parseInt((String)table.getValueAt(selected[0], 0));
                Model.getOrderService().changeOrderStatus(orderNumber, status);
            }
        }
    }

}
