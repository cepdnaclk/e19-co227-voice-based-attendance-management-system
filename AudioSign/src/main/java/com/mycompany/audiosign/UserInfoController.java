package com.mycompany.audiosign;

import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;

public class UserInfoController {
    private SceneChanger sceneChanger = new SceneChanger(); // Scene changer is a class that is used to change the scene
    @FXML
    private HBox markAttendanceBox, registerBox,loginBox; // These are used as Navigation buttons

    @FXML
    private TextField idTextField,nameTextField,genderTextField,emailTextField,attendanceTextField;

    @FXML
    private ListView listView;

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

    public void setData(Student student) {
        idTextField.setText(student.getRegNum()+"");
        nameTextField.setText(student.getName());
        genderTextField.setText(student.getGender());
        emailTextField.setText(student.getEmail());
        double totalDays = (double) student.getTotalDays();
        double attendance = student.getFilteredArrayList().size()*100.00/totalDays;
        //System.out.println(student.getFilteredArrayList().size());
        //System.out.println(attendance);
        attendanceTextField.setText(String.format("%.2f", attendance) + "%");
        listView.setItems((ObservableList) student.getFilteredArrayList());
        // Make all text fields non-editable
        idTextField.setEditable(false);
        nameTextField.setEditable(false);
        genderTextField.setEditable(false);
        emailTextField.setEditable(false);
        attendanceTextField.setEditable(false);
        idTextField.setMouseTransparent(true);
        nameTextField.setMouseTransparent(true);
        genderTextField.setMouseTransparent(true);
        emailTextField.setMouseTransparent(true);
        attendanceTextField.setMouseTransparent(true);
        idTextField.setFocusTraversable(false);
        nameTextField.setFocusTraversable(false);
        genderTextField.setFocusTraversable(false);
        emailTextField.setFocusTraversable(false);
        attendanceTextField.setFocusTraversable(false);

    }

    public void backBtn(javafx.event.ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        sceneChanger.changeScene("AdminPanel",stage);
    }
}
