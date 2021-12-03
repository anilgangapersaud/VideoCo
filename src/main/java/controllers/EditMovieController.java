package controllers;

import database.MovieRepository;
import model.Movie;
import view.dialogs.EditMovieDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditMovieController implements ActionListener {

    private final EditMovieDialog view;
    private final MovieRepository movieRepository;

    public EditMovieController(EditMovieDialog view) {
        this.view = view;
        movieRepository = MovieRepository.getInstance();
    }

    private void updateMovie() {
        Movie m = view.getEditingMovie();
        m.setTitle(view.getMovieTitleInput());
        m.setGenre(view.getMovieCategory());
        m.setPrice(view.getPrice());
        m.setReleaseDate(view.getReleaseDate());
        if (movieRepository.updateMovie(m)) {
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
