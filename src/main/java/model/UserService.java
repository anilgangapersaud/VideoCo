package model;

import model.user.User;

public interface UserService {

    boolean login(String username, String password);

    boolean register(User user);

    User getUser(String username);

    boolean updateUser(String username, String password, String emailAddress);

    boolean deleteAccount(String username);
}
