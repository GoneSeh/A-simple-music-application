package com.example.json_to_sql.controller;

import com.example.json_to_sql.service.FSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.example.json_to_sql.model.FSong;



import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fsongs")
@CrossOrigin(origins = "*") // Allows frontend access
public class FSongController {

    private final FSongService fsongService;

    @Autowired
    public FSongController(FSongService fsongService) {
        this.fsongService = fsongService;
    }
    
    @PostMapping("/add")
    public ResponseEntity<String> addSong(@RequestBody FSong song) {
        boolean added = fsongService.addSong(song);
        if (added) {
            return ResponseEntity.ok("Song added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add song");
        }
    }


    @GetMapping("/test")
    public String testEndpoint() {
        return "Hello, FSongs API is working!";
    }

    @GetMapping("/search")
    public List<Map<String, Object>> searchFSongs(@RequestParam String keyword) {
        return fsongService.searchFSongs(keyword);
    }

    
    @GetMapping("/all")
    public List<Map<String, Object>> getAllSongs() {
        return fsongService.getAllSongs();
    }
}
