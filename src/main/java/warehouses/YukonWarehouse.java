package warehouses;

public class YukonWarehouse implements Warehouse {

    @Override
    public void ship() {
        System.out.println("Shipping from Yukon warehouse...");
    }
}
