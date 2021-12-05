package view;

import dialin.DialInService;
import view.cards.LoginCards;
import view.cards.StoreFrontCards;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EntrancePanel extends JPanel implements ActionListener {

    private static final String videocoLogoPath = System.getProperty("user.dir") + "/src/main/resources/videoco_emblem.png";
    private static final String phonePath = System.getProperty("user.dir") + "/src/main/resources/phone.png";

    private final StoreFrontCards cards;

    public EntrancePanel(StoreFrontCards cards) {
        this.cards = cards;
        setLayout(new BorderLayout());
        JPanel southBar = new JPanel();
        JButton dialIn = null;
        try {
            BufferedImage videocoLogo = ImageIO.read(new File(videocoLogoPath));
            BufferedImage phoneLogo = ImageIO.read(new File(phonePath));
            Image phone = phoneLogo.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            Image dimg = videocoLogo.getScaledInstance(800, 500,Image.SCALE_SMOOTH);
            JLabel imageLogo = new JLabel(new ImageIcon(dimg));
            dialIn = new JButton(new ImageIcon(phone));
            dialIn.setOpaque(false);
            dialIn.setBorderPainted(false);
            dialIn.setContentAreaFilled(false);
            dialIn.addActionListener(this);
            dialIn.setActionCommand("dialInService");
            add(imageLogo, BorderLayout.CENTER);
        } catch(IOException e) {
            e.printStackTrace();
        }

        southBar.add(new LoginCards(this));
        if (dialIn != null) {
            add(dialIn, BorderLayout.NORTH);
        }
        add(southBar, BorderLayout.SOUTH);
        setVisible(true);
    }

    public StoreFrontCards getStoreFront() {
        return cards;
    }

    public void login() {
        cards.getLayout().show(cards, "sp");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("login")) {
            cards.getLayout().show(cards, "sp");
        } else if (e.getActionCommand().equals("dialInService")) {
            new DialInService();
        }
    }
}
