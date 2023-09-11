package com.pj2.pj2_projekat_2023.vozila;

import com.pj2.pj2_projekat_2023.terminali.Terminal;

import java.io.Serializable;

public class Incident  implements Serializable {
    private final transient Terminal terminal;
    private final int putnik;
    private final int vozilo;
    private final String opis;

    public Incident(Terminal terminal, Vozilo vozilo, Putnik putnik, String opis) {
        this.terminal = terminal;
        this.vozilo = vozilo.getIdVozila();
        this.putnik = putnik.getIdBroj();
        this.opis = opis;
    }

    public int getPutnik() { return putnik; }
    public String getOpis() { return opis; }
    public int getVozilo() { return vozilo; }
    public Terminal getTerminal() { return terminal; }

    @Override
    public String toString() {
        return "Vozilo: " + vozilo + "\n" +
               "Putnik: " + putnik + "\n" +
               "Opis: " + opis + "\n\n";
    }
}
