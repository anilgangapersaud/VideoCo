package view.menu;

import view.cards.ShopCards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMenuPanel extends MenuPanel implements ActionListener {

    public AdminMenuPanel(ShopCards cards) {
        super(cards);

        JButton manageAccounts = new JButton("Manage Accounts");
        manageAccounts.setActionCommand("manageAccounts");
        manageAccounts.addActionListener(this);

        add(manageAccounts);
        add(welcomeMessage);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("account")) {
            super.actionPerformed(e);
        } else if (e.getActionCommand().equals("logout")) {
            super.actionPerformed(e);
        } else if (e.getActionCommand().equals("store")) {
            super.actionPerformed(e);
        } else if (e.getActionCommand().equals("orders")) {
            super.actionPerformed(e);
        } else if (e.getActionCommand().equals("manageAccounts")) {
            super.actionPerformed(e);
        }
    }
}
