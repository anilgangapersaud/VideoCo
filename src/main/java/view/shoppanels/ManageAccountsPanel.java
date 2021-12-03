package view.shoppanels;

import controllers.ManageAccountsController;
import view.tablemodels.AccountTableModel;

import javax.swing.*;
import java.awt.*;

public class ManageAccountsPanel extends JPanel {

    private final JTable table;

    public ManageAccountsPanel() {
        setLayout(new BorderLayout(20,10));
        ManageAccountsController controller = new ManageAccountsController(this);

        // north bar
        JPanel northBar = new JPanel();

        JButton deleteAccount = new JButton("Delete Account");
        deleteAccount.addActionListener(controller);
        deleteAccount.setActionCommand("delete");

        JButton updateAccount = new JButton("Update Account");
        updateAccount.addActionListener(controller);
        updateAccount.setActionCommand("update");

        northBar.add(deleteAccount);
        northBar.add(updateAccount);

        table = new JTable();
        AccountTableModel atm = new AccountTableModel();
        table.setModel(atm);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);
        add(northBar, BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public JTable getTable() {
        return table;
    }

}
