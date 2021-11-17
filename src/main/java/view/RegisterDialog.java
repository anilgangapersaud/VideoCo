package view;

import model.user.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class RegisterDialog extends JDialog implements ActionListener {

    private static final int windowHeight = 500;
    private static final int windowWidth = 800;
    private static final String windowName = "Register";
    private static final String registerCommand = "register";
    private static final String registerFailed = "Registration failed. Enter unique credentials.";
    private static final String registerSuccess = "Registration successful. Login with credentials to shop.";

    private JTextField usernameInput;
    private JPasswordField passwordField;
    private JTextField emailInput;
    private JRadioButton customerOption;
    private JRadioButton employeeOption;
    private ButtonGroup accountTypes;

    RegisterDialog(JFrame owner) throws IOException {
        super(owner);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setSize(windowWidth, windowHeight);
        this.setTitle(windowName);
        createView();
        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(registerCommand)) {
            User u = new User(
                    usernameInput.getText(),
                    new String(passwordField.getPassword()),
                    emailInput.getText(),
                    accountTypes.getSelection().getActionCommand()
            );
            boolean registerResult = register(u);
            if (!registerResult) {
                JOptionPane.showMessageDialog(this, registerFailed, "Registration Failed", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, registerSuccess);
            }
            dispose();
        }
    }

    private boolean register(User u) {
        return App.getUserService().register(u);
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
        accountTypes = new ButtonGroup();

        customerOption = new JRadioButton("Customer");
        employeeOption = new JRadioButton("Employee");
        customerOption.setMnemonic(KeyEvent.VK_C);
        employeeOption.setMnemonic(KeyEvent.VK_C);
        customerOption.setActionCommand("customer");
        employeeOption.setActionCommand("employee");
        customerOption.setSelected(true);
        accountTypes.add(employeeOption);
        accountTypes.add(customerOption);

        registrationPanel.add(usernameLabel);
        registrationPanel.add(usernameInput);
        registrationPanel.add(passwordLabel);
        registrationPanel.add(passwordField);
        registrationPanel.add(emailLabel);
        registrationPanel.add(emailInput);
        registrationPanel.add(register);
        registrationPanel.add(customerOption);
        registrationPanel.add(employeeOption);

        this.add(registrationPanel);
    }
}
