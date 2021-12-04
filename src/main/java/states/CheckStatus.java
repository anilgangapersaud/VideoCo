package states;

import model.Order;
import services.OrderServiceImpl;
import view.StoreFront;

import java.util.Scanner;

public class CheckStatus implements State {

    private final DialInService dialInService;
    private final OrderServiceImpl orderService;

    public CheckStatus(DialInService dialInService) {
        this.dialInService = dialInService;
        orderService = StoreFront.getOrderService();
    }

    @Override
    public void mainMenu() {

    }

    @Override
    public void checkStatus() {
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter the order number you would like to check");
            int orderNumber = scan.nextInt();
            Order o = orderService.getOrder(orderNumber);
            if (o != null) {
                System.out.println("Order Status: " + o.getOrderStatus());
                System.out.println("Due Date: " + o.getDueDate());
            } else {
                System.out.println("Invalid Order Number");
            }
            System.out.println("Press 0 to check the status of another order");
            System.out.println("Press 1 to return to the main menu");
            int decision = scan.nextInt();
            if (decision == 0) {
                dialInService.setState(dialInService.getCheckStatus());
                dialInService.checkStatus();
            } else if (decision == 1) {
                dialInService.setState(dialInService.getMainMenu());
                dialInService.mainMenu();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void placeOrder() {

    }
}
