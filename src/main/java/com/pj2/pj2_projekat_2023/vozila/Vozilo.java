package com.pj2.pj2_projekat_2023.vozila;

import com.pj2.pj2_projekat_2023.Main;
import com.pj2.pj2_projekat_2023.kontroleri.Kolona;
import com.pj2.pj2_projekat_2023.kontroleri.MainInterface;
import com.pj2.pj2_projekat_2023.terminali.CarinskiTerminal;
import com.pj2.pj2_projekat_2023.terminali.PolicijskiTerminal;
import com.pj2.pj2_projekat_2023.terminali.Terminal;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

public abstract class Vozilo extends Thread {
    private static final Object LOCK = new Object();
    private static int globalIdVozila = 0;
    private static ArrayList<Putnik> sviPutnici = new ArrayList<>();
    protected MainInterface mainInterface;
    protected ArrayList<Putnik> putnici = new ArrayList<>();
    protected ArrayList<PolicijskiTerminal> terminaliP = new ArrayList<>();
    protected CarinskiTerminal terminalC;
    private Terminal terminal = null;
    protected int brojPutnika;
    protected int idVozila;
    protected boolean suspendovano = false;
    protected boolean obradjen = false;
    private boolean presaoGranicu = false;
    protected boolean unutarMatrice = false;
    protected boolean policijskiProvjereno = false;
    protected boolean carinskiProvjereno = false;
    protected ArrayList<Incident> incidenti = new ArrayList<>();

    protected Image slika;
    protected int posX, posY;


    public Vozilo(int maxPutnika, MainInterface mainInterface) {
        this.mainInterface = mainInterface;
        Random random = new Random();
        this.brojPutnika = random.nextInt(1, maxPutnika+1);
        for(int i = 0; i < brojPutnika; i++) {
            Putnik putnik = new Putnik();
            putnici.add(putnik);
            sviPutnici.add(putnik);
            if (i == 0) putnik.setVozac(true);
        }
        this.idVozila = ++globalIdVozila;
    }

    @Override
    public void run() {
        while(!obradjen && !suspendovano) {
            while(mainInterface.getZaustavljeno()) {
                synchronized (LOCK) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
                    }
                    LOCK.notifyAll();
                }
            }

            synchronized (LOCK) {
                if(posX == 1 && posY > 2 && mainInterface.getFromMatrica(posY - 1, posX) == null) {
                    if(posY == 6 && Kolona.getKolona().size() > 0) {
                        Vozilo vozilo = Kolona.pop();
                        mainInterface.setMatrica(vozilo, posY--, posX);
                        vozilo.setUnutarMatrice(true);
                        vozilo.start();
                    }
                    else mainInterface.setMatrica(null, posY--, posX);
                    mainInterface.setMatrica(this, posY, posX);
                    goToSleep();
                    LOCK.notifyAll();
                }
                else if(posX == 1 && posY == 2) {
                    predjiNaTerminalP();
                    goToSleep();
                    LOCK.notifyAll();
                }
                else if(posY == 1 && policijskiProvjereno && !suspendovano) {
                    if (terminal != null) {
                        terminal.setVozilo(null);
                        terminal.setSlobodan(true);
                        terminal = null;
                        predjiNaTerminalC();
                        goToSleep();
                        LOCK.notifyAll();
                    }
                }
                else try {
                    LOCK.wait(250);
                } catch (InterruptedException e) {
                    Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
                }
                LOCK.notifyAll();
            }
        }
    }

    public void ukloniPutnika(Putnik putnik) {
        putnici.remove(putnik);
    }

    public void predjiNaTerminalP() {
        if(predjiNaTerminal(terminaliP.get(0)))
            return;
        else if(terminaliP.size() > 1) predjiNaTerminal(terminaliP.get(1));
    }

    public void predjiNaTerminalC() {
        predjiNaTerminal(terminalC);
    }

    public boolean predjiNaTerminal(Terminal terminal) {
        if(terminal.isAktivan() && terminal.isSlobodan()) {
            this.terminal = terminal;
            terminal.setVozilo(this);
            if(mainInterface.getFromMatrica(posY,posX) instanceof Vozilo)mainInterface.setMatrica(null, posY, posX);
            this.posY = terminal.getPosY();
            this.posX = terminal.getPosX();
            terminal.setSlobodan(false);
            return true;
        }
        else return false;
    }

    public static void generisiNevazecaDokumenta () {
        Random random  = new Random();
        int broj = sviPutnici.size() * 3 / 100;
        int i = 0;
        while(i < broj) {
            Putnik putnik = sviPutnici.get(random.nextInt(sviPutnici.size()));
            if(putnik.isIdVazeci()) {
                putnik.setIdVazeci(false);
                i++;
            }
        }
    }

    public void setUnutarMatrice(boolean unutarMatrice) { this.unutarMatrice = unutarMatrice; }
    public void setPosX(int posX) { this.posX = posX; }
    public void setPosY(int posY) { this.posY = posY; }
    public void setObradjen(boolean obradjen) { this.obradjen = obradjen; }
    public void setPresaoGranicu(boolean presaoGranicu) { this.presaoGranicu = presaoGranicu; }
    public void setSuspendovano(boolean suspendovano) { this.suspendovano = suspendovano; }
    public void setPolicijskiProvjereno(boolean policijskiProvjereno) { this.policijskiProvjereno = policijskiProvjereno; }
    public void setCarinskiProvjereno(boolean carinskiProvjereno) { this.carinskiProvjereno = carinskiProvjereno; }
    public void setTerminal(Terminal terminal) { this.terminal = terminal; }
    public void addIncident(Incident incident) { this.incidenti.add(incident); }

    public static Object getLock() { return LOCK; }
    public ArrayList<Putnik> getPutnici() { return putnici; }
    public int getIdVozila() { return idVozila; }
    public int getPosX() { return posX; }
    public int getPosY() { return posY; }
    public boolean isObradjen() { return obradjen; }
    public boolean isPresaoGranicu() { return presaoGranicu; }
    public boolean nijeUnutarMatrice() { return !unutarMatrice; }
    public Image getSlika() { return slika; }
    public boolean isSuspendovano() { return suspendovano; }
    public boolean isCarinskiProvjereno() { return carinskiProvjereno; }
    public boolean isPolicijskiProvjereno() { return policijskiProvjereno; }
    public ArrayList<Incident> getIncidenti() { return incidenti; }

    public void goToSleep() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
        }
    }
}
