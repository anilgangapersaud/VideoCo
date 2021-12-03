package view.shoppanels;

import controllers.AddressController;
import database.AddressRepository;
import database.Observer;
import database.UserRepository;
import model.Model;
import model.Address;
import view.cards.AccountCards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddressPanel extends JPanel implements Observer {

    private final AccountCards cards;

    private Address customerAddress;

    private final JTextField streetInput;

    private final JTextField cityInput;

    private final JComboBox<String> provinceList;

    private final String[] provinces = {"Ontario", "Quebec", "British Columbia", "Alberta", "New Brunswick", "Manitoba", "Newfoundland and Labrador", "Northwest Territories", "Nova Scotia", "Nunavut", "Prince Edward Island", "Saskatchewan", "Yukon"};

    private final JTextField postalCodeInput;

    private final String username;

    public AddressPanel(AccountCards cards) {
        this.cards = cards;
        setLayout(new GridBagLayout());
        username = UserRepository.getInstance().getLoggedInUser().getUsername();
        customerAddress = AddressRepository.getInstance().getAddress(username);
        AddressController addressController = new AddressController(this);
        AddressRepository.getInstance().registerObserver(this);

        // address
        JLabel streetLabel = new JLabel("Street:");
        streetInput = new JTextField(20);
        JPanel streetPanel = new JPanel();
        streetPanel.setLayout(new BoxLayout(streetPanel, BoxLayout.X_AXIS));
        streetPanel.add(streetLabel);
        int horizontalStrutSize = 5;
        streetPanel.add(Box.createHorizontalStrut(horizontalStrutSize));
        streetPanel.add(streetInput);

        JLabel cityLabel = new JLabel("City:");
        cityInput = new JTextField(20);
        JPanel cityPanel = new JPanel();
        cityPanel.setLayout(new BoxLayout(cityPanel, BoxLayout.X_AXIS));
        cityPanel.add(cityLabel);
        cityPanel.add(Box.createHorizontalStrut(horizontalStrutSize));
        cityPanel.add(cityInput);

        JLabel provinceLabel = new JLabel("Province:");
        provinceList = new JComboBox<>(provinces);
        JPanel provincePanel = new JPanel();
        provincePanel.setLayout(new BoxLayout(provincePanel, BoxLayout.X_AXIS));
        provincePanel.add(provinceLabel);
        provincePanel.add(Box.createHorizontalStrut(horizontalStrutSize));
        provincePanel.add(provinceList);

        JLabel postalCodeLabel = new JLabel("Postal Code:");
        postalCodeInput = new JTextField(20);
        JPanel postalCodePanel = new JPanel();
        postalCodePanel.setLayout(new BoxLayout(postalCodePanel, BoxLayout.X_AXIS));
        postalCodePanel.add(postalCodeLabel);
        postalCodePanel.add(Box.createHorizontalStrut(horizontalStrutSize));
        postalCodePanel.add(postalCodeInput);

        // buttons
        JButton saveAddress = new JButton("Save Address");
        saveAddress.addActionListener(addressController);
        saveAddress.setActionCommand("saveAddress");
        JButton accountDetails = new JButton("Account");
        accountDetails.setActionCommand("account");
        accountDetails.addActionListener(addressController);
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(accountDetails);
        buttons.add(Box.createHorizontalStrut(horizontalStrutSize));
        buttons.add(saveAddress);

        if (customerAddress != null) {
            streetInput.setText(customerAddress.getLineAddress());
            cityInput.setText(customerAddress.getCity());
            provinceList.setSelectedItem(customerAddress.getProvince());
            postalCodeInput.setText(customerAddress.getPostalCode());
        }

        JLabel accountInformation = new JLabel("Address Information");
        accountInformation.setAlignmentX(Component.CENTER_ALIGNMENT);

        Box box = Box.createVerticalBox();
        box.add(accountInformation);
        int verticalStrutSize = 35;
        box.add(Box.createVerticalStrut(verticalStrutSize));
        box.add(streetPanel);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        box.add(cityPanel);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        box.add(provincePanel);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        box.add(postalCodePanel);
        box.add(Box.createVerticalStrut(verticalStrutSize));
        box.add(buttons);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30,30,30,30),
                BorderFactory.createLoweredBevelBorder()
        ));

        add(box);
        setVisible(true);
    }

    public AccountCards getCards() {
        return cards;
    }

    public JTextField getStreetInput() {
        return streetInput;
    }

    public String getProvince() {
        return (String)provinceList.getSelectedItem();
    }

    public String getCity() {
        return cityInput.getText();
    }

    public String getUsername() {
        return username;
    }

    public String getPostalCode() {
        return postalCodeInput.getText();
    }

    public Address getCustomerAddress() {
        return customerAddress;
    }

    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, "", "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void update() {
        customerAddress = AddressRepository.getInstance().getAddress(UserRepository.getInstance().getLoggedInUser().getUsername());
        if (customerAddress != null) {
            streetInput.setText(customerAddress.getLineAddress());
            cityInput.setText(customerAddress.getCity());
            provinceList.setSelectedItem(customerAddress.getProvince());
            postalCodeInput.setText(customerAddress.getPostalCode());
        }
    }
}
