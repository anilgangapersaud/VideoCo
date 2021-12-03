package controllers;

import database.UserRepository;
import view.shoppanels.AccountPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountController implements ActionListener {

    private final AccountPanel view;
    private final UserRepository userRepository;

    public AccountController(AccountPanel view) {
        this.view = view;
        userRepository = UserRepository.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("editName")) {
            view.getNameInput().setEditable(true);
        } else if (e.getActionCommand().equals("saveName")) {
            view.getNameInput().setEditable(false);
            if (userRepository.changeUsername(view.getNameInput().getText())) {
                view.displayMessage("Username changed");
            } else {
                view.displayErrorMessage("Invalid username");
            }
        } else if (e.getActionCommand().equals("editPassword")) {
            view.getPasswordInput().setEchoChar((char) 0);
            view.getPasswordInput().setEditable(true);
        } else if (e.getActionCommand().equals("savePassword")) {
            view.getPasswordInput().setEditable(false);
            view.getPasswordInput().setEchoChar('*');
            if (UserRepository.getInstance().changePassword(new String(view.getPasswordInput().getPassword()))) {
                view.displayMessage("Password Changed");
            } else {
                view.displayErrorMessage("Invalid Password");
            }
        } else if (e.getActionCommand().equals("editEmail")) {
            view.getEmailInput().setEditable(true);
        } else if (e.getActionCommand().equals("saveEmail")) {
            view.getEmailInput().setEditable(false);
            if (UserRepository.getInstance().changeEmail(view.getEmailInput().getText())) {
                view.displayMessage("Email Changed");
            } else {
                view.displayErrorMessage("Invalid Email");
            }
        } else if (e.getActionCommand().equals("address")) {
            CardLayout cl = (CardLayout) view.getCards().getLayout();
            cl.show(view.getCards(), "eadp");
        } else if (e.getActionCommand().equals("billing")) {
            CardLayout cl = (CardLayout) view.getCards().getLayout();
            cl.show(view.getCards(), "ebp");
        }
    }
}
