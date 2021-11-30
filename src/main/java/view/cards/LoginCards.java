package view.cards;

import view.accountpanels.LoginPanel;
import view.accountpanels.RegisterPanel;
import view.storefrontpanels.EntrancePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginCards extends JPanel implements ActionListener {

    protected CardLayout cl;
    private EntrancePanel panel;

    public LoginCards(EntrancePanel panel) {
        this.panel = panel;
        cl = new CardLayout();
        this.setLayout(cl);
        LoginPanel lp = new LoginPanel(this);
        RegisterPanel rp = new RegisterPanel(this);
        add(lp, "lp");
        add(rp, "rp");

        setVisible(true);
    }

    @Override
    public CardLayout getLayout() {
        return cl;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("login")) {
            panel.getStoreFront(). addShopPanel();
            panel.actionPerformed(e);
        }
    }
}
