package warehouses;

import model.Order;

public class NewfoundlandAndLabradorWarehouse implements Warehouse {
    @Override
    public void ship(Order o) {
        System.out.println("Shipping Order " + o.getOrderId() + " from Newfoundland and Labrador warehouse...");
    }
}
