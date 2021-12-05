package warehouses;

import model.Address;

public class OntarioWarehouse implements Warehouse {

    @Override
    public void ship() {
        System.out.println("Shipping from Ontario Warehouse...");
    }

}
