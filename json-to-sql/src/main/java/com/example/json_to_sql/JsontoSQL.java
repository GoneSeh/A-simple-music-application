package com.example.json_to_sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import org.json.JSONObject;

public class JsontoSQL {

    public static void main(String[] args) {
        String propertiesFilePath = "C:/Users/User/Desktop/FYP/databaseinfo.properties";
        String jsonFilePath = "C:/Users/User/Desktop/arist.json";

        Properties properties = new Properties();
        try {
            // Load properties from file
            properties = loadProperties(propertiesFilePath);

            // Establish database connection
            Connection connection = establishConnection(properties);

            // Process the JSON file and insert data into the database
            processJsonFile(connection, jsonFilePath);

            connection.close();
            System.out.println("All data imported successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load properties from a file
    private static Properties loadProperties(String propertiesFilePath) throws Exception {
        Properties properties = new Properties();
        try (FileReader input = new FileReader(propertiesFilePath)) {
            properties.load(input);
        }
        return properties;
    }

    // Method to establish a database connection
    private static Connection establishConnection(Properties properties) throws Exception {
        String url = properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        // Load MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish and return the database connection
        return DriverManager.getConnection(url, user, password);
    }

    // Method to process the JSON file and insert data into the database
    private static void processJsonFile(Connection connection, String jsonFilePath) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath));
        StringBuilder jsonObjectBuilder = new StringBuilder();
        String line;

        String sqlInsert = "INSERT INTO allartist (id, name, sort_name, type_id, type, disambiguation) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

        String sqlCheck = "SELECT COUNT(*) FROM allartist WHERE id = ?";
        PreparedStatement checkStatement = connection.prepareStatement(sqlCheck);

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue; // Skip empty lines
            }

            jsonObjectBuilder.append(line);

            if (line.endsWith("}")) {
                String jsonObjectString = jsonObjectBuilder.toString();
                if (jsonObjectString.startsWith("{") && jsonObjectString.endsWith("}")) {
                    processJsonObject(jsonObjectString, checkStatement, preparedStatement);
                } else {
                    System.out.println("Invalid JSON object detected: " + jsonObjectString);
                }
                jsonObjectBuilder.setLength(0); // Clear the StringBuilder
            }
        }

        reader.close();
        checkStatement.close();
        preparedStatement.close();
    }

    // Method to process each JSON object and insert it into the database
    private static void processJsonObject(String jsonObjectString, PreparedStatement checkStatement, PreparedStatement preparedStatement) {
        try {
            JSONObject jsonObject = new JSONObject(jsonObjectString);

            String id = jsonObject.optString("id", null);
            String name = jsonObject.optString("name", null);
            String sortName = jsonObject.optString("sort-name", null);
            String typeId = jsonObject.optString("type-id", null);
            String type = jsonObject.optString("type", null);
            String disambiguation = jsonObject.optString("disambiguation", null);

            checkStatement.setString(1, id);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count == 0) {
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, sortName);
                preparedStatement.setString(4, typeId);
                preparedStatement.setString(5, type);
                preparedStatement.setString(6, disambiguation);
                preparedStatement.executeUpdate();
            } else {
                System.out.println("Duplicate entry for ID: " + id + " - Skipping insert.");
            }

        } catch (Exception e) {
            System.out.println("Error processing JSON object: " + jsonObjectString);
            e.printStackTrace();
        }
    }
    
  
}
    

