package com.example.json_to_sql.repository;

import com.example.json_to_sql.model.FArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FArtistRepository extends JpaRepository<FArtist, String> {

    @Query("SELECT f FROM FArtist f WHERE LOWER(f.artistName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<FArtist> searchArtists(String keyword);
}
