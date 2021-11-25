package view.cards;

import view.storefrontpanels.EntrancePanel;
import view.storefrontpanels.ShopPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StoreFrontCards extends JPanel implements ActionListener {

    protected CardLayout cl;

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

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
