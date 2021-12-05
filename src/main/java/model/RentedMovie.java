package model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentedMovie that = (RentedMovie) o;
        return orderId == that.orderId && Objects.equals(barcode, that.barcode);
    }

}
