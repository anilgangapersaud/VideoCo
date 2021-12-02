package services;

import model.User;

import java.util.List;

public interface UserService {

    boolean changeUsername(String newUsername);

    boolean changePassword(String newPassword);

    boolean changeEmail(String newEmail);

    void deleteUser(String username);

    List<User> getAllCustomers();

    User getUser(String username);

    User getLoggedInUser();

    boolean login(String username, String password);

    boolean register(User user);

    void updateUser(User user);

}
