import scheduledtasks.CheckOverdueOrders;
import scheduledtasks.DeliverOrders;
import scheduledtasks.ShipOrders;
import view.StoreFront;

import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class App {


    private static final long DELIVERY_TIME = 600000;

    private static final long SHIP_TIME = 900000;

    public App() {
        initializeApp();
    }

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new CheckOverdueOrders(), Calendar.getInstance().getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        timer.schedule(new DeliverOrders(), Calendar.getInstance().getTime(), DELIVERY_TIME);
        timer.schedule(new ShipOrders(), Calendar.getInstance().getTime(), SHIP_TIME);
        new App();
    }

    private void initializeApp() {
        new StoreFront();
    }

}
