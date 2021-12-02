package services;

import model.User;

public interface UserService {

    boolean changeUsername(String newUsername);

    boolean changePassword(String newPassword);

    boolean changeEmail(String newEmail);

    User getLoggedInUser();

    boolean login(String username, String password);

    boolean register(User user);

    void updateUser(User user);

}
