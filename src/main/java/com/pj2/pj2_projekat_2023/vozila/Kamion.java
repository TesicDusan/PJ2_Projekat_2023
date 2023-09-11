package com.pj2.pj2_projekat_2023.vozila;

import com.pj2.pj2_projekat_2023.kontroleri.MainInterface;
import com.pj2.pj2_projekat_2023.terminali.CarinskiTerminal;
import com.pj2.pj2_projekat_2023.terminali.PolicijskiTerminal;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.Random;

public class Kamion extends Vozilo{
    private static final String IMAGE_PATH = "kamion.png";
    private static final int MAX_BROJ_PUTNIKA = 3;
    private static final int MIN_MASA = 1000;
    private static final int MAX_MASA = 10000;
    private final int deklarisanaMasa;
    private final int stvarnaMasa;
    private final boolean potrebnaDokumentacija;

    public Kamion(boolean jednakaMasa, MainInterface mainInterface) {
        super(MAX_BROJ_PUTNIKA, mainInterface);
        terminaliP.add((PolicijskiTerminal) mainInterface.getTerminali().get(4));
        terminalC = (CarinskiTerminal) mainInterface.getTerminali().get(3);
        URL resource = Kamion.class.getResource(IMAGE_PATH);
        slika = new Image(resource.toString());
        Random random = new Random();
        potrebnaDokumentacija = random.nextInt(1, 11) > 5;
        deklarisanaMasa = random.nextInt(MIN_MASA, MAX_MASA);
        if(jednakaMasa)
            stvarnaMasa = deklarisanaMasa;
        else stvarnaMasa = deklarisanaMasa + random.nextInt((int)Math.round((double)deklarisanaMasa*0.3));
    }

    public void generisiDokumentaciju() {
        mainInterface.appendTextArea("Kamion " + idVozila + " kreira dokumentaciju na carisnkom terminalu.\n");
    }

    public boolean getPotrebnaDokumentacija() { return potrebnaDokumentacija; }
    public int getDeklarisanaMasa() { return deklarisanaMasa; }
    public int getStvarnaMasa() { return stvarnaMasa; }
}

