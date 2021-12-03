package scheduled_tasks;

import database.OrderRepository;
import model.Order;

import java.util.List;
import java.util.TimerTask;

public class ShipOrders extends TimerTask {

    private final OrderRepository orderRepository;

    public ShipOrders() {
        orderRepository = OrderRepository.getInstance();
    }

    @Override
    public void run() {
        List<Order> orders = orderRepository.getAllOrders();
        for (Order o : orders) {
            if (o.getOrderStatus().equals("PROCESSED")) {
                orderRepository.changeOrderStatus(o.getOrderId(), "SHIPPED");
                System.out.println("Shipping order number " + o.getOrderId());
            }
        }
    }
}
