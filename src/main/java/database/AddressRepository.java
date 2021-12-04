package database;

import model.Address;
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

public class AddressRepository implements DatabaseAccess, Subject {

    private static final String ADDRESS_CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/addresses.csv";

    private volatile static AddressRepository addressRepositoryInstance;

    private final Map<String, Address> addressDatabase;

    private final List<Observer> observers;

    private AddressRepository() {
        addressDatabase = new HashMap<>();
        observers = new ArrayList<>();
        loadCSV();
    }

    /**
     * @return the singleton instance of this class
     */
    public static AddressRepository getInstance() {
        if (addressRepositoryInstance == null) {
            synchronized (AddressRepository.class) {
                if (addressRepositoryInstance == null) {
                    addressRepositoryInstance = new AddressRepository();
                }
            }
        }
        return addressRepositoryInstance;
    }

    @Override
    public void loadCSV() {
        try (CSVParser parser = new CSVParser(new FileReader(ADDRESS_CSV_PATH), CSVFormat.RFC4180
                .withDelimiter(',')
                .withHeader("username", "street", "city", "province", "postalCode"))) {
            List<CSVRecord> records = parser.getRecords();
            for (int i = 1; i < records.size(); i++) {
                Address address = new Address();
                address.setUsername(records.get(i).get("username"));
                address.setLineAddress(records.get(i).get("street"));
                address.setCity(records.get(i).get("city"));
                address.setProvince(records.get(i).get("province"));
                address.setPostalCode(records.get(i).get("postalCode"));
                addressDatabase.put(address.getUsername(), address);
            }
        } catch (IOException ignore) {
        }
    }

    /**
     * update the csv file
     */
    @Override
    public void updateCSV() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(ADDRESS_CSV_PATH, false),
                CSVFormat.RFC4180.withDelimiter(',')
                        .withHeader(
                                "username",
                                "street",
                                "city",
                                "province",
                                "postalCode"
                        ))) {
            for (Map.Entry<String, Address> entry : addressDatabase.entrySet()) {
                Address a = entry.getValue();
                printer.printRecord(a.getUsername(), a.getLineAddress(), a.getCity(), a.getProvince(), a.getPostalCode());
            }
        } catch (IOException ignore) {
        }
    }

    /**
     *  save a new address to the database
     * @param address the address to save
     * @return if true
     */
    public boolean saveAddress(Address address) {
        if (validateAddress(address)) {
            addressDatabase.put(address.getUsername(), address);
            updateCSV();
            return true;
        } else {
            return false;
        }
    }

    /**
     * delete the address associated with user
     * @param username user address
     */
    public void deleteAddress(String username) {
        addressDatabase.remove(username);
        updateCSV();
    }

    /**
     * Get an address from the database
     * @param username the username to get
     * @return the users address
     */
    public Address getAddress(String username) {
        return addressDatabase.getOrDefault(username, null);
    }

    /**
     * update the address in database
     * @param address the updated address
     * @return true if successful
     */
    public boolean updateAddress(Address address) {
        if (validateAddress(address)) {
            addressDatabase.replace(address.getUsername(), address);
            updateCSV();
            return true;
        } else {
            return false;
        }
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

    /**
     * validate address fields checks if any empty fields
     * @param address the address to validate
     * @return validation
     */
    private boolean validateAddress(Address address) {
        if (address == null) {
            return false;
        } else {
            if (address.getLineAddress() == null || address.getCity() == null || address.getProvince() == null
                || address.getPostalCode() == null || address.getUsername() == null) {
                return false;
            } else {
                return !address.getLineAddress().equals("") && !address.getCity().equals("")
                        && !address.getProvince().equals("") && !address.getPostalCode().equals("")
                        && !address.getUsername().equals("");
            }
        }
    }
}
