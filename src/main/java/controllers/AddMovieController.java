package controllers;

import database.MovieRepository;
import model.Movie;
import view.dialogs.AddMovieDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddMovieController implements ActionListener {

    private final MovieRepository movieRepository;

    private final AddMovieDialog view;

    public AddMovieController(AddMovieDialog addMovieDialog) {
        movieRepository = MovieRepository.getInstance();
        view = addMovieDialog;
    }

    private void addMovie() {
        Movie m = new Movie();
        m.setBarcode(view.getBarcodeInput());
        m.setTitle(view.getMovieTitleInput());
        m.setPrice(Double.parseDouble(view.getPriceInput()));
        m.setGenre(view.getCategory());
        m.setReleaseDate(view.getReleaseDateInput());
        if (movieRepository.addMovie(m, Integer.parseInt(view.getQuantityInput()))) {
            view.dispose();
        } else {
            view.displayErrorMessage("Error adding movie\nCheck all fields");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("addMovie")) {
            addMovie();
        }
    }
}
