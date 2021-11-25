package view.cards;

import view.menupanels.AccountPanel;
import view.menupanels.InventoryPanel;
import view.menupanels.StorePanel;
import view.storefrontpanels.ShopPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShopCards extends JPanel implements ActionListener {

    private CardLayout cl;
    private ShopPanel shopPanel;

    public ShopCards(ShopPanel shopPanel) {
        this.shopPanel = shopPanel;
        cl = new CardLayout();
        this.setLayout(cl);
        AccountPanel ap = new AccountPanel();
        InventoryPanel ip = new InventoryPanel();
        StorePanel sp = new StorePanel();

        add(ap, "ap");
        add(ip, "ip");
        add(sp, "sp");

        setVisible(true);
    }


    public CardLayout getLayout() {
        return cl;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("logout")) {
            shopPanel.actionPerformed(e);
        }
    }
}
