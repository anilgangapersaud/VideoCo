package controllers;

import database.MovieRepository;
import model.Movie;
import services.MovieServiceImpl;
import view.StoreFront;
import view.dialogs.EditMovieDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditMovieController implements ActionListener {

    private final EditMovieDialog view;
    private final MovieServiceImpl movieService;

    public EditMovieController(EditMovieDialog view) {
        this.view = view;
        movieService = StoreFront.getMovieService();
    }

    private void updateMovie() {
        Movie m = view.getEditingMovie();
        m.setTitle(view.getMovieTitleInput());
        m.setGenre(view.getMovieCategory());
        m.setPrice(view.getPrice());
        m.setReleaseDate(view.getReleaseDate());
        if (movieService.updateMovie(m)) {
            view.dispose();
        } else {
            view.displayErrorMessage("Failed to update movie\nCheck all fields");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("update")) {
            updateMovie();
        }
    }
}
