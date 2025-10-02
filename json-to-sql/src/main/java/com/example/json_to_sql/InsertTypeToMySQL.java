package com.example.json_to_sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import org.json.JSONObject;

public class InsertTypeToMySQL {

    public static void main(String[] args) {
        String propertiesFilePath = "C:/Users/User/Desktop/FYP/databaseinfo.properties";
        String jsonFilePath = "C:/Users/User/Desktop/completely_cleaned_relations_tree.json"; // JSON file path

        Properties properties = new Properties();
        try {
            // Load database properties
            properties = loadProperties(propertiesFilePath);

            // Establish database connection
            Connection connection = establishConnection(properties);

            // Process JSON file and insert data into ftypes table
            processJsonFile(connection, jsonFilePath);

            connection.close();
            System.out.println("\n✅ All type data processed successfully!");

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

    // Process JSON file and insert unique type_id and type_name into ftypes table
    private static void processJsonFile(Connection connection, String jsonFilePath) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath));
        StringBuilder jsonObjectBuilder = new StringBuilder();
        String line;

        String sqlInsert = "INSERT INTO ftypes (type_id, type_name) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

        String sqlCheck = "SELECT COUNT(*) FROM ftypes WHERE type_id = ?";
        PreparedStatement checkStatement = connection.prepareStatement(sqlCheck);

        HashSet<String> processedTypes = new HashSet<>(); // Track unique type_id

        System.out.println("\n🚀 Processing JSON file and inserting data into ftypes...");

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue; // Skip empty lines
            }

            jsonObjectBuilder.append(line);

            if (line.endsWith("}")) {
                String jsonObjectString = jsonObjectBuilder.toString();
                if (jsonObjectString.startsWith("{") && jsonObjectString.endsWith("}")) {
                    processJsonObject(jsonObjectString, checkStatement, preparedStatement, processedTypes);
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

    // Process individual JSON objects and insert into ftypes table
    private static void processJsonObject(String jsonObjectString, PreparedStatement checkStatement, PreparedStatement preparedStatement, HashSet<String> processedTypes) {
        try {
            JSONObject jsonObject = new JSONObject(jsonObjectString);

            String typeId = jsonObject.optString("type_id", null);
            String typeName = jsonObject.optString("type_name", null);

            // Skip if type_id is empty or already processed
            if (typeId == null || typeId.isEmpty()) {
                System.out.println("⏭️ Skipping entry (empty type_id)");
                return;
            }
            if (processedTypes.contains(typeId)) {
                System.out.println("🔄 Skipping duplicate type_id: " + typeId);
                return;
            }

            // Check if type_id already exists in MySQL
            checkStatement.setString(1, typeId);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count == 0) {
                // Insert new type_id
                preparedStatement.setString(1, typeId);
                preparedStatement.setString(2, typeName);
                preparedStatement.executeUpdate();
                processedTypes.add(typeId); // Add to HashSet
                System.out.println("✅ Inserted: type_id = " + typeId + ", type_name = " + typeName);
            } else {
                System.out.println("🔄 Skipping existing type_id: " + typeId);
            }

        } catch (Exception e) {
            System.out.println("❌ Error processing JSON object: " + jsonObjectString);
            e.printStackTrace();
        }
    }
}
