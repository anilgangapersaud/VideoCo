package services;

import database.BillingRepository;
import database.Observer;
import model.payments.CreditCard;

public class BillingService {

    private static  String BILLING_CSV_PATH;

    private final BillingRepository billingRepository;

    private volatile static BillingService instance;

    private BillingService() {
        billingRepository = BillingRepository.getInstance(BILLING_CSV_PATH);
    }

    public static BillingService getInstance() {
        if (instance == null) {
            synchronized (BillingService.class) {
                if (instance == null) {
                    instance = new BillingService();
                }
            }
        }
        return instance;
    }

    public static void setCsvPath(String path) {
        BILLING_CSV_PATH = path;
    }

    public CreditCard getCreditCard(String username) {
        return billingRepository.getCreditCard(username);
    }

    public boolean saveCreditCard(CreditCard c) {
        return billingRepository.saveCreditCard(c);
    }

    public void deleteCreditCard(String username) {
        billingRepository.deleteCreditCard(username);
    }

    public boolean updateCreditCard(CreditCard c) {
        return billingRepository.updateCreditCard(c);
    }

    public void refundCustomer(String username, double amount) {
        billingRepository.refundCustomer(username, amount);
    }

    public void registerObserver(Observer o) {
        billingRepository.registerObserver(o);
    }

    public void removeObserver(Observer o) {
        billingRepository.removeObserver(o);
    }
}
