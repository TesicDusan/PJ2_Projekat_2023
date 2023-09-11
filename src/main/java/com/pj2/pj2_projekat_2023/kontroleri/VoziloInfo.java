package com.pj2.pj2_projekat_2023.kontroleri;

import com.pj2.pj2_projekat_2023.vozila.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class VoziloInfo implements Initializable {
    private Vozilo vozilo;
    private ObservableList<PutnikInfo> putnici = FXCollections.observableArrayList();
    private ObservableList<Incident> incidenti = FXCollections.observableArrayList();
    @FXML
    private ImageView imageView;
    @FXML
    private Text idVozila;
    @FXML
    private Text obradjeno;
    @FXML
    private Text preslo;
    @FXML
    private Text deklMasa;
    @FXML
    private Text stvarnaMasa;
    @FXML
    private Text dokumentacija;
    @FXML
    private Label deklMasaLabel;
    @FXML
    private Label stvarnaMasaLabel;
    @FXML
    private Label dokumentacijaLabel;
    @FXML
    private TableView<PutnikInfo> tabelaInfo;
    @FXML
    private TableColumn<PutnikInfo, Integer> idPutnika;
    @FXML
    private TableColumn<PutnikInfo, String> isprave;
    @FXML
    private TableColumn<PutnikInfo, String> prtljag;
    @FXML
    private TableView<Incident> tabelaIncident;
    @FXML
    private TableColumn<Incident, Integer> putnik;
    @FXML
    private TableColumn<Incident, String> incident;

    public class PutnikInfo {
        private int idBroj;
        private String vazeciId;
        private String prtljag;
        PutnikInfo(Putnik putnik) {
            this.idBroj = putnik.getIdBroj();
            this.vazeciId = putnik.isIdVazeci() ? "Ispravan" : "Neispravan";
            if(putnik.getImaPrtljag()) {
                this.prtljag = putnik.getNedozvoljenPrtljag() ? "Nedozvoljen" : "Uredan";
            } else this.prtljag = "Bez prtljaga";
        }

        public int getIdBroj() { return idBroj; }
        public String getVazeciId() { return vazeciId; }
        public String getPrtljag() { return prtljag; }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageView.setFitWidth(150);
        imageView.setFitWidth(150);

        idPutnika.setCellValueFactory(new PropertyValueFactory<PutnikInfo, Integer>("idBroj"));
        isprave.setCellValueFactory(new PropertyValueFactory<PutnikInfo, String>("vazeciId"));
        prtljag.setCellValueFactory(new PropertyValueFactory<PutnikInfo, String>("prtljag"));
        tabelaInfo.setItems(putnici);

        putnik.setCellValueFactory(new PropertyValueFactory<Incident, Integer>("putnik"));
        incident.setCellValueFactory(new PropertyValueFactory<Incident, String>("opis"));
        tabelaIncident.setItems(incidenti);
    }

    public void setVozilo(Vozilo vozilo) {
        this.vozilo = vozilo;
        imageView.setImage(vozilo.getSlika());

        idVozila.setText(Integer.toString(vozilo.getIdVozila()));
        idVozila.setVisible(true);
        obradjeno.setText(vozilo.isObradjen() ? "Da" : "Ne");
        obradjeno.setVisible(true);
        preslo.setText(vozilo.isPresaoGranicu() ? "Da" : "Ne");
        preslo.setVisible(true);

        if(vozilo instanceof Kamion kamion) {
            deklMasaLabel.setVisible(true);
            stvarnaMasaLabel.setVisible(true);
            dokumentacijaLabel.setVisible(true);
            deklMasa.setText(Integer.toString(kamion.getDeklarisanaMasa()));
            deklMasa.setVisible(true);
            stvarnaMasa.setText(Integer.toString(kamion.getStvarnaMasa()));
            stvarnaMasa.setVisible(true);
            dokumentacija.setText(kamion.getPotrebnaDokumentacija() ? "Potrebna" : "Nije potrebna");
            dokumentacija.setVisible(true);
        }

        if(vozilo instanceof Autobus autobus) {
            prtljag.setVisible(true);
        }
        for(Putnik putnik : vozilo.getPutnici())
            putnici.add(new PutnikInfo(putnik));
        for(Incident incident : vozilo.getIncidenti())
            incidenti.add(incident);

    }

    public Vozilo getVozilo() { return vozilo; }
}
