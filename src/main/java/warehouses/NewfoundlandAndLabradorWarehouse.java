package warehouses;

public class NewfoundlandAndLabradorWarehouse implements Warehouse {
    @Override
    public void ship() {
        System.out.println("Shipping from Newfoundland and Labrador warehouse...");
    }
}
