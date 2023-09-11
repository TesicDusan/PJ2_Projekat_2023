package com.pj2.pj2_projekat_2023.dokumentacija;

import com.pj2.pj2_projekat_2023.Main;
import com.pj2.pj2_projekat_2023.kontroleri.MainInterface;
import com.pj2.pj2_projekat_2023.vozila.Incident;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;

public abstract class CarinskaDokumentacija {
    private static Path FILE_PATH = Paths.get("src", "main", "resources","com", "pj2", "pj2_projekat_2023", "dokumentacija",
            "CarinskaDokumentacija_" + MainInterface.SIMULATION_START_TIME);

    public static void upisiUFajl(Incident incident) {
        try {
            if(!Files.exists(FILE_PATH)) {
                Files.createFile(FILE_PATH);
            }
            Files.writeString(FILE_PATH, incident.toString() + System.lineSeparator(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            Main.LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
        }
    }
}
