package com.example.json_to_sql;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Scanner;


public class FuzzySearchWithLevenshtein {

    public static void main(String[] args) {
        String propertiesFilePath = "C:/Users/User/Desktop/FYP/databaseinfo.properties";

        Properties properties = new Properties();
        try {
            // Load properties from file
            properties = loadProperties(propertiesFilePath);

            // Establish database connection
            Connection connection = establishConnection(properties);
            
            System.out.println("What song are you looking?");
            Scanner pro = new Scanner(System.in);
            
            String keyword = pro.nextLine();

            // Perform fuzzy searchx
            searchSongsFuzzy(connection, keyword);
            
            pro.close();

            connection.close();

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

    // Method to search songs based on fuzzy logic and substring match
    private static void searchSongsFuzzy(Connection connection, String keyword) throws Exception {
        String sqlSearch = "SELECT * FROM allsong WHERE title LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlSearch);
        preparedStatement.setString(1, "%" + keyword + "%");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String title = resultSet.getString("title");
            int distance = calculateLevenshteinDistance(keyword, title);

            // You can define a threshold distance below which you consider a match
            int threshold = 3;

            // Check if the title contains the keyword or if it meets the fuzzy search threshold
            if (title.toLowerCase().contains(keyword.toLowerCase()) || distance <= threshold) {
                System.out.println("ID: " + resultSet.getString("id"));
                System.out.println("Title: " + title);
                System.out.println("Length: " + resultSet.getInt("length"));
                System.out.println("Disambiguation: " + resultSet.getString("disambiguation"));
                System.out.println("Video: " + resultSet.getBoolean("video"));
                //System.out.println("Levenshtein Distance: " + distance);
                System.out.println("-------------------");
            }
        }

        resultSet.close();
        preparedStatement.close();
    }

    // Method to calculate the Levenshtein distance between two strings
    private static int calculateLevenshteinDistance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
}
