package view.tablemodels;

import database.MovieRepository;
import database.Observer;
import model.Movie;

import javax.swing.table.DefaultTableModel;
import java.util.Map;

public class StoreTableModel extends DefaultTableModel implements Observer {

    public StoreTableModel() {
        subscribe();
        updateTable();
    }

    private void subscribe() {
        MovieRepository.getInstance().registerObserver(this);
    }

    public void filterTable(Map<Movie,Integer> movies) {
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

        setDataVector(data, column);
    }

    private void updateTable() {
        Map<Movie,Integer> movies = MovieRepository.getInstance().getAllMovies();
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

        setDataVector(data, column);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public void update() {
        updateTable();
    }
}
