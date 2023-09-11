package com.pj2.pj2_projekat_2023.vozila;

public class Putnik {
    private static int globalIdPutnika = 0;
    private int idBroj;
    private boolean vozac = false;
    private boolean idVazeci = true;
    private boolean imaPrtljag = false;
    private boolean nedozvoljenPrtljag = false;

    public Putnik(){
        idBroj = ++globalIdPutnika;
    }

    public void setIdVazeci(boolean idVazeci) { this.idVazeci = idVazeci; }
    public void setVozac(boolean vozac) { this.vozac = vozac; }
    public void setImaPrtljag(boolean imaPrtljag) { this.imaPrtljag = imaPrtljag; }
    public void setNedozvoljenPrtljag(boolean nedozvoljenPrtljag) { this.nedozvoljenPrtljag = nedozvoljenPrtljag; }

    public int getIdBroj() { return idBroj; }
    public boolean getImaPrtljag() { return imaPrtljag; }
    public boolean getNedozvoljenPrtljag() { return nedozvoljenPrtljag; }
    public boolean isIdVazeci() { return idVazeci; }
    public boolean isVozac() { return vozac; }
}
