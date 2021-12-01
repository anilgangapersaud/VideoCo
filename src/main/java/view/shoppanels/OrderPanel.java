package view.shoppanels;

import model.Model;
import model.Order;
import view.cards.ShopCards;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OrderPanel extends JPanel implements ActionListener {

    ShopCards cards;

    private JButton cancelOrder;
    private JButton returnMovies;

    private final JTable table;
    private final JScrollPane scrollPane;

    public OrderPanel(ShopCards cards) {
        this.cards = cards;
        setLayout(new BorderLayout(20, 10));

        cancelOrder = new JButton("Cancel Order");
        cancelOrder.addActionListener(this);
        cancelOrder.setActionCommand("cancel");

        returnMovies = new JButton("Return Movies");
        returnMovies.setActionCommand("return");
        returnMovies.addActionListener(this);

        JPanel northbar = new JPanel();
        northbar.add(cancelOrder);
        northbar.add(returnMovies);

        table = new JTable();
        updateTable();
        scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);
        add(northbar, BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private void updateViews() {
        updateTable();
        cards.getStorePanel().displayAllMovies();
    }

    public void updateTable() {
        List<Order> orders = Model.getOrderService().getOrdersByCustomer(Model.getUserService().getLoggedInUser().getUsername());
        DefaultTableModel tmodel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("cancel")) {
            int[] selected = table.getSelectedRows();
            if (selected.length != 1) {
                JOptionPane.showMessageDialog(this, "Select 1 order to cancel","Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int orderNumber = Integer.parseInt((String)table.getValueAt(selected[0],0));
                boolean result = Model.getOrderService().cancelOrder(orderNumber);
                if (result) {
                    JOptionPane.showMessageDialog(this, "Cancelled Order");
                    updateViews();
                } else {
                    JOptionPane.showMessageDialog(this, "Error cancelling order","Error", JOptionPane.ERROR_MESSAGE);}
                }
        }
    }

}
