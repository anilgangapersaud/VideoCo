package database;

import model.User;
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

    private final String ADMIN_CSV_PATH;
    private final String USER_CSV_PATH;

    private UserRepository(String adminPath, String userPath) {
        ADMIN_CSV_PATH = adminPath;
        USER_CSV_PATH = userPath;
        clearCSV();
        adminEmails = new HashSet<>();
        userAccounts = new HashMap<>();
        observers = new ArrayList<>();
        loadCSV();
    }

    public static UserRepository getInstance(String adminPath, String userPath) {
        if (userRepositoryInstance == null) {
            synchronized (UserRepository.class) {
                if (userRepositoryInstance == null) {
                    userRepositoryInstance = new UserRepository(adminPath, userPath);
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

    public void changeUsername(String newUsername, String oldUsername) {
        // replace user
        User u = userAccounts.get(oldUsername);
        u.setUsername(newUsername);
        userAccounts.remove(oldUsername);
        userAccounts.put(newUsername, u);

        // set logged in user
        if (loggedInUser != null) {
            loggedInUser.setUsername(newUsername);
        }
        updateCSV();
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

    public boolean checkUserExists(String username) {
        return userAccounts.containsKey(username);
    }

    public void deleteUser(String username) {
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


    private boolean validateNewUserRegistration(User newUser) {
        return newUser != null && !newUser.getUsername().equals("") && validatePassword(newUser.getPassword())
                && validateEmail(newUser.getEmailAddress()) && !newUser.getAccountType().equals("");
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
