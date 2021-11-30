package model.movie;

import java.util.Objects;

/**
 * POJO to represent a Movie
 */
public class Movie {

    /**
     * Movie ID
     */
    private String barcode;

    /**
     * Movie title
     */
    private String title;

    /**
     * Movie genre
     */
    private String genre;

    /**
     * Movie Release Date
     */
    private String releaseDate;

    /**
     * Cost of movie
     */
    private double price;

    /**
     * Construct an empty movie object
     */
    public Movie() {}

    /**
     * @return title of movie
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title movie title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the movie id
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the movie id
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * @return the release date of the movie
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate the release date of the movie
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * @return movie genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @param genre the movie genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * @return the price of the movie
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param cost the cost of the movie
     */
    public void setPrice(double cost) {
        this.price = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return barcode.equals(movie.barcode) && title.equals(movie.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barcode, title);
    }

}
