package model.user;

import model.Movie;

import java.util.Map;

public class ShoppingCart {

    Map<Movie, Integer> cart;

    public void clearCart() {
        cart.clear();
    }

    public boolean addMovieToCart(Movie movie, int quantity) {
        if (cart.containsKey(movie)) {
            cart.put(movie, cart.get(movie)+quantity);
        } else {
            cart.put(movie, quantity);
        }
        return true;
    }

    public boolean removeMovieFromCart(Movie movie, int quantity) {
        if (cart.containsKey(movie)) {
            if (cart.get(movie) <= quantity) {
                int q = cart.get(movie);
                if (q - quantity <= 0) {
                    cart.remove(movie);
                } else {
                    cart.replace(movie, q-quantity);
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
