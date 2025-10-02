package com.example.json_to_sql;



import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import org.json.JSONObject;

public class Import_Song_From_Formatted_Json {

    public static void main(String[] args) {
        String propertiesFilePath = "C:/Users/User/Desktop/FYP/databaseinfo.properties";
        String jsonFilePath = "C:/Users/User/Desktop/recoding=song.json"; // Path to your song JSON file

        Properties properties = new Properties();
        try {
            // Load properties from file
            properties = loadProperties(propertiesFilePath);

            // Establish database connection
            Connection connection = establishConnection(properties);

            // Process the JSON file and insert data into the song table
            processJsonFile(connection, jsonFilePath);

            connection.close();
            System.out.println("All song data imported successfully!");

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

    // Method to process the JSON file and insert data into the song table
    private static void processJsonFile(Connection connection, String jsonFilePath) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath));
        StringBuilder jsonObjectBuilder = new StringBuilder();
        String line;

        String sqlInsert = "INSERT INTO allsong (id, title, length, disambiguation, video) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

        String sqlCheck = "SELECT COUNT(*) FROM allsong WHERE id = ?";
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

    // Method to process each JSON object and insert it into the song table
    private static void processJsonObject(String jsonObjectString, PreparedStatement checkStatement, PreparedStatement preparedStatement) {
        try {
            JSONObject jsonObject = new JSONObject(jsonObjectString);

            String id = jsonObject.optString("id", null);
            String title = jsonObject.optString("title", null);
            Integer length = jsonObject.has("length") && !jsonObject.isNull("length") ? jsonObject.getInt("length") : null;
            String disambiguation = jsonObject.optString("disambiguation", "");
            Boolean video = jsonObject.getBoolean("video");

            checkStatement.setString(1, id);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count == 0) {
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, title);
                if (length != null) {
                    preparedStatement.setInt(3, length);
                } else {
                    preparedStatement.setNull(3, java.sql.Types.INTEGER);
                }
                preparedStatement.setString(4, disambiguation);
                preparedStatement.setBoolean(5, video);
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
