package com.mycompany.audiosign;

import com.gluonhq.charm.glisten.control.TextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
//import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//import java.util.concurrent.TimeUnit;

public class UserRegisterController {
    private SceneChanger sceneChanger = new SceneChanger(); // Scene changer is a class that is used to change the scene
    @FXML
    private HBox markAttendanceBox, registerBox,loginBox,addVoiceSamplesBox; // These are used as Navigation buttons and addVoiceSamplesBox is used to add voice samples

    private Student student; // Used to store student data

    private RecordAndTranscribe recordAndTranscribe = new RecordAndTranscribe(); // This is used to help record audio and transcribe

    private FXMLLoader loader;  // This is used to get the controller which can be used to pass arguments between controllers

    private Thread t1;
    private  RecordingController recordingController;  // Controller for recording screen. Used to pass arguments

    @FXML
    private Text nameRequired, idRequired, emailRequired, genderRequired, voiceRequired; // If the user did not enter the input correctly this is used to ask for correnct input

    @FXML
    private AnchorPane pane;

    @FXML
    private TextField NameTextField,RegisterNumberTextField,EmailTextField; // This is where user enters their data
    @FXML
    private RadioButton maleRadioButton, femaleRadioButton; // TO choose gender

    private boolean inputChecked,voiceSampleAdded = false; // To see if all the inputs are filled and voice samples are added
    private String sampleFilePath;


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
    private void addSamples() throws InterruptedException {
        student = new Student();
        // To store the values of the user before clicking the add samples button.
        // Because after it is clicked all the data entered will be gone
        String name = NameTextField.getText();
        int userID = Integer.parseInt(RegisterNumberTextField.getText());
        String email = EmailTextField.getText();
        Boolean isMale = true;

        if(maleRadioButton.isSelected()){
            isMale = true;
        }else if(femaleRadioButton.isSelected()){
            isMale = false;
        }
        // Use student object to store those values
        student.setName(name);
        student.setRegNum(userID);
        student.setEmail(email);
        student.setMale(isMale);

        t1= new Thread(()->{
            String line = recordAndTranscribe.runCode("recordSample"); // Record voice samples using recordSample.py
            if (line.equals("1")) {
                voiceSampleAdded = true; // To mark the voice samples are added
                student.setVoiceAdded(voiceSampleAdded);
                successRecordingScreen(); // Prompt a success screen to user
            }
        });
        t1.start();
        //TimeUnit.MILLISECONDS.sleep(700); // Wait 0.7 second for python modules to loadup
        Stage stage = (Stage)addVoiceSamplesBox.getScene().getWindow();
        loader = sceneChanger.changeScene("RecordingScreen",stage); // Display the recording screen

    }
    public void restoreValues(Student student){
        // This is where we restore the previous values which was there before clicking the add voice samples button.
        // When it is clicked, all the values wil be gone and a new scene will be set to the screen when we come back.
        // Therefor we restore that by using student object
        NameTextField.setText(student.getName());
        EmailTextField.setText(student.getEmail());
        RegisterNumberTextField.setText(student.getRegNum()+"");
        //System.out.println(student.getMale());
        if(student.getMale()){
            maleRadioButton.setSelected(true);
        }else {
            femaleRadioButton.setSelected(true);
        }
        voiceSampleAdded = student.getVoiceAdded();
    }
    private void successRecordingScreen() {
        recordingController = loader.getController(); // Get the controller of the recording screen
        recordingController.successRecordingScreen();
        recordingController.transferData(student);

    }

    @FXML
    private void backBtn(ActionEvent event){ // When the back button is pressed, user will be taken to the main screen
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        sceneChanger.changeScene("MainView",stage);
    }

