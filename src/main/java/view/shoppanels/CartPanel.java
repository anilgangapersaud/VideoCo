package view.shoppanels;

import controllers.CartController;
import database.Observer;
import database.UserRepository;
import view.tablemodels.CartTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class CartPanel extends JPanel implements Observer {

    private final JLabel totalCost;

    private final JLabel displayLoyaltyPoints;

    private final ButtonGroup paymentServices;

    /**
     * Components for displaying data
     */
    private final JTable table;

    public CartPanel() {
        setLayout(new BorderLayout(20, 10));
        CartController cartController = new CartController(this);
        UserRepository.getInstance().registerObserver(this);
        UserRepository.getInstance().getLoggedInUser().getCart().registerObserver(this);

        JLabel paymentLabel = new JLabel("Choose your payment method:");
        JRadioButton loyaltyPointsOption = new JRadioButton("Loyalty Points");
        loyaltyPointsOption.setMnemonic(KeyEvent.VK_C);
        loyaltyPointsOption.setActionCommand("loyaltyPoints");
        JRadioButton creditCardOption = new JRadioButton("Credit Card");
        creditCardOption.setMnemonic(KeyEvent.VK_C);
        creditCardOption.setActionCommand("creditCard");
        paymentServices = new ButtonGroup();
        paymentServices.add(loyaltyPointsOption);
        paymentServices.add(creditCardOption);

        JButton removeItem = new JButton("Remove Item");
        removeItem.addActionListener(cartController);
        removeItem.setActionCommand("removeItem");

        JButton clearCart = new JButton("Clear Cart");
        clearCart.addActionListener(cartController);
        clearCart.setActionCommand("clearCart");

        JButton checkout = new JButton("Place Order");
        checkout.addActionListener(cartController);
        checkout.setActionCommand("checkout");

        JLabel loyaltyPointsLabel = new JLabel("Loyalty Points:");
        int customerLoyaltyPoints = UserRepository.getInstance().getLoggedInUser().getLoyaltyPoints();
        displayLoyaltyPoints = new JLabel(String.valueOf(customerLoyaltyPoints));

        table = new JTable();

        CartTableModel ctm = new CartTableModel();
        table.setModel(ctm);
        JScrollPane scrollPane = new JScrollPane(table);

        totalCost = new JLabel("");

        JPanel northBar = new JPanel();
        northBar.add(removeItem);
        northBar.add(clearCart);
        northBar.add(loyaltyPointsLabel);
        northBar.add(displayLoyaltyPoints);

        JPanel southBar = new JPanel();
        southBar.add(paymentLabel);
        southBar.add(creditCardOption);
        southBar.add(loyaltyPointsOption);
        southBar.add(totalCost);
        southBar.add(checkout);

        add(scrollPane, BorderLayout.CENTER);
        add(northBar, BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);
        add(southBar, BorderLayout.SOUTH);
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

    public ButtonGroup getPaymentServices() {
        return paymentServices;
    }

    @Override
    public void update() {
        displayLoyaltyPoints.setText(String.valueOf(UserRepository.getInstance().getLoggedInUser().getLoyaltyPoints()));
        totalCost.setText(String.format("Total: %.2f$", UserRepository.getInstance().getLoggedInUser().getCart().getTotal()));
    }
}
