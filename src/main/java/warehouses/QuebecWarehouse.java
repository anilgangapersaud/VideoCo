package warehouses;

import model.Order;

public class QuebecWarehouse implements Warehouse {
    @Override
    public void ship(Order o) {
        System.out.println("Shipping Order: " + o.getOrderId() + " from Quebec Warehouse");
    }
}
