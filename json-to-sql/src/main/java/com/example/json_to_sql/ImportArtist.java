package com.example.json_to_sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import org.json.JSONObject;

public class ImportArtist {

    public static void main(String[] args) {
        String propertiesFilePath = "C:/Users/User/Desktop/FYP/databaseinfo.properties";
        String jsonFilePath = "C:/Users/User/Desktop/artistdatawithtypes.json"; // JSON file path

        Properties properties = new Properties();
        try {
            // Load database properties
            properties = loadProperties(propertiesFilePath);

            // Establish database connection
            Connection connection = establishConnection(properties);

            // Process JSON file and insert data into fartists table
            processJsonFile(connection, jsonFilePath);

            connection.close();
            System.out.println("\n✅ All artist data processed successfully!");

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

    // Process JSON file and insert unique artist data into fartists table
    private static void processJsonFile(Connection connection, String jsonFilePath) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath));
        StringBuilder jsonObjectBuilder = new StringBuilder();
        String line;

        String sqlInsert = "INSERT INTO fartists (artist_id, artist_name, artist_sort_name, artist_disambiguation, type_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

        String sqlCheck = "SELECT COUNT(*) FROM fartists WHERE artist_id = ?";
        PreparedStatement checkStatement = connection.prepareStatement(sqlCheck);

        HashSet<String> processedArtists = new HashSet<>(); // Track unique artist_id

        System.out.println("\n🚀 Processing JSON file and inserting data into fartists...");

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue; // Skip empty lines
            }

            jsonObjectBuilder.append(line);

            if (line.endsWith("}")) {
                String jsonObjectString = jsonObjectBuilder.toString();
                if (jsonObjectString.startsWith("{") && jsonObjectString.endsWith("}")) {
                    processJsonObject(jsonObjectString, checkStatement, preparedStatement, processedArtists);
                } else {
                    System.out.println("⚠️ Invalid JSON object detected: " + jsonObjectString);
                }
                jsonObjectBuilder.setLength(0); // Clear buffer
            }
        }

        reader.close();
        checkStatement.close();
        preparedStatement.close();
    }

    // Process individual JSON objects and insert into fartists table
    private static void processJsonObject(String jsonObjectString, PreparedStatement checkStatement, PreparedStatement preparedStatement, HashSet<String> processedArtists) {
        try {
            JSONObject jsonObject = new JSONObject(jsonObjectString);

            String artistId = jsonObject.optString("artist_id", null);
            String artistName = jsonObject.optString("artist_name", null);
            String artistSortName = jsonObject.optString("artist_sort_name", null);
            String artistDisambiguation = jsonObject.optString("artist_disambiguation", "");
            String typeId = jsonObject.optString("type_id", null);

            // Skip if artist_id is empty or already processed
            if (artistId == null || artistId.isEmpty()) {
                System.out.println("⏭️ Skipping entry (empty artist_id)");
                return;
            }
            if (processedArtists.contains(artistId)) {
                System.out.println("🔄 Skipping duplicate artist_id: " + artistId);
                return;
            }

            // Check if artist_id already exists in MySQL
            checkStatement.setString(1, artistId);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count == 0) {
                // Insert new artist
                preparedStatement.setString(1, artistId);
                preparedStatement.setString(2, artistName);
                preparedStatement.setString(3, artistSortName);
                preparedStatement.setString(4, artistDisambiguation);
                preparedStatement.setString(5, typeId);
                preparedStatement.executeUpdate();
                processedArtists.add(artistId); // Add to HashSet
                System.out.println("✅ Inserted: artist_id = " + artistId + ", artist_name = " + artistName);
            } else {
                System.out.println("🔄 Skipping existing artist_id: " + artistId);
            }

        } catch (Exception e) {
            System.out.println("❌ Error processing JSON object: " + jsonObjectString);
            e.printStackTrace();
        }
    }
}
