package scheduled_tasks;

import model.Order;
import services.OrderService;
import view.StoreFront;

import java.util.List;
import java.util.TimerTask;

public class ShipOrders extends TimerTask {

    private final OrderService orderService;

    public ShipOrders() {
        orderService = StoreFront.getOrderService();
    }

    @Override
    public void run() {
        List<Order> orders = orderService.getAllOrders();
        for (Order o : orders) {
            if (o.getOrderStatus().equals("PROCESSED")) {
                orderService.changeOrderStatus(o.getOrderId(), "SHIPPED");
                System.out.println("Shipping order number " + o.getOrderId());
            }
        }
    }
}
