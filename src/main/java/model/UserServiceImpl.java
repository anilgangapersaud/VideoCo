package model;

import database.UserRepository;
import model.user.User;

public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    public UserServiceImpl() {
        userRepository = new UserRepository();
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
