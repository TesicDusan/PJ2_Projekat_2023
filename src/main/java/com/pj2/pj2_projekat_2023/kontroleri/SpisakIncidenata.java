package com.pj2.pj2_projekat_2023.kontroleri;

import com.pj2.pj2_projekat_2023.vozila.Vozilo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SpisakIncidenata implements Initializable {

    private static final int BROJ_REDOVA = 6;
    private static final int BROJ_KOLONA = 3;

    private MainInterface mainInterface;

    private static final Object[][] matricaPresli = new Object[BROJ_REDOVA][BROJ_KOLONA];
    private static final Object[][] matricaZadrzani = new Object[BROJ_REDOVA][BROJ_KOLONA];

    @FXML
    private GridPane presliGrid;
    @FXML
    private GridPane zadrzaniGrid;

    private ArrayList<Vozilo> presli = new ArrayList<>();
    private ArrayList<Vozilo> zadrzani = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int col = 0; col < BROJ_KOLONA; col++) {
            for (int row = 0; row < BROJ_REDOVA; row++) {
                int kolona = col, red = row;
                ImageView imageView = new ImageView();
                imageView.setFitHeight(70);
                imageView.setFitWidth(70);
                imageView.setOnMouseClicked(mouseEvent -> imageClick(matricaPresli, kolona, red));
                presliGrid.add(imageView, col, row);
                imageView = new ImageView();
                imageView.setFitHeight(70);
                imageView.setFitWidth(70);
                imageView.setOnMouseClicked(mouseEvent -> imageClick(matricaZadrzani, kolona, red));
                zadrzaniGrid.add(imageView, col, row);
            }
        }
    }
    public void prenesiAtribute(MainInterface mainInterface, List<Vozilo> vozila) {
        this.mainInterface = mainInterface;
        for (Vozilo vozilo : vozila) {
            if (vozilo.isPresaoGranicu()) presli.add(vozilo);
            else zadrzani.add(vozilo);
        }

        int red = 0, kolona = 0;
        for(Vozilo vozilo : presli) {
            matricaPresli[red][kolona] = vozilo;
            kolona = (kolona + 1) % BROJ_KOLONA;
            if(kolona == 0) ++red;
        }

        red = kolona = 0;
        for(Vozilo vozilo : zadrzani) {
            matricaZadrzani[red][kolona] = vozilo;
            kolona = (kolona + 1) % BROJ_KOLONA;
            if(kolona == 0) ++red;
        }

        postaviSlike(presliGrid, matricaPresli);
        postaviSlike(zadrzaniGrid, matricaZadrzani);
    }

    public void postaviSlike(GridPane gridPane, Object[][] matrica) { mainInterface.postaviSlike(gridPane, matrica); }
    public void imageClick(Object[][] matrica, int kolona, int red) { mainInterface.imageClick(matrica, kolona, red); }
}
