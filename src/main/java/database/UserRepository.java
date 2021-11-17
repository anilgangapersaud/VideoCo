package database;

import model.user.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class UserRepository {

    public List<User> users = new ArrayList<>();
    public Set<String> adminEmails = new HashSet<>();
    public Map<String, User> userAccounts = new HashMap<>();

    private static final String USER_FILE_PATH = "/src/main/resources/users.csv";
    private static final String ADMIN_FILE_PATH = "/src/main/resources/admins.csv";
    private static final String adminPath = System.getProperty("user.dir") + ADMIN_FILE_PATH;
    private static final String path = System.getProperty("user.dir") + USER_FILE_PATH;

    public UserRepository() {
        load(path);
    }

    public void load (String path) {
        try {
            CSVParser parser = new CSVParser(new FileReader(path), CSVFormat.RFC4180
                    .withDelimiter(',')
                    .withHeader("email", "username", "password", "account"));
            List<CSVRecord> records = parser.getRecords();
            for (int i = 1; i < records.size(); i++) {
                User user = new User();
                user.setUsername(records.get(i).get("username"));
                user.setEmailAddress(records.get(i).get("email"));
                user.setPassword(records.get(i).get("password"));
                user.setAccountType(records.get(i).get("account"));
                users.add(user);
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

    public boolean register(User user)  {
        if (userAccounts.containsKey(user.getUsername())) {
            return false;
        } else {
            users.add(user);
            if (user.getAccountType().equals("employee") && adminEmails.contains(user.getEmailAddress())) {
                user.setAccountType("admin");
            }
            userAccounts.put(user.getUsername(), user);
            update();
            return true;
        }
    }

    public User login(String username, String password) {
        User u = null;
        if (userAccounts.containsKey(username)) {
            if (userAccounts.get(username).getPassword().equals(password)) {
                u = userAccounts.get(username);
            }
        }
        return u;
    }

    public boolean changeUsername(String newUsername, User user) {
        if (newUsername != null &&
                !newUsername.equals("") &&
                !userAccounts.containsKey(newUsername) &&
                userAccounts.containsKey(user.getUsername())) {
            users.remove(user);
            userAccounts.remove(user.getUsername());
            user.setUsername(newUsername);
            users.add(user);
            userAccounts.put(newUsername, user);
            update();
            return true;
        } else {
            return false;
        }
    }

    public boolean changePassword(String newPassword, User user) {
        if (newPassword != null && !newPassword.equals("") && userAccounts.containsKey(user.getUsername())) {
            userAccounts.get(user.getUsername()).setPassword(newPassword);
            users.remove(user);
            user.setPassword(newPassword);
            users.add(user);
            update();
            return true;
        } else {
            return false;
        }
    }

    public boolean changeEmail(String newEmail, User user) {
        if (newEmail != null && !newEmail.equals("")) {
            userAccounts.get(user.getUsername()).setEmailAddress(newEmail);
            users.remove(user);
            user.setEmailAddress(newEmail);
            users.add(user);
            update();
            return true;
        } else {
            return false;
        }
    }

    public void update() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(path, false),
                CSVFormat.RFC4180
                        .withDelimiter(',')
                        .withHeader(
                "email",
                "username",
                "password",
                "account"
        ))) {
            for (User u : users) {
                printer.printRecord(u.getEmailAddress(), u.getUsername(), u.getPassword(), u.getAccountType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
