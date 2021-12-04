package services;

import database.BillingRepository;
import database.Observer;
import model.payments.CreditCard;

public class BillingServiceImpl {

    private static  String BILLING_CSV_PATH;

    private final BillingRepository billingRepository;

    private volatile static BillingServiceImpl instance;

    private BillingServiceImpl() {
        billingRepository = BillingRepository.getInstance(BILLING_CSV_PATH);
    }

    public static BillingServiceImpl getInstance() {
        if (instance == null) {
            synchronized (BillingServiceImpl.class) {
                if (instance == null) {
                    instance = new BillingServiceImpl();
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

    public void chargeCustomer(String username, double amount) {
        billingRepository.chargeCustomer(username, amount);
    }

    public void registerObserver(Observer o) {
        billingRepository.registerObserver(o);
    }

    public void removeObserver(Observer o) {
        billingRepository.removeObserver(o);
    }
}
