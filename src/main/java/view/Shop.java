package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Shop extends JDialog implements ActionListener {

    private static final int windowHeight = 500;
    private static final int windowWidth = 800;
    private static final String windowName = "Shop";

    Shop(JFrame owner) {
        super(owner);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setSize(windowWidth, windowHeight);
        this.setTitle(windowName);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
