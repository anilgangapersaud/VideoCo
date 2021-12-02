package view.dialogs;

import jdk.nashorn.internal.scripts.JO;
import model.Model;
import model.Movie;
import view.shoppanels.StorePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditMovieDialog extends JDialog implements ActionListener {

    private static final int windowWidth = 300;
    private static final int windowHeight = 500;

    private String[] categories = {
            "Horror", "Mystery", "Adventure", "Action", "Thriller", "Comedy", "Sci-fi", "Drama"
    };

    private Movie editingMovie;

    private JLabel movieTitleLabel;
    private JTextField movieTitleInput;

    private JLabel movieCategoryLabel;
    private JComboBox categoryList;

    private JLabel releaseDateLabel;
    private JTextField releaseDateInput;

    private JLabel priceLabel;
    private JTextField priceInput;

    private JButton updateButton;

    private StorePanel sp;

    private int verticalStrut = 10;
    private int columnsize = 5;

    public EditMovieDialog(StorePanel store, Movie m) {
        sp = store;
        editingMovie = new Movie();
        editingMovie.setReleaseDate(m.getReleaseDate());
        editingMovie.setBarcode(m.getBarcode());
        editingMovie.setPrice(m.getPrice());
        editingMovie.setTitle(m.getTitle());
        editingMovie.setGenre(m.getGenre());
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(windowWidth, windowHeight);
        setResizable(false);
        setTitle("Edit Movie");

        movieTitleLabel = new JLabel("Title");
        movieTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        movieTitleInput = new JTextField(columnsize);
        movieTitleInput.setText(editingMovie.getTitle());

        movieCategoryLabel = new JLabel("Category");
        movieCategoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryList = new JComboBox(categories);
        categoryList.addActionListener(this);
        categoryList.setSelectedItem(editingMovie.getGenre());

        releaseDateLabel = new JLabel("Release Date - MM/DD/YY");
        releaseDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        releaseDateInput = new JTextField(columnsize);
        releaseDateInput.setText(editingMovie.getReleaseDate());

        priceLabel = new JLabel("Price");
        priceInput = new JTextField(columnsize);
        priceInput.setText(String.valueOf(editingMovie.getPrice()));

        updateButton = new JButton("Update");
        updateButton.setActionCommand("update");
        updateButton.addActionListener(this);

        Box box = Box.createVerticalBox();
        box.add(movieTitleLabel);
        box.add(movieTitleInput);
        box.add(Box.createVerticalStrut(verticalStrut));
        box.add(movieCategoryLabel);
        box.add(categoryList);
        box.add(Box.createVerticalStrut(verticalStrut));
        box.add(releaseDateLabel);
        box.add(releaseDateInput);
        box.add(Box.createVerticalStrut(verticalStrut));
        box.add(priceLabel);
        box.add(priceInput);
        box.add(Box.createVerticalStrut(verticalStrut));
        box.add(updateButton);

        add(box);
        setVisible(true);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("update")) {
            editingMovie.setTitle(movieTitleInput.getText());
            editingMovie.setGenre((String)categoryList.getSelectedItem());
            editingMovie.setPrice(Double.parseDouble(priceInput.getText()));
            editingMovie.setReleaseDate(releaseDateInput.getText());
            if (Model.getMovieService().updateMovie(editingMovie)) {
                sp.displayAllMovies();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating movie.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
