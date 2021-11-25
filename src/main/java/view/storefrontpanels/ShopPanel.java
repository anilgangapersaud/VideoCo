package view.storefrontpanels;

import view.App;
import view.StoreFront;
import view.cards.ShopCards;
import view.cards.StoreFrontCards;
import view.menu.AdminMenuPanel;
import view.menu.CustomerMenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShopPanel extends JPanel implements ActionListener {

    private StoreFrontCards cards;

    public ShopPanel(StoreFrontCards cards) {
        this.cards = cards;
        // shop window configs
        this.setLayout(new BorderLayout());

        // add card layout to center
        ShopCards shopCards = new ShopCards(this);
        this.add(shopCards, BorderLayout.CENTER);

        // menu
        if (App.getUserService().getLoggedInUser() != null) {
            if (App.getUserService().getLoggedInUser().isAdmin()) {
                this.add(new AdminMenuPanel(shopCards), BorderLayout.NORTH);
            } else {
                this.add(new CustomerMenuPanel(shopCards), BorderLayout.NORTH);
            }
        }
        // default view
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("logout")) {
            cards.getLayout().show(cards, "ep");
        }
    }
}
