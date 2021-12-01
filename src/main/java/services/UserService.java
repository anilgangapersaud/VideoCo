package services;

import model.User;

public interface UserService {

    User login(String username, String password);

    boolean register(User user);

    boolean changeUsername(String newUsername);

    boolean changePassword(String newPassword);

    boolean changeEmail(String newEmail);

    User getLoggedInUser();

    boolean updateUser(String username, String password, String emailAddress);

    boolean deleteAccount(String username);
}
