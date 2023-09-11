package com.pj2.pj2_projekat_2023;

import com.pj2.pj2_projekat_2023.kontroleri.MainInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    public static Logger LOGGER = Logger.getLogger("LOGGER");
    public static void main(String[] args) {
        try {
            LOGGER.addHandler(new FileHandler("logs.log", false));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
        }
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(MainInterface.class.getResource("MainInterface.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.fillInStackTrace().toString(), e);
        }
    }
}
