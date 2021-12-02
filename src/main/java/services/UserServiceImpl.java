package services;

import database.UserRepository;
import model.User;

public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    User loggedInUser;

    public UserServiceImpl() {
        userRepository = UserRepository.getInstance();
    }

    @Override
    public User login(String username, String password) {
        User u = userRepository.login(username, password);
        if (u != null) {
            loggedInUser = u;
        }
        return u;
    }

    @Override
    public boolean register(User user) {
        return userRepository.register(user);
    }

    @Override
    public boolean changeUsername(String newUsername) {
        if (userRepository.changeUsername(newUsername, loggedInUser)) {
            loggedInUser.setUsername(newUsername);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean changePassword(String newPassword) {
        return userRepository.changePassword(newPassword, loggedInUser);
    }

    @Override
    public boolean changeEmail(String newEmail) {
        return userRepository.changeEmail(newEmail, loggedInUser);
    }

    @Override
    public User getLoggedInUser() {
        return loggedInUser;
    }

    @Override
    public void updateUser(User u) {
        userRepository.updateUser(u);
    }

}
