package model;

import java.util.Objects;

public class Movie {

    private String barcode;

    private String title;

    private String genre;

    private String releaseDate;

    private double price;

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double cost) {
        this.price = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(barcode, movie.barcode) && Objects.equals(title, movie.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barcode, title);
    }

}
