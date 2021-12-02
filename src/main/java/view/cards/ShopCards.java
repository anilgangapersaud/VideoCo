package view.cards;

import view.shoppanels.CartPanel;
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

    private final AccountCards ap;

    private final StorePanel sp;
    private final CartPanel cp;
    private final OrderPanel op;

    public ShopCards(ShopPanel shopPanel) {
        this.shopPanel = shopPanel;
        cl = new CardLayout();
        this.setLayout(cl);

        ap = new AccountCards();
        sp = new StorePanel(this);
        cp = new CartPanel(this);
        op = new OrderPanel(this);

        add(ap, "ap");
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
