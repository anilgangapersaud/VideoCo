package model;

import model.movie.Movie;
import java.util.HashMap;
import java.util.Map;

/**
 * A shopping cart to hold movies
 */
public class Cart {

    /**
     * A cart with movies and quantity of each
     */
    private Map<Movie, Integer> cart;

    /**
     * Initialize the cart object
     */
    public Cart() {
        cart = new HashMap<>();
    }

    /**
     * Add a movie to the cart
     * @param m the movie to add
     * @param quantity the quantity of the movie
     */
    public void addMovieToCart(Movie m, int quantity) {
        if (cart.containsKey(m)) {
            cart.replace(m, cart.get(m) + quantity);
        } else {
            cart.put(m, quantity);
        }
    }

    /**
     * @param m the movie
     * @return the quantity of the desired movie in the cart
     */
    public Integer getQuantity(Movie m) {
        return cart.getOrDefault(m, 0);
    }

    /**
     * @return the cart mapping
     */
    public Map<Movie, Integer> getMoviesInCart() {
        return cart;
    }

    /**
     * Remove a movie from the cart
     * @param m the movie to remove from the cart
     */
    public void removeMovieFromCart(Movie m) {
        if (cart.containsKey(m)) {
            int quantity = cart.get(m);
            if (quantity == 1) {
                cart.remove(m);
            } else {
                cart.replace(m, cart.get(m)-1);
            }
        }
    }

    /**
     * Clear the cart of all movies
     */
    public void clearCart() {
        cart.clear();
    }
}
