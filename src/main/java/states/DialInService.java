package states;

public class DialInService {

    private final State mainMenu;
    private final State placeOrder;
    private final State checkStatus;
    private State state;

    public DialInService() {
        mainMenu = new MainMenu(this);
        placeOrder = new PlaceOrder(this);
        checkStatus = new CheckStatus(this);

        state = mainMenu;
        state.mainMenu();
    }

    public void setState(State state) {
        this.state = state;
    }

    public void mainMenu() {
        state.mainMenu();
    }

    public void placeOrder() {
        state.placeOrder();
    }

    public void checkStatus() {state.checkStatus(); }

    public State getMainMenu() {
        return mainMenu;
    }

    public State getPlaceOrder() {
        return placeOrder;
    }

    public State getCheckStatus() { return checkStatus; }

}
