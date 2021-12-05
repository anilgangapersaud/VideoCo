package warehouses;

public class NunavutWarehouse implements Warehouse {

    @Override
    public void ship() {
        System.out.println("Shipping from Nunavut warehouse...");
    }
}
