package com.mycompany.audiosign;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SuccessRegisterController {
    private SceneChanger sceneChanger = new SceneChanger(); // Scene changer is a class that is used to change the scene
    @FXML
    private HBox markAttendanceBox, registerBox,loginBox; // These are used as Navigation buttons

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
    private void homeBtn(ActionEvent event){ // Go to home which is MainView
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        sceneChanger.changeScene("MainView",stage);
    }
}
