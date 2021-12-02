package model.payments;

import model.Movie;
import services.PaymentService;

import java.util.Map;

public class CreditCard implements PaymentService {

    public CreditCard() {}

    @Override
    public boolean pay(Map<Movie, Integer> movies) {
        return true;
    }

}
