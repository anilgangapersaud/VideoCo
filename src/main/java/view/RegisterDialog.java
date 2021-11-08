package view;

import model.UserService;
import model.UserServiceImpl;
import model.user.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RegisterDialog extends JDialog implements ActionListener {

    private static final int windowHeight = 500;
    private static final int windowWidth = 400;
    private static final String windowName = "Register";
    private static final String registerCommand = "register";

    JTextField usernameInput;
    JPasswordField passwordField;
    JTextField emailInput;

    UserService userService;

    RegisterDialog(JFrame owner) throws IOException {
        super(owner);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setSize(windowWidth, windowHeight);
        this.setTitle(windowName);

        createView();

        this.setVisible(true);
        userService = new UserServiceImpl();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(registerCommand)) {
            if (passwordField.getPassword() != null) {
                User newUser = new User(usernameInput.getText(), new String(passwordField.getPassword()), emailInput.getText());
                try {
                    userService.register(newUser);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    private void createView() {
        JPanel registrationPanel = new JPanel();
        JLabel usernameLabel = new JLabel("Username:");
        usernameInput = new JTextField(10);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(10);
        JLabel emailLabel = new JLabel("Email Address:");
        emailInput = new JTextField(20);
        JButton register = new JButton("Register");
        register.setActionCommand(registerCommand);
        register.addActionListener(this);

        registrationPanel.add(usernameLabel);
        registrationPanel.add(usernameInput);
        registrationPanel.add(passwordLabel);
        registrationPanel.add(passwordField);
        registrationPanel.add(emailLabel);
        registrationPanel.add(emailInput);
        registrationPanel.add(register);

        this.add(registrationPanel);
    }
}
