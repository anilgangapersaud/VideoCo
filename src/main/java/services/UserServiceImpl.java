package services;

import database.Observer;
import database.UserRepository;
import model.Address;
import model.Order;
import model.User;
import model.payments.CreditCard;

import java.util.List;

public class UserServiceImpl {

    private final UserRepository userRepository;

    private volatile static UserServiceImpl instance;

    private static String ADMIN_CSV_PATH;
    private static String USER_CSV_PATH;

    private UserServiceImpl() {
        userRepository = UserRepository.getInstance(ADMIN_CSV_PATH, USER_CSV_PATH);
    }

    public static UserServiceImpl getInstance() {
        if (instance == null) {
            synchronized (UserServiceImpl.class) {
                if (instance == null) {
                    instance = new UserServiceImpl();
                }
            }
        }
        return instance;
    }

    public static void setCsvPath(String adminPath, String userPath) {
        ADMIN_CSV_PATH = adminPath;
        USER_CSV_PATH = userPath;
    }

    public void awardLoyaltyPoint(String username) {
        userRepository.awardLoyaltyPoint(username);
    }

    public boolean changeUsername(String newUsername, String oldUsername) {
        User u = userRepository.getUser(oldUsername);
        if (u != null) {
            if (validateUsername(newUsername)) {
                userRepository.changeUsername(newUsername, oldUsername);

                Address a = getAddressService().getAddress(oldUsername);
                if (a != null) {
                    a.setUsername(newUsername);
                    getAddressService().deleteAddress(oldUsername);
                    getAddressService().saveAddress(a);
                }

                CreditCard c = getBillingService().getCreditCard(oldUsername);
                if (c != null) {
                    c.setUsername(newUsername);
                    getBillingService().updateCreditCard(c);
                    getBillingService().deleteCreditCard(oldUsername);
                    getBillingService().saveCreditCard(c);
                }

                List<Order> orders = getOrderService().getOrdersByCustomer(oldUsername);
                if (orders.size() > 0) {
                    for (Order o : orders) {
                        o.setUsername(newUsername);
                        getOrderService().updateOrder(o.getOrderId(), o);
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean changePassword(String newPassword, String username) {
        return userRepository.changePassword(newPassword, username);
    }

    public boolean changeEmail(String newEmail, String username) {
        return userRepository.changeEmail(newEmail, username);
    }

    public boolean checkUserExists(String username) {
        return userRepository.checkUserExists(username);
    }

    public void deleteUser(String username) {
        getAddressService().deleteAddress(username);
        getBillingService().deleteCreditCard(username);
        List<Order> userOrders = getOrderService().getOrdersByCustomer(username);
        for (Order o : userOrders) {
            getOrderService().deleteOrder(o.getOrderId());
        }
        userRepository.deleteUser(username);
    }

    public List<User> getAllCustomers() {
        return userRepository.getAllCustomers();
    }

    public User getLoggedInUser() {
        return userRepository.getLoggedInUser();
    }

    public User getUser(String username) {
        return userRepository.getUser(username);
    }

    public void updateUser(User u) {
        userRepository.updateUser(u);
    }

    public boolean isAdmin() {
        return userRepository.isAdmin();
    }

    public boolean register(User user) {
        return userRepository.register(user);
    }

    public boolean saveGuestAccount(User user) {
        return userRepository.saveGuestAccount(user);
    }

    public boolean login(String username, String password) {
        return userRepository.login(username, password);
    }

    private boolean validateUsername(String username) {
        return username != null &&
                !username.equals("");
    }

    public void registerObserver(Observer o) {
        userRepository.registerObserver(o);
    }

    public void removeObserver(Observer o) {
        userRepository.removeObserver(o);
    }

    private AddressServiceImpl getAddressService() {
        return AddressServiceImpl.getInstance();
    }

    private BillingServiceImpl getBillingService() {
        return BillingServiceImpl.getInstance();
    }

    private OrderServiceImpl getOrderService() {
        return OrderServiceImpl.getInstance();
    }
}
