package database;

import model.Model;
import model.Address;
import model.User;
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
public class UserRepository implements DatabaseAccess {

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
        load();
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
    public void load() {
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
    public void update() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            update();
            return true;
        }
    }

    /**
     * Check user's username and password to validate login
     * @param username user's username
     * @param password user's password
     * @return {@code true} if the credentials match, {@code false} otherwise
     */
    public User login(String username, String password) {
        User u = null;
        if (userAccounts.containsKey(username)) {
            if (userAccounts.get(username).getPassword().equals(password)) {
                u = userAccounts.get(username);
            }
        }
        return u;
    }

    /**
     * update a user in the database
     * @param u the user to update
     * @return true if successful, false otherwise
     */
    public boolean updateUser(User u) {
        if (userAccounts.containsKey(u.getUsername())) {
            userAccounts.replace(u.getUsername(), u);
            update();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Change the username of an existing user
     * @param newUsername the new username
     * @param user the existing user
     * @return {@code true} if the username has been successfully changed, {@code false} otherwise
     */
    public boolean changeUsername(String newUsername, User user) {
        if (validateUsername(newUsername) && userAccounts.containsKey(user.getUsername())) {
            // remove old user
            String oldUsername = user.getUsername();
            userAccounts.remove(user.getUsername());

            // update the username
            user.setUsername(newUsername);
            Address a = Model.getAddressService().getAddress(oldUsername);
            a.setUsername(newUsername);
            Model.getAddressService().deleteAddress(oldUsername);
            Model.getAddressService().saveAddress(a);

            // add the new user
            userAccounts.put(newUsername, user);

            update();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Change the password of an existing user
     * @param newPassword the new password
     * @param user the existing user
     * @return {@code true} if the password was changed successfully, {@code false} otherwise
     */
    public boolean changePassword(String newPassword, User user) {
        if (validatePassword(newPassword) && userAccounts.containsKey(user.getUsername())) {
            // remove old user
            userAccounts.remove(user.getUsername());

            // change password
            user.setPassword(newPassword);

            // add user back
            userAccounts.put(user.getUsername(), user);

            update();
            return true;
        } else {
            return false;
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
        update();
    }

    /**
     * Change the email of an existing user
     * @param newEmail the new email
     * @param user the existing user
     * @return {@code true} if the email was changed successfully, {@code false} otherwise
     */
    public boolean changeEmail(String newEmail, User user) {
        if (validateEmail(newEmail)) {
            // remove the old user
            userAccounts.remove(user.getUsername());

            // update the email address
            user.setEmailAddress(newEmail);

            // add the updated user
            userAccounts.put(user.getUsername(), user);

            update();
            return true;
        } else {
            return false;
        }
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
}
