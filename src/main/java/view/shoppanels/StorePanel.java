package view.shoppanels;

import model.Cart;
import model.Model;
import model.Movie;
import model.User;
import view.cards.ShopCards;
import view.dialogs.AddMovieDialog;
import view.dialogs.EditMovieDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Admin functionalities
     */
    private JButton adminAddStock;
    private JButton adminRemoveStock;
    private JButton deleteMovie;
    private JButton addMovie;
    private JButton editMovie;


    /**
     * Components for displaying data
     */
    private final JTable table;
    private final JScrollPane scrollPane;

    /**
     * Components for adding movie to order
     */
    private JButton addMovieButton = null;

    private ShopCards shopCards;

    public StorePanel(ShopCards cards) {
        shopCards = cards;
        setLayout(new BorderLayout(20, 10));
        JPanel north = new JPanel();
        searchLabel = new JLabel("Title:");
        searchInput = new JTextField(10);

        searchCategoryLabel = new JLabel("Category:");
        categoryList = new JComboBox(categories);
        categoryList.addActionListener(this);

        searchMoviesButton = new JButton("Reset");
        searchMoviesButton.setActionCommand("searchAll");
        searchMoviesButton.addActionListener(this);

        searchButton = new JButton("Search");
        searchButton.setActionCommand("searchTitle");
        searchButton.addActionListener(this);



        north.add(searchLabel);
        north.add(searchInput);
        north.add(searchButton);
        north.add(searchCategoryLabel);
        north.add(categoryList);
        north.add(searchMoviesButton);


        if (!Model.getUserService().getLoggedInUser().isAdmin()) {
            addMovieButton = new JButton("Add to Cart");
            addMovieButton.setActionCommand("addMovie");
            addMovieButton.addActionListener(this);
            north.add(addMovieButton);
        } else {
            adminAddStock = new JButton("Increase Stock");
            adminAddStock.setActionCommand("adminAdd");
            adminAddStock.addActionListener(this);
            north.add(adminAddStock);
            adminRemoveStock = new JButton("Decrease Stock");
            adminRemoveStock.setActionCommand("adminRemove");
            adminRemoveStock.addActionListener(this);
            north.add(adminRemoveStock);
        }

        add(north, BorderLayout.NORTH);
        table = new JTable();
        displayAllMovies();
        scrollPane = new JScrollPane(table);

        add(new JPanel(), BorderLayout.WEST);

        if (Model.getUserService().getLoggedInUser().isAdmin()) {
            JPanel southbar = new JPanel();
            editMovie = new JButton("Edit Movie");
            editMovie.addActionListener(this);
            editMovie.setActionCommand("edit");
            addMovie = new JButton("Add Movie");
            addMovie.addActionListener(this);
            addMovie.setActionCommand("add");
            deleteMovie = new JButton("Delete Movie");
            deleteMovie.addActionListener(this);
            deleteMovie.setActionCommand("delete");
            southbar.add(editMovie);
            southbar.add(deleteMovie);
            southbar.add(addMovie);
            add(southbar, BorderLayout.SOUTH);
        } else {
            add(new JPanel(), BorderLayout.SOUTH);
        }
        add(new JPanel(), BorderLayout.EAST);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    public void displayAllMovies() {
        Map<Movie,Integer> movies = Model.getMovieService().getAllMovies();
        displayResultsInTable(movies);
    }

    public void displayResultsInTable(Map<Movie,Integer> movies) {
        DefaultTableModel tmodel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[][] data = new String[movies.size()][6];
        String[] column = {"BARCODE", "TITLE", "GENRE", "RELEASE", "PRICE", "STOCK"};

        int i = 0;
        for (Map.Entry<Movie,Integer> entry : movies.entrySet()) {
            Movie m = entry.getKey();
            data[i][0] = m.getBarcode();
            data[i][1] = m.getTitle();
            data[i][2] = m.getGenre();
            data[i][3] = m.getReleaseDate();
            data[i][4] = String.valueOf(m.getPrice());
            data[i][5] = String.valueOf(entry.getValue());
            i++;
        }

        tmodel.setDataVector(data, column);
        table.setModel(tmodel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("searchTitle")) {
            Map<Movie,Integer> result;
            result = Model.getMovieService().findMovieByTitle(searchInput.getText());
            if (result.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No movies match the desired search");
            } else {
                displayResultsInTable(result);
            }
        } else if (e.getActionCommand().equals("comboBoxChanged")) {
            Map<Movie,Integer> result = new HashMap<>();
            String category = (String) categoryList.getSelectedItem();
            if (category != null) {
                result = Model.getMovieService().getMoviesByCategory(category.toLowerCase());
            }
            if (result.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No movies match the desired search");
            } else {
                displayResultsInTable(result);
            }
            if (result.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No movies match the desired search");
            } else {
                displayResultsInTable(result);
            }
        } else if (e.getActionCommand().equals("searchAll")) {
            Map<Movie,Integer> result;
            result = Model.getMovieService().getAllMovies();
            searchInput.setText("");
            if (result.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No movies match the desired search");
            } else {
                displayResultsInTable(result);
            }
        } else if (e.getActionCommand().equals("addMovie")) {
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
                    m.setPrice(Double.parseDouble((String) table.getValueAt(row, 4)));
                    int quantityInCart = cart.getQuantity(m);
                    if ((stock - quantityInCart) <= 0) {
                        JOptionPane.showMessageDialog(this, "No stock available for the selected movie.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        cart.addMovieToCart(m, 1);
                        shopCards.getCartPanel().updateCart();
                    }
                }
            }
        } else if (e.getActionCommand().equals("adminAdd")) {
            int[] selected = table.getSelectedRows();
            if (selected.length <= 0) {
                JOptionPane.showMessageDialog(this, "No items selected","Error",JOptionPane.ERROR_MESSAGE);
            } else {
                for (int i = 0; i < selected.length; i++) {
                    String barcode = (String)table.getValueAt(selected[i], 0);
                    Model.getMovieService().addStock(barcode);
                }
                displayAllMovies();
            }
        } else if (e.getActionCommand().equals("adminRemove")) {
            int[] selected = table.getSelectedRows();
            if (selected.length <= 0) {
                JOptionPane.showMessageDialog(this, "No items selected","Error",JOptionPane.ERROR_MESSAGE);
            } else {
                for (int j : selected) {
                    String barcode = (String) table.getValueAt(j, 0);
                    Model.getMovieService().removeStock(barcode);
                }
                displayAllMovies();
            }
        } else if (e.getActionCommand().equals("delete")) {
            int[] selected = table.getSelectedRows();
            if (selected.length == 0) {
                JOptionPane.showMessageDialog(this, "Select a movie to delete", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                List<String> barcodes = new ArrayList<>();
                for (int j : selected) {
                    String barcode = (String) table.getValueAt(j, 0);
                    barcodes.add(barcode);
                }
                Model.getMovieService().deleteMovies(barcodes);
                displayAllMovies();
            }
        } else if (e.getActionCommand().equals("add")) {
            new AddMovieDialog(this);
        } else if (e.getActionCommand().equals("edit")) {
            int[] selected = table.getSelectedRows();
            if (selected.length != 1) {
                JOptionPane.showMessageDialog(this, "Select a movie to edit.");
            } else {
                String barcode = (String)table.getValueAt(selected[0],0);
                Movie m = Model.getMovieService().getMovie(barcode);
                new EditMovieDialog(this, m);
            }
        }
    }
}
