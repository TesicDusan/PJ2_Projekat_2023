package com.pj2.pj2_projekat_2023.kontroleri;

import com.pj2.pj2_projekat_2023.vozila.Vozilo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Kolona implements Initializable {
    private static final int BASE_INTERVAL = 50;
    public static final int BROJ_KOLONA = 7;
    public static final int BROJ_REDOVA = 7;
    private MainInterface mainInterface;
    private static  ArrayList<Vozilo> kolona = new ArrayList<>();
    private static Object[][] matrica = new Object[BROJ_REDOVA][BROJ_KOLONA];

    @FXML
    private GridPane gridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int col = 0; col < BROJ_KOLONA; col++)
            for(int row = 0; row < BROJ_REDOVA; row++) {
                int kolona = col, red = row;
                ImageView imageView = new ImageView();
                imageView.setFitHeight(70);
                imageView.setFitWidth(70);
                imageView.setOnMouseClicked(mouseEvent -> imageClick(matrica, kolona, red));
                gridPane.add(imageView, col, row);
            }

        Timeline gridTimeline = new Timeline(new KeyFrame(Duration.millis(BASE_INTERVAL), event -> { postaviSlike(gridPane, matrica); }));
        gridTimeline.setCycleCount(Timeline.INDEFINITE);
        gridTimeline.play();
    }

    public void prenosAtributa(MainInterface mainInterface) {
        this.mainInterface = mainInterface;
        int i = 0;
        for(Vozilo vozilo : mainInterface.getVozila())
            if(vozilo.nijeUnutarMatrice()) {
                this.kolona.add(vozilo);
                vozilo.setPosX(i%BROJ_KOLONA);
                vozilo.setPosY(i/BROJ_REDOVA);
                matrica[vozilo.getPosY()][vozilo.getPosX()] = vozilo;
                i++;
            }

    }

    public static void pomjeriKolonu() {
        if(matrica[0][0] == null)
            for(Vozilo vozilo : kolona) {
                setMatrica(null, vozilo.getPosY(), vozilo.getPosX());
                if(vozilo.getPosX() == 0) {
                    vozilo.setPosY(vozilo.getPosY()-1);
                    vozilo.setPosX(BROJ_KOLONA - 1);
                }
                else vozilo.setPosX(vozilo.getPosX()-1);
                setMatrica(vozilo, vozilo.getPosY(), vozilo.getPosX());
            }
    }

    public static Vozilo pop() {
        Vozilo vozilo = kolona.get(0);
        setMatrica(null, vozilo.getPosY(), vozilo.getPosX());
        vozilo.setPosY(6);
        vozilo.setPosX(1);
        ukloniIzKolone(vozilo);
        pomjeriKolonu();
        return vozilo;
    }

    public void postaviSlike(GridPane gridPane, Object[][] matrica) { mainInterface.postaviSlike(gridPane, matrica); }
    public void imageClick(Object[][] matrica, int kolona, int red) { mainInterface.imageClick(matrica, kolona, red); }

    public static void setMatrica(Vozilo vozilo, int red, int kolona) { matrica[red][kolona] = vozilo; }
    public static Vozilo getFromMatrica(int red, int kolona) { return (Vozilo) matrica[red][kolona]; }
    public static ArrayList<Vozilo> getKolona() { return kolona; }
    public static void ukloniIzKolone(Vozilo vozilo) { kolona.remove(vozilo); }
}
