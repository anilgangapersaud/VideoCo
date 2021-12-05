package warehouses;

import model.Address;
import model.Order;

public class OntarioWarehouse implements Warehouse {

    @Override
    public void ship(Order o) {
        System.out.println("Shipping Order: " + o.getOrderId() + " from Ontario Warehouse...");
    }

}
