package com.mycompany.audiosign;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds(); // This is used to get the maximum screen height and width

        scene = new Scene(loadFXML("MainView"),winSize.width,winSize.height); // Initialze scene with MainView.fxml file
        stage.setScene(scene);
        stage.setMaximized(true); // This is used to maximize the window.
        stage.show(); // Show the UI
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml")); // FXMLLoader is used to load the fxml file
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    } // Launch the app

}