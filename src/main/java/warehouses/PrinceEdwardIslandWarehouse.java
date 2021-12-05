package warehouses;

public class PrinceEdwardIslandWarehouse implements Warehouse {
    @Override
    public void ship() {
        System.out.println("Shipping from Prince Edward Island warehouse...");
    }
}
