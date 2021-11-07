package view;

import javax.swing.*;
import java.awt.*;

public class App {

    private static final String appName = "VideoCo Inc.";
    private static final int windowWidth = 800;
    private static final int windowHeight = 500;

    public App() {
        initializeApp();
    }



    public static void main(String[] args) {
        new App();
    }

    private void initializeApp() {
        JFrame mainframe = new JFrame(appName);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setSize(windowWidth, windowHeight);
        mainframe.setLayout(new BorderLayout());
        mainframe.add(new LoginPanel(), BorderLayout.SOUTH);
        mainframe.setVisible(true);
    }
}
