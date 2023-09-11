package com.pj2.pj2_projekat_2023.vozila;

import com.pj2.pj2_projekat_2023.kontroleri.MainInterface;
import com.pj2.pj2_projekat_2023.terminali.CarinskiTerminal;
import com.pj2.pj2_projekat_2023.terminali.PolicijskiTerminal;
import javafx.scene.image.Image;

import java.net.URL;

public class Automobil extends Vozilo {
    private static final String IMAGE_PATH = "auto.png";
    private static final int MAX_BROJ_PUTNIKA = 5;
    public Automobil(MainInterface mainInterface) {

        super(MAX_BROJ_PUTNIKA, mainInterface);
        terminaliP.add((PolicijskiTerminal) mainInterface.getTerminali().get(1));
        terminaliP.add((PolicijskiTerminal) mainInterface.getTerminali().get(2));
        terminalC = (CarinskiTerminal) mainInterface.getTerminali().get(0);
        URL resource = Automobil.class.getResource(IMAGE_PATH);
        slika = new Image(resource.toString());
    }
}
