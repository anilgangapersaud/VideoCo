package model;

import model.movie.Movie;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private Map<Movie, Integer> cart;

    public Cart() {
        cart = new HashMap<>();
    }

    public void addMovieToCart(Movie m, int quantity) {
        if (cart.containsKey(m)) {
            cart.replace(m, cart.get(m) + quantity);
        } else {
            cart.put(m, quantity);
        }
    }

    public Integer getQuantity(Movie m) {
        return cart.getOrDefault(m, 0);
    }

    public void clearCart() {
        cart.clear();
    }
}
