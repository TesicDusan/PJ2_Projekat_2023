package com.pj2.pj2_projekat_2023.kontroleri;

import com.pj2.pj2_projekat_2023.dokumentacija.PolicijskaDokumentacija;
import com.pj2.pj2_projekat_2023.vozila.Incident;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PregledDokumentacije implements Initializable {
    private static ArrayList<Incident> incidenti;
    @FXML
    private TextArea textArea;

    @Override
    @FXML
    public void initialize (URL url, ResourceBundle resourceBundle) {
        incidenti = (ArrayList<Incident>) PolicijskaDokumentacija.ucitajIzFajla();
        for(Incident incident : incidenti)
            textArea.appendText(incident.toString());
    }
}
