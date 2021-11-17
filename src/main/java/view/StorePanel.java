package view;

import model.movie.Movie;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StorePanel extends JPanel implements ActionListener {

    private String[] categories = {"Horror", "Mystery", "Adventure", "Action", "Thriller", "Comedy"};

    JLabel searchLabel;
    JTextField searchInput;
    JButton searchButton;

    JLabel searchCategoryLabel;
    JComboBox categoryList;
    JButton searchCategoryButton;

    JButton searchMoviesButton;


    public StorePanel() {
        searchLabel = new JLabel("Search Movies by Title:");
        searchInput = new JTextField(20);

        searchCategoryLabel = new JLabel("Search Movies by Category:");
        categoryList = new JComboBox(categories);
        categoryList.addActionListener(this);

        searchCategoryButton = new JButton("Search by Category");
        searchCategoryButton.setActionCommand("searchCategory");
        searchCategoryButton.addActionListener(this);

        searchMoviesButton = new JButton("Search All Movies");
        searchMoviesButton.setActionCommand("searchAll");
        searchMoviesButton.addActionListener(this);

        searchButton = new JButton("Search by Title");
        searchButton.setActionCommand("searchTitle");
        searchButton.addActionListener(this);

        this.add(searchLabel);
        this.add(searchInput);
        this.add(searchButton);
        this.add(searchCategoryLabel);
        this.add(categoryList);
        this.add(searchCategoryButton);
        this.add(searchMoviesButton);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<Movie> result = new ArrayList<>();
        if (e.getActionCommand().equals("searchTitle")) {
            result = App.getMovieService().findMovieByTitle(searchInput.getText());

        } else if (e.getActionCommand().equals("searchCategory")) {
            String category = (String) categoryList.getSelectedItem();
            if (category != null) {
                result = App.getMovieService().getMoviesByCategory(category.toLowerCase());
            }
        } else if (e.getActionCommand().equals("searchAll")) {
            result = App.getMovieService().getAllMovies();
        }
        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No movies match the desired search");
        }
    }
}
