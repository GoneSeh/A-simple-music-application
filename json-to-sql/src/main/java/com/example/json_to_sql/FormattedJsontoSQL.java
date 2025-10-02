package com.example.json_to_sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import org.json.JSONObject;

public class FormattedJsontoSQL {

    public static void main(String[] args) {
        // Path to the properties file and JSON file
        String propertiesFilePath = "C:/Users/User/Desktop/FYP/databaseinfo.properties";
        String jsonFilePath = "C:/Users/User/Desktop/arist.json"; // Path to your JSON file

        Properties properties = new Properties();
        try {
            // Load properties from file
            try (FileReader input = new FileReader(propertiesFilePath)) {
                properties.load(input);
            }

            // Retrieve database connection details from properties file
            String url = properties.getProperty("jdbc.url");
            String user = properties.getProperty("jdbc.username");
            String password = properties.getProperty("jdbc.password");

            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            Connection connection = DriverManager.getConnection(url, user, password);

            // Read JSON file line by line, assuming each line is a separate JSON object
            BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath));
            StringBuilder jsonObjectBuilder = new StringBuilder();
            String line;
            String sqlInsert = "INSERT INTO allartist (id, name, sort_name, type_id, type, disambiguation) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

            // SQL statement to check if the record exists
            String sqlCheck = "SELECT COUNT(*) FROM allartist WHERE id = ?";
            PreparedStatement checkStatement = connection.prepareStatement(sqlCheck);

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }

                // Accumulate lines into a complete JSON object
                jsonObjectBuilder.append(line);

                // Check if we've completed a JSON object
                if (line.endsWith("}")) {
                    String jsonObjectString = jsonObjectBuilder.toString();
                    if (jsonObjectString.startsWith("{") && jsonObjectString.endsWith("}")) {
                        try {
                            // Parse the accumulated JSON object
                            JSONObject jsonObject = new JSONObject(jsonObjectString);

                            // Retrieve values using field names
                            String id = jsonObject.optString("id", null);
                            String name = jsonObject.optString("name", null);
                            String sortName = jsonObject.optString("sort-name", null);
                            String typeId = jsonObject.optString("type-id", null);
                            String type = jsonObject.optString("type", null);
                            String disambiguation = jsonObject.optString("disambiguation", null);

                            // Check if the record already exists
                            checkStatement.setString(1, id);
                            ResultSet resultSet = checkStatement.executeQuery();
                            resultSet.next();
                            int count = resultSet.getInt(1);

                            if (count == 0) {
                                // Record does not exist, insert it
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
                        } finally {
                            // Clear the StringBuilder for the next JSON object
                            jsonObjectBuilder.setLength(0);
                        }
                    } else {
                        System.out.println("Invalid JSON object detected: " + jsonObjectString);
                        jsonObjectBuilder.setLength(0); // Clear the StringBuilder if the object is invalid
                    }
                }
            }

            reader.close();
            // Close resources
            checkStatement.close();
            preparedStatement.close();
            connection.close();

            System.out.println("All data imported successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
