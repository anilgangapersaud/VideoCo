package warehouses;

import model.Order;

public class NovaScotiaWarehouse implements Warehouse {
    @Override
    public void ship(Order o) {
        System.out.println("Shipping Order: " + o.getOrderId() + " from Nova Scotia warehouse...");
    }
}
