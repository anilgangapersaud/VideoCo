package model;

/**
 * Class representing a rented movie in the system
 */
public class RentedMovie {

    /**
     * the order id associated with this movie
     */
    private int orderId;

    /**
     * the barcode of this movie
     */
    private String barcode;

    /**
     * Construct a new RentedMovie
     * @param orderId the orderid
     * @param barcode the barcode
     */
    public RentedMovie(int orderId, String barcode) {
        this.orderId = orderId;
        this.barcode = barcode;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
