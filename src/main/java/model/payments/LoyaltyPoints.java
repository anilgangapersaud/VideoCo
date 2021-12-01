package model.payments;

import model.Movie;
import services.PaymentService;

import java.util.Map;

public class LoyaltyPoints implements PaymentService {

    int loyaltyPoints;

    public LoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
    @Override
    public boolean pay(Map<Movie, Integer> movies) {
        int totalMovies = 0;
        for (Map.Entry<Movie,Integer> entry : movies.entrySet()) {
            totalMovies += entry.getValue();
        }

        return loyaltyPoints <= (totalMovies * 10);
    }
}
