package model.order;

import model.user.Address;

public interface OrderService {

    public boolean createOrder(Address shipping);

}
