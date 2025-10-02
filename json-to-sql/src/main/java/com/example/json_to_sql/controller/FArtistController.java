package com.example.json_to_sql.controller;

import com.example.json_to_sql.model.FArtist;
import com.example.json_to_sql.service.FArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fartists")
@CrossOrigin(origins = "*")
public class FArtistController {

    private final FArtistService fArtistService;

    @Autowired
    public FArtistController(FArtistService fArtistService) {
        this.fArtistService = fArtistService;
    }

    @GetMapping("/all")
    public List<Map<String, Object>> getAllArtists() {
        return fArtistService.getAllArtists();
    }
    
    @GetMapping("/test")
    public String testEndpoint() {
        return "Hello, FSongs API is working!";
    }

    @GetMapping("/search")
    public List<Map<String, Object>> searchArtists(@RequestParam String keyword) {
        return fArtistService.searchArtists(keyword);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addArtist(@RequestBody FArtist artist) {
        boolean added = fArtistService.addArtist(artist);
        if (added) {
            return ResponseEntity.ok("Artist added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add artist");
        }
    }
}
