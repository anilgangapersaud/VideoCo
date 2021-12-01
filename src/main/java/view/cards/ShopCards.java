package view.cards;

import view.shoppanels.CartPanel;
import view.menupanels.InventoryPanel;
import view.shoppanels.OrderPanel;
import view.shoppanels.StorePanel;
import view.storefrontpanels.ShopPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShopCards extends JPanel implements ActionListener {

    private CardLayout cl;
    private ShopPanel shopPanel;
    private AccountCards ap;
    private InventoryPanel ip;
    private StorePanel sp;
    private CartPanel cp;
    private OrderPanel op;

    public ShopCards(ShopPanel shopPanel) {
        this.shopPanel = shopPanel;
        cl = new CardLayout();
        this.setLayout(cl);

        ap = new AccountCards();
        ip = new InventoryPanel(this);
        sp = new StorePanel(this);
        cp = new CartPanel(this);
        op = new OrderPanel(this);

        add(ap, "ap");
        add(ip, "ip");
        add(sp, "sp");
        add(cp, "cp");
        add(op, "op");

        setVisible(true);
    }


    public CardLayout getLayout() {
        return cl;
    }

    public AccountCards getAccountPanel() {
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

    public OrderPanel getOrderPanel() { return op; }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("logout")) {
            shopPanel.actionPerformed(e);
        }
    }
}
