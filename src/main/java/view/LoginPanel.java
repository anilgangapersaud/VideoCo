package view;

import model.UserService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel implements ActionListener {

    private static final String usernameLabel = "Username:";
    private static final String passwordLabel = "Password:";
    private static final String loginCommand = "login";
    private static final String registerCommand = "register";
    private final JTextField usernameInput;
    private final JPasswordField passwordInput;

    UserService userService;

    public LoginPanel() {
        this.usernameInput = new JTextField(20);
        this.passwordInput = new JPasswordField(20);
        createView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(loginCommand)) {
            login();
        }
        if (e.getActionCommand().equals(registerCommand)) {
            register();
        }
    }

    private void login() {
        if (passwordInput.getPassword() != null) {
            //TODO: add user service
//            userService.login(usernameInput.getText(), new String(passwordInput.getPassword()));
            System.out.println(usernameInput + "logged in!");
        }
    }

    private void register() {
        RegisterDialog rd = new RegisterDialog((JFrame)this.getTopLevelAncestor());
    }

    private void createView() {
        JLabel username = new JLabel(usernameLabel);
        JLabel password = new JLabel(passwordLabel);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        loginButton.setActionCommand("login");
        registerButton.setActionCommand("register");
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        this.add(username);
        this.add(usernameInput);
        this.add(password);
        this.add(passwordInput);
        this.add(loginButton);
        this.add(registerButton);
    }
}
