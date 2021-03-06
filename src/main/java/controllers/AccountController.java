package controllers;

import services.UserService;
import view.StoreFront;
import view.shoppanels.AccountPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountController implements ActionListener {

    private final AccountPanel view;
    private final UserService userService;

    public AccountController(AccountPanel view) {
        this.view = view;
        userService = StoreFront.getUserService();
    }

    private void editName() {
        view.getNameInput().setEditable(true);
    }

    private void saveName() {
        view.getNameInput().setEditable(false);
        if (userService.changeUsername(view.getNameInput().getText(), userService.getLoggedInUser().getUsername())) {
            view.displayMessage("Username changed");
        } else {
            view.displayErrorMessage("Invalid username");
        }
    }

    private void editPassword() {
        view.getPasswordInput().setEchoChar((char) 0);
        view.getPasswordInput().setEditable(true);
    }

    private void savePassword() {
        view.getPasswordInput().setEditable(false);
        view.getPasswordInput().setEchoChar('*');
        if (userService.changePassword(new String(view.getPasswordInput().getPassword()), userService.getLoggedInUser().getUsername())) {
            view.displayMessage("Password Changed");
        } else {
            view.displayErrorMessage("Invalid Password");
        }
    }

    private void editEmail() {
        view.getEmailInput().setEditable(true);
    }

    private void saveEmail() {
        view.getEmailInput().setEditable(false);
        if (userService.changeEmail(view.getEmailInput().getText(), userService.getLoggedInUser().getUsername())) {
            view.displayMessage("Email Changed");
        } else {
            view.displayErrorMessage("Invalid Email");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("editName")) {
            editName();
        } else if (e.getActionCommand().equals("saveName")) {
            saveName();
        } else if (e.getActionCommand().equals("editPassword")) {
            editPassword();
        } else if (e.getActionCommand().equals("savePassword")) {
            savePassword();
        } else if (e.getActionCommand().equals("editEmail")) {
            editEmail();
        } else if (e.getActionCommand().equals("saveEmail")) {
            saveEmail();
        } else if (e.getActionCommand().equals("address")) {
            CardLayout cl = (CardLayout) view.getCards().getLayout();
            cl.show(view.getCards(), "eadp");
        } else if (e.getActionCommand().equals("billing")) {
            CardLayout cl = (CardLayout) view.getCards().getLayout();
            cl.show(view.getCards(), "ebp");
        }
    }
}
