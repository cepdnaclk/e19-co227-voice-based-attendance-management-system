package com.mycompany.audiosign;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserConfirmationController {
    private SceneChanger sceneChanger = new SceneChanger(); // Scene changer is a class that is used to change the scene
    @FXML
    private HBox markAttendanceBox, registerBox,loginBox; // These are used as Navigation buttons
    @FXML
    private TextField idTextBox; // Text field used to display id
    @FXML
    private TextField nameTextBox; // Text field used to display id
    private Student student; // Used to store student data

    private RecordAndTranscribe recordAndTranscribe = new RecordAndTranscribe(); // This is used to help record audio and transcribe

    private FXMLLoader loader; // This is used to get the controller which can be used to pass arguments between controllers

    private RecordingController recordingController;

    private Thread t1;
    @FXML
    private AnchorPane pane;

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
    private void tryAgainBtn(ActionEvent event) throws InterruptedException {
        t1= new Thread(()->{ // Done in a different thread
            String line = recordAndTranscribe.runCode("recordTest"); // This code will execute recordTest.py code. It will output 1 if everything worked properly
            if (line.equals("1")) {
                //changeToSuccessScreen();
                startTranscribe(line);// If recording was okay, Then start transcribing the recording
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
        recordingController.startTranscribe(line);
    }
    @FXML
    private void compareVoiceBtn(ActionEvent event){
        try {
            String workingDir = System.getProperty("user.dir"); // Working directory path
            String pathToPy = workingDir + "/src/main/py/Normalizing.py"; // Absolute path for the file
            // Change the voice comparison model as needed

            /* 1) ChromaFeatures.py - Uses Chroma Features, MFFCs (The Mel-frequency cepstral coefficients), VAD (Voice Activity Detection to identify speech segments) ,
             Dynamic Time Warping (DTW) to align the feature sequences and Normalization */

            /* 2) compareOnly.py - Uses MFFCs (The Mel-frequency cepstral coefficients), Dynamic Time Warping (DTW) to align the feature sequences and Normalization*/

            /* 3) Normalizing.py - Uses  MFFCs (The Mel-frequency cepstral coefficients), VAD (Voice Activity Detection to identify speech segments) ,
             Dynamic Time Warping (DTW) to align the feature sequences and Normalization  */

            /* 4) SpectralBandwidth.py - Uses Spectral Bandwidth features, MFFCs (The Mel-frequency cepstral coefficients), VAD (Voice Activity Detection to identify speech segments) ,
             Dynamic Time Warping (DTW) to align the feature sequences and Normalization */

            /* 5) SpectralContrast.py - Uses Spectral Constrast features, MFFCs (The Mel-frequency cepstral coefficients), VAD (Voice Activity Detection to identify speech segments) ,
             Dynamic Time Warping (DTW) to align the feature sequences and Normalization */

            /* 6) VAD.py - Uses MFCCs, DTW and VAD*/

            ProcessBuilder processBuilder = new ProcessBuilder("python", pathToPy, student.getRegNum()+"");

            // Run python code
            Process process = processBuilder.start();

            // If python outputs something, display it
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = reader.readLine(); // Read the python output
            //System.out.println(line);
            if(line.equals("1")){ // If it is 1, then the voice samples matches
                attendanceMarked();
            }else if(line.equals("0")){
                attendanceDidNotMarked(); // If it is 0 then the voice samples does not match
            }
            line = reader.readLine();
            System.out.println(line);
            // Get the exit code. 0 -> Normal termination. 1 -> Abnormal termination
            int exitCode = process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void attendanceDidNotMarked() { // If the voice samples does not match, show this screen
        Stage stage = (Stage) pane.getScene().getWindow();
        sceneChanger.changeScene("AttendanceDidNotMarked",stage);
    }

    private void attendanceMarked() { // If the voice samples match, show this screen
        Stage stage = (Stage) pane.getScene().getWindow();
        sceneChanger.changeScene("AttendanceMarked",stage);
        updateAttendance(); // This will update the attendace file
    }

    private void updateAttendance() {
        String attendanceFilePath = "";
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT AttendanceFile FROM student WHERE RegisterNumber=?")) { // SQL Statement

            preparedStatement.setInt(1,student.getRegNum()); // Set the 1st unknown to regNum
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                {
                    // Fetch values
                    while (resultSet.next()) {
                        attendanceFilePath = resultSet.getString("AttendanceFile");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        String date = String.valueOf(java.time.LocalDate.now()) + "\n";
        try {
            FileWriter writer = new FileWriter(attendanceFilePath, true);
            writer.write(date);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setStudent(Student student) {
        this.student = student;
        setData();
    }

    public void setData() {
        nameTextBox.setText(student.getName());
        idTextBox.setText(student.getRegNum()+"");

        // Make those two text fields not editable
        nameTextBox.setEditable(false);
        nameTextBox.setMouseTransparent(true);
        nameTextBox.setFocusTraversable(false);
        idTextBox.setEditable(false);
        idTextBox.setMouseTransparent(true);
        idTextBox.setFocusTraversable(false);
    }
}
