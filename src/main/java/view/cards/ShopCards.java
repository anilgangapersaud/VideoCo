package view.cards;

import view.shoppanels.CartPanel;
import view.shoppanels.ManageAccountsPanel;
import view.shoppanels.OrderPanel;
import view.shoppanels.StorePanel;
import view.storefrontpanels.ShopPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShopCards extends JPanel implements ActionListener {

    private final ShopPanel shopPanel;

    private final CardLayout cl;

    public ShopCards(ShopPanel shopPanel) {
        this.shopPanel = shopPanel;
        cl = new CardLayout();
        this.setLayout(cl);

        AccountCards ap = new AccountCards();
        StorePanel sp = new StorePanel();
        CartPanel cp = new CartPanel();
        OrderPanel op = new OrderPanel();
        ManageAccountsPanel map = new ManageAccountsPanel();

        add(ap, "ap");
        add(sp, "sp");
        add(cp, "cp");
        add(op, "op");
        add(map, "map");

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
