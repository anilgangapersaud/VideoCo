package model.payments;

import database.UserRepository;
import model.Movie;

import java.util.Map;

public class LoyaltyPoints implements PaymentService {

    private final int loyaltyPoints;
    private UserRepository userRepository;

    public LoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
        userRepository = UserRepository.getInstance();
    }

    @Override
    public boolean pay(Map<Movie, Integer> movies) {
        int totalMovies = 0;
        for (Map.Entry<Movie,Integer> entry : movies.entrySet()) {
            totalMovies += entry.getValue();
        }
        if (loyaltyPoints >= totalMovies * 10) {
            int deduction = totalMovies * 10;
            userRepository.getLoggedInUser().setLoyaltyPoints(loyaltyPoints - deduction);
            userRepository.updateUser(userRepository.getLoggedInUser());
            return true;
        } else {
            return false;
        }
    }

}
