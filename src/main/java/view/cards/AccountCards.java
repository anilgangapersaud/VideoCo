package view.cards;

import view.shoppanels.EditAccountPanel;
import view.shoppanels.EditAddressPanel;
import view.menupanels.EditBillingPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountCards extends JPanel {

   protected CardLayout cl;

   private EditAccountPanel eacp;

   private EditAddressPanel eadp;

   private EditBillingPanel ebp;

   public AccountCards() {
       cl = new CardLayout();
       setLayout(cl);
       eacp = new EditAccountPanel(this);
       eadp = new EditAddressPanel(this);
       ebp = new EditBillingPanel(this);

       add(eacp, "eacp");
       add(ebp, "ebp");
       add(eadp, "eadp");

       cl.show(this, "eacp");

       setVisible(true);
   }

   public EditAccountPanel getAccountPanel() {
       return eacp;
   }

   public EditBillingPanel getBillingPanel() {
       return ebp;
   }

   public EditAddressPanel getAddressPanel() {
       return eadp;
   }
}
