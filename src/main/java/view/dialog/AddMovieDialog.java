package view.dialog;

import model.Model;
import model.movie.Movie;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddMovieDialog extends JDialog implements ActionListener {

    private static final int windowHeight = 500;
    private static final int windowWidth = 800;
    private static final String windowName = "Add Movie";
    private JTextField barcodeInput;
    private JTextField movieTitleInput;
    private JTextArea descriptionInput;
    private String[] categories = {"Horror", "Mystery", "Adventure", "Action", "Thriller", "Comedy"};
    private JComboBox categoryList;
    private JTextField releaseDate;
    private JTextField quantityInput;
    private JTextField costInput;


    public AddMovieDialog() {
        JPanel addMoviePanel = new JPanel();

        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setSize(windowWidth, windowHeight);
        this.setTitle(windowName);


        JLabel barcodeLabel = new JLabel("Enter 10-digit Barcode:");
        barcodeInput = new JTextField(10);

        JLabel titleLabel = new JLabel("Enter Movie Title:");
        movieTitleInput = new JTextField(30);

        JLabel descriptionLabel = new JLabel("Enter Movie Description:");
        descriptionInput = new JTextArea(10,30);

        JLabel categoryLabel = new JLabel("Choose a category:");
        categoryList = new JComboBox(categories);
        categoryList.addActionListener(this);

        JLabel dateLabel = new JLabel("Enter the release date MM/DD/YYYY:");
        releaseDate = new JTextField(10);

        JLabel quantityLabel = new JLabel("Enter the quantity:");
        quantityInput = new JTextField(5);

        JLabel costLabel = new JLabel("Enter the cost:");
        costInput = new JTextField(9);

        JButton addMovie = new JButton("Add Movie");
        addMovie.setActionCommand("addMovie");
        addMovie.addActionListener(this);

        addMoviePanel.add(barcodeLabel);
        addMoviePanel.add(barcodeInput);
        addMoviePanel.add(titleLabel);
        addMoviePanel.add(movieTitleInput);
        addMoviePanel.add(descriptionLabel);
        addMoviePanel.add(descriptionInput);
        addMoviePanel.add(categoryLabel);
        addMoviePanel.add(categoryList);
        addMoviePanel.add(dateLabel);
        addMoviePanel.add(releaseDate);
        addMoviePanel.add(quantityLabel);
        addMoviePanel.add(quantityInput);
        addMoviePanel.add(costLabel);
        addMoviePanel.add(costInput);
        addMoviePanel.add(addMovie);

        add(addMoviePanel);
        this.setVisible(true);
    }

    private void clearInputs() {
        barcodeInput.setText("");
        movieTitleInput.setText("");
        descriptionInput.setText("");
        releaseDate.setText("");
        quantityInput.setText("");
        costInput.setText("");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("addMovie")) {
            Movie m = new Movie();
            m.setBarcode(barcodeInput.getText());
            m.setTitle(movieTitleInput.getText());
            m.setGenre((String) categoryList.getSelectedItem());
            m.setCost(Double.parseDouble(costInput.getText()));
            m.setQuantity(Integer.parseInt(quantityInput.getText()));
            m.setReleaseDate(releaseDate.getText());
            if (Model.getMovieService().addMovie(m)) {
                JOptionPane.showMessageDialog(this, "Added Movie!");
                clearInputs();
            } else {
                JOptionPane.showMessageDialog(this, "Error", "Check input fields", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
