package database;

import model.payments.CreditCard;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import view.StoreFront;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillingRepository implements DatabaseAccess, Subject {

    private volatile static BillingRepository billingRepositoryInstance;

    private final Map<String, CreditCard> billingDatabase;

    private final List<Observer> observers;

    private static String BILLING_CSV_PATH;

    private BillingRepository(String path) {
        BILLING_CSV_PATH = path;
        billingDatabase = new HashMap<>();
        observers = new ArrayList<>();
        loadCSV();
    }

    public static BillingRepository getInstance(String path) {
        if (billingRepositoryInstance == null) {
            synchronized (BillingRepository.class) {
                if (billingRepositoryInstance == null) {
                    billingRepositoryInstance = new BillingRepository(path);
                }
            }
        }
        return billingRepositoryInstance;
    }

    @Override
    public synchronized void loadCSV() {
        try (CSVParser parser = new CSVParser(new FileReader(BILLING_CSV_PATH), CSVFormat.RFC4180
                .withDelimiter(',')
                .withHeader("username","cardNumber","expiry","csv","balance"))) {
            List<CSVRecord> records = parser.getRecords();
            for (int i = 1; i < records.size(); i++) {
                CreditCard card = new CreditCard();
                card.setUsername(records.get(i).get("username"));
                card.setCardNumber(records.get(i).get("cardNumber"));
                card.setExpiry(records.get(i).get("expiry"));
                card.setCsv(records.get(i).get("csv"));
                card.setBalance(Double.parseDouble(records.get(i).get("balance")));
                billingDatabase.put(card.getUsername(), card);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void updateCSV() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(BILLING_CSV_PATH, false),
            CSVFormat.RFC4180.withDelimiter(',')
                    .withHeader(
                            "username",
                            "cardNumber",
                            "expiry",
                            "csv",
                            "balance"
                    ))) {
            for (Map.Entry<String,CreditCard> entry : billingDatabase.entrySet()) {
                CreditCard c = entry.getValue();
                printer.printRecord(c.getUsername(), c.getCardNumber(), c.getExpiry(), c.getCsv(), c.getBalance());
            }
            notifyObservers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void clearCSV() {
        try {
            FileWriter fw = new FileWriter(BILLING_CSV_PATH, false);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CreditCard getCreditCard(String username) {
        return billingDatabase.get(username);
    }

    public boolean saveCreditCard(CreditCard c) {
        if (validateCreditCard(c)) {
            billingDatabase.put(c.getUsername(), c);
            updateCSV();
            return true;
        } else {
            return false;
        }
    }

    public void deleteCreditCard(String username) {
        billingDatabase.remove(username);
    }

    public boolean updateCreditCard(CreditCard c) {
        if (validateCreditCard(c)) {
            billingDatabase.replace(c.getUsername(), c);
            updateCSV();
            return true;
        } else {
            return false;
        }
    }

    public void refundCustomer(String username, double amount) {
        CreditCard c = billingDatabase.get(username);
        c.refund(amount);
        updateCreditCard(c);
    }

    public void chargeCustomer(String username, double amount) {
        CreditCard c = billingDatabase.get(username);
        c.charge(amount);
        updateCreditCard(c);
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

    private boolean validateCreditCard(CreditCard c) {
        if (c == null) {
            return false;
        } else {
            if (c.getUsername() == null || c.getExpiry() == null || c.getCsv() == null
            || c.getCardNumber() == null || c.getBalance() < 0) {
                return false;
            } else {
                return !c.getCardNumber().equals("") &&
                        !c.getCsv().equals("") && !c.getExpiry().equals("");
            }
        }
    }
}
