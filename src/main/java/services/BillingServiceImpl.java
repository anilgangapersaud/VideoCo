package services;

import database.BillingRepository;
import model.payments.CreditCard;

public class BillingServiceImpl implements BillingService {

    private final BillingRepository billingRepository;

    public BillingServiceImpl() {
        billingRepository = BillingRepository.getInstance();
    }

    @Override
    public CreditCard getCreditCard(String username) {
        return billingRepository.getCreditCard(username);
    }

    @Override
    public boolean saveCreditCard(CreditCard c) {
        return billingRepository.saveCreditCard(c);
    }

    @Override
    public boolean updateCreditCard(CreditCard c) {
        return billingRepository.updateCreditCard(c);
    }

    @Override
    public void deleteCreditCard(String username) {
        billingRepository.deleteCreditCard(username);
    }
}
