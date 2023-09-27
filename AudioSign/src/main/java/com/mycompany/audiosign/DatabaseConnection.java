package com.mycompany.audiosign;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection connect() {
        String url = "jdbc:sqlite:C:\\sqlite-voice.db"; // Replace with your SQLite database file path
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to the SQLite database.");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return conn;
    }
}
