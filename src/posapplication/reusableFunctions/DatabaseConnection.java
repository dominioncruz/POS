/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.reusableFunctions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class DatabaseConnection {
    private static Connection connection;

    private DatabaseConnection() {
        // Initialize the connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_application?user=root&password=serialKiller");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection unsuccessful");
        }
    }

    public static Connection getInstance() {
        if (connection == null) {
            new DatabaseConnection();
        }
        return connection;
    }
    
    public static ResultSet checkIfUserExists(String email, String query) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs;
    }
}
