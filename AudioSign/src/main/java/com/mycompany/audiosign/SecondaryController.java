package com.mycompany.audiosign;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SecondaryController {

    @FXML
    /*private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    } */

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField NameTextField;

    @FXML
    private TextField RegisterNumberTextField;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    public void back(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void submit(ActionEvent event) {
        // Database connection parameters
        String url = "jdbc:sqlite:C:\\sqlite:voice.db"; // Change the URL to your database file
        Connection conn = null;

        try {
            // Create a connection to the database
            conn = DriverManager.getConnection(url);

            // SQL statement to create the "student" table if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS student ("
                    + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "Name TEXT,"
                    + "RegisterNumber TEXT,"
                    + "Gender TEXT"
                    + ")";

            // Execute the SQL statement to create the table
            conn.createStatement().executeUpdate(createTableSQL);

            // Get user input from your JavaFX fields
            String name = NameTextField.getText();
            String registerNumber = RegisterNumberTextField.getText();
            String gender = maleRadioButton.isSelected() ? "Male" : "Female"; // Assuming only male and female options


            // SQL statement to insert user input into the "student" table
            String insertSQL = "INSERT INTO student (Name, RegisterNumber, Gender) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1, name);
            pstmt.setString(2, registerNumber);
            pstmt.setString(3, gender);

            // Execute the insert statement
            pstmt.executeUpdate();



            // Close the prepared statement
            pstmt.close();

            // Optionally, show a confirmation message to the user
            // You can add your logic here to inform the user about successful submission
            // For example, display a message or reset the input fields

            showAlert();

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database errors or validation errors here
        } finally {
            try {
                if (conn != null) {

                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Helper method to show an alert
    private void showAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Data has been successfully submitted.");
        alert.showAndWait();
    }





}