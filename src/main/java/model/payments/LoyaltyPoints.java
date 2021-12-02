package model.payments;

import model.Model;
import model.Movie;
import services.PaymentService;

import java.util.Map;

public class LoyaltyPoints implements PaymentService {

    private final int loyaltyPoints;

    public LoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
    @Override
    public boolean pay(Map<Movie, Integer> movies) {
        int totalMovies = 0;
        for (Map.Entry<Movie,Integer> entry : movies.entrySet()) {
            totalMovies += entry.getValue();
        }
        if (loyaltyPoints >= totalMovies * 10) {
            int deduction = totalMovies * 10;
            Model.getUserService().getLoggedInUser().setLoyaltyPoints(loyaltyPoints - deduction);
            Model.getUserService().updateUser(Model.getUserService().getLoggedInUser());
            return true;
        } else {
            return false;
        }
    }
}
