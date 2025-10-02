package com.example.json_to_sql.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.*;

@Service
public class FuzzySearchService {

    @Value("${jdbc.url}")
    private String dbUrl;

    @Value("${jdbc.username}")
    private String dbUsername;

    @Value("${jdbc.password}")
    private String dbPassword;

    public List<Map<String, Object>> searchSongs(String keyword) {
        List<Map<String, Object>> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String sql = "SELECT * FROM allsong";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                int distance = levenshteinDistance(keyword.toLowerCase(), title.toLowerCase());

                if (distance <= 3 || title.toLowerCase().contains(keyword.toLowerCase())) { // Adjust threshold
                    Map<String, Object> song = new HashMap<>();
                    song.put("id", resultSet.getString("id"));
                    song.put("title", title);
                    song.put("length", resultSet.getInt("length"));
                    song.put("disambiguation", resultSet.getString("disambiguation"));
                    song.put("video", resultSet.getBoolean("video"));
                    song.put("levenshtein_distance", distance);
                    results.add(song);
                }
            }

            resultSet.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }
}
