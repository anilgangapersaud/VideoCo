package warehouses;

import model.Order;

public class NorthwestTerritoriesWarehouse implements Warehouse {
    @Override
    public void ship(Order o) {
        System.out.println("Shipping Order: " + o.getOrderId() + " from Northwest Territories warehouse...");
    }
}
