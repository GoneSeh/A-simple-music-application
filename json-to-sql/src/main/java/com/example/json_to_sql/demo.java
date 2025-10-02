package com.example.json_to_sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;

public class demo {

    public static void main(String[] args) {
        String propertiesFilePath = "C:/Users/User/Desktop/FYP/databaseinfo.properties";
        String jsonFilePath = "C:/Users/User/Desktop/dataplease.json"; // JSON file path

        Properties properties = new Properties();
        try {
            properties = loadProperties(propertiesFilePath);
            try (Connection connection = establishConnection(properties)) {
                processJsonFile(connection, jsonFilePath);
            }
            System.out.println("🎵 Song data imported successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Properties loadProperties(String propertiesFilePath) throws Exception {
        Properties properties = new Properties();
        try (FileReader input = new FileReader(propertiesFilePath)) {
            properties.load(input);
        }
        return properties;
    }

    private static Connection establishConnection(Properties properties) throws Exception {
        String url = properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }

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
            if (line.isEmpty()) continue;

            jsonObjectBuilder.append(line);

            if (line.endsWith("}")) { // Ensure we have a complete JSON object
                String jsonObjectString = jsonObjectBuilder.toString();
                try {
                    processJsonObject(jsonObjectString, checkStatement, preparedStatement);
                } catch (Exception e) {
                    System.out.println("❌ Skipping malformed JSON: " + jsonObjectString);
                    e.printStackTrace();
                }
                jsonObjectBuilder.setLength(0);
            }
        }

        reader.close();
        checkStatement.close();
        preparedStatement.close();
    }

    private static void processJsonObject(String jsonObjectString, PreparedStatement checkStatement, PreparedStatement preparedStatement) {
        try {
            JSONObject jsonObject = new JSONObject(jsonObjectString);

            String songId = jsonObject.optString("song_id", null);
            String title = jsonObject.optString("title", null);
            String disambiguation = jsonObject.optString("disambiguation", "");
            Integer length = jsonObject.isNull("length") ? null : jsonObject.optInt("length");

            JSONArray tagsArray = jsonObject.optJSONArray("tags");
            String tagsString = (tagsArray != null) ? tagsArray.toString() : "[]";

            String language = jsonObject.optString("language", "Unknown");

            JSONArray relationsArray = jsonObject.optJSONArray("relations");
            String relationsString = (relationsArray != null) ? relationsArray.toString() : "[]";

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
                preparedStatement.setString(7, relationsString);
                preparedStatement.setNull(8, java.sql.Types.VARCHAR); // Default NULL for URL
                preparedStatement.executeUpdate();
            } else {
                System.out.println("⚠️ Duplicate entry for song ID: " + songId + " - Skipping insert.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error processing JSON object: " + jsonObjectString);
            e.printStackTrace();
        }
    }
}
