package view.cards;

import view.shoppanels.EditAccountPanel;
import view.shoppanels.EditAddressPanel;
import view.menupanels.EditBillingPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountCards extends JPanel implements ActionListener {

   protected CardLayout cl;

   public AccountCards() {
       cl = new CardLayout();
       setLayout(cl);
       EditAccountPanel eacp = new EditAccountPanel(this);
       EditAddressPanel eadp = new EditAddressPanel(this);
       EditBillingPanel ebp = new EditBillingPanel();

       add(eacp, "eacp");
       add(ebp, "ebp");
       add(eadp, "eadp");

       cl.show(this, "eacp");

       setVisible(true);
   }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
