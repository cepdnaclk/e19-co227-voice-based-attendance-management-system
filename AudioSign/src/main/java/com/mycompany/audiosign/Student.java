package com.mycompany.audiosign;

import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Student {
    // To store data related to a student when needed
    private String name; // Name
    private int regNum; // User ID
    private String email; // Email
    private String fileLocation; // File location of the student voice samples
    private boolean isMale; // To find out student's gender
    private boolean voiceAdded = false; // To see if the student added voice sample during registration
    private String gender; // To store gender
    private ArrayList<String> attendanceList; // To store attendance records
    private ObservableList<String> filteredArrayList; // To store filtered attendance list
    private int totalDays; // To store total days used to calculate attendance precentage

    // The following are getters and setters

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRegNum(int regNum) {
        this.regNum = regNum;
    }

    public int getRegNum() {
        return regNum;
    }


    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public boolean getMale(){
        return  isMale;
    }
    public void setVoiceAdded(boolean bool) {
        voiceAdded = bool;
    }

    public boolean getVoiceAdded(){
        return  voiceAdded;
    }

    public void setGender(String gender) {
        this.gender  = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setAttendanceList(ArrayList<String> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public ArrayList<String> getAttendanceList() {
        return attendanceList;
    }

    public void setFilteredArrayList(ObservableList<String> tempAttendanceList) {
        this.filteredArrayList = tempAttendanceList;
    }
    public ObservableList<String> getFilteredArrayList() {
        return filteredArrayList;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public int getTotalDays() {
        return totalDays;
    }
}
