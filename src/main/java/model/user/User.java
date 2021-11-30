package model.user;

import model.Cart;

public class User {

    private String username;
    private String password;
    private String emailAddress;
    private String accountType;
    private Cart cart;
    private int loyaltyPoints;

    public User(String username, String password, String emailAddress, String accountType) {
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.accountType = accountType;
        loyaltyPoints = 0;
    }

    public User() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public Cart getCart() {
        if (cart == null) {
            cart = new Cart();
        }
        return cart;
    }

    public boolean isAdmin() {
        return accountType.equals("admin");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && emailAddress.equals(user.emailAddress) && password.equals(user.password);
    }
}
