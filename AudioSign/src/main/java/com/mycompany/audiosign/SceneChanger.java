package com.mycompany.audiosign;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class SceneChanger {
    private Scene scene;

    public FXMLLoader changeScene(String screenName, Stage stage) {

        Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds(); // This is used to get the maximum screen height and width
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(getClass().getResource(screenName +".fxml")); // Setup the loader with the given screen name
            Parent root = loader.load();
            scene = new Scene(root,winSize.width,winSize.height-23); // To start in maximized view. stage.setMaximized(true) does not work
            // Reduced 23 from height to stop small transition change.
            // Change that value as needed
            stage.setMaximized(true); // To maximize the view
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader;
    }
}
