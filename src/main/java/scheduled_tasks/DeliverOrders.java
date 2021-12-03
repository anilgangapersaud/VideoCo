package scheduled_tasks;

import database.OrderRepository;
import model.Order;

import java.util.List;
import java.util.TimerTask;

public class DeliverOrders extends TimerTask {

    private OrderRepository orderRepository;

    public DeliverOrders() {
        orderRepository = OrderRepository.getInstance();
    }

    @Override
    public void run() {
        List<Order> orders = orderRepository.getAllOrders();
        for (Order o : orders) {
            if (o.getOrderStatus().equals("SHIPPED")) {
                orderRepository.changeOrderStatus(o.getOrderId(), "DELIVERED");
                System.out.println("Delivered order " + o.getOrderId());
            }
        }
    }
}
