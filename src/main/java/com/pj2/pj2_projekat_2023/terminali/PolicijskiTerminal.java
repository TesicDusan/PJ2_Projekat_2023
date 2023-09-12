package com.pj2.pj2_projekat_2023.terminali;

import com.pj2.pj2_projekat_2023.Main;
import com.pj2.pj2_projekat_2023.kontroleri.MainInterface;
import com.pj2.pj2_projekat_2023.vozila.Autobus;
import com.pj2.pj2_projekat_2023.vozila.Incident;
import com.pj2.pj2_projekat_2023.vozila.Putnik;
import com.pj2.pj2_projekat_2023.dokumentacija.PolicijskaDokumentacija;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.logging.Level;

public class PolicijskiTerminal extends Terminal {
    private static final String IMAGE_PATH = "policija.png";
    public PolicijskiTerminal(boolean aktivan, int x, int y, MainInterface mainInterface) {

        super(aktivan, x, y, mainInterface);
        URL resource = PolicijskiTerminal.class.getResource(IMAGE_PATH);
        slika = new Image(resource.toString());
    }

    @Override
    public void obradiVozilo() {
        if(!vozilo.isPolicijskiProvjereno()) {
            try {
                mainInterface.appendTextArea("Počinje policijska kontrola vozila " + this.vozilo.getIdVozila() + "\n");
                for (int i = 0; i < vozilo.getPutnici().size(); i++) {
                    Putnik putnik = vozilo.getPutnici().get(i);
                    if (!putnik.isIdVazeci()) {
                        mainInterface.appendTextArea("Putnik " + putnik.getIdBroj() + " je suspendovan zbog nevažećih dokumenata.\n");
                        vozilo.addIncident(new Incident(this, vozilo, putnik,"Putnik " + putnik.getIdBroj() + " je suspendovan zbog nevažećih dokumenata."));
                        if (putnik.isVozac()) {
                            vozilo.setSuspendovano(true);
                            break;
                        }
                        else vozilo.ukloniPutnika(putnik);
                    }
                    if (vozilo instanceof Autobus)
                        sleep(100);
                    else sleep(500);
                }
                vozilo.setPolicijskiProvjereno(true);
                mainInterface.appendTextArea("Vozilo " + vozilo.getIdVozila() + " je prošlo policijsku kontrolu.\n");
            } catch (InterruptedException e) {
                Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
            }
            for(Incident incident : vozilo.getIncidenti())
                if(incident.getTerminal() instanceof PolicijskiTerminal)PolicijskaDokumentacija.generisiDokumentaciju(incident);
            if(vozilo.isSuspendovano()) {
                vozilo.setObradjen(true);
                if(mainInterface.getVozila().indexOf(vozilo) == mainInterface.getVozila().size()-1)
                    mainInterface.getTimerTimeline().stop();
                this.setVozilo(null);
                this.setSlobodan(true);
            }
        }
    }
}
