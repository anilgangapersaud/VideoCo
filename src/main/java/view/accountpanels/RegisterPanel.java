package view.accountpanels;

import model.Model;
import model.User;
import view.cards.LoginCards;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class RegisterPanel extends JPanel implements ActionListener {

    private static final String registerFailed = "Registration failed. Enter unique credentials.";
    private static final String registerSuccess = "Registration successful. Login with credentials to shop.";

    private JRadioButton customerOption;
    private JRadioButton employeeOption;
    private ButtonGroup accountTypes;
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JTextField emailInput;
    private LoginCards cards;

    public RegisterPanel(LoginCards cards) {
        this.cards = cards;
        JLabel usernameLabel = new JLabel("Username:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");
        loginButton.setActionCommand("login");
        signupButton.setActionCommand("signup");
        loginButton.addActionListener(this);
        signupButton.addActionListener(this);
        usernameInput = new JTextField(10);
        passwordInput = new JPasswordField(10);
        emailInput = new JTextField(10);
        customerOption = new JRadioButton("Customer");
        customerOption.setMnemonic(KeyEvent.VK_C);
        customerOption.setActionCommand("customer");
        employeeOption = new JRadioButton("Employee");
        employeeOption.setMnemonic(KeyEvent.VK_C);
        employeeOption.setActionCommand("employee");
        accountTypes = new ButtonGroup();
        accountTypes.add(customerOption);
        accountTypes.add(employeeOption);

        add(usernameLabel);
        add(usernameInput);
        add(emailLabel);
        add(emailInput);
        add(passwordLabel);
        add(passwordInput);
        add(customerOption);
        add(employeeOption);
        add(signupButton);
        add(loginButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("login")) {
            cards.getLayout().show(cards,"lp");
        } else if (e.getActionCommand().equals("signup")) {
            try {
                User u = new User(
                        usernameInput.getText(),
                        new String(passwordInput.getPassword()),
                        emailInput.getText(),
                        accountTypes.getSelection().getActionCommand()
                );
                if (!Model.getUserService().register(u)) {
                    JOptionPane.showMessageDialog(this, registerFailed, "Registration Failed", JOptionPane.ERROR_MESSAGE);
                    clearInputs();
                } else {
                    JOptionPane.showMessageDialog(this, registerSuccess);
                    clearInputs();
                    cards.getLayout().show(cards, "lp");
                }
            } catch (NullPointerException npe) {
                JOptionPane.showMessageDialog(this, registerFailed, "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private void clearInputs() {
        usernameInput.setText("");
        passwordInput.setText("");
        emailInput.setText("");
    }
}
