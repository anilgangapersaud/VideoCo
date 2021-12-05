package warehouses;

public class SaskatchewanWarehouse implements Warehouse {
    @Override
    public void ship() {
        System.out.println("Shipping from Saskatchewan warehouse...");
    }
}
