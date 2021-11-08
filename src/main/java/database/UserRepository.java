package database;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import model.user.User;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class UserRepository {

    CSVWriter writer;
    Set<User> users;

    public UserRepository() throws IOException {
        writer = new CSVWriter(new FileWriter("accounts.csv"));
        users = new HashSet<>();
    }

    public boolean register(User user) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        users.add(user);
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder<>(writer).withSeparator(CSVWriter.DEFAULT_SEPARATOR).build();
        beanToCsv.write(users);
        return true;
    }
}
