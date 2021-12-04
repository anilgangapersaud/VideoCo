package database;

import model.Address;
import model.Order;
import model.User;
import model.payments.CreditCard;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class UserRepository implements DatabaseAccess, Subject {

    private final Set<String> adminEmails;

    private volatile static UserRepository userRepositoryInstance;

    private final Map<String, User> userAccounts;

    private final List<Observer> observers;

    private User loggedInUser;

    private static final String ADMIN_CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/admins.csv";
    private static final String USER_CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/users.csv";

    private UserRepository() {
        clearCSV();
        adminEmails = new HashSet<>();
        userAccounts = new HashMap<>();
        observers = new ArrayList<>();
        loadCSV();
    }

    public static UserRepository getInstance() {
        if (userRepositoryInstance == null) {
            synchronized (UserRepository.class) {
                if (userRepositoryInstance == null) {
                    userRepositoryInstance = new UserRepository();
                }
            }
        }
        return userRepositoryInstance;
    }

    @Override
    public synchronized void loadCSV() {
        try {
            CSVParser parser = new CSVParser(new FileReader(USER_CSV_PATH), CSVFormat.RFC4180
                    .withDelimiter(',')
                    .withHeader("email", "username", "password", "account", "loyaltyPoints"));
            List<CSVRecord> records = parser.getRecords();
            for (int i = 1; i < records.size(); i++) {
                User user = new User();
                user.setUsername(records.get(i).get("username"));
                user.setEmailAddress(records.get(i).get("email"));
                user.setPassword(records.get(i).get("password"));
                user.setAccountType(records.get(i).get("account"));
                user.setLoyaltyPoints(Integer.parseInt(records.get(i).get("loyaltyPoints")));
                userAccounts.put(records.get(i).get("username"), user);
            }

            CSVParser adminParser = new CSVParser(new FileReader(ADMIN_CSV_PATH), CSVFormat.RFC4180.withDelimiter(',').withHeader("email"));
            List<CSVRecord> adminRecords = adminParser.getRecords();
            for (int i = 1; i < adminRecords.size(); i++) {
                adminEmails.add(adminRecords.get(i).get("email"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void updateCSV() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(USER_CSV_PATH, false),
                CSVFormat.RFC4180
                        .withDelimiter(',')
                        .withHeader(
                                "email",
                                "username",
                                "password",
                                "account",
                                "loyaltyPoints"
                        ))) {
            for (Map.Entry<String,User> entry : userAccounts.entrySet()) {
                User u = entry.getValue();
                printer.printRecord(u.getEmailAddress(), u.getUsername(), u.getPassword(), u.getAccountType(), u.getLoyaltyPoints());
            }
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void clearCSV() {
        try {
            FileWriter fw = new FileWriter(USER_CSV_PATH, false);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void awardLoyaltyPoint(String username) {
        User u = userAccounts.get(username);
        u.setLoyaltyPoints(u.getLoyaltyPoints()+1);
        userAccounts.replace(username, u);
        updateCSV();
    }

    public boolean changeUsername(String newUsername, String oldUsername) {
        if (userAccounts.containsKey(oldUsername)) {
            if (validateUsername(newUsername)) {

                // replace user
                User u = userAccounts.get(oldUsername);
                u.setUsername(newUsername);
                userAccounts.remove(oldUsername);
                userAccounts.put(newUsername, u);

                // replace address
                Address a = getAddressRepository().getAddress(oldUsername);
                if (a != null) {
                    a.setUsername(newUsername);
                    getAddressRepository().deleteAddress(oldUsername);
                    getAddressRepository().saveAddress(a);
                }

                // replace card
                CreditCard c = getBillingRepository().getCreditCard(oldUsername);
                if (c != null) {
                    c.setUsername(newUsername);
                    getBillingRepository().updateCreditCard(c);
                    getBillingRepository().deleteCreditCard(oldUsername);
                    getBillingRepository().saveCreditCard(c);
                }

                // replace orders
                List<Order> orders = getOrderRepository().getOrdersByCustomer(oldUsername);
                if (orders != null) {
                    for (Order o : orders) {
                        o.setUsername(newUsername);
                        getOrderRepository().updateOrder(o.getOrderId(), o);
                    }
                }

                // set logged in user
                if (loggedInUser != null) {
                    loggedInUser.setUsername(newUsername);
                }
                updateCSV();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean changePassword(String newPassword, String username) {
        if (userAccounts.containsKey(username)) {
            if (validatePassword(newPassword)) {
                User u = userAccounts.get(username);
                userAccounts.remove(u.getUsername());
                u.setPassword(newPassword);
                userAccounts.put(username, u);
                updateCSV();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean changeEmail(String newEmail, String username) {
        if (userAccounts.containsKey(username)) {
            if (validateEmail(newEmail)) {
                User u = userAccounts.get(username);
                userAccounts.remove(u.getUsername());
                u.setEmailAddress(newEmail);
                userAccounts.put(username, u);
                updateCSV();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean checkUserExists(String barcode) {
        return userAccounts.containsKey(barcode);
    }

    public void deleteUser(String username) {
        getAddressRepository().deleteAddress(username);
        getBillingRepository().deleteCreditCard(username);
        List<Order> userOrders = getOrderRepository().getOrdersByCustomer(username);
        for (Order o : userOrders) {
            getOrderRepository().deleteOrder(o.getOrderId());
        }
        userAccounts.remove(username);
        updateCSV();
    }

    public List<User> getAllCustomers() {
        List<User> customers = new ArrayList<>();
        for (Map.Entry<String, User> entry : userAccounts.entrySet()) {
            if (entry.getValue().getAccountType().equals("customer")) {
                customers.add(entry.getValue());
            }
        }
        return customers;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }


    public User getUser(String username) {
        return userAccounts.getOrDefault(username, null);
    }

    public void updateUser(User u) {
        if (userAccounts.containsKey(u.getUsername())) {
            userAccounts.replace(u.getUsername(), u);
            updateCSV();
        }
    }

    public boolean isAdmin() {
        return loggedInUser != null && loggedInUser.isAdmin();
    }

    public boolean register(User user)  {
        if (!validateNewUserRegistration(user)) {
            return false;
        } else {
            if (user.getAccountType().equals("employee") && adminEmails.contains(user.getEmailAddress())) {
                user.setAccountType("admin");
            }
            userAccounts.put(user.getUsername(), user);
            updateCSV();
            return true;
        }
    }

    public boolean saveGuestAccount(User user) {
        if (user != null) {
            if (user.getUsername() != null && !user.getUsername().equals("")) {
                user.setAccountType("guest");
                userAccounts.put(user.getUsername(), user);
                updateCSV();
                return true;
            }
        }
        return false;
    }

    public boolean login(String username, String password) {
        if (userAccounts.containsKey(username)) {
            if (userAccounts.get(username).getPassword().equals(password)) {
                loggedInUser = userAccounts.get(username);
                return true;
            }
        }
        return false;
    }

    private boolean validateUsername(String username) {
       return username != null &&
                !username.equals("") &&
                !userAccounts.containsKey(username);
    }

    private boolean validateNewUserRegistration(User newUser) {
        return validateUsername(newUser.getUsername()) && validatePassword(newUser.getPassword())
                && validateEmail(newUser.getEmailAddress()) && !newUser.getAccountType().equals("");
    }

    private boolean validatePassword(String password) {
        return password != null && !password.equals("");
    }

    private boolean validateEmail(String email) {
        return email != null && !email.equals("");
    }

    private AddressRepository getAddressRepository() {
        return AddressRepository.getInstance();
    }

    private BillingRepository getBillingRepository() {
        return BillingRepository.getInstance();
    }

    private OrderRepository getOrderRepository() {
        return OrderRepository.getInstance();
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
