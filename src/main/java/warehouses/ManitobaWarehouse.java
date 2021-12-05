package warehouses;

public class ManitobaWarehouse implements Warehouse {

    @Override
    public void ship() {
        System.out.println("Shipping from Manitoba warehouse...");
    }

}
