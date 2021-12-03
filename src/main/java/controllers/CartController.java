package controllers;

import database.AddressRepository;
import database.BillingRepository;
import database.UserRepository;
import model.*;
import model.payments.LoyaltyPoints;
import services.PaymentService;
import view.shoppanels.CartPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CartController implements ActionListener {

    private CartPanel view;
    private UserRepository userRepository;

    public CartController(CartPanel view) {
        this.view = view;
        userRepository = UserRepository.getInstance();
    }

    private void removeMovieFromCart(int[] selected) {
        Cart userCart = UserRepository.getInstance().getLoggedInUser().getCart();
        for (int row : selected) {
            Movie m = new Movie();
            m.setBarcode((String) view.getTable().getValueAt(row,0));
            m.setTitle((String) view.getTable().getValueAt(row,1));
            m.setPrice(Double.parseDouble((String)view.getTable().getValueAt(0,2)));
            userCart.removeMovieFromCart(m);
        }
    }

    private void performCheckout() {
        User u = userRepository.getLoggedInUser();
        Address userAddress = AddressRepository.getInstance().getAddress(u.getUsername());
        ButtonModel buttonModel = view.getPaymentServices().getSelection();
        if (buttonModel != null) {
            if (u.getCart().getMoviesInCart().size() == 0) {
                view.displayErrorMessage("Cart is empty");
            } else if (userAddress == null) {
                view.displayErrorMessage("No address on file\nUpdate your address in Account Details");
            } else {
                PaymentService paymentMethod = null;
                if (buttonModel.getActionCommand().equals("loyaltyPoints")) {
                    paymentMethod = new LoyaltyPoints(u.getLoyaltyPoints());
                } else if (buttonModel.getActionCommand().equals("creditCard")) {
                    paymentMethod = BillingRepository.getInstance().getCreditCard(u.getUsername());
                }
                if (paymentMethod == null) {
                    view.displayErrorMessage("No credit card on file\nAdd a credit card in Account Details");
                } else {
                    boolean paymentAccepted = Model.getOrderService().createOrder(u.getCart(), paymentMethod, userAddress);
                    if (paymentAccepted) {
                        view.displayMessage("Created Order");
                        u.getCart().clearCart();
                    } else {
                        view.displayErrorMessage("Payment Failed");
                    }
                }
            }
        } else {
            view.displayErrorMessage("Invalid Payment");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("removeItem")) {
            int[] selected = view.getTable().getSelectedRows();
            if (selected.length == 0) {
                view.displayErrorMessage("Select a movie");
            } else {
                removeMovieFromCart(selected);
            }
        } else if (e.getActionCommand().equals("clearCart")) {
            userRepository.getLoggedInUser().getCart().clearCart();
        } else if (e.getActionCommand().equals("checkout")) {
            performCheckout();
        }
    }
}
