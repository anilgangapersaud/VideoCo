package controllers;

import database.UserRepository;
import model.User;
import view.shoppanels.ManageAccountsPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageAccountsController implements ActionListener {

    private final ManageAccountsPanel view;
    private final UserRepository userRepository;

    public ManageAccountsController(ManageAccountsPanel view) {
        this.view = view;
        userRepository = UserRepository.getInstance();
    }

    private void deleteAccount() {
        int[] selected = view.getTable().getSelectedRows();
        if (selected.length == 0) {
            view.displayMessage("Select an account to delete");
        } else {
            for (int j : selected) {
                String username = (String) view.getTable().getValueAt(j, 0);
                userRepository.deleteUser(username);
            }
        }
    }

    private void updateAccount() {
        int[] selected = view.getTable().getSelectedRows();
        if (selected.length != 1) {
            view.displayMessage("Select an account to update");
        } else {
            String username = (String) view.getTable().getValueAt(selected[0],0);
            User u = userRepository.getUser(username);
            u.setPassword((String)view.getTable().getValueAt(selected[0],1));
            u.setEmailAddress((String)view.getTable().getValueAt(selected[0],2));
            userRepository.updateUser(u);
            view.displayMessage("Updated Account");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("delete")) {
            deleteAccount();
        } else if (e.getActionCommand().equals("update")) {
            updateAccount();
        }
    }

}
