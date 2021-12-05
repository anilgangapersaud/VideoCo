package warehouses;

public class NorthwestTerritoriesWarehouse implements Warehouse {
    @Override
    public void ship() {
        System.out.println("Shipping from Northwest Territories warehouse...");
    }
}
