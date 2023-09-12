package com.pj2.pj2_projekat_2023.kontroleri;

import com.pj2.pj2_projekat_2023.Main;
import com.pj2.pj2_projekat_2023.terminali.CarinskiTerminal;
import com.pj2.pj2_projekat_2023.terminali.PolicijskiTerminal;
import com.pj2.pj2_projekat_2023.terminali.Terminal;
import com.pj2.pj2_projekat_2023.vozila.Autobus;
import com.pj2.pj2_projekat_2023.vozila.Automobil;
import com.pj2.pj2_projekat_2023.vozila.Kamion;
import com.pj2.pj2_projekat_2023.vozila.Vozilo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class MainInterface implements Initializable {

    private static final int BASE_INTERVAL = 50;
    Timeline timerTimeline;
    private java.time.Duration vrijeme = java.time.Duration.ZERO;
    private static final ArrayList<Terminal> terminali = new ArrayList<>();
    private static final ArrayList<Vozilo> vozila = new ArrayList<>();

    private static boolean zaustavljeno = true;

    private static final int BROJ_REDOVA = 7;
    private static final int BROJ_KOLONA = 3;
    public static final String SIMULATION_START_TIME;

    private final Object[][] matrica = new Object[BROJ_REDOVA][BROJ_KOLONA];

    @FXML
    private GridPane gridPane;
    @FXML
    private Button pokreniButton;
    @FXML
    private Button zaustaviButton;
    @FXML
    private Button incidentiButton;
    @FXML
    private Button dokumentacijaButton;
    @FXML
    private TextArea textArea;
    @FXML
    private Label timer;

    static {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        SIMULATION_START_TIME = time.format(formatter);
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Generisanje matrice
        for(int col = 0; col < BROJ_KOLONA; col++) {
            for (int row = 0; row < BROJ_REDOVA; row++) {
                if ((col == 1 && row > 0) || (col != 1 && row < 2)) {

                    int kolona = col, red = row;
                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(70);
                    imageView.setFitWidth(70);
                    imageView.setOnMouseClicked(mouseEvent -> imageClick(matrica, kolona, red));
                    gridPane.add(imageView, col, row);

                    //Generisanje terminala
                    if (row == 0) {
                        CarinskiTerminal carinskiTerminal = new CarinskiTerminal(true, col, row, this);
                        terminali.add(carinskiTerminal);
                        matrica[row][col] = carinskiTerminal;
                    }
                    else if (row == 1) {
                        PolicijskiTerminal policijskiTerminal = new PolicijskiTerminal(true, col, row, this);
                        terminali.add(policijskiTerminal);
                        matrica[row][col] = policijskiTerminal;
                    }
                }
            }
        }

        //Generisanje vozila
        generisiKolonu();

        //Postavljanje prvih pet vozila u matricu
        for (int i = 0; i < 5; i++) {
            Vozilo temp = vozila.get(i);
            temp.setUnutarMatrice(true);
            temp.setPosX(1);
            temp.setPosY(i + 2);
            matrica[temp.getPosY()][temp.getPosX()] = temp;
        }

        //Kreiranje Timeline-a za azuriranje tajmera
        timerTimeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), event -> azurirajTimer()));
        timerTimeline.setCycleCount(Timeline.INDEFINITE);

        //Kreiranje Timeline-a za azuriranje grafickog interfejsa
        Timeline gridTimeline = new Timeline(new KeyFrame(Duration.millis(BASE_INTERVAL), event -> postaviSlike(gridPane, matrica)));
        gridTimeline.setCycleCount(Timeline.INDEFINITE);
        gridTimeline.play();

        //Kreiranje novog prozora koji prikazuje ostatak kolone
        setKolona();

        //Pokretanje tredova
        for(Terminal terminal : terminali)
            terminal.start();
        for(int i = 0; i < 5; i++)
           vozila.get(i).start();
    }

    @FXML
    public void setPokreniButton(ActionEvent event) { //Pokretanje izvršavanja simulacije
        if(zaustavljeno) {
            zaustavljeno = false;
            timerTimeline.play();
            synchronized (Vozilo.getLock()) {
                Vozilo.getLock().notifyAll();
            }
            textArea.appendText("Simulacija pokrenuta.\n");
        }
    }

    @FXML
    public void setZaustaviButton(ActionEvent event) { //Zaustavljanje izvršavanje simulacije
        if(!zaustavljeno) {
            zaustavljeno = true;
            timerTimeline.pause();
            synchronized (Vozilo.getLock()) {
                Vozilo.getLock().notifyAll();
            }
            textArea.appendText("Simulacija zaustavljena.\n");
        }
    }

    public void setKolona() {   //Poziva se tokom inicijalizacije
        try {
            FXMLLoader loader = new FXMLLoader(MainInterface.class.getResource("Kolona.fxml"));
            Parent root = loader.load();
            Kolona kolonaController = loader.getController();
            kolonaController.prenosAtributa(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
        }
    }

    @FXML
    public void setIncidentiButton(ActionEvent event) { //Kreiranje novog prozora sa svim incidentima
        try {
            FXMLLoader loader = new FXMLLoader(MainInterface.class.getResource("SpisakIncidenata.fxml"));
            Parent root = loader.load();

            SpisakIncidenata controller = loader.getController();
            List<Vozilo> incidenti = vozila.stream().filter(v -> v.getIncidenti().size() > 0).toList();
            controller.prenesiAtribute(this, incidenti);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
        }

    }

    @FXML
    public void setDokumentacijaButton(ActionEvent event) { //Kreiranje novog prozora sa serijalizovanom dokumentacijom
        try{
            FXMLLoader loader = new FXMLLoader(MainInterface.class.getResource("PregledDokumentacije.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
        }
    }

    public void imageClick(Object[][] matrica, int kolona, int red) { //Metoda koja se poziva klikom na vozilo
        if(matrica[red][kolona] instanceof Vozilo vozilo)
            ucitajInfo(vozilo);
        else if(matrica[red][kolona] instanceof Terminal terminal && !terminal.isSlobodan())
            ucitajInfo(terminal.getVozilo());
    }

    public void ucitajInfo(Vozilo vozilo) { //Otvaranje novog prozora sa informacijama o vozilu
        try {
            FXMLLoader loader = new FXMLLoader(MainInterface.class.getResource("VoziloInfo.fxml"));
            Parent root = loader.load();

            VoziloInfo controller = loader.getController();
            controller.setVozilo(vozilo);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
        }
    }
    public void generisiKolonu() {  //Poziva se tokom inicijalizacije
        for(int i = 0; i < 5; i++)
            vozila.add(new Autobus(this));
        for(int i = 0; i < 8; i++)
            vozila.add(new Kamion(true, this));
        for(int i = 0; i < 2; i++)
            vozila.add(new Kamion(false, this));
        for(int i = 0; i < 35; i++)
            vozila.add(new Automobil(this));
        Vozilo.generisiNevazecaDokumenta();
        Collections.shuffle(vozila);
    }

    public void postaviSlike(GridPane gridPane, Object[][] matrica) {   //Metoda koja postavlja ikone u gridu
        for(Node child: gridPane.getChildren()) {
            if(child instanceof ImageView imageView) {
                int row = GridPane.getRowIndex(child);
                int column = GridPane.getColumnIndex(child);
                    if (matrica[row][column] instanceof Vozilo vozilo)
                        imageView.setImage(vozilo.getSlika());
                    else if(matrica[row][column] instanceof Terminal terminal) {
                        if(terminal.getVozilo() != null) imageView.setImage(terminal.getVozilo().getSlika());
                        else imageView.setImage(terminal.getSlika());
                    }
                    else imageView.setImage(null);
            }
        }
    }

    public void  azurirajTimer () { //Metoda koja azurira tajmer
        vrijeme = vrijeme.plus(java.time.Duration.ofSeconds(1));
        String vrijemeTekst = formatirajVrijeme(vrijeme);
        timer.setText(vrijemeTekst);
    }

    public String formatirajVrijeme (java.time.Duration duration) {
        double sati = duration.toHours();
        double minute = duration.toMinutesPart();
        double sekunde = duration.toSecondsPart();

        DecimalFormat format = new DecimalFormat("00");
        String formatiranSat = format.format(sati);
        String formatiranMinut = format.format(minute);
        String formatiranSekund = format.format(sekunde);

        return String.format(" %s:%s:%s", formatiranSat,  formatiranMinut, formatiranSekund);
    }

    //Seteri, geteri i druge pomoćne metode
    public void setMatrica(Object object, int row, int column) { matrica[row][column] = object; }
    public void appendTextArea(String string) { textArea.appendText(string); }

    public boolean getZaustavljeno() { return zaustavljeno; }
    public Timeline getTimerTimeline() { return timerTimeline; }
    public Object getFromMatrica(int row, int column) { return matrica[row][column]; }
    public ArrayList<Terminal> getTerminali() { return terminali; }
    public ArrayList<Vozilo> getVozila() { return vozila; }
}
