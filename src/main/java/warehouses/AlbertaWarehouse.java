package warehouses;

public class AlbertaWarehouse implements Warehouse {

    @Override
    public void ship() {
        System.out.println("Shipping from Alberta Warehouse...");
    }

}
