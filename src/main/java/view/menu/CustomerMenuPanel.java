package view.menu;

import view.cards.ShopCards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerMenuPanel extends MenuPanel implements ActionListener {

    private JButton cart, orders;

    public CustomerMenuPanel(ShopCards cards) {
        super(cards);
        super.cl = cards.getLayout();
        cart = new JButton("Shopping Cart");
        cart.setActionCommand("cart");
        cart.addActionListener(this);

        orders = new JButton("Orders");
        orders.setActionCommand("orders");
        orders.addActionListener(this);

        add(cart);
        add(orders);
        add(welcomeMessage);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("store")) {
            super.actionPerformed(e);
        } else if (e.getActionCommand().equals("logout")) {
            super.actionPerformed(e);
        } else if (e.getActionCommand().equals("account")) {
            super.actionPerformed(e);
        }
    }
}
