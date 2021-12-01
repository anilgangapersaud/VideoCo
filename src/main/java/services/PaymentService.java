package services;

import model.Movie;

import java.util.Map;

public interface PaymentService {

    boolean pay(Map<Movie,Integer> movies);

}
