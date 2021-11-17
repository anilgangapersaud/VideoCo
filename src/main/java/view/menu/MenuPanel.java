package view.menu;

import view.AccountPanel;
import view.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class MenuPanel extends JPanel implements ActionListener {

    private JButton account, logout;
    protected JLabel welcomeMessage;

    public MenuPanel() {
        logout = new JButton("Logout");
        logout.setActionCommand("logout");
        logout.addActionListener(this);

        account = new JButton("Account");
        account.setActionCommand("account");
        account.addActionListener(this);

        welcomeMessage = new JLabel("Welcome " + App.getUserService().getLoggedInUser().getUsername());

        add(logout);
        add(account);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("account")) {
            AccountPanel ap = new AccountPanel();
            this.getTopLevelAncestor().add(ap, BorderLayout.CENTER);
            this.getTopLevelAncestor().setVisible(true);
        } else if (e.getActionCommand().equals("logout")) {
            JDialog frame = (JDialog)this.getTopLevelAncestor();
            frame.dispose();
        }
    }
}
