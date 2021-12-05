package scheduled_tasks;

import model.Order;
import services.OrderService;
import view.StoreFront;

import java.util.List;
import java.util.TimerTask;

public class DeliverOrders extends TimerTask {

    private final OrderService orderService;

    public DeliverOrders() {
        orderService = StoreFront.getOrderService();
    }

    @Override
    public void run() {
        List<Order> orders = orderService.getAllOrders();
        for (Order o : orders) {
            if (o.getOrderStatus().equals("SHIPPED")) {
                orderService.changeOrderStatus(o.getOrderId(), "DELIVERED");
                System.out.println("Delivered order " + o.getOrderId());
            }
        }
    }
}
