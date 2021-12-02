package view.shoppanels;

import model.Model;
import model.payments.CreditCard;
import view.cards.AccountCards;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.jar.JarEntry;

public class BillingPanel extends JPanel implements ActionListener {

    private final AccountCards cards;
    private CreditCard customerCreditCard;

    private final JTextField cardNumberInput;

    private final JTextField expiryInput;

    private final JTextField csvInput;

    private final JLabel accountBalance;

    private final String username;

    public BillingPanel(AccountCards cards) {
        this.cards = cards;
        setLayout(new GridBagLayout());
        username =  Model.getUserService().getLoggedInUser().getUsername();
        customerCreditCard = Model.getBillingService().getCreditCard(username);

        // credit card
        JLabel accountBalanceLabel = new JLabel("Balance:");
        accountBalance = new JLabel("");
        JPanel balancePanel = new JPanel();
        balancePanel.setLayout(new BoxLayout(balancePanel, BoxLayout.X_AXIS));
        balancePanel.add(accountBalanceLabel);
        int horizontalStrutSize = 5;
        balancePanel.add(Box.createHorizontalStrut(horizontalStrutSize));
        balancePanel.add(accountBalance);

        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberInput = new JTextField(20);
        JPanel cardNumberPanel = new JPanel();
        cardNumberPanel.setLayout(new BoxLayout(cardNumberPanel, BoxLayout.X_AXIS));
        cardNumberPanel.add(cardNumberLabel);
        cardNumberPanel.add(Box.createHorizontalStrut(horizontalStrutSize));
        cardNumberPanel.add(cardNumberInput);

        JLabel expiryLabel = new JLabel("Expiry");
        expiryInput = new JTextField(20);
        JPanel expiryPanel = new JPanel();
        expiryPanel.setLayout(new BoxLayout(expiryPanel, BoxLayout.X_AXIS));
        expiryPanel.add(expiryLabel);
        expiryPanel.add(Box.createHorizontalStrut(horizontalStrutSize));
        expiryPanel.add(expiryInput);

         JLabel csvLabel = new JLabel("CSV");
         csvInput = new JTextField(20);
         JPanel csvPanel = new JPanel();
         csvPanel.setLayout(new BoxLayout(csvPanel, BoxLayout.X_AXIS));
         csvPanel.add(csvLabel);
         csvPanel.add(Box.createHorizontalStrut(horizontalStrutSize));
         csvPanel.add(csvInput);

         // buttons
        JButton saveCreditCard = new JButton("Save Card");
        saveCreditCard.addActionListener(this);
        saveCreditCard.setActionCommand("saveBilling");
        JButton accountDetails = new JButton("Account");
        accountDetails.setActionCommand("account");
        accountDetails.addActionListener(this);
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(accountDetails);
        buttons.add(Box.createHorizontalStrut(horizontalStrutSize));
        buttons.add(saveCreditCard);

        JLabel accountInformation = new JLabel("Billing Information");
        accountInformation.setAlignmentX(Component.CENTER_ALIGNMENT);

        updateFields();

        Box box = Box.createVerticalBox();
        box.add(accountInformation);
        int verticalStrutSize = 35;
        box.add(Box.createVerticalStrut(verticalStrutSize));
        box.add(balancePanel);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        box.add(cardNumberPanel);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        box.add(expiryPanel);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        box.add(csvPanel);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        box.add(buttons);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30,30,30,30),
                BorderFactory.createLoweredBevelBorder()
        ));

        add(box);
        setVisible(true);
    }

    public void updateBalance() {
        accountBalance.setText(String.format("%.2f", Model.getBillingService().getCreditCard(username).getBalance()) + "$");
    }

    private void updateFields() {
        if (customerCreditCard != null) {
            cardNumberInput.setText(customerCreditCard.getCardNumber());
            expiryInput.setText(customerCreditCard.getExpiry());
            csvInput.setText(customerCreditCard.getCsv());
            accountBalance.setText(String.format("%.2f", customerCreditCard.getBalance() ) + "$");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("account")) {
            CardLayout cl = (CardLayout) cards.getLayout();
            cl.show(cards, "eacp");
        } else if (e.getActionCommand().equals("saveBilling")) {
            CreditCard c = new CreditCard();
            c.setCsv(csvInput.getText());
            c.setExpiry(expiryInput.getText());
            c.setUsername(username);
            c.setCardNumber(cardNumberInput.getText());
            boolean result;
            if (customerCreditCard == null) {
                c.setBalance(0.00D);
                result = Model.getBillingService().saveCreditCard(c);
            } else {
                c.setBalance(customerCreditCard.getBalance());
                result = Model.getBillingService().updateCreditCard(c);
            }
            if (!result) {
                JOptionPane.showMessageDialog(this, "Invalid Billing Information. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Saved Billing");
            }
            customerCreditCard = c;
            updateFields();
        }
    }
}
