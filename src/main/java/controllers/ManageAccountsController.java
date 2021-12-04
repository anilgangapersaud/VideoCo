package controllers;

import model.User;
import services.UserServiceImpl;
import view.StoreFront;
import view.shoppanels.ManageAccountsPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageAccountsController implements ActionListener {

    private final ManageAccountsPanel view;
    private final UserServiceImpl userService;

    public ManageAccountsController(ManageAccountsPanel view) {
        this.view = view;
        userService = StoreFront.getUserService();
    }

    private void deleteAccount() {
        int[] selected = view.getTable().getSelectedRows();
        if (selected.length == 0) {
            view.displayMessage("Select an account to delete");
        } else {
            for (int j : selected) {
                String username = (String) view.getTable().getValueAt(j, 0);
                userService.deleteUser(username);
            }
        }
    }

    private void updateAccount() {
        int[] selected = view.getTable().getSelectedRows();
        if (selected.length != 1) {
            view.displayMessage("Select an account to update");
        } else {
            String username = (String) view.getTable().getValueAt(selected[0],0);
            User u = userService.getUser(username);
            u.setPassword((String)view.getTable().getValueAt(selected[0],1));
            u.setEmailAddress((String)view.getTable().getValueAt(selected[0],2));
            userService.updateUser(u);
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
