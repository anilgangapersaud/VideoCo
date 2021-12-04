package model.payments;

import model.Cart;
import model.Movie;

import java.util.Map;

public interface PaymentVisitor {

    boolean visitLoyaltyPoints(LoyaltyPoints loyaltyPoints, Cart cart);

    boolean visitCreditCard(CreditCard creditCard, Cart cart);

}
