package com.mycompany.audiosign;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdminPanelController {
    @FXML
    private TextField idTextField, yearTextField, monthTextField, dateTextField;
    @FXML
    private Text fillUserIDText,fillYearText,monthRequiredText,dateRequiredText;
    private boolean dataFilled;

    private String year,month,date;

    private SceneChanger sceneChanger = new SceneChanger(); // Scene changer is a class that is used to change the scene
    @FXML
    private HBox markAttendanceBox, registerBox,loginBox; // These are used as Navigation buttons
    private UserInfoController userInfoController;
    private FXMLLoader loader;
    @FXML
    private AnchorPane pane;

    private Student student;
    private ObservableList<String> tempAttendanceList;
    private int monthInt,dateInt;
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
    public void searchBtn(javafx.event.ActionEvent event) {
        dataFilled = true;
        // Check for input and validate them
        if(idTextField.getText().isEmpty()){
            fillUserIDText.setVisible(true);
            dataFilled= false;
        }else{
            fillUserIDText.setVisible(false);
        }

        if(yearTextField.getText().isEmpty()){
            fillYearText.setVisible(true);
            dataFilled = false;
        }else{
            fillYearText.setVisible((false));
        }

        if(!dateTextField.getText().isEmpty()){
            if(monthTextField.getText().isEmpty()){
                monthRequiredText.setVisible(true);
                dataFilled = false;
            }else{
                monthRequiredText.setVisible(false);
            }
        }

        if(dateTextField.getText().isEmpty()){
            if(!monthTextField.getText().isEmpty()){
                monthRequiredText.setVisible(false);
            }
        }
        // To get only numbers are inputs for month and date

        try{
            monthInt = Integer.parseInt(monthTextField.getText());
            monthRequiredText.setVisible(false);
        }catch (NumberFormatException e){
            monthRequiredText.setVisible(true);
        }
        try{
            dateInt = Integer.parseInt(dateTextField.getText());
            dateRequiredText.setVisible(false);
        }catch (NumberFormatException e){
            if(!dateTextField.getText().isEmpty()){
                dateRequiredText.setVisible(true);
            }
        }
        //If all the data are valid,

        if(dataFilled){
            int userID= -1;
            try {
                userID = Integer.parseInt(idTextField.getText());
                fillUserIDText.setVisible(false);
            }catch (NumberFormatException e){
                fillUserIDText.setVisible(true);
            }
            year = yearTextField.getText();
            month = monthTextField.getText();
            date = dateTextField.getText();
            student = new Student();
            try (Connection connection = DatabaseConnection.connect();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT Name, Gender, Email, AttendanceFile FROM student WHERE RegisterNumber=?")) { // SQL Statement

                preparedStatement.setInt(1,userID); // Set the 1st unknown to regNum
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    {
                        String name = "";
                        String fileLocation = "";
                        String gender = "";
                        String email = "";
                        // Fetch values
                        while (resultSet.next()) {
                            name = resultSet.getString("Name");
                            fileLocation = resultSet.getString("AttendanceFile");
                            gender = resultSet.getString("Gender");
                            email = resultSet.getString("Email");
                        }
                        // Print data
                        student.setRegNum(userID); // Set the student ID
                        student.setName(name); // Set the student name
                        student.setFileLocation(fileLocation); // Set the file location of the audio sample
                        student.setGender(gender);
                        student.setEmail(email);
                        getData(student);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private void getData(Student student) {
        // To store attendance records
        ArrayList<String> attendanceList = new ArrayList<>();
        // To read text file
        BufferedReader bufReader = null;
        try {
            bufReader = new BufferedReader(new FileReader(student.getFileLocation()));
            String line = bufReader.readLine();
            while (line != null) {
                attendanceList.add(line);
                line = bufReader.readLine();
            }
            bufReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        student.setAttendanceList(attendanceList);
        analyseData();
    }

    private void analyseData() {
        String token; // This is used to search
        String monthFormatted = String.format("%02d", monthInt); // This is used to covert a number like 1 to 01
        String dateFormatted = String.format("%02d", dateInt); // This is used to covert a number like 1 to 01
        int totalDays; // This is used to calculate attendance precentage
        if(!date.isEmpty()){ // Tokenizing
            token = year + "-" +monthFormatted+"-"+dateFormatted;
            totalDays = 1;
        }else if(!month.isEmpty()){
            token = year + "-" +monthFormatted;
            totalDays = 30;
        }else{
            token = year;
            totalDays = 365;
        }
        tempAttendanceList = FXCollections.observableArrayList();
        student.getAttendanceList().forEach((str)->{ // Comparing with values in the arrayList
            if(str.startsWith(token)){
                tempAttendanceList.add(str);
            }
        });
        student.setFilteredArrayList(tempAttendanceList);
        student.setTotalDays(totalDays);
        displayAdminPanel();
        //tempAttendanceList.forEach((n)-> System.out.println(n));
    }

    private void displayAdminPanel() {
        Stage stage = (Stage) pane.getScene().getWindow();
        loader = sceneChanger.changeScene("UserInfo",stage);
        userInfoController = loader.getController();
        userInfoController.setData(student);
    }
}
