package view.menu;

import view.cards.ShopCards;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMenuPanel extends MenuPanel implements ActionListener {

    private JButton inventory, manageOrders, manageAccounts;

    public AdminMenuPanel(ShopCards cards) {
        super(cards);

        inventory = new JButton("Inventory");
        inventory.setActionCommand("inventory");
        inventory.addActionListener(this);

        manageOrders = new JButton("Manage Orders");
        manageOrders.setActionCommand("manageOrders");
        manageOrders.addActionListener(this);

        manageAccounts = new JButton("Manage Accounts");
        manageAccounts.setActionCommand("manageAccounts");
        manageAccounts.addActionListener(this);

        add(inventory);
        add(manageOrders);
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
        } else if (e.getActionCommand().equals("inventory")) {
            shopCards.getLayout().show(shopCards, "ip");
        }
    }
}
