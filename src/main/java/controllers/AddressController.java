package controllers;

import model.Address;
import services.AddressService;
import view.StoreFront;
import view.shoppanels.AddressPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddressController implements ActionListener {

    private final AddressPanel view;
    private final AddressService addressService;

    public AddressController(AddressPanel view) {
        this.view = view;
        addressService = StoreFront.getAddressService();
    }

    private void saveAddress() {
        Address a = new Address();
        a.setLineAddress(view.getStreetInput().getText());
        a.setProvince(view.getProvince());
        a.setCity(view.getCity());
        a.setUsername(view.getUsername());
        a.setPostalCode(view.getPostalCode());
        boolean result;
        if (view.getCustomerAddress() == null) {
            result = addressService.saveAddress(a);
        } else {
            result = addressService.updateAddress(a);
        }
        if (!result) {
            view.displayErrorMessage("Invalid Address Information\nCheck fields and try again");
        } else {
            view.displayMessage("Address saved");
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("account")) {
            CardLayout cl = (CardLayout) view.getCards().getLayout();
            cl.show(view.getCards(), "eacp");
        } else if (e.getActionCommand().equals("saveAddress")) {
            saveAddress();
        }
    }
}
