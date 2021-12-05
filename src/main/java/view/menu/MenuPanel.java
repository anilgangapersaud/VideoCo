package view.menu;

import view.StoreFront;
import view.cards.ShopCards;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel implements ActionListener {

    private final ShopCards shopCards;

    public MenuPanel(ShopCards cards) {
        shopCards = cards;

        // first panel
        JButton logout = new JButton("Logout");
        logout.setActionCommand("logout");
        logout.addActionListener(this);

        // second panel
        JButton account = new JButton("Account Details");
        account.setActionCommand("account");
        account.addActionListener(this);

        // third panel
        JButton store;
        if (StoreFront.getUserService().isAdmin()) {
            store = new JButton("Manage Inventory");
        } else {
            store = new JButton("Shop");
        }
        store.setActionCommand("store");
        store.addActionListener(this);

        // fourth panel
        JButton fourthPanel;
        if (StoreFront.getUserService().isAdmin()) {
            fourthPanel = new JButton("Manage Accounts");
            fourthPanel.setActionCommand("manageAccounts");
        } else {
            fourthPanel = new JButton("Cart");
            fourthPanel.setActionCommand("cart");
        }
        fourthPanel.addActionListener(this);

        // fifth panel
        JButton orders;
        if (StoreFront.getUserService().isAdmin()) {
            orders = new JButton("Manage Orders");
        } else {
            orders = new JButton("Orders");
        }
        orders.setActionCommand("orders");
        orders.addActionListener(this);

        JLabel welcomeMessage = new JLabel("Welcome " + StoreFront.getUserService().getLoggedInUser().getUsername() + "!");

        add(logout);
        add(account);
        add(store);
        add(fourthPanel);
        add(orders);
        add(welcomeMessage);

        cards.getLayout().show(cards, "sp");

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("account")) {
            shopCards.getLayout().show(shopCards, "ap");
        } else if (e.getActionCommand().equals("logout")) {
            shopCards.actionPerformed(e);
        } else if (e.getActionCommand().equals("store")) {
            shopCards.getLayout().show(shopCards, "sp");
        } else if (e.getActionCommand().equals("cart")) {
            shopCards.getLayout().show(shopCards, "cp");
        } else if (e.getActionCommand().equals("orders")) {
            shopCards.getLayout().show(shopCards, "op");
        } else if (e.getActionCommand().equals("manageAccounts")) {
            shopCards.getLayout().show(shopCards, "map");
        }
    }
}
