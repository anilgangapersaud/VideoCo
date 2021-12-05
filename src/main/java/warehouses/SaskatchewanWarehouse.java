package warehouses;

import model.Order;

public class SaskatchewanWarehouse implements Warehouse {
    @Override
    public void ship(Order o) {
        System.out.println("Shipping Order:" + o.getOrderId() + " from Saskatchewan warehouse...");
    }
}
