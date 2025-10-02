package com.example.json_to_sql.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.*;
import com.example.json_to_sql.model.FSong;


@Service
public class FSongService {

    @Value("${jdbc.url}")
    private String dbUrl;

    @Value("${jdbc.username}")
    private String dbUsername;

    @Value("${jdbc.password}")
    private String dbPassword;

    public List<Map<String, Object>> searchFSongs(String keyword) {
        List<Map<String, Object>> results = new ArrayList<>();

        int threshold = (keyword.length() > 4) ? 3 : 1;  // 🔥 Dynamic typo tolerance

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            // ✅ Fetch only relevant songs using `LIKE` to improve efficiency
            String sql = "SELECT * FROM fsongs WHERE title LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");  // 🔥 Searching for partial matches
            ResultSet resultSet = stmt.executeQuery();

            List<Map<String, Object>> tempResults = new ArrayList<>();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                int distance = levenshteinDistance(keyword.toLowerCase(), title.toLowerCase());

                // ✅ Allow typo correction + partial matches
                if (distance <= threshold || title.toLowerCase().contains(keyword.toLowerCase())) {
                    Map<String, Object> fsong = new HashMap<>();
                    fsong.put("song_id", resultSet.getString("song_id"));
                    fsong.put("title", title);
                    fsong.put("disambiguation", resultSet.getString("disambiguation"));
                    fsong.put("length", resultSet.getInt("length"));
                    fsong.put("tags", resultSet.getString("tags"));
                    fsong.put("relation", resultSet.getString("relation"));
                    fsong.put("language", resultSet.getString("language"));
                    fsong.put("url", resultSet.getString("url"));
                    fsong.put("levenshtein_distance", distance);
                    tempResults.add(fsong);
                }
            }

            // ✅ Sort by Levenshtein distance (best match first)
            tempResults.sort(Comparator.comparingInt(a -> (int) a.get("levenshtein_distance")));
            results.addAll(tempResults);

            resultSet.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public List<Map<String, Object>> getAllSongs() {
        List<Map<String, Object>> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String sql = "SELECT * FROM fsongs";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> fsong = new HashMap<>();
                fsong.put("song_id", resultSet.getString("song_id"));
                fsong.put("title", resultSet.getString("title"));
                fsong.put("disambiguation", resultSet.getString("disambiguation"));
                fsong.put("length", resultSet.getInt("length"));
                fsong.put("tags", resultSet.getString("tags"));
                fsong.put("relation", resultSet.getString("relation"));
                fsong.put("language", resultSet.getString("language"));
                fsong.put("url", resultSet.getString("url"));
                results.add(fsong);
            }

            resultSet.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
    
    public boolean addSong(FSong song) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String sql = "INSERT INTO fsongs (song_id, title, disambiguation, length, tags, relation, language, url) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, song.getSongId());
            stmt.setString(2, song.getTitle());
            stmt.setString(3, song.getDisambiguation());
            stmt.setInt(4, song.getLength());
            
            String validTags = (song.getTags() == null || song.getTags().trim().isEmpty()) ? "{}" : song.getTags();
            String validRelation = (song.getRelation() == null || song.getRelation().trim().isEmpty()) ? "{}" : song.getRelation();
            
            stmt.setString(5, validTags);
            stmt.setString(6, validRelation);
            stmt.setString(7, song.getLanguage());
            stmt.setString(8, song.getUrl());

            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // ✅ Optimized Levenshtein Distance Calculation
    private int levenshteinDistance(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) dp[i][0] = i;
        for (int j = 0; j <= len2; j++) dp[0][j] = j;

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        return dp[len1][len2];
    }
}
