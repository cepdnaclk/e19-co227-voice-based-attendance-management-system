package com.mycompany.audiosign;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.concurrent.TimeUnit;

public class FailureTranscribingScreenController {
    private SceneChanger sceneChanger = new SceneChanger(); // Scene changer is a class that is used to change the scene 
    private RecordAndTranscribe recordAndTranscribe = new RecordAndTranscribe(); // This is used to help record audio and transcribe
    @FXML
    private HBox markAttendanceBox, registerBox,loginBox; // These are used as Navigation buttons

    private FXMLLoader loader; // This is used to get the controller which can be used to pass arguments between controllers

    private RecordingController recordingController; // Controller for recording screen. Used to pass arguments

    private Thread t1;
    @FXML
    private TextField idTextField; // User can enter the User ID if the system fails to transcribe it properly
    private Student student; // Used to store student data
    @FXML
    private AnchorPane pane;

    @FXML
    private Text invalidIDText;

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
    public void recordAgainBtn(javafx.event.ActionEvent event) throws InterruptedException { // When the system fails to transcribe, user can retry
        t1= new Thread(()->{ // Done in a different thread
            String line = recordAndTranscribe.runCode("recordTest"); // This code will execute recordTest.py code. It will output 1 if everything worked properly
            if (line.equals("1")) {
                //changeToSuccessScreen();
                startTranscribe(line); // If recording was okay, Then start transcribing the recording
            }
        });
        t1.start();
        //TimeUnit.MILLISECONDS.sleep(700); // Wait 0.7 second for python modules to loadup
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loader = sceneChanger.changeScene("RecordingScreen",stage); // Display the recording screen 
        //System.out.println(loader);
    }


    private void startTranscribe(String line) {
        //System.out.println(loader);
        recordingController = loader.getController(); // Get the controller of the recording screen
        recordingController.startTranscribe(line); // Run the startTranscribe method inside recording controller
    }
    @FXML
    public void submitIDBtn(javafx.event.ActionEvent event){
        try{
            fetchData(Integer.parseInt(idTextField.getText())); // Fetch the data from the database

        }catch (NumberFormatException e){
            invalidIDText.setVisible(true);
        }
    }

    public void fetchData(int regNum){
        student = new Student();
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT Name, SampleFile FROM student WHERE RegisterNumber=?")) { // SQL Statement 
            preparedStatement.setInt(1,regNum); // Set the 1st unknown to regNum
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                {
                    String name = "";
                    String fileLocation = "";
                    // Fetch values
                    while (resultSet.next()) {
                        name = resultSet.getString("Name");
                        fileLocation = resultSet.getString("SampleFile");
                    }
                    // Print data
                    //nameTextBox.setText(name);
                    //idTextBox.setText(regNum+"");
                    //System.out.println(fileLocation);
                    //System.out.println(name);
                    //System.out.println(regNum);
                    student.setRegNum(regNum); // Set the student ID
                    student.setName(name); // Set the student name
                    student.setFileLocation(fileLocation); // Set the file location of the audio sample
                    if(!(name.isEmpty() && fileLocation.isEmpty())){
                        changeToConfirmationScreen();
                    }else{
                        invalidIDText.setVisible(true);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void changeToConfirmationScreen() { // Change the screen to User Confirmation Screen
        Platform.runLater(()->{
            Stage stage = (Stage) pane.getScene().getWindow();
            FXMLLoader loader = sceneChanger.changeScene("UserConfirmation",stage);
            UserConfirmationController userConfirmationController = loader.getController();
            userConfirmationController.setStudent(student);
            userConfirmationController.setData();
        });
    }
}
