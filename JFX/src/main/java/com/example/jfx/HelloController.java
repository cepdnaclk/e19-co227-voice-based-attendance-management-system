package com.example.jfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    // private Label welcomeText;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    /*protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to Our System..!");
    } */

    public void login(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("login-page.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}



