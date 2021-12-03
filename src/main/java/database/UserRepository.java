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

/**
 * Maintain the User database and provide common operations on Users
 */
public class UserRepository implements DatabaseAccess, Subject {

    /**
     * Maintains a set of admin emails that correspond to admin accounts
     */
    private final Set<String> adminEmails;

    /**
     * Singleton instance
     */
    private static UserRepository userRepositoryInstance = null;

    /**
     * A quick way to get a User by username
     */
    private final Map<String, User> userAccounts;

    List<Observer> observers;

    /**
     * The currently logged-in user
     */
    private User loggedInUser;

    private final AddressRepository addressRepository;

    private final BillingRepository billingRepository;

    private final OrderRepository orderRepository;

    /**
     * Configurations for the csv file
     */
    private static final String USER_FILE_PATH = "/src/main/resources/users.csv";
    private static final String ADMIN_FILE_PATH = "/src/main/resources/admins.csv";
    private static final String adminPath = System.getProperty("user.dir") + ADMIN_FILE_PATH;
    private static final String path = System.getProperty("user.dir") + USER_FILE_PATH;

    /**
     * Construct a UserRepository class
     */
    private UserRepository() {
        adminEmails = new HashSet<>();
        userAccounts = new HashMap<>();
        observers = new ArrayList<>();
        addressRepository = AddressRepository.getInstance();
        billingRepository = BillingRepository.getInstance();
        orderRepository = OrderRepository.getInstance();
        loadCSV();
    }

    /**
     * @return get the singleton instance of this class
     */
    public static UserRepository getInstance() {
        if (userRepositoryInstance == null) {
            userRepositoryInstance = new UserRepository();
        }
        return userRepositoryInstance;
    }

    @Override
    public void loadCSV() {
        try {
            CSVParser parser = new CSVParser(new FileReader(UserRepository.path), CSVFormat.RFC4180
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

            CSVParser adminParser = new CSVParser(new FileReader(adminPath), CSVFormat.RFC4180.withDelimiter(',').withHeader("email"));
            List<CSVRecord> adminRecords = adminParser.getRecords();
            for (int i = 1; i < adminRecords.size(); i++) {
                adminEmails.add(adminRecords.get(i).get("email"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCSV() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(path, false),
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

    /**
     * give a loyalty point to a user
     * @param username the user receiving the loyalty point
     */
    public void awardLoyaltyPoint(String username) {
        User u = userAccounts.get(username);
        u.setLoyaltyPoints(u.getLoyaltyPoints()+1);
        userAccounts.replace(username, u);
        updateCSV();
    }

    /**
     * Change the username of an existing user
     * @param newUsername the new username
     * @return {@code true} if the username has been successfully changed, {@code false} otherwise
     */
    public boolean changeUsername(String newUsername) {
        if (validateUsername(newUsername) && userAccounts.containsKey(loggedInUser.getUsername())) {
            String oldUsername = loggedInUser.getUsername();
            userAccounts.remove(oldUsername);

            loggedInUser.setUsername(newUsername);
            Address a = addressRepository.getAddress(oldUsername);
            if (a != null) {
                a.setUsername(newUsername);
                addressRepository.deleteAddress(oldUsername);
                addressRepository.saveAddress(a);
            }

            CreditCard c = billingRepository.getCreditCard(oldUsername);
            if (c != null) {
                c.setUsername(newUsername);
                billingRepository.deleteCreditCard(oldUsername);
                billingRepository.saveCreditCard(c);
            }

            List<Order> orders = orderRepository.getOrdersByCustomer(oldUsername);
            if (orders != null) {
                for (Order o : orders) {
                    o.setUsername(newUsername);
                    orderRepository.cancelOrder(o.getOrderId());
                    orderRepository.createOrder(o);
                }
            }

            userAccounts.put(newUsername, loggedInUser);
            updateCSV();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Change the password of an existing user
     * @param newPassword the new password
     * @return {@code true} if the password was changed successfully, {@code false} otherwise
     */
    public boolean changePassword(String newPassword) {
        if (validatePassword(newPassword) && userAccounts.containsKey(loggedInUser.getUsername())) {
            // remove old user
            userAccounts.remove(loggedInUser.getUsername());

            // change password
            loggedInUser.setPassword(newPassword);

            // add user back
            userAccounts.put(loggedInUser.getUsername(), loggedInUser);

            updateCSV();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Change the email of an existing user
     * @param newEmail the new email
     * @return {@code true} if the email was changed successfully, {@code false} otherwise
     */
    public boolean changeEmail(String newEmail) {
        if (validateEmail(newEmail)) {
            // remove the old user
            userAccounts.remove(loggedInUser.getUsername());

            // update the email address
            loggedInUser.setEmailAddress(newEmail);

            // add the updated user
            userAccounts.put(loggedInUser.getUsername(), loggedInUser);

            updateCSV();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Delete a user from the database
     * @param username the user to delete
     */
    public void deleteUser(String username) {
        // remove the users address
        addressRepository.deleteAddress(username);
        // remove the users billing
        billingRepository.deleteCreditCard(username);
        // remove the users orders
        List<Order> userOrders = orderRepository.getOrdersByCustomer(username);
        for (Order o : userOrders) {
            orderRepository.cancelOrder(o.getOrderId());
        }
        userAccounts.remove(username);
        updateCSV();
    }

    /**
     * @return all customer accounts
     */
    public List<User> getAllCustomers() {
        List<User> customers = new ArrayList<>();
        for (Map.Entry<String, User> entry : userAccounts.entrySet()) {
            if (entry.getValue().getAccountType().equals("customer")) {
                customers.add(entry.getValue());
            }
        }
        return customers;
    }

    /**
     * @return the currently logged-in user
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * @return a user account
     */
    public User getUser(String username) {
        return userAccounts.get(username);
    }

    /**
     * update a user in the database
     * @param u the user to update
     */
    public void updateUser(User u) {
        if (userAccounts.containsKey(u.getUsername())) {
            userAccounts.replace(u.getUsername(), u);
            updateCSV();
        }
    }

    public boolean isAdmin() {
        return loggedInUser != null && loggedInUser.isAdmin();
    }

    /**
     * Register a new user
     * @param user the new user
     * @return {@code true} if registration successful, otherwise {@code false}
     */
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

    /**
     * Check user's username and password to validate login
     * @param username user's username
     * @param password user's password
     * @return {@code true} if the credentials match, {@code false} otherwise
     */
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
                && validateEmail(newUser.getEmailAddress());
    }

    private boolean validatePassword(String password) {
        return password != null && !password.equals("");
    }

    private boolean validateEmail(String email) {
        return email != null && !email.equals("");
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
