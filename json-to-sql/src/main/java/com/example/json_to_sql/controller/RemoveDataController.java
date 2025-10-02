package com.example.json_to_sql.controller;

import com.example.json_to_sql.service.RemoveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/remove")
@CrossOrigin(origins = "*")
public class RemoveDataController {

    private final RemoveDataService removeDataService;

    @Autowired
    public RemoveDataController(RemoveDataService removeDataService) {
        this.removeDataService = removeDataService;
    }

    // ✅ Fix: Delete Artist by Name (Make Sure It Matches RemoveDataService.java)
    @DeleteMapping("/artist")
    public ResponseEntity<String> deleteArtist(@RequestParam String artistName) {
        boolean deleted = removeDataService.deleteArtistByName(artistName);
        if (deleted) {
            return ResponseEntity.ok("Artist deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete artist");
        }
    }

    // ✅ Fix: Delete Song by Title (Make Sure It Matches RemoveDataService.java)
    @DeleteMapping("/song")
    public ResponseEntity<String> deleteSong(@RequestParam String title) {
        boolean deleted = removeDataService.deleteSongByTitle(title);
        if (deleted) {
            return ResponseEntity.ok("Song deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete song");
        }
    }
}
