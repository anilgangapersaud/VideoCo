package scheduled_tasks;

import database.AddressRepository;
import database.BillingRepository;
import database.OrderRepository;
import database.RentedRepository;
import model.Address;
import model.Order;
import model.payments.CreditCard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class CheckOverdueOrders extends TimerTask {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final BillingRepository billingRepository;
    private final RentedRepository rentedRepository;

    public CheckOverdueOrders() {
        orderRepository = OrderRepository.getInstance();
        addressRepository = AddressRepository.getInstance();
        billingRepository = BillingRepository.getInstance();
        rentedRepository = RentedRepository.getInstance();
    }

    public void run() {
        System.out.println("Running daily check for overdue orders...");
        List<Order> orders = orderRepository.getAllOrders();
        for (Order o : orders) {
            String dueDate = o.getDueDate();
            if (!dueDate.equals("")) {
                try {
                    Date todaysDate = Calendar.getInstance().getTime();
                    Date dueDateP = new SimpleDateFormat("yyyy/MM/dd").parse(dueDate);
                    if (dueDateP.before(todaysDate)) {
                        if (!o.getOverdue()) {
                            o.setOverdue(true);
                            System.out.println(o.getUsername() + "'s order is overdue");
                            // If user is outside of Ontario, charge 9.99 for late fee
                            Address usersAddress = addressRepository.getAddress(o.getUsername());
                            if (!usersAddress.getProvince().equals("Ontario")) {
                                CreditCard c = billingRepository.getCreditCard(o.getUsername());
                                double charge = 9.99D;
                                c.charge(charge);
                                billingRepository.updateCreditCard(c);
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
            if (o.getOrderStatus().equals("DELIVERED") && o.getOverdue()) {
                CreditCard c = billingRepository.getCreditCard(o.getUsername());
                int totalMovies = rentedRepository.countMoviesInOrder(o.getOrderId());
                double charge = 1.00D * totalMovies;
                c.charge(charge);
                billingRepository.updateCreditCard(c);
                System.out.println("Charging " + o.getUsername() + " " + charge + " for an overdue order");
            }
        }
    }
}
