package model.payments;

import model.Movie;

import java.util.Map;

public class LoyaltyPoints implements PaymentService {

    private final int loyaltyPoints;

    public LoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    @Override
    public boolean acceptPayment(PaymentVisitor visitor, Map<Movie, Integer> movies) {
         return visitor.visitLoyaltyPoints(this, movies);
    }

}
