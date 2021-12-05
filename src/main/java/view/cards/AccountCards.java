package view.cards;

import view.shoppanels.AccountPanel;
import view.shoppanels.AddressPanel;
import view.shoppanels.BillingPanel;

import javax.swing.*;
import java.awt.*;

public class AccountCards extends JPanel {

    public AccountCards() {
       CardLayout cl = new CardLayout();
       setLayout(cl);
       AccountPanel eacp = new AccountPanel(this);
       AddressPanel eadp = new AddressPanel(this);
        BillingPanel ebp = new BillingPanel(this);

       add(eacp, "eacp");
       add(ebp, "ebp");
       add(eadp, "eadp");

       cl.show(this, "eacp");

       setVisible(true);
   }

}
