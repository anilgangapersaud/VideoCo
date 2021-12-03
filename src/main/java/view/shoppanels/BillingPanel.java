package view.shoppanels;

import controllers.BillingController;
import database.BillingRepository;
import database.Observer;
import database.UserRepository;
import model.payments.CreditCard;
import view.cards.AccountCards;

import javax.swing.*;
import java.awt.*;

public class BillingPanel extends JPanel implements Observer {

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

        username =  UserRepository.getInstance().getLoggedInUser().getUsername();
        customerCreditCard = BillingRepository.getInstance().getCreditCard(username);
        BillingRepository.getInstance().registerObserver(this);
        BillingController billingController = new BillingController(this);

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
        saveCreditCard.addActionListener(billingController);
        saveCreditCard.setActionCommand("saveBilling");
        JButton accountDetails = new JButton("Account");
        accountDetails.setActionCommand("account");
        accountDetails.addActionListener(billingController);
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(accountDetails);
        buttons.add(Box.createHorizontalStrut(horizontalStrutSize));
        buttons.add(saveCreditCard);

        JLabel accountInformation = new JLabel("Billing Information");
        accountInformation.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (customerCreditCard != null) {
            cardNumberInput.setText(customerCreditCard.getCardNumber());
            expiryInput.setText(customerCreditCard.getExpiry());
            csvInput.setText(customerCreditCard.getCsv());
            accountBalance.setText(String.format("%.2f", customerCreditCard.getBalance() ) + "$");
        }

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

    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public AccountCards getCards() {
        return cards;
    }

    public String getCsv() {
        return csvInput.getText();
    }

    public String getExpiry() {
        return expiryInput.getText();
    }

    public String getUsername() {
        return username;
    }

    public String getCardNumber() {
        return cardNumberInput.getText();
    }

    public CreditCard getCustomerCreditCard() {
        return customerCreditCard;
    }

    @Override
    public void update() {
        customerCreditCard = BillingRepository.getInstance().getCreditCard(username);
        if (customerCreditCard != null) {
            cardNumberInput.setText(customerCreditCard.getCardNumber());
            expiryInput.setText(customerCreditCard.getExpiry());
            csvInput.setText(customerCreditCard.getCsv());
            accountBalance.setText(String.format("%.2f", customerCreditCard.getBalance() ) + "$");
        }
    }
}
