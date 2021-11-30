package view.cards;

import view.menupanels.AccountPanel;
import view.menupanels.CartPanel;
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
    private AccountPanel ap;
    private InventoryPanel ip;
    private StorePanel sp;
    private CartPanel cp;

    public ShopCards(ShopPanel shopPanel) {
        this.shopPanel = shopPanel;
        cl = new CardLayout();
        this.setLayout(cl);
        ap = new AccountPanel(this);
        ip = new InventoryPanel(this);
        sp = new StorePanel(this);
        cp = new CartPanel(this);

        add(ap, "ap");
        add(ip, "ip");
        add(sp, "sp");
        add(cp, "cp");

        setVisible(true);
    }


    public CardLayout getLayout() {
        return cl;
    }

    public AccountPanel getAccountPanel() {
        return ap;
    }

    public InventoryPanel getInventoryPanel() {
        return ip;
    }

    public StorePanel getStorePanel() {
        return sp;
    }

    public CartPanel getCartPanel() {
        return cp;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("logout")) {
            shopPanel.actionPerformed(e);
        }
    }
}
