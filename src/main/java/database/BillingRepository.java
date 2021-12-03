package database;

import model.payments.CreditCard;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillingRepository implements DatabaseAccess, Subject {

    private final Map<String, CreditCard> billingDatabase;
    private static BillingRepository billingRepositoryInstance = null;
    List<Observer> observers;

    private static final String BILLING_FILE_PATH = "/src/main/resources/billing.csv";
    private static final String billingPath = System.getProperty("user.dir") + BILLING_FILE_PATH;

    private BillingRepository() {
        billingDatabase = new HashMap<>();
        observers = new ArrayList<>();
        loadCSV();
    }

    public static BillingRepository getInstance() {
        if (billingRepositoryInstance == null) {
            billingRepositoryInstance = new BillingRepository();
        }
        return billingRepositoryInstance;
    }

    @Override
    public void loadCSV() {
        try (CSVParser parser = new CSVParser(new FileReader(BillingRepository.billingPath), CSVFormat.RFC4180
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
    public void updateCSV() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(billingPath, false),
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
                notifyObservers();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a users credit card
     * @param username the user
     * @return the users credit card
     */
    public CreditCard getCreditCard(String username) {
        return billingDatabase.get(username);
    }

    /**
     * save a credit card in the billing database
     * @param c the credit card to save
     * @return true if successful, false otherwise
     */
    public boolean saveCreditCard(CreditCard c) {
        if (validateCreditCard(c)) {
            billingDatabase.put(c.getUsername(), c);
            updateCSV();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Delete a credit card from the database
     * @param username
     */
    public void deleteCreditCard(String username) {
        billingDatabase.remove(username);
    }

    /**
     * update a credit card in the database
     * @param c the credit card to update
     * @return true if successful, false otherwise
     */
    public boolean updateCreditCard(CreditCard c) {
        if (validateCreditCard(c)) {
            billingDatabase.replace(c.getUsername(), c);
            updateCSV();
            return true;
        } else {
            return false;
        }
    }

    private boolean validateCreditCard(CreditCard c) {
        return c.getBalance() >= 0.00D && !c.getCardNumber().equals("") &&
                !c.getCsv().equals("") && !c.getExpiry().equals("");
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
}
