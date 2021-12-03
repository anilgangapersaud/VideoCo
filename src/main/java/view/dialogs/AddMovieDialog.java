package view.dialogs;

import controllers.AddMovieController;

import javax.swing.*;
import java.awt.*;

public class AddMovieDialog extends JDialog {

    private static final int windowWidth = 300;
    private static final int windowHeight = 400;

    private String[] categories = {
            "Horror", "Mystery", "Adventure", "Action", "Thriller", "Comedy", "Sci-fi", "Drama"
    };

    private final JTextField barcodeInput;

    private final JTextField movieTitleInput;

    private final JComboBox<String> categoryList;

    private final JTextField releaseDateInput;

    private final JTextField priceInput;

    private final JTextField quantityInput;

    public AddMovieDialog() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(windowWidth, windowHeight);
        setResizable(false);
        setTitle("Add Movie");

        AddMovieController movieController = new AddMovieController(this);

        JLabel barcodeLabel = new JLabel("Barcode");
        barcodeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        int columnSize = 5;
        barcodeInput = new JTextField(columnSize);

        JLabel movieTitleLabel = new JLabel("Title");
        movieTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        movieTitleInput = new JTextField(columnSize);

        JLabel movieCategoryLabel = new JLabel("Category");
        movieCategoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryList = new JComboBox<>(categories);
        categoryList.addActionListener(movieController);

        JLabel releaseDateLabel = new JLabel("Release Date - MM/DD/YY");
        releaseDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        releaseDateInput = new JTextField(columnSize);

        JLabel priceLabel = new JLabel("Price");
        priceInput = new JTextField(columnSize);

        JLabel quantityLabel = new JLabel("Quantity");
        quantityInput = new JTextField(columnSize);

        JButton addButton = new JButton("Add");
        addButton.setActionCommand("addMovie");
        addButton.addActionListener(movieController);

        Box box = Box.createVerticalBox();
        box.add(barcodeLabel);
        box.add(barcodeInput);
        int verticalStrut = 10;
        box.add(Box.createVerticalStrut(verticalStrut));
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
        box.add(quantityLabel);
        box.add(quantityInput);
        box.add(Box.createVerticalStrut(verticalStrut));
        box.add(addButton);

        add(box);
        setVisible(true);
    }

    public void displayErrorMessage(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public String getBarcodeInput() {
        return barcodeInput.getText();
    }

    public String getMovieTitleInput() {
        return movieTitleInput.getText();
    }

    public String getCategory() {
        return (String)categoryList.getSelectedItem();
    }

    public String getReleaseDateInput() {
        return releaseDateInput.getText();
    }

    public String getPriceInput() {
        return priceInput.getText();
    }

    public String getQuantityInput() {
        return quantityInput.getText();
    }
}
