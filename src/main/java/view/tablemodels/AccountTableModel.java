package view.tablemodels;

import database.Observer;
import database.UserRepository;
import model.User;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AccountTableModel extends DefaultTableModel implements Observer {

    public AccountTableModel() {
        subscribe();
        updateTable();
    }

    private void subscribe() {
        UserRepository.getInstance().registerObserver(this);
    }

    private void updateTable() {
        List<User> customerAccounts = UserRepository.getInstance().getAllCustomers();
        String[][] data = new String[customerAccounts.size()][3];
        String[] column = {"USERNAME", "PASSWORD", "EMAIL"};
        for (int i = 0; i < customerAccounts.size(); i++) {
            data[i][0] = customerAccounts.get(i).getUsername();
            data[i][1] = customerAccounts.get(i).getPassword();
            data[i][2] = customerAccounts.get(i).getEmailAddress();
        }
        setDataVector(data,column);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }

    @Override
    public void update() {
        updateTable();
    }
}
