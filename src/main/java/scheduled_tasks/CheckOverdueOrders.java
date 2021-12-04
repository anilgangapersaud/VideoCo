package scheduled_tasks;

import model.Address;
import model.Order;
import model.payments.CreditCard;
import services.AddressServiceImpl;
import services.BillingServiceImpl;
import services.OrderServiceImpl;
import services.RentedServiceImpl;
import view.StoreFront;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class CheckOverdueOrders extends TimerTask {

    private final OrderServiceImpl orderService;
    private final AddressServiceImpl addressService;
    private final BillingServiceImpl billingService;
    private final RentedServiceImpl rentedService;

    public CheckOverdueOrders() {
        orderService = StoreFront.getOrderService();
        addressService = StoreFront.getAddressService();
        billingService = StoreFront.getBillingService();
        rentedService = StoreFront.getRentedService();
    }

    public void run() {
        System.out.println("Running daily check for overdue orders...");
        List<Order> orders = orderService.getAllOrders();
        for (Order o : orders) {
            String dueDate = o.getDueDate();
            if (!dueDate.equals("")) {
                try {
                    Date todaysDate = Calendar.getInstance().getTime();
                    Date dueDateP = new SimpleDateFormat("yyyy/MM/dd").parse(dueDate);
                    if (todaysDate.before(dueDateP)) {
                        if (!o.getOverdue()) {
                            o.setOverdue(true);
                            System.out.println(o.getUsername() + "'s order is overdue");
                            // If user is outside of Ontario, charge 9.99 for late fee
                            Address usersAddress = addressService.getAddress(o.getUsername());
                            if (!usersAddress.getProvince().equals("Ontario")) {
                                CreditCard c = billingService.getCreditCard(o.getUsername());
                                double charge = 9.99D;
                                c.charge(charge);
                                billingService.updateCreditCard(c);
                                System.out.println("Charging " + o.getUsername() + " 9.99 for a late fee outside of Ontario");
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Order o : orders) {
            if (o.getOverdue()) {
                CreditCard c = billingService.getCreditCard(o.getUsername());
                int totalMovies = rentedService.countMoviesInOrder(o.getOrderId());
                double charge = 1.00D * totalMovies;
                c.charge(charge);
                billingService.updateCreditCard(c);
                System.out.println("Charging " + o.getUsername() + " " + charge + "$ for an overdue order");
            }
        }
    }
}
