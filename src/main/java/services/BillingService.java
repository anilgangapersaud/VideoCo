package services;

import model.payments.CreditCard;

public interface BillingService {

    CreditCard getCreditCard(String username);

    boolean saveCreditCard(CreditCard c);

    boolean updateCreditCard(CreditCard c);

    void deleteCreditCard(String username);

}
