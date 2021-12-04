package controllers;

import model.User;
import services.UserServiceImpl;
import view.StoreFront;
import view.accountpanels.RegisterPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RegisterController implements ActionListener {

    private final UserServiceImpl userService;
    private final RegisterPanel view;

    public RegisterController(RegisterPanel view) {
        this.view = view;
        userService = StoreFront.getUserService();
    }

    private boolean serviceRegister(User u) {
       return userService.register(u);
    }

    private void signup() {
        if (serviceRegister(new User(
                view.getUsernameInput(), view.getPasswordInput(), view.getEmailInput(), view.getAccountType()))) {
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
