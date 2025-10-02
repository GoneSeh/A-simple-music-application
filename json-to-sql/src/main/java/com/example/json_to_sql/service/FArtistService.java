package com.example.json_to_sql.service;

import com.example.json_to_sql.model.FArtist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.*;

@Service
public class FArtistService {

    @Value("${jdbc.url}")
    private String dbUrl;

    @Value("${jdbc.username}")
    private String dbUsername;

    @Value("${jdbc.password}")
    private String dbPassword;

    // Get All Artists
    public List<Map<String, Object>> getAllArtists() {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String sql = "SELECT * FROM fartists";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> fartist = new HashMap<>();
                fartist.put("artist_id", resultSet.getString("artist_id"));
                fartist.put("artist_name", resultSet.getString("artist_name"));
                fartist.put("artist_sort_name", resultSet.getString("artist_sort_name"));
                fartist.put("artist_disambiguation", resultSet.getString("artist_disambiguation"));
                fartist.put("type_id", resultSet.getString("type_id"));
                results.add(fartist);
            }
            resultSet.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    // Search Artist
    public List<Map<String, Object>> searchArtists(String keyword) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String sql = "SELECT * FROM fartists WHERE LOWER(artist_name) LIKE LOWER(CONCAT('%', ?, '%'))";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, keyword);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> fartist = new HashMap<>();
                fartist.put("artist_id", resultSet.getString("artist_id"));
                fartist.put("artist_name", resultSet.getString("artist_name"));
                fartist.put("artist_sort_name", resultSet.getString("artist_sort_name"));
                fartist.put("artist_disambiguation", resultSet.getString("artist_disambiguation"));
                fartist.put("type_id", resultSet.getString("type_id"));
                results.add(fartist);
            }
            resultSet.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    // Add Artist
    public boolean addArtist(FArtist artist) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String sql = "INSERT INTO fartists (artist_id, artist_name, artist_sort_name, artist_disambiguation, type_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, artist.getArtistId());
            stmt.setString(2, artist.getArtistName());
            stmt.setString(3, artist.getArtistSortName());
            stmt.setString(4, artist.getArtistDisambiguation());
            stmt.setString(5, artist.getTypeId());

            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
