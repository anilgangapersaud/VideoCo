package model.payments;

import model.Cart;
import model.Movie;

import java.util.Map;

public class LoyaltyPoints implements PaymentService {

    private int loyaltyPoints;

    public LoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }

    @Override
    public boolean acceptPayment(PaymentVisitor visitor, Cart cart) {
         return visitor.visitLoyaltyPoints(this, cart);
    }

}
