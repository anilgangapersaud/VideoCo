package services;

import database.UserRepository;
import model.User;

public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    public UserServiceImpl() {
        userRepository = UserRepository.getInstance();
    }

    @Override
    public boolean login(String username, String password) {
        return userRepository.login(username, password);
    }

    @Override
    public boolean register(User user) {
        return userRepository.register(user);
    }

    @Override
    public boolean changeUsername(String newUsername) {
        return userRepository.changeUsername(newUsername);
    }

    @Override
    public boolean changePassword(String newPassword) {
        return userRepository.changePassword(newPassword);
    }

    @Override
    public boolean changeEmail(String newEmail) {
        return userRepository.changeEmail(newEmail);
    }

    @Override
    public User getLoggedInUser() {
        return userRepository.getLoggedInUser();
    }

    @Override
    public void updateUser(User u) {
        userRepository.updateUser(u);
    }

}
