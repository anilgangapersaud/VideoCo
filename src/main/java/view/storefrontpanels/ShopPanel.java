package view.storefrontpanels;

import database.UserRepository;
import view.cards.ShopCards;
import view.cards.StoreFrontCards;
import view.menu.MenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShopPanel extends JPanel implements ActionListener {

    private final StoreFrontCards cards;

    public ShopPanel(StoreFrontCards cards) {
        this.cards = cards;
        this.setLayout(new BorderLayout());

        ShopCards shopCards = new ShopCards(this);
        this.add(shopCards, BorderLayout.CENTER);

        if (UserRepository.getInstance().getLoggedInUser() != null) {
            this.add(new MenuPanel(shopCards), BorderLayout.NORTH);
        }

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("logout")) {
            cards.getLayout().show(cards, "ep");
        }
    }
}
