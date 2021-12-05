package dialin;

import java.util.Scanner;

public class MainMenu implements State {

    private final DialInService dialInService;

    public MainMenu(DialInService dialIn) {
        dialInService = dialIn;
    }

    @Override
    public void mainMenu() {
        System.out.println("Hello! Welcome to Video-Co's Dial-In Service!\nPress 1 to place an order\nPress 2 to check the status of your order\nPress 3 to exit the dial-in service");
        try {
            Scanner scan = new Scanner(System.in);
            int input = scan.nextInt();
            if (input == 1) {
                dialInService.setState(dialInService.getPlaceOrder());
                dialInService.placeOrder();
            } else if (input == 2) {
                dialInService.setState(dialInService.getCheckStatus());
                dialInService.checkStatus();
            } else if (input == 3) {
                System.out.println("Thank you for shopping with VideoCo!");
            } else {
                System.out.println("Invalid input, please try again");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkStatus() {

    }

    @Override
    public void placeOrder() {

    }
}
