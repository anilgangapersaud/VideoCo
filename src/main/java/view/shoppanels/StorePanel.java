package view.shoppanels;

import controllers.StoreController;
import database.MovieRepository;
import database.UserRepository;

import model.Movie;
import view.StoreFront;
import view.tablemodels.StoreTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;


public class StorePanel extends JPanel {

    private final String[] categories = {
            "Horror", "Mystery", "Adventure", "Action", "Thriller", "Comedy", "Sci-fi", "Drama"
    };


    private JTextField searchInput;

    private JComboBox<String> categoryList;

    private JTable table;

    private StoreTableModel tableModel;

    public StorePanel() {
        setLayout(new BorderLayout(20, 10));
        StoreController controller = new StoreController(this);
        constructNorthBarView(controller);
        constructCenterTable();
        constructSouthBarView(controller);

        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);

        setVisible(true);
    }

    private void constructSouthBarView(StoreController controller) {
        if (StoreFront.getUserService().getLoggedInUser().isAdmin()) {
            JPanel southBar = new JPanel();
            JButton editMovie = new JButton("Edit Movie");
            editMovie.addActionListener(controller);
            editMovie.setActionCommand("edit");
            JButton addMovie = new JButton("Add Movie");
            addMovie.addActionListener(controller);
            addMovie.setActionCommand("add");
            JButton deleteMovie = new JButton("Delete Movie");
            deleteMovie.addActionListener(controller);
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
        tableModel = new StoreTableModel();
        StoreFront.getMovieService().registerObserver(tableModel);
        table.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void constructNorthBarView(StoreController controller) {
        JPanel north = new JPanel();

        JLabel searchLabel = new JLabel("Title:");
        searchInput = new JTextField(10);

        JLabel searchCategoryLabel = new JLabel("Category:");
        categoryList = new JComboBox<>(categories);
        categoryList.addActionListener(controller);

        JButton searchMoviesButton = new JButton("Reset");
        searchMoviesButton.setActionCommand("searchAll");
        searchMoviesButton.addActionListener(controller);

        JButton searchButton = new JButton("Search");
        searchButton.setActionCommand("searchTitle");
        searchButton.addActionListener(controller);

        north.add(searchLabel);
        north.add(searchInput);
        north.add(searchButton);
        north.add(searchCategoryLabel);
        north.add(categoryList);
        north.add(searchMoviesButton);

        if (!StoreFront.getUserService().getLoggedInUser().isAdmin()) {
            JButton addMovieButton = new JButton("Add to Cart");
            addMovieButton.setActionCommand("addMovie");
            addMovieButton.addActionListener(controller);
            north.add(addMovieButton);
        } else {
            JButton adminAddStock = new JButton("Increase Stock");
            adminAddStock.setActionCommand("adminAdd");
            adminAddStock.addActionListener(controller);
            north.add(adminAddStock);
            JButton adminRemoveStock = new JButton("Decrease Stock");
            adminRemoveStock.setActionCommand("adminRemove");
            adminRemoveStock.addActionListener(controller);
            north.add(adminRemoveStock);
        }
        add(north, BorderLayout.NORTH);
    }

    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void displayErrorMessage(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public JTextField getSearchInput() {
        return searchInput;
    }

    public JComboBox<String> getCategoryList() {
        return categoryList;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(Map<Movie,Integer> movies) {
        tableModel.filterTable(movies);
    }

}
