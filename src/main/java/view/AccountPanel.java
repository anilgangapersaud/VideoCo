package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountPanel extends JPanel implements ActionListener {

    private JTextField changeUsernameInput;
    private JPasswordField changePasswordInput;
    private JTextField changeEmailInput;

    public AccountPanel() {
        JLabel changeUsernameLabel = new JLabel("Change Username:");
        changeUsernameInput = new JTextField(20);
        JButton changeUsernameButton = new JButton("Save Username");
        changeUsernameButton.setActionCommand("saveUsername");
        changeUsernameButton.addActionListener(this);

        JLabel changePasswordLabel = new JLabel("Change Password:");
        changePasswordInput = new JPasswordField(20);
        JButton changePasswordButton = new JButton("Save Password");
        changePasswordButton.setActionCommand("savePassword");
        changePasswordButton.addActionListener(this);

        JLabel changeEmailAddressLabel = new JLabel("Change Email:");
        changeEmailInput = new JTextField(20);
        JButton changeEmailButton = new JButton("Save Email");
        changeEmailButton.setActionCommand("saveEmail");
        changeEmailButton.addActionListener(this);

        add(changeUsernameLabel);
        add(changeUsernameInput);
        add(changeUsernameButton);

        add(changePasswordLabel);
        add(changePasswordInput);
        add(changePasswordButton);

        add(changeEmailAddressLabel);
        add(changeEmailInput);
        add(changeEmailButton);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("saveUsername")) {
            if (App.getUserService().changeUsername(changeUsernameInput.getText())) {
                JOptionPane.showMessageDialog(this, "Changed Username!");
            } else {
                JOptionPane.showMessageDialog(this, "Enter a valid and unique username", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("savePassword")) {
            if (App.getUserService().changePassword(new String(changePasswordInput.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Changed Password!");
            } else {
                JOptionPane.showMessageDialog(this, "Enter a valid password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("saveEmail")) {
            if (App.getUserService().changeEmail(changeEmailInput.getText())) {
                JOptionPane.showMessageDialog(this, "Changed Email!");
            } else {
                JOptionPane.showMessageDialog(this, "Enter a valid email", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
