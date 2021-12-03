package model;

public class RentedMovie {

    private final int orderId;

    private final String barcode;

    public RentedMovie(int orderId, String barcode) {
        this.orderId = orderId;
        this.barcode = barcode;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getBarcode() {
        return barcode;
    }
}
