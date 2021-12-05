package warehouses;

public class NewBrunswickWarehouse implements Warehouse {
    @Override
    public void ship() {
        System.out.println("Shipping from New Brunswick warehouse...");
    }
}
