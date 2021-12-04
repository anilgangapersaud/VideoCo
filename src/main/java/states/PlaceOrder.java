package states;

import model.Address;
import model.Cart;
import model.Movie;
import model.Order;
import model.payments.CreditCard;
import services.MovieServiceImpl;
import services.OrderServiceImpl;
import view.StoreFront;

import java.util.Scanner;

public class PlaceOrder implements State {

    private final DialInService dialInService;
    private final MovieServiceImpl movieService;
    private final OrderServiceImpl orderService;

    public PlaceOrder(DialInService dialIn) {
        dialInService = dialIn;
        movieService = StoreFront.getMovieService();
        orderService = StoreFront.getOrderService();
    }

    @Override
    public void mainMenu() {

    }

    @Override
    public void checkStatus() {

    }

    @Override
    public void placeOrder() {
        try {
            Scanner scan = new Scanner(System.in);
            Address a = new Address();
            System.out.println("Enter a guest username");
            String username = scan.nextLine();
            a.setUsername(username);
            System.out.println("Please enter your shipping information");
            System.out.println("Enter street address");
            a.setLineAddress(scan.nextLine());
            System.out.println("Enter city");
            a.setCity(scan.nextLine());
            System.out.println("Enter province");
            a.setProvince(scan.nextLine());
            System.out.println("Enter postal code");
            a.setPostalCode(scan.nextLine());

            CreditCard c = new CreditCard();
            c.setUsername(username);
            System.out.println("Please enter your billing information");
            System.out.println("Enter your credit card number");
            c.setCardNumber(scan.nextLine());
            System.out.println("Enter your cards expiry date");
            c.setExpiry(scan.nextLine());
            System.out.println("Enter your cards csv");
            c.setCsv(scan.nextLine());
            c.setBalance(0.00D);

            System.out.println("Thank you for entering your shipping/billing");
            System.out.println("Please enter the barcodes of the movie(s) you wish to order. Enter the word 'done' to complete your order");
            Cart cart = new Cart();
            cart.setUsername(username);
            double orderTotal = 0;
            while (true) {
                String barcode = scan.nextLine();
                if (barcode.equals("done")) {
                    break;
                }
                Movie m = movieService.getMovie(barcode);
                if (m != null) {
                    int stock = movieService.getStockForMovie(barcode);
                    if (stock != 0) {
                        System.out.println(m.getTitle() + ": " + stock + " in stock, " + "price: " + m.getPrice());
                        System.out.println("Enter the quantity you would like to order");
                        int quantity = scan.nextInt();
                        if (quantity >= 0 && quantity <= stock) {
                            cart.addMovieToCart(m, quantity);
                            orderTotal += m.getPrice() * quantity;
                            System.out.println("Added " + quantity + " " + m.getTitle() + " to your cart");
                        } else {
                            System.out.println("Invalid Quantity");
                        }
                    } else {
                        System.out.println("No stock is available for that movie");
                    }
                }
            }
            if (cart.getMoviesInCart().size() > 0) {
                System.out.println("Order Total: " + String.format("%.2f", orderTotal));
                System.out.println("Press 1 to confirm order");
                System.out.println("Press 0 to cancel order");
                int userInput = scan.nextInt();
                if (userInput == 1) {
                    Order o = orderService.createOrder(cart, c);
                    if (o == null) {
                        System.out.println("There was a service error creating order, please try again");
                        scan.close();
                    } else {
                        System.out.println("Thank you for shopping with VideoCo!\nYour order number is " + o.getOrderId());
                    }
                } else {
                    System.out.println("Thank you for shopping with VideoCo!\nYour order was cancelled");
                }
            }
            System.out.println("Press 1 to return to the main menu");
            System.out.println("Press 0 to make another order");
            int decision = scan.nextInt();
            if (decision == 1) {
                dialInService.setState(dialInService.getMainMenu());
                dialInService.mainMenu();
            } else if (decision == 0) {
                dialInService.placeOrder();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
