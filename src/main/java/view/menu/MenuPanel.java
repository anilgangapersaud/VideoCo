package view.menu;

import view.cards.ShopCards;
import view.App;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class MenuPanel extends JPanel implements ActionListener {

    private JButton account, logout, store;
    protected JLabel welcomeMessage;
    protected JPanel cards;
    ShopCards shopCards;

    public MenuPanel(ShopCards cards) {
        shopCards = cards;
        logout = new JButton("Logout");
        logout.setActionCommand("logout");
        logout.addActionListener(this);

        account = new JButton("Account");
        account.setActionCommand("account");
        account.addActionListener(this);

        store = new JButton("Store");
        store.setActionCommand("store");
        store.addActionListener(this);

        welcomeMessage = new JLabel("Welcome " + App.getUserService().getLoggedInUser().getUsername() + "!");

        add(logout);
        add(store);
        add(account);

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
        }
    }
}
