package scheduledtasks;

import model.Address;
import model.Model;
import model.Order;
import model.payments.CreditCard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class CheckOverdueOrders extends TimerTask {
    public void run() {
        System.out.println("Running daily check for overdue orders...");
        List<Order> orders = Model.getOrderService().getAllOrders();
        for (Order o : orders) {
            String dueDate = o.getDueDate();
            try {
                Date todaysDate = Calendar.getInstance().getTime();
                Date dueDateP = new SimpleDateFormat("yyyy/MM/dd").parse(dueDate);
                if (dueDateP.before(todaysDate)) {
                    if (!o.getOverdue()) {
                        o.setOverdue(true);
                        System.out.println(o.getUsername() + "'s order is overdue");
                        // If user is outside of Ontario, charge 9.99 for late fee
                        Address usersAddress = Model.getAddressService().getAddress(o.getUsername());
                        if (!usersAddress.getProvince().equals("Ontario")) {
                            CreditCard c = Model.getBillingService().getCreditCard(o.getUsername());
                            double charge = 9.99D;
                            c.charge(charge);
                            System.out.println("Charging " + o.getUsername() + " 9.99 for a late fee outside of Ontario");
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (Order o : orders) {
            if (o.getOrderStatus().equals("DELIVERED") && o.getOverdue()) {
                // charge the users credit card $1.00 for each movie
                CreditCard c = Model.getBillingService().getCreditCard(o.getUsername());
                int totalMovies = Model.getRentedService().getRentedRepository().countMoviesInOrder(o.getOrderId());
                double charge = 1.00D * totalMovies;
                c.charge(charge);
                System.out.println("Charging " + o.getUsername() + " " + charge + " for an overdue order");
            }
        }
    }
}