    public void submitRegBtn(ActionEvent event) {
        inputChecked = true;
        // When the submit button is pressed.
        // This is where all the input validation happens.
        // Also it is validating if all the filed are filled before submitting data to the database
        if(NameTextField.getText().isEmpty()){
            inputChecked = false;
            nameRequired.setVisible(true);
        }else{
            nameRequired.setVisible(false);
        }

        if(RegisterNumberTextField.getText().isEmpty()) {
            inputChecked = false;
            idRequired.setVisible(true);
        }else if(!(RegisterNumberTextField.getText().isEmpty())){
            idRequired.setVisible(false);
        }else{
            try{
                Integer.parseInt(RegisterNumberTextField.getText());
                idRequired.setVisible(false);
            }catch (NumberFormatException e){
                idRequired.setVisible(true);
                inputChecked = false;
            }
        }

        if(EmailTextField.getText().isEmpty()){
            inputChecked = false;
            emailRequired.setVisible(true);
        }else{
            emailRequired.setVisible(false);
        }

        if(!voiceSampleAdded){
            voiceRequired.setVisible(true);
        }else{
            voiceRequired.setVisible(false);
        }

        if(!(maleRadioButton.isSelected() || femaleRadioButton.isSelected())){
            genderRequired.setVisible(true);
            inputChecked = false;
        }else{
            genderRequired.setVisible(false);
        }

        if(inputChecked && voiceSampleAdded) {
            Connection connection = null;

            try {
                // Load the JDBC driver
                Class.forName("org.sqlite.JDBC");

                // Connect to the database
                String workingDir = System.getProperty("user.dir"); // Working directory path
                String path = workingDir + "/src/main/res/database/studentData.db"; // Absolute path for the database
                String url = "jdbc:sqlite:" + path; // url for the database
                connection = DriverManager.getConnection(url);

                // SQL statement for the database table creation
                String createTableSQL = "CREATE TABLE IF NOT EXISTS student ("
                        + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "Name TEXT,"
                        + "RegisterNumber INTEGER,"
                        + "Gender TEXT,"
                        + "SampleFile TEXT,"
                        + "Email TEXT,"
                        + "AttendanceFile TEXT"
                        + ")";

                connection.createStatement().executeUpdate(createTableSQL);

                // Get user data from UI
                String name = NameTextField.getText();
                String registerNumber = RegisterNumberTextField.getText();
                String email = EmailTextField.getText();
                String gender = "";

                if (maleRadioButton.isSelected()) {
                    gender = "Male";
                } else if (femaleRadioButton.isSelected()) {
                    gender = "Female";
                }

                // Move the sample recording from src/res/temp folder to src/res/audioSamples folder.
                // Also this renames the audio sample with user ID
                moveRecordingToFile(registerNumber);

                // Make the attendacne recording file
                String currentDir = System.getProperty("user.dir"); // Current directory path
                String pathToAttendanceFile = workingDir + "\\src\\main\\res\\attendance\\"+registerNumber+".txt";
                File file = new File(pathToAttendanceFile);

                if (!file.exists()) {
                    file.createNewFile();
                }else {
                    System.out.println("Attendance sheet already exists");
                }

                // SQL statement to insert data to the database
                String insertSQL = "INSERT INTO student (Name, RegisterNumber, Gender,SampleFile, Email, AttendanceFile) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(insertSQL);
                pstmt.setString(1, name);
                pstmt.setString(2, registerNumber);
                pstmt.setString(3, gender);
                pstmt.setString(4, sampleFilePath);
                pstmt.setString(5, email);
                pstmt.setString(6, pathToAttendanceFile);


                pstmt.executeUpdate();
                pstmt.close();
                successRegScreen(); // If the registration is success
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void successRegScreen() {
        Platform.runLater(()->{
            Stage stage = (Stage) pane.getScene().getWindow();
            sceneChanger.changeScene("SuccessRegister",stage);
        });
    }

    private void moveRecordingToFile(String registerNumber) { // This moves recording sample
        String workingDir = System.getProperty("user.dir"); // Working directory path
        String pathToFile = workingDir + "\\src\\main\\res\\temp\\ref.wav";
        sampleFilePath = workingDir + "\\src\\main\\res\\audioSamples\\"+registerNumber+".wav";
        File file = new File(pathToFile);
        file.renameTo(new File(sampleFilePath));
    }
}
