package view.shoppanels;

import model.Model;
import model.Address;
import view.cards.AccountCards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditAddressPanel extends JPanel implements ActionListener {


    private final AccountCards cards;
    private Address customerAddress;

    private final JTextField streetInput;

    private final JTextField cityInput;

    private final JTextField provinceInput;

    private final JTextField postalCodeInput;

    private final String username;

    public EditAddressPanel(AccountCards cards) {
        this.cards = cards;
        setLayout(new GridBagLayout());
        username = Model.getUserService().getLoggedInUser().getUsername();
        customerAddress = Model.getAddressService().getAddress(username);

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
        provinceInput = new JTextField(20);
        JPanel provincePanel = new JPanel();
        provincePanel.setLayout(new BoxLayout(provincePanel, BoxLayout.X_AXIS));
        provincePanel.add(provinceLabel);
        provincePanel.add(Box.createHorizontalStrut(horizontalStrutSize));
        provincePanel.add(provinceInput);

        JLabel postalCodeLabel = new JLabel("Postal Code:");
        postalCodeInput = new JTextField(20);
        JPanel postalCodePanel = new JPanel();
        postalCodePanel.setLayout(new BoxLayout(postalCodePanel, BoxLayout.X_AXIS));
        postalCodePanel.add(postalCodeLabel);
        postalCodePanel.add(Box.createHorizontalStrut(horizontalStrutSize));
        postalCodePanel.add(postalCodeInput);

        // buttons
        JButton saveAddress = new JButton("Save Address");
        saveAddress.addActionListener(this);
        saveAddress.setActionCommand("saveAddress");
        JButton accountDetails = new JButton("Account");
        accountDetails.setActionCommand("account");
        accountDetails.addActionListener(this);
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(accountDetails);
        buttons.add(Box.createHorizontalStrut(horizontalStrutSize));
        buttons.add(saveAddress);

        JLabel accountInformation = new JLabel("Address Information");
        accountInformation.setAlignmentX(Component.CENTER_ALIGNMENT);

        updateFields();

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

    private void updateFields() {
        if (customerAddress != null) {
            streetInput.setText(customerAddress.getLineAddress());
            cityInput.setText(customerAddress.getCity());
            provinceInput.setText(customerAddress.getProvince());
            postalCodeInput.setText(customerAddress.getPostalCode());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("account")) {
            CardLayout cl = (CardLayout) cards.getLayout();
            cl.show(cards, "eacp");
        } else if (e.getActionCommand().equals("saveAddress")) {
            Address a = new Address();
            a.setLineAddress(streetInput.getText());
            a.setProvince(provinceInput.getText());
            a.setCity(cityInput.getText());
            a.setUsername(username);
            a.setPostalCode(postalCodeInput.getText());
            boolean result;
            if (customerAddress == null) {
                result = Model.getAddressService().saveAddress(a);
            } else {
                result = Model.getAddressService().updateAddress(a);
            }
            if (!result) {
                JOptionPane.showMessageDialog(this, "Invalid Address Information. Please try again.","Error",JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Saved Address");
            }
            customerAddress = a;
            updateFields();
        }
    }
}
