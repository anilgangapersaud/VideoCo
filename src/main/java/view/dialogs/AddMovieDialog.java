package view.dialogs;

import model.Model;
import model.Movie;
import view.shoppanels.StorePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddMovieDialog extends JDialog implements ActionListener {

    private static final int windowWidth = 300;
    private static final int windowHeight = 400;

    private String[] categories = {
            "Horror", "Mystery", "Adventure", "Action", "Thriller", "Comedy", "Sci-fi", "Drama"
    };

    private JLabel barcodeLabel;
    private JTextField barcodeInput;

    private JLabel movieTitleLabel;
    private JTextField movieTitleInput;

    private JLabel movieCategoryLabel;
    private JComboBox categoryList;

    private JLabel releaseDateLabel;
    private JTextField releaseDateInput;

    private JLabel priceLabel;
    private JTextField priceInput;

    private JLabel quantityLabel;
    private JTextField quantityInput;

    private JButton addButton;

    private StorePanel sp;

    private int verticalStrut = 10;
    private int columnsize = 5;

    public AddMovieDialog(StorePanel store) {
        sp = store;
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(windowWidth, windowHeight);
        setResizable(false);
        setTitle("Add Movie");

        barcodeLabel = new JLabel("Barcode");
        barcodeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        barcodeInput = new JTextField(columnsize);

        movieTitleLabel = new JLabel("Title");
        movieTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        movieTitleInput = new JTextField(columnsize);

        movieCategoryLabel = new JLabel("Category");
        movieCategoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryList = new JComboBox(categories);
        categoryList.addActionListener(this);

        releaseDateLabel = new JLabel("Release Date - MM/DD/YY");
        releaseDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        releaseDateInput = new JTextField(columnsize);

        priceLabel = new JLabel("Price");
        priceInput = new JTextField(columnsize);

        quantityLabel = new JLabel("Quantity");
        quantityInput = new JTextField(columnsize);

        addButton = new JButton("Add");
        addButton.setActionCommand("add");
        addButton.addActionListener(this);

        Box box = Box.createVerticalBox();
        box.add(barcodeLabel);
        box.add(barcodeInput);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("add")) {
            Movie m = new Movie();
            m.setBarcode(barcodeInput.getText());
            m.setTitle(movieTitleInput.getText());
            m.setPrice(Double.parseDouble(priceInput.getText()));
            m.setGenre((String)categoryList.getSelectedItem());
            m.setReleaseDate(releaseDateInput.getText());
            if (Model.getMovieService().addMovie(m, Integer.parseInt(quantityInput.getText()))) {
                sp.displayAllMovies();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding movie. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
