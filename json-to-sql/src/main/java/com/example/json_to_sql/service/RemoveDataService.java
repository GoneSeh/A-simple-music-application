package com.example.json_to_sql.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.sql.*;

@Service
public class RemoveDataService {

    @Value("${jdbc.url}")
    private String dbUrl;

    @Value("${jdbc.username}")
    private String dbUsername;

    @Value("${jdbc.password}")
    private String dbPassword;

    // ✅ Fix: Delete Artist by artist_name
    public boolean deleteArtistByName(String artistName) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String sql = "DELETE FROM fartists WHERE artist_name = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, artistName);
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Fix: Delete Song by title (THIS WAS MISSING)
    public boolean deleteSongByTitle(String title) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String sql = "DELETE FROM fsongs WHERE title = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, title);
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
