package model;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import model.user.User;

import java.io.IOException;

public interface UserService {

    boolean login(String username, String password);

    boolean register(User user) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException;

    User getUser(String username);

    boolean updateUser(String username, String password, String emailAddress);

    boolean deleteAccount(String username);
}
