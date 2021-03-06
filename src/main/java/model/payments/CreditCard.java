package model.payments;

import model.Cart;
import model.Movie;

import java.util.Map;
import java.util.Objects;

public class CreditCard implements PaymentService {

    private String username;
    private String cardNumber;
    private String expiry;
    private String csv;
    private double balance;

    public CreditCard() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void charge(double amount) {
        balance += amount;
    }


    public void refund(double amount) {
        balance -= amount;
    }

    @Override
    public boolean acceptPayment(PaymentVisitor visitor, Cart cart) {
        return visitor.visitCreditCard(this, cart);
    }

}
