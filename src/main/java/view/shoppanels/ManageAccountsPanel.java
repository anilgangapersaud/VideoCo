package view.shoppanels;

import model.Model;
import model.User;
import view.cards.ShopCards;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManageAccountsPanel extends JPanel implements ActionListener {

    private final ShopCards cards;

    private final JTable table;

    public ManageAccountsPanel(ShopCards cards) {
        this.cards = cards;
        setLayout(new BorderLayout(20,10));

        // north bar
        JPanel northbar = new JPanel();

        JButton deleteAccount = new JButton("Delete Account");
        deleteAccount.addActionListener(this);
        deleteAccount.setActionCommand("delete");
        JButton updateAccount = new JButton("Update Account");
        updateAccount.addActionListener(this);
        updateAccount.setActionCommand("update");

        northbar.add(deleteAccount);
        northbar.add(updateAccount);

        table = new JTable();

        // fill in table
        updateTable();

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);
        add(northbar, BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateTable() {
        List<User> customerAccounts = Model.getUserService().getAllCustomers();
        DefaultTableModel tmodel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        String[][] data = new String[customerAccounts.size()][3];
        String[] column = {"USERNAME", "PASSWORD", "EMAIL"};
        for (int i = 0; i < customerAccounts.size(); i++) {
            data[i][0] = customerAccounts.get(i).getUsername();
            data[i][1] = customerAccounts.get(i).getPassword();
            data[i][2] = customerAccounts.get(i).getEmailAddress();
        }
        tmodel.setDataVector(data,column);
        table.setModel(tmodel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("delete")) {
            int[] selected = table.getSelectedRows();
            if (selected.length == 0) {
                JOptionPane.showMessageDialog(this, "Select an account to delete");
            } else {
                for (int i = 0; i < selected.length; i++) {
                    String username = (String)table.getValueAt(selected[i], 0);
                    Model.getUserService().deleteUser(username);
                }
                updateTable();
            }
        } else if (e.getActionCommand().equals("update")) {
            int[] selected = table.getSelectedRows();
            if (selected.length != 1) {
                JOptionPane.showMessageDialog(this, "Select an account to update");
            } else {
                String username = (String)table.getValueAt(selected[0],0);
                User u = Model.getUserService().getUser(username);
                u.setPassword((String)table.getValueAt(selected[0], 1));
                u.setEmailAddress((String) table.getValueAt(selected[0],2));
                Model.getUserService().updateUser(u);
                JOptionPane.showMessageDialog(this, "Updated User Account");
                updateTable();
            }
        }
    }
}
