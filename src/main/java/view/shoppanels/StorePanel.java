package view.shoppanels;

import database.MovieRepository;
import model.Cart;
import model.Model;
import model.Movie;
import model.User;
import view.cards.ShopCards;
import view.dialogs.AddMovieDialog;
import view.dialogs.EditMovieDialog;
import view.tablemodels.StoreTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorePanel extends JPanel implements ActionListener {

    private String[] categories = {
            "Horror", "Mystery", "Adventure", "Action", "Thriller", "Comedy", "Sci-fi", "Drama"
    };


    private JTextField searchInput;
    private JComboBox<String> categoryList;

    private JTable table;

    private final ShopCards shopCards;

    public StorePanel(ShopCards cards) {
        shopCards = cards;
        setLayout(new BorderLayout(20, 10));

        constructNorthBarView();
        constructCenterTable();
        constructSouthBarView();

        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);

        setVisible(true);
    }

    private void constructSouthBarView() {
        if (Model.getUserService().getLoggedInUser().isAdmin()) {
            JPanel southBar = new JPanel();
            JButton editMovie = new JButton("Edit Movie");
            editMovie.addActionListener(this);
            editMovie.setActionCommand("edit");
            JButton addMovie = new JButton("Add Movie");
            addMovie.addActionListener(this);
            addMovie.setActionCommand("add");
            JButton deleteMovie = new JButton("Delete Movie");
            deleteMovie.addActionListener(this);
            deleteMovie.setActionCommand("delete");
            southBar.add(editMovie);
            southBar.add(deleteMovie);
            southBar.add(addMovie);
            add(southBar, BorderLayout.SOUTH);
        } else {
            add(new JPanel(), BorderLayout.SOUTH);
        }
    }

    private void constructCenterTable() {
        table = new JTable();
        StoreTableModel stm = new StoreTableModel();
        MovieRepository.getInstance().registerObserver(stm);
        table.setModel(stm);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void constructNorthBarView() {
        JPanel north = new JPanel();

        JLabel searchLabel = new JLabel("Title:");
        searchInput = new JTextField(10);

        JLabel searchCategoryLabel = new JLabel("Category:");
        categoryList = new JComboBox<String>(categories);
        categoryList.addActionListener(this);

        JButton searchMoviesButton = new JButton("Reset");
        searchMoviesButton.setActionCommand("searchAll");
        searchMoviesButton.addActionListener(this);

        JButton searchButton = new JButton("Search");
        searchButton.setActionCommand("searchTitle");
        searchButton.addActionListener(this);

        north.add(searchLabel);
        north.add(searchInput);
        north.add(searchButton);
        north.add(searchCategoryLabel);
        north.add(categoryList);
        north.add(searchMoviesButton);

        if (!Model.getUserService().getLoggedInUser().isAdmin()) {
            JButton addMovieButton = new JButton("Add to Cart");
            addMovieButton.setActionCommand("addMovie");
            addMovieButton.addActionListener(this);
            north.add(addMovieButton);
        } else {
            JButton adminAddStock = new JButton("Increase Stock");
            adminAddStock.setActionCommand("adminAdd");
            adminAddStock.addActionListener(this);
            north.add(adminAddStock);
            JButton adminRemoveStock = new JButton("Decrease Stock");
            adminRemoveStock.setActionCommand("adminRemove");
            adminRemoveStock.addActionListener(this);
            north.add(adminRemoveStock);
        }
        add(north, BorderLayout.NORTH);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("searchTitle")) {
            Map<Movie,Integer> result;
            result = Model.getMovieService().findMovieByTitle(searchInput.getText());
            if (result.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No movies match the desired search");
            }
        } else if (e.getActionCommand().equals("comboBoxChanged")) {
            Map<Movie,Integer> result = new HashMap<>();
            String category = (String) categoryList.getSelectedItem();
            if (category != null) {
                result = Model.getMovieService().getMoviesByCategory(category.toLowerCase());
            }
            if (result.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No movies match the desired search");
            }
        } else if (e.getActionCommand().equals("searchAll")) {
            Map<Movie,Integer> result;
            result = Model.getMovieService().getAllMovies();
            searchInput.setText("");
            if (result.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No movies match the desired search");
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
            }
        } else if (e.getActionCommand().equals("add")) {
            new AddMovieDialog();
        } else if (e.getActionCommand().equals("edit")) {
            int[] selected = table.getSelectedRows();
            if (selected.length != 1) {
                JOptionPane.showMessageDialog(this, "Select a movie to edit.");
            } else {
                String barcode = (String)table.getValueAt(selected[0],0);
                Movie m = Model.getMovieService().getMovie(barcode);
                new EditMovieDialog(m);
            }
        }
    }
}
