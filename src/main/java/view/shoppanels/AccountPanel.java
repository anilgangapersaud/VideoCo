package view.shoppanels;

import model.Model;
import view.cards.AccountCards;

import javax.smartcardio.Card;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountPanel extends JPanel implements ActionListener {

    private final AccountCards cards;

    private final JTextField nameInput;

    private final JPasswordField passwordInput;

    private final JTextField emailInput;

    JLabel loyaltyPoints;

    public AccountPanel(AccountCards cards) {
        this.cards = cards;
        setLayout(new GridBagLayout());
        String customerName = Model.getUserService().getLoggedInUser().getUsername();
        String customerPassword = Model.getUserService().getLoggedInUser().getPassword();
        String customerEmail = Model.getUserService().getLoggedInUser().getEmailAddress();
        int customerLoyaltyPoints = Model.getUserService().getLoggedInUser().getLoyaltyPoints();

        JLabel username = new JLabel("Username:");
        nameInput = new JTextField(20);
        nameInput.setText(customerName);
        nameInput.setEditable(false);
        JButton editName = new JButton("Edit");
        editName.setActionCommand("editName");
        editName.addActionListener(this);
        JButton saveName = new JButton("Save");
        saveName.setActionCommand("saveName");
        saveName.addActionListener(this);
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
        editPassword.addActionListener(this);
        JButton savePassword = new JButton("Save");
        savePassword.addActionListener(this);
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
        editEmail.addActionListener(this);
        JButton saveEmail = new JButton("Save");
        saveEmail.setActionCommand("saveEmail");
        saveEmail.addActionListener(this);
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
        billing.addActionListener(this);
        JButton address = new JButton("Address");
        address.setActionCommand("address");
        address.addActionListener(this);
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(Box.createHorizontalStrut(horizontalStrutSize));
        buttons.add(billing);
        buttons.add(Box.createHorizontalStrut(horizontalStrutSize));
        buttons.add(address);

        JLabel accountInformation = new JLabel("Account Information");
        accountInformation.setAlignmentX(Component.CENTER_ALIGNMENT);

        Box box = Box.createVerticalBox();
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30,30,30,30),
                BorderFactory.createLoweredBevelBorder()
        ));

        box.add(accountInformation);
        int verticalStrutSize = 35;
        box.add(Box.createVerticalStrut(verticalStrutSize));
        if (!Model.getUserService().getLoggedInUser().isAdmin()) {
            box.add(lpPanel);
            box.add(Box.createVerticalStrut(verticalStrutSize));
        }
        box.add(changeUsername);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        box.add(changePassword);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        box.add(changeEmail);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        if (!Model.getUserService().getLoggedInUser().isAdmin()) {
            box.add(buttons);
        }
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.setAlignmentY(Component.CENTER_ALIGNMENT);

        add(box);

        setVisible(true);
    }

    public void updateLoyaltyPoints() {
        loyaltyPoints.setText(String.valueOf(Model.getUserService().getLoggedInUser().getLoyaltyPoints()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("editName")) {
            nameInput.setEditable(true);
        } else if (e.getActionCommand().equals("saveName")) {
            nameInput.setEditable(false);
            boolean result = Model.getUserService().changeUsername(nameInput.getText());
            if (result) {
                JOptionPane.showMessageDialog(this, "Changed Username");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("editPassword")) {
            passwordInput.setEchoChar((char) 0);
            passwordInput.setEditable(true);
        } else if (e.getActionCommand().equals("savePassword")) {
            passwordInput.setEditable(false);
            passwordInput.setEchoChar('*');
            boolean result = Model.getUserService().changePassword(new String(passwordInput.getPassword()));
            if (result) {
                JOptionPane.showMessageDialog(this, "Changed Password");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("editEmail")) {
            emailInput.setEditable(true);
        } else if (e.getActionCommand().equals("saveEmail")) {
            emailInput.setEditable(false);
            boolean result = Model.getUserService().changeEmail(emailInput.getText());
            if (result) {
                JOptionPane.showMessageDialog(this, "Changed Email");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Email", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("address")) {
            CardLayout cl = (CardLayout) cards.getLayout();
            cl.show(cards, "eadp");
        } else if (e.getActionCommand().equals("billing")) {
            CardLayout cl = (CardLayout) cards.getLayout();
            cl.show(cards, "ebp");
        }
    }
}
