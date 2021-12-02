package model;

public class RentedMovie {

    private int orderId;

    private String barcode;

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
