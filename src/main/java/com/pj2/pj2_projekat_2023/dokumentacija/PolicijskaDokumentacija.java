package com.pj2.pj2_projekat_2023.dokumentacija;

import com.pj2.pj2_projekat_2023.Main;
import com.pj2.pj2_projekat_2023.kontroleri.MainInterface;
import com.pj2.pj2_projekat_2023.vozila.Incident;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public abstract class PolicijskaDokumentacija {
    private static Path FILE_PATH = Paths.get("src", "main", "resources","com", "pj2", "pj2_projekat_2023", "dokumentacija",
            "PolicijskaDokumentacija_" + MainInterface.SIMULATION_START_TIME);

    public static void generisiDokumentaciju(Incident incident) {
        List<Incident> incidentList = ucitajIzFajla();
        incidentList.add(incident);
        sacuvajFajl(incidentList);
    }

    public static List<Incident> ucitajIzFajla() {
        if(!Files.exists(FILE_PATH)) {
            try {
                Files.createFile(FILE_PATH);
            } catch (IOException e) {
                Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
            }
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(FILE_PATH))) {
            return (List<Incident>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
        }
        return new ArrayList<>();
    }

    private static void sacuvajFajl(List<Incident> incidentList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(FILE_PATH))) {
            oos.writeObject(incidentList);
        } catch (IOException e) {
            Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
        }
    }
}
