package view.shoppanels;

import controllers.AccountController;
import database.Observer;
import database.UserRepository;
import view.cards.AccountCards;

import javax.swing.*;
import java.awt.*;

public class AccountPanel extends JPanel implements Observer {

    private final AccountCards cards;

    private final JTextField nameInput;

    private final JPasswordField passwordInput;

    private final JTextField emailInput;

    private final JLabel loyaltyPoints;

    public AccountPanel(AccountCards cards) {
        this.cards = cards;
        setLayout(new GridBagLayout());

        AccountController accountController = new AccountController(this);

        String customerName = UserRepository.getInstance().getLoggedInUser().getUsername();
        String customerPassword = UserRepository.getInstance().getLoggedInUser().getPassword();
        String customerEmail = UserRepository.getInstance().getLoggedInUser().getEmailAddress();
        int customerLoyaltyPoints = UserRepository.getInstance().getLoggedInUser().getLoyaltyPoints();

        UserRepository.getInstance().registerObserver(this);

        JLabel username = new JLabel("Username:");
        nameInput = new JTextField(20);
        nameInput.setText(customerName);
        nameInput.setEditable(false);
        JButton editName = new JButton("Edit");
        editName.setActionCommand("editName");
        editName.addActionListener(accountController);
        JButton saveName = new JButton("Save");
        saveName.setActionCommand("saveName");
        saveName.addActionListener(accountController);
        JPanel changeUsername = new JPanel();
        changeUsername.setLayout(new BoxLayout(changeUsername, BoxLayout.X_AXIS));
        changeUsername.add(username);
        int horizontalStrutSize = 5;
        changeUsername.add(Box.createHorizontalStrut(horizontalStrutSize));
        changeUsername.add(nameInput);
        changeUsername.add(Box.createHorizontalStrut(horizontalStrutSize));
        changeUsername.add(editName);
        changeUsername.add(Box.createHorizontalStrut(horizontalStrutSize));
        changeUsername.add(saveName);

        JLabel passwordLabel = new JLabel("Password:");
        passwordInput = new JPasswordField(18);
        passwordInput.setText(customerPassword);
        passwordInput.setEditable(false);
        passwordInput.setEchoChar('*');
        JButton editPassword = new JButton("Edit");
        editPassword.setActionCommand("editPassword");
        editPassword.addActionListener(accountController);
        JButton savePassword = new JButton("Save");
        savePassword.addActionListener(accountController);
        savePassword.setActionCommand("savePassword");
        JPanel changePassword = new JPanel();
        changePassword.setLayout(new BoxLayout(changePassword, BoxLayout.X_AXIS));
        changePassword.add(passwordLabel);
        changePassword.add(Box.createHorizontalStrut(horizontalStrutSize));
        changePassword.add(passwordInput);
        changePassword.add(Box.createHorizontalStrut(horizontalStrutSize));
        changePassword.add(editPassword);
        changePassword.add(Box.createHorizontalStrut(horizontalStrutSize));
        changePassword.add(savePassword);

        JLabel emailLabel = new JLabel("Email Address:");
        emailInput = new JTextField(20);
        emailInput.setText(customerEmail);
        emailInput.setEditable(false);
        JButton editEmail = new JButton("Edit");
        editEmail.setActionCommand("editEmail");
        editEmail.addActionListener(accountController);
        JButton saveEmail = new JButton("Save");
        saveEmail.setActionCommand("saveEmail");
        saveEmail.addActionListener(accountController);
        JPanel changeEmail = new JPanel();
        changeEmail.setLayout(new BoxLayout(changeEmail, BoxLayout.X_AXIS));
        changeEmail.add(emailLabel);
        changeEmail.add(Box.createHorizontalStrut(horizontalStrutSize));
        changeEmail.add(emailInput);
        changeEmail.add(Box.createHorizontalStrut(horizontalStrutSize));
        changeEmail.add(editEmail);
        changeEmail.add(Box.createHorizontalStrut(horizontalStrutSize));
        changeEmail.add(saveEmail);

        JLabel loyaltyPointsLabel = new JLabel("Loyalty Points:");
        loyaltyPoints = new JLabel(String.valueOf(customerLoyaltyPoints));
        JPanel lpPanel = new JPanel();
        lpPanel.setLayout(new BoxLayout(lpPanel, BoxLayout.X_AXIS));
        lpPanel.add(loyaltyPointsLabel);
        lpPanel.add(Box.createHorizontalStrut(horizontalStrutSize));
        lpPanel.add(loyaltyPoints);

        JButton billing = new JButton("Billing");
        billing.setActionCommand("billing");
        billing.addActionListener(accountController);
        JButton address = new JButton("Address");
        address.setActionCommand("address");
        address.addActionListener(accountController);
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(Box.createHorizontalStrut(horizontalStrutSize));
        buttons.add(billing);
        buttons.add(Box.createHorizontalStrut(horizontalStrutSize));
        buttons.add(address);

        JLabel accountInformation = new JLabel("Account Information");
        accountInformation.setAlignmentX(Component.CENTER_ALIGNMENT);

        int verticalStrutSize = 35;
        Box box = Box.createVerticalBox();
        box.add(accountInformation);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        if (!UserRepository.getInstance().isAdmin()) {
            box.add(lpPanel);
            box.add(Box.createVerticalStrut(verticalStrutSize));
        }
        box.add(changeUsername);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        box.add(changePassword);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        box.add(changeEmail);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        if (!UserRepository.getInstance().isAdmin()) {
            box.add(buttons);
        }

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30,30,30,30),
                BorderFactory.createLoweredBevelBorder()
        ));

        add(box);
        setVisible(true);
    }

    public JTextField getNameInput() {
        return nameInput;
    }

    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void displayErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public JPasswordField getPasswordInput() {
        return passwordInput;
    }

    public JTextField getEmailInput() {
        return emailInput;
    }

    public AccountCards getCards() {
        return cards;
    }

    @Override
    public void update() {
        loyaltyPoints.setText(String.valueOf(UserRepository.getInstance().getLoggedInUser().getLoyaltyPoints()));
    }
}
