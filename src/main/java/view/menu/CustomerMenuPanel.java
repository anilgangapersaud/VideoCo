package view.menu;

import view.cards.ShopCards;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerMenuPanel extends MenuPanel implements ActionListener {

    public CustomerMenuPanel(ShopCards cards) {
        super(cards);

        JButton cart = new JButton("Cart");
        cart.setActionCommand("cart");
        cart.addActionListener(this);

        add(cart);
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
        } else if (e.getActionCommand().equals("cart")) {
            super.actionPerformed(e);
        } else if (e.getActionCommand().equals("orders")) {
            super.actionPerformed(e);
        }
    }
}
