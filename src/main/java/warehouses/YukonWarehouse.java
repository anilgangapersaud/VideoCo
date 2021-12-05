package warehouses;

import model.Order;

public class YukonWarehouse implements Warehouse {

    @Override
    public void ship(Order o) {
        System.out.println("Shipping Order:" + o.getOrderId() + " from Yukon warehouse...");
    }
}
