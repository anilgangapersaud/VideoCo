package controllers;

import model.*;
import model.payments.LoyaltyPoints;
import model.payments.PaymentService;
import services.AddressService;
import services.BillingService;
import services.OrderService;
import services.UserService;
import view.StoreFront;
import view.shoppanels.CartPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CartController implements ActionListener {

    private final CartPanel view;
    private final UserService userService;
    private final AddressService addressService;
    private final BillingService billingService;
    private final OrderService orderService;

    public CartController(CartPanel view) {
        this.view = view;
        userService = StoreFront.getUserService();
        addressService = StoreFront.getAddressService();
        billingService = StoreFront.getBillingService();
        orderService = StoreFront.getOrderService();
    }

    private void removeMovieFromCart(int[] selected) {
        Cart userCart = userService.getLoggedInUser().getCart();
        for (int row : selected) {
            Movie m = new Movie();
            m.setBarcode((String) view.getTable().getValueAt(row,0));
            m.setTitle((String) view.getTable().getValueAt(row,1));
            m.setPrice(Double.parseDouble((String)view.getTable().getValueAt(0,2)));
            userCart.removeMovieFromCart(m);
        }
    }

    private void performCheckout() {
        User u = userService.getLoggedInUser();
        Address userAddress = addressService.getAddress(u.getUsername());
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
                    paymentMethod = billingService.getCreditCard(u.getUsername());
                }
                if (paymentMethod == null) {
                    view.displayErrorMessage("No credit card on file\nAdd a credit card in Account Details");
                } else {
                    u.getCart().setUsername(u.getUsername());
                    Order paymentAccepted = orderService.createOrder(u.getCart(), paymentMethod);
                    if (paymentAccepted != null) {
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

    private void removeItem() {
        int[] selected = view.getTable().getSelectedRows();
        if (selected.length == 0) {
            view.displayErrorMessage("Select a movie");
        } else {
            removeMovieFromCart(selected);
        }
    }

    private void clearCart() {
        userService.getLoggedInUser().getCart().clearCart();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("removeItem")) {
            removeItem();
        } else if (e.getActionCommand().equals("clearCart")) {
            clearCart();
        } else if (e.getActionCommand().equals("checkout")) {
            performCheckout();
        }
    }
}
