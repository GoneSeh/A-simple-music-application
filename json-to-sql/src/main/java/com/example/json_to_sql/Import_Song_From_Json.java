package com.example.json_to_sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;

public class Import_Song_From_Json {

    public static void main(String[] args) {
        String propertiesFilePath = "C:/Users/User/Desktop/FYP/databaseinfo.properties";
        String jsonFilePath = "C:/Users/User/Desktop/dataplease.json"; // Path to your JSON file

        Properties properties = new Properties();
        try {
            // Load database properties
            properties = loadProperties(propertiesFilePath);

            // Establish database connection
            Connection connection = establishConnection(properties);

            // Process JSON file and insert data
            processJsonFile(connection, jsonFilePath);

            connection.close();
            System.out.println("Song data imported successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load properties from a file
    private static Properties loadProperties(String propertiesFilePath) throws Exception {
        Properties properties = new Properties();
        try (FileReader input = new FileReader(propertiesFilePath)) {
            properties.load(input);
        }
        return properties;
    }

    // Establish a database connection
    private static Connection establishConnection(Properties properties) throws Exception {
        String url = properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        // Load MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Return database connection
        return DriverManager.getConnection(url, user, password);
    }

    // Process JSON file and insert song data
    private static void processJsonFile(Connection connection, String jsonFilePath) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath));
        StringBuilder jsonObjectBuilder = new StringBuilder();
        String line;

        String sqlInsert = "INSERT INTO fsongs (song_id, title, disambiguation, length, tags, language, relations, url) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

        String sqlCheck = "SELECT COUNT(*) FROM fsongs WHERE song_id = ?";
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
                jsonObjectBuilder.setLength(0); // Clear the buffer
            }
        }

        reader.close();
        checkStatement.close();
        preparedStatement.close();
    }

    // Process individual JSON objects and insert into database
    private static void processJsonObject(String jsonObjectString, PreparedStatement checkStatement, PreparedStatement preparedStatement) {
        try {
            JSONObject jsonObject = new JSONObject(jsonObjectString);

            String songId = jsonObject.optString("song_id", null);
            String title = jsonObject.optString("title", null);
            String disambiguation = jsonObject.optString("disambiguation", "");
            Integer length = jsonObject.isNull("length") ? null : jsonObject.optInt("length");
            JSONArray tags = jsonObject.optJSONArray("tags");
            String language = jsonObject.optString("language", null);
            JSONArray relations = jsonObject.optJSONArray("relations");

            // Convert JSON arrays to string format
            String tagsString = (tags != null) ? tags.toString() : "[]";
            String relationsString = (relations != null) ? relations.toString() : "[]";

            // Check if song_id already exists
            checkStatement.setString(1, songId);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count == 0) {
                preparedStatement.setString(1, songId);
                preparedStatement.setString(2, title);
                preparedStatement.setString(3, disambiguation);
                if (length != null) {
                    preparedStatement.setInt(4, length);
                } else {
                    preparedStatement.setNull(4, java.sql.Types.INTEGER);
                }
                preparedStatement.setString(5, tagsString);
                preparedStatement.setString(6, language);
                preparedStatement.setString(7, relationsString); // Store relations as JSON
                preparedStatement.setNull(8, java.sql.Types.VARCHAR); // Default URL is NULL
                preparedStatement.executeUpdate();
            } else {
                System.out.println("Duplicate entry for song ID: " + songId + " - Skipping insert.");
            }

        } catch (Exception e) {
            System.out.println("Error processing JSON object: " + jsonObjectString);
            e.printStackTrace();
        }
    }
}
