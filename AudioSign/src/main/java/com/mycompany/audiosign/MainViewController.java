package com.mycompany.audiosign;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class MainViewController {

    private SceneChanger sceneChanger = new SceneChanger(); // Scene changer is a class that is used to change the scene
    private RecordAndTranscribe recordAndTranscribe = new RecordAndTranscribe(); // This is used to help record audio and transcribe
    @FXML
    private HBox markAttendanceBox, registerBox,loginBox; // These are used as Navigation buttons
    @FXML
    private AnchorPane recordBox;

    private FXMLLoader loader; // This is used to get the controller which can be used to pass arguments between controllers
    private RecordingController recordingController; // Controller for recording screen. Used to pass arguments

    private Thread t1;


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
    private void recordVoice() throws InterruptedException {
        t1= new Thread(()->{ // Done in a different thread
            String line = recordAndTranscribe.runCode("recordTest"); // This code will execute recordTest.py code. It will output 1 if everything worked properly
            if (line.equals("1")) {
                //changeToSuccessScreen();
                startTranscribe(line); // If recording was okay, Then start transcribing the recording
            }
        });
        t1.start();
        TimeUnit.MILLISECONDS.sleep(700); // Wait 0.7 second for python modules to loadup
        Stage stage = (Stage) recordBox.getScene().getWindow();
        loader = sceneChanger.changeScene("RecordingScreen",stage); // Display the recording screen
        //System.out.println(loader);
    }

    private void startTranscribe(String line) {
        //System.out.println(loader);
        recordingController = loader.getController(); // Get the controller of the recording screen
        recordingController.startTranscribe(line);

    }

}
