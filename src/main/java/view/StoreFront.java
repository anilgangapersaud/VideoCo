package view;

import view.cards.StoreFrontCards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StoreFront extends JFrame implements ActionListener {

    private static final String appName = "VideoCo Inc.";
    private static final int windowWidth = 1000;
    private static final int windowHeight = 500;

    public StoreFront() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(windowWidth, windowHeight);
        setResizable(false);
        setTitle(appName);
        getContentPane().setBackground(Color.WHITE);
        add(new StoreFrontCards(), BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
