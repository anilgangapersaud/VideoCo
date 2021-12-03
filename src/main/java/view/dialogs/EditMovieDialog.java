package view.dialogs;

import controllers.EditMovieController;
import model.Movie;

import javax.swing.*;
import java.awt.*;

public class EditMovieDialog extends JDialog{

    private static final int windowWidth = 300;
    private static final int windowHeight = 500;

    private String[] categories = {
            "Horror", "Mystery", "Adventure", "Action", "Thriller", "Comedy", "Sci-fi", "Drama"
    };

    private final Movie editingMovie;
    private final JTextField movieTitleInput;
    private final JComboBox<String> categoryList;
    private final JTextField releaseDateInput;
    private final JTextField priceInput;

    public EditMovieDialog(Movie m) {
        editingMovie = new Movie();
        editingMovie.setReleaseDate(m.getReleaseDate());
        editingMovie.setBarcode(m.getBarcode());
        editingMovie.setPrice(m.getPrice());
        editingMovie.setTitle(m.getTitle());
        editingMovie.setGenre(m.getGenre());

        EditMovieController editMovieController = new EditMovieController(this);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(windowWidth, windowHeight);
        setResizable(false);
        setTitle("Edit Movie");

        JLabel movieTitleLabel = new JLabel("Title");
        movieTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        int columnSize = 5;
        movieTitleInput = new JTextField(columnSize);
        movieTitleInput.setText(editingMovie.getTitle());

        JLabel movieCategoryLabel = new JLabel("Category");
        movieCategoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryList = new JComboBox<>(categories);
        categoryList.addActionListener(editMovieController);
        categoryList.setSelectedItem(editingMovie.getGenre());

        JLabel releaseDateLabel = new JLabel("Release Date - MM/DD/YY");
        releaseDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        releaseDateInput = new JTextField(columnSize);
        releaseDateInput.setText(editingMovie.getReleaseDate());

        JLabel priceLabel = new JLabel("Price");
        priceInput = new JTextField(columnSize);
        priceInput.setText(String.valueOf(editingMovie.getPrice()));

        JButton updateButton = new JButton("Update");
        updateButton.setActionCommand("update");
        updateButton.addActionListener(editMovieController);

        Box box = Box.createVerticalBox();
        box.add(movieTitleLabel);
        box.add(movieTitleInput);
        int verticalStrut = 10;
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
    }

    public Movie getEditingMovie() {
        return editingMovie;
    }

    public String getMovieTitleInput() {
        return movieTitleInput.getText();
    }

    public String getMovieCategory() {
        return (String)categoryList.getSelectedItem();
    }

    public double getPrice() {
        return Double.parseDouble(priceInput.getText());
    }

    public String getReleaseDate() {
        return releaseDateInput.getText();
    }

    public void displayErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
