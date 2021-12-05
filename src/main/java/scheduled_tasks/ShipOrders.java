package scheduled_tasks;

import model.Order;
import services.OrderService;
import view.StoreFront;
import warehouses.WarehouseFactory;

import java.util.List;
import java.util.TimerTask;

public class ShipOrders extends TimerTask {

    private final OrderService orderService;
    private final WarehouseFactory warehouseFactory;

    public ShipOrders() {
        orderService = StoreFront.getOrderService();
        warehouseFactory = new WarehouseFactory();
    }

    @Override
    public void run() {
        List<Order> orders = orderService.getAllOrders();
        for (Order o : orders) {
            if (o.getOrderStatus().equals("PROCESSED")) {
                // send the order to warehouse to be shipped
                warehouseFactory.getWarehouse(o).ship();
                orderService.changeOrderStatus(o.getOrderId(), "SHIPPED");
            }
        }
    }
}
