package view.accountpanels;

import controllers.LoginController;
import view.cards.LoginCards;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel implements ActionListener {

    private static final String usernameLabel = "Username:";
    private static final String passwordLabel = "Password:";
    private static final String registerCommand = "register";
    public static final String loginCommand = "login";
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    public final LoginCards cards;

    private final LoginController loginController;

    public LoginPanel(LoginCards cards) {
        this.cards = cards;
        loginController = new LoginController(this);
        createView();
    }

    private void createView() {
        this.usernameInput = new JTextField(20);
        this.passwordInput = new JPasswordField(20);
        JLabel username = new JLabel(usernameLabel);
        JLabel password = new JLabel(passwordLabel);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        loginButton.setActionCommand(loginCommand);
        registerButton.setActionCommand(registerCommand);
        loginButton.addActionListener(loginController);
        registerButton.addActionListener(this);
        this.add(username);
        this.add(usernameInput);
        this.add(password);
        this.add(passwordInput);
        this.add(loginButton);
        this.add(registerButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(registerCommand)) {
            cards.getLayout().show(cards, "rp");
        }
        clearInputs();
    }

    private void clearInputs() {
        usernameInput.setText("");
        passwordInput.setText("");
    }

    public String getUsernameInput() {
        return usernameInput.getText();
    }

    public String getPasswordInput() {
        return new String(passwordInput.getPassword());
    }
}
