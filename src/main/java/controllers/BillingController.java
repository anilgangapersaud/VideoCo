package controllers;

import model.payments.CreditCard;
import services.BillingServiceImpl;
import view.StoreFront;
import view.shoppanels.BillingPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BillingController implements ActionListener {

    private final BillingPanel view;
    private final BillingServiceImpl billingService;

    public BillingController(BillingPanel view) {
        this.view = view;
        billingService = StoreFront.getBillingService();
    }

    private void saveBilling() {
        CreditCard c = new CreditCard();
        c.setCsv(view.getCsv());
        c.setExpiry(view.getExpiry());
        c.setUsername(view.getUsername());
        c.setCardNumber(view.getCardNumber());
        boolean result;
        if (view.getCustomerCreditCard() == null) {
            c.setBalance(0.00D);
            result = billingService.saveCreditCard(c);
        } else {
            c.setBalance(view.getCustomerCreditCard().getBalance());
            result = billingService.updateCreditCard(c);
        }
        if (!result) {
            view.displayErrorMessage("Invalid Billing Information\nCheck fields and try again");
        } else {
            view.displayMessage("Billing saved");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("account")) {
            CardLayout cl = (CardLayout) view.getCards().getLayout();
            cl.show(view.getCards(), "eacp");
        } else if (e.getActionCommand().equals("saveBilling")) {
            saveBilling();
        }
    }
}
