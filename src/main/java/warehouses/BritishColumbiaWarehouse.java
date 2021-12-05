package warehouses;

import model.Order;

public class BritishColumbiaWarehouse implements Warehouse {

    @Override
    public void ship(Order o) {
        System.out.println("Shipping Order: " + o.getOrderId() + " from British Columbia warehouse...");
    }

}
