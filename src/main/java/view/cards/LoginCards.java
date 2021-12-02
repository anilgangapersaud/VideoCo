package view.cards;

import view.accountpanels.LoginPanel;
import view.accountpanels.RegisterPanel;
import view.storefrontpanels.EntrancePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginCards extends JPanel {

    private final EntrancePanel panel;
    private final CardLayout cl;

    public LoginCards(EntrancePanel panel) {
        this.panel = panel;
        cl = new CardLayout();
        setLayout(cl);

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

    public void login() {
        panel.getStoreFront().addShopPanel();
        panel.login();
    }
}
