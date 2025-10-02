package com.example.json_to_sql;

import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class MySQLConnectionPart2 {

    private Connection con;

    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException {
        MySQLConnectionPart2 mySQLConnection = new MySQLConnectionPart2();
        mySQLConnection.connect();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an operation: add, modify, delete");
        String operation = scanner.nextLine().toLowerCase();

        switch (operation) {
            case "add":
                System.out.println("Enter artistId:");
                int artistId = scanner.nextInt();
                scanner.nextLine(); // consume newline
                System.out.println("Enter name:");
                String name = scanner.nextLine();
                System.out.println("Enter genre:");
                String genre = scanner.nextLine();
                mySQLConnection.insertData(artistId, name, genre);
                break;
            case "modify":
                System.out.println("Enter current name to modify:");
                String currentName = scanner.nextLine();
                System.out.println("Enter new genre:");
                String newGenre = scanner.nextLine();
                System.out.println("Enter new name:");
                String newName = scanner.nextLine();
                mySQLConnection.updateDataByName(currentName, newGenre, newName);
                break;
            case "delete":
                System.out.println("Enter name to delete:");
                String nameToDelete = scanner.nextLine();
                mySQLConnection.deleteDataByName(nameToDelete);
                break;
            default:
                System.out.println("Invalid operation");
                break;
        }

        mySQLConnection.disconnect();
        scanner.close();
    }

    public void connect() throws SQLException, ClassNotFoundException, IOException {
        // Load properties file
        Properties props = new Properties();
        FileInputStream in = new FileInputStream("C:\\Users\\User\\Desktop\\FYP\\databaseinfo.properties");
        props.load(in);
        in.close();

        // Get properties
        String driverClassName = props.getProperty("jdbc.driverClassName");
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        // Load driver class
        Class.forName(driverClassName);

        // Obtain a connection
        con = DriverManager.getConnection(url, username, password);
    }

    public void disconnect() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }

    public void insertData(int artistId, String name, String genre) throws SQLException {
        String query = "INSERT INTO Artist (artistID, name, genre) VALUES (?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, artistId);
            pst.setString(2, name);
            pst.setString(3, genre);
            int count = pst.executeUpdate();
            System.out.println("Number of rows affected by insert query = " + count);
        }
    }

    public void updateDataByName(String currentName, String newGenre, String newName) throws SQLException {
        String query = "UPDATE Artist SET genre = ?, name = ? WHERE name = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, newGenre);
            pst.setString(2, newName);
            pst.setString(3, currentName);
            int count = pst.executeUpdate();
            System.out.println("Number of rows affected by update query = " + count);
        }
    }

    public void deleteDataByName(String name) throws SQLException {
        String query = "DELETE FROM Artist WHERE name = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, name);
            int count = pst.executeUpdate();
            System.out.println("Number of rows affected by delete query = " + count);
        }
    }
}
