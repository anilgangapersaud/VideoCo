package model;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import database.UserRepository;
import model.user.User;

import java.io.IOException;

public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    public UserServiceImpl() throws IOException {
        userRepository = new UserRepository();
    }

    @Override
    public boolean login(String username, String password) {
        return false;
    }

    @Override
    public boolean register(User user) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        return userRepository.register(user);
    }

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public boolean updateUser(String username, String password, String emailAddress) {
        return false;
    }

    @Override
    public boolean deleteAccount(String username) {
        return false;
    }
}
