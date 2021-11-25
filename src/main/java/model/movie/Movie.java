package model.movie;

/**
 * POJO to represent a Movie
 */
public class Movie {

    private String barcode;
    private String title;
    private String description;
    private String genre;
    private String releaseDate;
    private int quantity;
    private double cost;

    public Movie(String title, String barcode, String releaseDate, String description, String genre, int quantity, double cost) {
        this.title = title;
        this.barcode = barcode;
        this.releaseDate = releaseDate;
        this.description = description;
        this.genre = genre;
        this.quantity = quantity;
        this.cost = cost;
    }

    public Movie() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
