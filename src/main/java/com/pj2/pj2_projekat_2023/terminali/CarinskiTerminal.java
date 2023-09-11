package com.pj2.pj2_projekat_2023.terminali;

import com.pj2.pj2_projekat_2023.Main;
import com.pj2.pj2_projekat_2023.dokumentacija.CarinskaDokumentacija;
import com.pj2.pj2_projekat_2023.kontroleri.MainInterface;
import com.pj2.pj2_projekat_2023.vozila.*;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.List;
import java.util.logging.Level;

public class CarinskiTerminal extends Terminal {
    private static final String IMAGE_PATH = "carina.png";
    public CarinskiTerminal(boolean aktivan, int x, int y, MainInterface mainInterface) {

        super(aktivan, x, y, mainInterface);
        URL resource = CarinskiTerminal.class.getResource(IMAGE_PATH);
        slika = new Image(resource.toString());
    }

    @Override
    public void obradiVozilo() {
        if(!vozilo.isCarinskiProvjereno()) {
            mainInterface.appendTextArea("Počinje carinska kontrola vozila " + vozilo.getIdVozila() + "\n");
            if (vozilo instanceof Autobus autobus) {
                List<Putnik> putnici = autobus.filterPutnici();
                for (int i = putnici.size()-1; i >= 0; i--) {
                    Putnik putnik = putnici.get(i);
                    if (putnik.getNedozvoljenPrtljag()) {
                        mainInterface.appendTextArea("Putnik " + putnik.getIdBroj() + " je suspendovan na carini zbog nedozvoljenog prtljaga.\n");
                        autobus.addIncident(new Incident(this, vozilo, putnik, "Putnik " + putnik.getIdBroj() + " je suspendovan na carini zbog nedozvoljenog prtljaga."));
                        if (putnik.isVozac()) autobus.setSuspendovano(true);
                        else autobus.ukloniPutnika(putnik);
                    }
                }
            } else if (vozilo instanceof Kamion kamion) {
                if (kamion.getPotrebnaDokumentacija()) kamion.generisiDokumentaciju();
                if (kamion.getStvarnaMasa() > kamion.getDeklarisanaMasa()) {
                    mainInterface.appendTextArea("Kamion " + vozilo.getIdVozila() + " je suspendovan na carini zbog preopterećenja.\n");
                    kamion.setSuspendovano(true);
                    kamion.addIncident(new Incident(this, kamion, kamion.getPutnici().get(0), "Kamion " + vozilo.getIdVozila() + " je suspendovan na carini zbog preopterećenja."));
                }
            } else if (vozilo instanceof Automobil) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
                }
            }
            mainInterface.appendTextArea("Vozilo " + vozilo.getIdVozila() + " je prošlo carinsku kontrolu.\n");
            vozilo.setObradjen(true);
            vozilo.setCarinskiProvjereno(true);
            vozilo.setPresaoGranicu(!vozilo.isSuspendovano());
            vozilo.setUnutarMatrice(false);
            vozilo.setTerminal(null);
            for(Incident incident : vozilo.getIncidenti())
                if(incident.getTerminal() instanceof CarinskiTerminal)CarinskaDokumentacija.upisiUFajl(incident);
            setVozilo(null);
            setSlobodan(true);
        }
    }
}
