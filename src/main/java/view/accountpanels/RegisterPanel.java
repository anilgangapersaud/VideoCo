package view.accountpanels;

import controllers.RegisterController;
import view.cards.LoginCards;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class RegisterPanel extends JPanel implements ActionListener {

    private ButtonGroup accountTypes;
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JTextField emailInput;
    public final LoginCards cards;

    private final RegisterController registerController;

    public RegisterPanel(LoginCards cards) {
        this.cards = cards;
        registerController = new RegisterController(this);
        createView();
    }

    private void createView() {
        JLabel usernameLabel = new JLabel("Username:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");
        loginButton.setActionCommand("login");
        loginButton.addActionListener(this);
        signupButton.setActionCommand("signup");
        signupButton.addActionListener(registerController);
        usernameInput = new JTextField(10);
        passwordInput = new JPasswordField(10);
        emailInput = new JTextField(10);
        JRadioButton customerOption = new JRadioButton("Customer");
        customerOption.setMnemonic(KeyEvent.VK_C);
        customerOption.setActionCommand("customer");
        JRadioButton employeeOption = new JRadioButton("Employee");
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
        }
    }

    public String getUsernameInput() {
        return usernameInput.getText();
    }

    public String getPasswordInput() {
        return new String(passwordInput.getPassword());
    }

    public String getEmailInput() {
        return emailInput.getText();
    }

    public String getAccountType() {
        if (accountTypes.getSelection() == null) {
            return "";
        }
        return accountTypes.getSelection().getActionCommand();
    }

    public LoginCards getCards() {
        return cards;
    }

    public void clearInputs() {
        usernameInput.setText("");
        passwordInput.setText("");
        emailInput.setText("");
    }
}
