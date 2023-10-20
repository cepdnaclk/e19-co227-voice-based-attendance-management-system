package com.mycompany.audiosign;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.gluonhq.charm.glisten.control.TextField;
import java.awt.*;
import java.sql.*;

public class UserLoginController {
    private SceneChanger sceneChanger = new SceneChanger(); // Scene changer is a class that is used to change the scene
    @FXML
    private HBox markAttendanceBox, registerBox,loginBox; // These are used as Navigation buttons

    @FXML
    private TextField userName;

    @FXML
    private PasswordField passwordField;
    @FXML
    private AnchorPane pane;
    @FXML
    private Text recheckText;


    @FXML
    private void markAttendance() { // Change the screen to Mark Attendance View ( MainView)
        Stage stage = (Stage) markAttendanceBox.getScene().getWindow();
        sceneChanger.changeScene("MainView",stage);
    }
    @FXML
    private void userRegister() { // Change the scene to User Registration screen
        Stage stage = (Stage) registerBox.getScene().getWindow();
        sceneChanger.changeScene("UserRegistration",stage);
    }
    
    @FXML
    private void userLogin() { // Change the scene to User Login screen
        Stage stage = (Stage) loginBox.getScene().getWindow();
        sceneChanger.changeScene("UserLogin",stage);
    }
    @FXML
    private void backBtn(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        sceneChanger.changeScene("MainView",stage);
    }
    @FXML
    private void loginBtn(ActionEvent event) {
        // Display a admin UI
        // Maybe analysis?
        String adminID = userName.getText();
        String password = "";

        String workingDir = System.getProperty("user.dir"); // Working directory path
        String path = workingDir + "\\src\\main\\res\\database\\admin.db"; // Absolute path for the database
        String url = "jdbc:sqlite:" + path; // url for the database
        //System.out.println(url);
        String username = "";
        String pass = "";
        // Make the connection and query
        try (Connection connection = DriverManager.getConnection(url, username, pass);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT Password FROM AdminInfo WHERE UserName=?")) { // SQL Statement

            preparedStatement.setString(1, adminID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                {
                    // Fetch values
                    while (resultSet.next()) {
                        password = resultSet.getString("Password");
                    }
                    // Print data
                    //System.out.println(password);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(passwordField.getText().equals(password)){
            displayAdminPanel();
            recheckText.setVisible(false);
        }else{
            recheckText.setVisible(true);
        }
    }

    private void displayAdminPanel() {
        Stage stage = (Stage) pane.getScene().getWindow();
        sceneChanger.changeScene("AdminPanel",stage);
    }
}
