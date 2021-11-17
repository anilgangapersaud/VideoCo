package view.menu;

import view.StorePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerMenuPanel extends MenuPanel implements ActionListener {

    private JButton store, cart, orders;

    public CustomerMenuPanel() {
        super();
        store = new JButton("Store");
        store.setActionCommand("store");
        store.addActionListener(this);

        cart = new JButton("Shopping Cart");
        cart.setActionCommand("cart");
        cart.addActionListener(this);

        orders = new JButton("Orders");
        orders.setActionCommand("orders");
        orders.addActionListener(this);


        add(store);
        add(cart);
        add(orders);
        add(welcomeMessage);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("store")) {
            StorePanel sp = new StorePanel();
            this.getTopLevelAncestor().add(sp, BorderLayout.CENTER);
            getTopLevelAncestor().setVisible(true);
        } else if (e.getActionCommand().equals("logout")) {
            super.actionPerformed(e);
        } else if (e.getActionCommand().equals("account")) {
            super.actionPerformed(e);
        }
    }
}
