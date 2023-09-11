package com.pj2.pj2_projekat_2023.vozila;

import com.pj2.pj2_projekat_2023.kontroleri.MainInterface;
import com.pj2.pj2_projekat_2023.terminali.CarinskiTerminal;
import com.pj2.pj2_projekat_2023.terminali.PolicijskiTerminal;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.List;
import java.util.Random;

public class Autobus extends Vozilo{
    private static final String IMAGE_PATH ="bus.png";
    private static final int MAX_BROJ_PUTNIKA = 52;
    private int brojKofera = 0;

    public Autobus(MainInterface mainInterface) {
        super(MAX_BROJ_PUTNIKA, mainInterface);
        terminaliP.add((PolicijskiTerminal) mainInterface.getTerminali().get(1));
        terminaliP.add((PolicijskiTerminal) mainInterface.getTerminali().get(2));
        terminalC = (CarinskiTerminal) mainInterface.getTerminali().get(0);
        URL resource = Autobus.class.getResource(IMAGE_PATH);
        slika = new Image(resource.toString());
        Random random = new Random();
        for(Putnik putnik : putnici) {
            if(random.nextInt(1, 11) <= 7) {
                putnik.setImaPrtljag(true);
                brojKofera++;
            }
        }
        List<Putnik> putniciSaPrtljagom = filterPutnici();
        int brojNedozvoljenih = (int) Math.round((double) brojKofera * 0.1);
        for(int i = 0; i < brojNedozvoljenih; i++) {
            Putnik putnik = putniciSaPrtljagom.get(random.nextInt(brojKofera));
            putnik.setNedozvoljenPrtljag(true);
        }
    }

    public List<Putnik> filterPutnici() { return putnici.stream().filter(p -> p.getImaPrtljag()).toList(); }
}
