package view.cards;

import view.EntrancePanel;
import view.storefrontpanels.ShopPanel;

import javax.swing.*;
import java.awt.*;

public class StoreFrontCards extends JPanel {

    private final CardLayout cl;

    public StoreFrontCards() {
        cl = new CardLayout();
        this.setLayout(cl);
        EntrancePanel ep = new EntrancePanel(this);
        add(ep, "ep");
        setVisible(true);
    }

    public CardLayout getLayout() {
        return cl;
    }

    public void addShopPanel() {
        ShopPanel sp = new ShopPanel(this);
        add(sp, "sp");
    }

}
