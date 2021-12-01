package view.accountpanels;

import model.Model;
import model.User;
import view.cards.LoginCards;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel implements ActionListener {

    private static final String usernameLabel = "Username:";
    private static final String passwordLabel = "Password:";
    private static final String loginCommand = "login";
    private static final String registerCommand = "register";
    private static final String loginUnsuccessful = "Invalid Credentials";
    private final JTextField usernameInput;
    private final JPasswordField passwordInput;
    private LoginCards cards;

    public LoginPanel(LoginCards cards) {
        this.cards = cards;
        this.usernameInput = new JTextField(20);
        this.passwordInput = new JPasswordField(20);
        createView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(loginCommand)) {
            User user = login();
            if (user == null) {
                JOptionPane.showMessageDialog(this.getTopLevelAncestor(), loginUnsuccessful, "Login Failed", JOptionPane.ERROR_MESSAGE);
            } else {
                cards.actionPerformed(e);
            }
        }
        if (e.getActionCommand().equals(registerCommand)) {
            cards.getLayout().show(cards, "rp");
        }
        clearInputs();
    }

    private User login() {
        return Model.getUserService().login(usernameInput.getText(), new String(passwordInput.getPassword()));
    }

    private void createView() {
        JLabel username = new JLabel(usernameLabel);
        JLabel password = new JLabel(passwordLabel);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        loginButton.setActionCommand(loginCommand);
        registerButton.setActionCommand(registerCommand);
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        this.add(username);
        this.add(usernameInput);
        this.add(password);
        this.add(passwordInput);
        this.add(loginButton);
        this.add(registerButton);
    }

    private void clearInputs() {
        usernameInput.setText("");
        passwordInput.setText("");
    }
}
