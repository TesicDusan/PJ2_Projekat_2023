package com.pj2.pj2_projekat_2023.terminali;

import com.pj2.pj2_projekat_2023.Main;
import com.pj2.pj2_projekat_2023.kontroleri.MainInterface;
import com.pj2.pj2_projekat_2023.vozila.Vozilo;
import javafx.scene.image.Image;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;

public abstract class Terminal extends Thread {

    protected MainInterface mainInterface;
    private static int globalId = 0;
    protected int id;
    protected boolean aktivan;
    protected boolean slobodan = true;
    protected Image slika;
    protected int posX, posY;
    protected Vozilo vozilo = null;

    public Terminal(boolean aktivan, int x, int y, MainInterface mainInterface) {

        this.mainInterface = mainInterface;
        this.id = globalId++;
        this.aktivan = aktivan;
        this.posX = x;
        this.posY = y;
    }

    @Override
    public void run() {
        while(true) {
            while (mainInterface.getZaustavljeno()) {
                synchronized (Vozilo.getLock()) {
                    try {
                        Vozilo.getLock().wait();
                    } catch (InterruptedException e) {
                        Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
                    }
                    Vozilo.getLock().notifyAll();
                }
            }
            synchronized (Vozilo.getLock()) {
                aktivan = provjeriAktivnost();
                if(this.vozilo != null) obradiVozilo();
                else try {
                    Vozilo.getLock().wait(250);
                } catch (InterruptedException e) {
                    Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
                }
                Vozilo.getLock().notifyAll();
            }
        }
    }

    public boolean provjeriAktivnost() {
            try {
                String resource = "src/main/resources/com/pj2/pj2_projekat_2023/terminali/TerminalConfig";
                List<String> stringList = Files.readAllLines(Path.of(resource));
                String string = stringList.get(id);
                return Boolean.parseBoolean(string.trim());
            } catch (IOException e) {
                Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
                return false;
            }
    }

    public void obradiVozilo() {
    }

    public void setVozilo(Vozilo vozilo) { this.vozilo = vozilo; }
    public void setSlobodan(boolean slobodan) { this.slobodan = slobodan; }

    public Image getSlika() { return slika; }
    public Vozilo getVozilo() { return vozilo; }

    public boolean isAktivan() { return aktivan; }
    public boolean isSlobodan() { return slobodan; }
    public int getPosX() { return posX; }
    public int getPosY() { return posY; }
}
