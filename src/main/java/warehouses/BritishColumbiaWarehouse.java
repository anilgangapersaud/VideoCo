package warehouses;

public class BritishColumbiaWarehouse implements Warehouse {

    @Override
    public void ship() {
        System.out.println("Shipping from British Columbia warehouse...");
    }

}
