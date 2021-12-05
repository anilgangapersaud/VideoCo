package model;

import java.util.Map;

public class Order {
    private int orderId;
    private String username;
    private String orderStatus;
    private String orderDate;
    private String dueDate;
    private boolean isOverdue;
    private Map<Movie,Integer> movies;

    public Order() {}

    public Map<Movie, Integer> getMovies() {
        return movies;
    }

    public void setMovies(Map<Movie, Integer> movies) {
        this.movies = movies;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }

    public boolean getOverdue() { return isOverdue; }
}
