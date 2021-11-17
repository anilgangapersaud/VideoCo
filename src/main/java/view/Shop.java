package view;

import model.user.User;
import view.menu.AdminMenuPanel;
import view.menu.CustomerMenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Shop extends JDialog implements ActionListener {

    private static final int windowHeight = 500;
    private static final int windowWidth = 800;
    private static final String windowName = "Shop";

    Shop(JFrame owner, User user) {
        super(owner);

        // shop window configs
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setSize(windowWidth, windowHeight);
        this.setTitle(windowName);
        this.setLayout(new BorderLayout());

        // menu
        if (user.isAdmin()) {
            this.add(new AdminMenuPanel(), BorderLayout.NORTH);
        } else {
            this.add(new CustomerMenuPanel(), BorderLayout.NORTH);
        }

        // default view
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
