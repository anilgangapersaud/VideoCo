package view.cards;

import view.shoppanels.AccountPanel;
import view.shoppanels.AddressPanel;
import view.shoppanels.BillingPanel;

import javax.swing.*;
import java.awt.*;

public class AccountCards extends JPanel {

   protected CardLayout cl;

   private final AccountPanel eacp;

   private final AddressPanel eadp;

   private final BillingPanel ebp;

   public AccountCards() {
       cl = new CardLayout();
       setLayout(cl);
       eacp = new AccountPanel(this);
       eadp = new AddressPanel(this);
       ebp = new BillingPanel(this);

       add(eacp, "eacp");
       add(ebp, "ebp");
       add(eadp, "eadp");

       cl.show(this, "eacp");

       setVisible(true);
   }

   public AccountPanel getAccountPanel() {
       return eacp;
   }

   public BillingPanel getBillingPanel() {
       return ebp;
   }

   public AddressPanel getAddressPanel() {
       return eadp;
   }
}
