package controllers;

import model.Cart;
import model.Movie;
import model.User;
import services.MovieService;
import services.UserService;
import view.StoreFront;
import view.dialogs.AddMovieDialog;
import view.dialogs.EditMovieDialog;
import view.shoppanels.StorePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreController implements ActionListener {

    private final StorePanel view;
    private final MovieService movieService;
    private final UserService userService;

    public StoreController(StorePanel view) {
        this.view = view;
        movieService = StoreFront.getMovieService();
        userService = StoreFront.getUserService();
    }

    private void searchTitle() {
        Map<Movie,Integer> result;
        result = movieService.getMovieByTitle(view.getSearchInput().getText());
        if (result.isEmpty()) {
            view.displayMessage("No movies match the desired search");
        }
        view.setTable(result);
    }

    private void searchCategory() {
        Map<Movie,Integer> result = new HashMap<>();
        String category = (String) view.getCategoryList().getSelectedItem();
        if (category != null) {
            result = movieService.getMoviesByCategory(category.toLowerCase());
        }
        if (result.isEmpty()) {
            view.displayMessage("No movies match the desired search");
        }
        view.setTable(result);
    }

    private void searchAll() {
        Map<Movie,Integer> result;
        result = movieService.getAllMovies();
        view.getSearchInput().setText("");
        if (result.isEmpty()) {
            view.displayMessage("No movies match the desired search");
        }
        view.setTable(result);
    }

    private void addMovieToCart() {
        int[] selected = view.getTable().getSelectedRows();
        if (selected.length == 0) {
            view.displayMessage("Select a movie");
        } else {
            User u = userService.getLoggedInUser();
            Cart cart = u.getCart();
            for (int row : selected) {
                int stock = Integer.parseInt((String) view.getTable().getValueAt(row, 5));
                Movie m = new Movie();
                m.setBarcode((String) view.getTable().getValueAt(row, 0));
                m.setTitle((String) view.getTable().getValueAt(row, 1));
                m.setGenre((String) view.getTable().getValueAt(row, 2));
                m.setReleaseDate((String) view.getTable().getValueAt(row, 3));
                m.setPrice(Double.parseDouble((String) view.getTable().getValueAt(row, 4)));
                int quantityInCart = cart.getQuantity(m);
                if ((stock - quantityInCart) <= 0) {
                    view.displayMessage("Stock is unavailable for the selected movie");
                } else {
                    cart.addMovieToCart(m, 1);
                }
            }
        }
    }

    private void increaseStock() {
        int[] selected = view.getTable().getSelectedRows();
        if (selected.length <= 0) {
            view.displayMessage("Select a movie");
        } else {
            for (int j : selected) {
                String barcode = (String) view.getTable().getValueAt(j, 0);
                movieService.returnMovie(barcode);
            }
        }
    }

    private void decreaseStock() {
        int[] selected = view.getTable().getSelectedRows();
        if (selected.length <= 0) {
            view.displayMessage("Select a movie");
        } else {
            for (int j : selected) {
                String barcode = (String) view.getTable().getValueAt(j, 0);
                movieService.removeStock(barcode);
            }
        }
    }

    private void deleteMovie() {
        int[] selected = view.getTable().getSelectedRows();
        if (selected.length == 0) {
            view.displayMessage("Select a movie");
        } else {
            List<String> barcodes = new ArrayList<>();
            for (int j : selected) {
                String barcode = (String) view.getTable().getValueAt(j, 0);
                barcodes.add(barcode);
            }
            for (String barcode : barcodes) {
                movieService.deleteMovie(barcode);
            }
        }
    }

    private void updateMovie() {
        int[] selected = view.getTable().getSelectedRows();
        if (selected.length != 1) {
            view.displayMessage("Select a movie");
        } else {
            String barcode = (String)view.getTable().getValueAt(selected[0],0);
            Movie m = movieService.getMovie(barcode);
            new EditMovieDialog(m);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("searchTitle")) {
            searchTitle();
        } else if (e.getActionCommand().equals("comboBoxChanged")) {
            searchCategory();
        } else if (e.getActionCommand().equals("searchAll")) {
            searchAll();
        } else if (e.getActionCommand().equals("addMovie")) {
            addMovieToCart();
        } else if (e.getActionCommand().equals("adminAdd")) {
            increaseStock();
        } else if (e.getActionCommand().equals("adminRemove")) {
            decreaseStock();
        } else if (e.getActionCommand().equals("delete")) {
            deleteMovie();
        } else if (e.getActionCommand().equals("edit")) {
            updateMovie();
        } else if (e.getActionCommand().equals("add")) {
            new AddMovieDialog();
        }
    }
}
