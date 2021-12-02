package scheduledtasks;

import model.Model;
import model.Order;

import java.util.List;
import java.util.TimerTask;

public class DeliverOrders extends TimerTask {
    @Override
    public void run() {
        List<Order> orders = Model.getOrderService().getAllOrders();
        for (Order o : orders) {
            if (o.getOrderStatus().equals("SHIPPED")) {
                Model.getOrderService().changeOrderStatus(o.getOrderId(), "DELIVERED");
                System.out.println("Delivered order " + o.getOrderId());
            }
        }
    }
}
