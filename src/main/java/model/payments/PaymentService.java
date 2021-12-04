package model.payments;

import model.Cart;
import model.Movie;

import java.util.Map;

public interface PaymentService {

    boolean acceptPayment(PaymentVisitor visitor, Cart cart);

}
