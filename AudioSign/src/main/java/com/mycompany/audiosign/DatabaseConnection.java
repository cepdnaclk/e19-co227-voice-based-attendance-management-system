package com.mycompany.audiosign;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // This is used to initialize database connection
    public static Connection connect() throws SQLException {
        String workingDir = System.getProperty("user.dir"); // Working directory path
        String path = workingDir + "\\src\\main\\res\\database\\studentData.db"; // Absolute path for the database
        String url = "jdbc:sqlite:" + path; // url for the database
        //System.out.println(url);
        String username = ""; // Username - Empty in localhost
        String password = ""; // Password - Empty in localhost
        Connection connection = null;
        connection = DriverManager.getConnection(url, username, password); // Initialize the connection
        return connection;
    }
}
