package model.payments;

import model.Movie;

import java.util.Map;

public interface PaymentVisitor {

    boolean visitLoyaltyPoints(LoyaltyPoints loyaltyPoints, Map<Movie, Integer> movies);

    boolean visitCreditCard(CreditCard creditCard, Map<Movie,Integer> movies);

}
