package view.tablemodels;

import database.Observer;
import database.UserRepository;
import model.Cart;
import model.Movie;

import javax.swing.table.DefaultTableModel;
import java.util.Map;

public class CartTableModel extends DefaultTableModel implements Observer {

    public CartTableModel() {
        subscribe();
        updateTable();
    }

    private void updateTable() {
        Cart userCart = UserRepository.getInstance().getLoggedInUser().getCart();
        String[][] data = new String[userCart.getMoviesInCart().size()][4];
        String[] column = {"BARCODE", "TITLE", "PRICE", "QUANTITY"};
        int i = 0;
        for (Map.Entry<Movie, Integer> entry : userCart.getMoviesInCart().entrySet()) {
            data[i][0] = entry.getKey().getBarcode();
            data[i][1] = entry.getKey().getTitle();
            data[i][2] = String.valueOf(entry.getKey().getPrice());
            data[i][3] = String.valueOf(entry.getValue());
            i++;
        }
        setDataVector(data,column);
    }

    private void subscribe() {
        UserRepository.getInstance().getLoggedInUser().getCart().registerObserver(this);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public void update() {
        updateTable();
    }
}
