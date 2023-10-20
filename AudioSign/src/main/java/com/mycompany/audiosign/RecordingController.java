package com.mycompany.audiosign;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecordingController {
    private SceneChanger sceneChanger = new SceneChanger(); // Scene changer is a class that is used to change the scene
    private RecordAndTranscribe recordAndTranscribe = new RecordAndTranscribe(); // This is used to help record audio and transcribe

    @FXML
    private HBox markAttendanceBox, registerBox,loginBox; // These are used as Navigation buttons
    @FXML
    private AnchorPane pane;
    private final int[] registerNumber = new int[1]; // To store register number
    private Scene scene;
    private Student student; // Used to store student data
    private UserRegisterController userRegisterController;
    private FXMLLoader loader; // This is used to get the controller which can be used to pass arguments between controllers

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

    public void startTranscribe(String line){
        //System.out.println("Reached" + line);
        line = recordAndTranscribe.runCode("STT"); // Runs STT.py file which will transcribe the recording
        registerNumber[0] = Integer.parseInt(line); // The return value of STT.py is user id which is said by the user and recorded
        System.out.println(registerNumber[0]); // Prints the user id
        fetchData(registerNumber[0]); // Fetch the data belongs to that user ID
    }


    public void fetchData(int regNum) {
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
                    changeToSuccessScreen();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeToSuccessScreen() {
        Platform.runLater(()->{
            Stage stage = (Stage)pane.getScene().getWindow();
            if(student.getName().equals("")){
                FXMLLoader loader = sceneChanger.changeScene("FailureTranscribingScreen",stage);
            }else{
                FXMLLoader loader = sceneChanger.changeScene("SuccessScreenRecording",stage);
                SuccessScreenRecordingController successScreenrecordingController = loader.getController(); // Get the controller of the recording screen
                //System.out.println(student);
                successScreenrecordingController.setStudent(student);
            }

        });}

    public void successRecordingScreen() {
        Platform.runLater(()->{ // Set the screen back to user registration screen. This is used after recording voice samples
            Stage stage = (Stage) pane.getScene().getWindow();
            loader = sceneChanger.changeScene("UserRegistration",stage);
            userRegisterController = loader.getController();
            userRegisterController.restoreValues(student);
        });
    }

    public void transferData(Student student) { // Used to help transfering data between two controllers
        this.student = student;
    }
}
