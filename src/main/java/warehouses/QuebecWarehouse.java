package warehouses;

public class QuebecWarehouse implements Warehouse {
    @Override
    public void ship() {
        System.out.println("Shipping from Quebec Warehouse");
    }
}
