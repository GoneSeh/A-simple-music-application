package com.example.json_to_sql;

import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MySQLConnection {

    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException {
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

        // SQL query
        String query = "INSERT INTO Artist VALUES (205 ,109, 'bhatt')";

        // Load driver class
        Class.forName(driverClassName);

        // Obtain a connection
        Connection con = DriverManager.getConnection(url, username, password);

        // Obtain a statement
        Statement st = con.createStatement();

        // Execute the query
        int count = st.executeUpdate(query);
        System.out.println("Number of rows affected by this query = " + count + " And is succesful load into database");
        

        // Closing the connection
        con.close();
    }
}