package view.storefrontpanels;

import view.StoreFront;
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
    private StoreFrontCards cards;

    public EntrancePanel(StoreFrontCards cards) {
        this.cards = cards;
        setLayout(new BorderLayout());
        // load logo in the center
        try {
            BufferedImage videocoLogo = ImageIO.read(new File(videocoLogoPath));
            Image dimg = videocoLogo.getScaledInstance(800, 500,Image.SCALE_SMOOTH);
            JLabel imageLogo = new JLabel(new ImageIcon(dimg));
            add(imageLogo, BorderLayout.CENTER);
        } catch(IOException e) {
            e.printStackTrace();
        }

        // login panel at the bottom
        add(new LoginCards(this), BorderLayout.SOUTH);

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
        }
    }
}
