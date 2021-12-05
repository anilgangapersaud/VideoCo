package warehouses;

import model.Order;

public class ManitobaWarehouse implements Warehouse {

    @Override
    public void ship(Order o) {
        System.out.println("Shipping Order: " + o.getOrderId() + " from Manitoba warehouse...");
    }

}
