package scheduledtasks;

import model.Model;
import model.Order;

import java.util.List;
import java.util.TimerTask;

public class ShipOrders extends TimerTask {
    @Override
    public void run() {
        // check for any orders that have "PROCESSED" status and ship it
        List<Order> orders = Model.getOrderService().getAllOrders();
        for (Order o : orders) {
            if (o.getOrderStatus().equals("PROCESSED")) {
                Model.getOrderService().changeOrderStatus(o.getOrderId(), "SHIPPED");
                System.out.println("Shipping order number " + o.getOrderId());
            }
        }
    }
}
