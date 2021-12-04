package scheduled_tasks;

import database.OrderRepository;
import model.Order;
import services.OrderServiceImpl;
import view.StoreFront;

import java.util.List;
import java.util.TimerTask;

public class ShipOrders extends TimerTask {

    private final OrderServiceImpl orderService;

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
