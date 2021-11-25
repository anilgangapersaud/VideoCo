package view.menupanels;

import view.dialog.AddMovieDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryPanel extends JPanel implements ActionListener {

    public InventoryPanel() {
        JButton addMovieButton = new JButton("Add Movie");
        addMovieButton.setActionCommand("addMovie");
        addMovieButton.addActionListener(this);

        JButton removeMovieButton = new JButton("Remove Movie");
        removeMovieButton.setActionCommand("removeMovie");
        removeMovieButton.addActionListener(this);

        add(addMovieButton);
        add(removeMovieButton);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("addMovie")) {
            new AddMovieDialog();
        } else if (e.getActionCommand().equals("removeMovie")) {
        }
    }
}
