package warehouses;

import model.Order;

public class NewBrunswickWarehouse implements Warehouse {
    @Override
    public void ship(Order o) {
        System.out.println("Shipping Order: " + o.getOrderId() + " from New Brunswick warehouse...");
    }
}
