package model;

import database.Observer;
import database.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart implements Subject {

    private final Map<Movie, Integer> cart;
    private final List<Observer> observers;
    private double total;
    private String username;

    public Cart() {
        cart = new HashMap<>();
        observers = new ArrayList<>();
        total = 0.00D;
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
        total += m.getPrice();
        notifyObservers();
    }

    public void clearCart() {
        cart.clear();
        total = 0.00D;
        notifyObservers();
    }

    /**
     * @param m the movie
     * @return the quantity of the desired movie in the cart
     */
    public Integer getQuantity(Movie m) {
        return cart.getOrDefault(m, 0);
    }

    public double getTotal() {
        return total;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
            total -= m.getPrice();
        }
        notifyObservers();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}
