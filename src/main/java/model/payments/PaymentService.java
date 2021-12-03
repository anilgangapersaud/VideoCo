package model.payments;

import model.Movie;

import java.util.Map;

public interface PaymentService {

    boolean acceptPayment(PaymentVisitor visitor, Map<Movie,Integer> movies);

}
