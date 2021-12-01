package database;

import model.Address;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Persist addresses in csv files and perform CRUD
 */
public class AddressRepository implements DatabaseAccess {

    /**
     * Stores the addresses by username
     */
    private final Map<String, Address> addressDatabase;

    /**
     * Singleton instance of this class
     */
    private static AddressRepository addressRepositoryInstance = null;

    /**
     * configs
     */
    private static final String ADDRESS_FILE_PATH = "/src/main/resources/addresses.csv";
    private static final String addressPath = System.getProperty("user.dir") + ADDRESS_FILE_PATH;

    /**
     * Initialize the address repo
     */
    private AddressRepository() {
        addressDatabase = new HashMap<>();
        load();
    }

    public static AddressRepository getInstance() {
        if (addressRepositoryInstance == null) {
            addressRepositoryInstance = new AddressRepository();
        }
        return addressRepositoryInstance;
    }

    /**
     * load the address data into the map
     */
    @Override
    public void load() {
        try {
            CSVParser parser = new CSVParser(new FileReader(AddressRepository.addressPath), CSVFormat.RFC4180
                    .withDelimiter(',')
                    .withHeader("username", "street", "city", "province", "postalCode"));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * update the csv file
     */
    @Override
    public void update() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(addressPath, false),
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
        } catch (IOException e) {
            e.printStackTrace();
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
            update();
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
        update();
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
            update();
            return true;
        } else {
            return false;
        }
    }

    /**
     * validate address fields
     * @param address the address to validate
     * @return validation
     */
    private boolean validateAddress(Address address) {
        // check if all fields are non null or empty
        return !address.getLineAddress().equals("") && !address.getCity().equals("")
                && !address.getProvince().equals("") && !address.getPostalCode().equals("")
                && !address.getUsername().equals("");
    }

}
