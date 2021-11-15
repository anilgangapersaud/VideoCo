package database;

import model.user.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class UserRepository {

    public List<User> users = new ArrayList<>();
    public Map<String, String> userAccounts = new HashMap<>();
    private static final String USER_FILE_PATH = "/src/main/resources/users.csv";
    private static final String path = System.getProperty("user.dir") + USER_FILE_PATH;

    public UserRepository() {
        load(path);
    }

    public void load (String path) {
        try {
            CSVParser parser = new CSVParser(new FileReader(path), CSVFormat.RFC4180
                    .withDelimiter(',')
                    .withHeader("email", "username", "password"));
            List<CSVRecord> records = parser.getRecords();
            for (int i = 1; i < records.size(); i++) {
                User user = new User();
                user.setUsername(records.get(i).get("username"));
                user.setEmailAddress(records.get(i).get("email"));
                user.setPassword(records.get(i).get("password"));
                users.add(user);
                userAccounts.put(records.get(i).get("username"), records.get(i).get("password"));
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
            userAccounts.put(user.getUsername(), user.getPassword());
            update();
            return true;
        }
    }

    public boolean login(String username, String password) {
        if (userAccounts.containsKey(username)) {
            return userAccounts.get(username).equals(password);
        }
        return false;
    }

    public void update() {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(path, false),
                CSVFormat.RFC4180
                        .withDelimiter(',')
                        .withHeader(
                "email",
                "username",
                "password"
        ))) {
            for (User u : users) {
                printer.printRecord(u.getEmailAddress(), u.getUsername(), u.getPassword());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
