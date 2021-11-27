package view.menupanels;

import model.Cart;
import model.Model;
import model.movie.Movie;
import model.user.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The store panel for customers to browse movies and add to order
 */
public class StorePanel extends JPanel implements ActionListener {

    /**
     * Movie categories
     */
    private String[] categories = {
            "Horror", "Mystery", "Adventure", "Action", "Thriller", "Comedy", "Sci-fi", "Drama"
    };

    /**
     * Components for searching
     */
    private final JLabel searchLabel;
    private final JTextField searchInput;
    private final JButton searchButton;
    private final JLabel searchCategoryLabel;
    private final JComboBox categoryList;
    private final JButton searchMoviesButton;

    /**
     * Components for displaying data
     */
    private final JTable table;
    private final JScrollPane scrollPane;

    /**
     * Components for adding movie to order
     */
    private final JButton addMovieButton;

    public StorePanel() {
        setLayout(new BorderLayout(20, 10));
        JPanel north = new JPanel();
        searchLabel = new JLabel("Title:");
        searchInput = new JTextField(10);

        searchCategoryLabel = new JLabel("Category:");
        categoryList = new JComboBox(categories);
        categoryList.addActionListener(this);
        categoryList.setSelectedItem("");

        searchMoviesButton = new JButton("Reset");
        searchMoviesButton.setActionCommand("searchAll");
        searchMoviesButton.addActionListener(this);

        searchButton = new JButton("Search");
        searchButton.setActionCommand("searchTitle");
        searchButton.addActionListener(this);

        addMovieButton = new JButton("Add to Cart");
        addMovieButton.setActionCommand("addMovie");
        addMovieButton.addActionListener(this);

        north.add(searchLabel);
        north.add(searchInput);
        north.add(searchButton);
        north.add(searchCategoryLabel);
        north.add(categoryList);
        north.add(searchMoviesButton);
        north.add(addMovieButton);
        add(north, BorderLayout.NORTH);


        table = new JTable();
        List<Movie> allMovies = Model.getMovieService().getAllMovies();
        displayResultsInTable(allMovies);
        scrollPane = new JScrollPane(table);

        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.SOUTH);
        add(new JPanel(), BorderLayout.EAST);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    public void displayResultsInTable(List<Movie> movies) {
        DefaultTableModel tmodel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String data[][] = new String[movies.size()][6];
        String column[] = {"BARCODE", "TITLE", "GENRE", "RELEASE", "PRICE", "STOCK"};

        int i = 0;
        for (Movie m : movies) {
            data[i][0] = m.getBarcode();
            data[i][1] = m.getTitle();
            data[i][2] = m.getGenre();
            data[i][3] = m.getReleaseDate();
            data[i][4] = String.valueOf(m.getCost());
            data[i][5] = String.valueOf(m.getQuantity());
            i++;
        }

        tmodel.setDataVector(data, column);
        table.setModel(tmodel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<Movie> result = new ArrayList<>();
        if (e.getActionCommand().equals("searchTitle")) {
            result = Model.getMovieService().findMovieByTitle(searchInput.getText());
        } else if (e.getActionCommand().equals("comboBoxChanged")) {
            String category = (String) categoryList.getSelectedItem();
            if (category != null) {
                result = Model.getMovieService().getMoviesByCategory(category.toLowerCase());
            }
        } else if (e.getActionCommand().equals("searchAll")) {
            result = Model.getMovieService().getAllMovies();
            searchInput.setText("");
        }
        if (e.getActionCommand().equals("addMovie")) {
            int[] selected = table.getSelectedRows();
            if (selected.length == 0) {
                JOptionPane.showMessageDialog(this, "No movie selected");
            } else {
                User u = Model.getUserService().getLoggedInUser();
                Cart cart = u.getCart();
                for (int row : selected) {
                    // check if stock is available
                    int stock = Integer.parseInt((String) table.getValueAt(row, 5));
                    Movie m = new Movie();
                    m.setBarcode((String) table.getValueAt(row, 0));
                    m.setTitle((String) table.getValueAt(row, 1));
                    m.setGenre((String) table.getValueAt(row, 2));
                    m.setReleaseDate((String) table.getValueAt(row, 3));
                    m.setCost(Double.parseDouble((String) table.getValueAt(row, 4)));
                    m.setQuantity(stock);
                    int quantityInCart = cart.getQuantity(m);
                    if ((stock - quantityInCart) <= 0) {
                        JOptionPane.showMessageDialog(this, "No stock available for the selected movie.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        cart.addMovieToCart(m, 1);
                    }
                }
            }
            return;
        }
        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No movies match the desired search");
        } else {
            displayResultsInTable(result);
        }
    }
}
