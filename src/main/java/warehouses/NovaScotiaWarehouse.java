package warehouses;

public class NovaScotiaWarehouse implements Warehouse {
    @Override
    public void ship() {
        System.out.println("Shipping from Nova Scotia warehouse...");
    }
}
