package controllers;

import services.UserService;
import services.UserServiceImpl;
import view.accountpanels.LoginPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController implements ActionListener {

    private final LoginPanel view;
    private final UserService userService;

    public LoginController(LoginPanel view) {
        this.view = view;
        userService = new UserServiceImpl();
    }

    private void serviceLogin(ActionEvent e) {
        if (userService.login(view.getUsernameInput(), view.getPasswordInput())) {
            view.cards.login();
        } else {
            JOptionPane.showMessageDialog(view, "Invalid Credentials", "Login Unsuccessful", JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(LoginPanel.loginCommand)) {
            serviceLogin(e);
        }
    }
}
