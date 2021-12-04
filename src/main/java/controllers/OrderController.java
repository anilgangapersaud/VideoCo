package controllers;

import database.OrderRepository;
import services.OrderServiceImpl;
import view.StoreFront;
import view.shoppanels.OrderPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderController implements ActionListener {

    private final OrderPanel view;

    private final OrderServiceImpl orderService;

    public OrderController(OrderPanel view) {
        this.view = view;
        orderService = StoreFront.getOrderService();

    }

    private void cancelOrder() {
        int[] selected = view.getTable().getSelectedRows();
        if (selected.length != 1) {
            view.displayMessage("Select an order to cancel");
        } else {
            int orderNumber = Integer.parseInt((String) view.getTable().getValueAt(selected[0],0));
            if (orderService.cancelOrder(orderNumber)) {
                view.displayMessage("Order cancelled");
            } else {
                view.displayErrorMessage("Failed to cancel order");
            }
        }
    }

    private void returnOrder() {
        int[] selected = view.getTable().getSelectedRows();
        if (selected.length != 1) {
            view.displayMessage("Select an order");
        } else {
            int orderNumber = Integer.parseInt((String)view.getTable().getValueAt(selected[0],0));
            if (orderService.returnOrder(orderNumber)) {
                view.displayMessage("Thanks for shopping with VideoCo!");
            } else {
                view.displayErrorMessage("Status must be DELIVERED");
            }
        }
    }

    private void updateOrderStatus(JComboBox<String> orderStatus) {
        String status = (String)orderStatus.getSelectedItem();
        int[] selected = view.getTable().getSelectedRows();
        if (selected.length == 1) {
            int orderNumber = Integer.parseInt((String) view.getTable().getValueAt(selected[0], 0));
            orderService.changeOrderStatus(orderNumber, status);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("cancel")) {
            cancelOrder();
        } else if (e.getActionCommand().equals("return")) {
            returnOrder();
        } else if (e.getActionCommand().equals("comboBoxChanged")) {
            updateOrderStatus(view.getOrderStatus());
        }
    }
}
