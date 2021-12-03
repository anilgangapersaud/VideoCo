package controllers;

import database.BillingRepository;
import model.payments.CreditCard;
import view.shoppanels.BillingPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class BillingController implements ActionListener {

    private BillingPanel view;
    private BillingRepository billingRepository;

    public BillingController(BillingPanel view) {
        this.view = view;
        billingRepository = BillingRepository.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("account")) {
            CardLayout cl = (CardLayout) view.getCards().getLayout();
            cl.show(view.getCards(), "eacp");
        } else if (e.getActionCommand().equals("saveBilling")) {
            CreditCard c = new CreditCard();
            c.setCsv(view.getCsv());
            c.setExpiry(view.getExpiry());
            c.setUsername(view.getUsername());
            c.setCardNumber(view.getCardNumber());
            boolean result;
            if (view.getCustomerCreditCard() == null) {
                c.setBalance(0.00D);
                result = billingRepository.saveCreditCard(c);
            } else {
                c.setBalance(view.getCustomerCreditCard().getBalance());
                result = billingRepository.updateCreditCard(c);
            }
            if (!result) {
                view.displayErrorMessage("Invalid Billing Information\nCheck fields and try again");
            } else {
                view.displayMessage("Billing saved");
            }
        }
    }
}
