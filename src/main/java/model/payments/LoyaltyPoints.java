package model.payments;

import model.Cart;

public class LoyaltyPoints implements PaymentService {

    private final int loyaltyPoints;

    public LoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    @Override
    public boolean acceptPayment(PaymentVisitor visitor, Cart cart) {
         return visitor.visitLoyaltyPoints(this, cart);
    }

}
