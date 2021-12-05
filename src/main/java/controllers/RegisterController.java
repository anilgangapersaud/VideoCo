package controllers;

import model.User;
import services.UserService;
import view.StoreFront;
import view.accountpanels.RegisterPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RegisterController implements ActionListener {

    private final UserService userService;
    private final RegisterPanel view;

    public RegisterController(RegisterPanel view) {
        this.view = view;
        userService = StoreFront.getUserService();
    }

    private boolean serviceRegister(User u) {
       return userService.register(u);
    }

    private void signup() {
        User u = new User();
        u.setUsername(view.getUsernameInput());
        u.setEmailAddress(view.getEmailInput());
        u.setAccountType(view.getAccountType());
        u.setPassword(view.getPasswordInput());
        if (serviceRegister(u)) {
            JOptionPane.showMessageDialog(view, "Registration Successful!\nLogin with your credentials to start shopping");
            view.clearInputs();
            view.getCards().getLayout().show(view.getCards(), "lp");
        } else {
            JOptionPane.showMessageDialog(view, "Registration Failed\nCheck fields and try again", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            view.clearInputs();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("signup")) {
            signup();
        }
    }
}
