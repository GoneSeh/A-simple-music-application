package com.example.json_to_sql.controller;

import com.example.json_to_sql.service.FuzzySearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/songs")
//@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allows frontend to call this API
public class FuzzySearchController {

    private final FuzzySearchService fuzzySearchService;

    @Autowired
    public FuzzySearchController(FuzzySearchService fuzzySearchService) {
        this.fuzzySearchService = fuzzySearchService;
    }
    
    @GetMapping("/test")
    public String testEndpoint() {
        return "Hello, API is working!";
    }

    @GetMapping("/search")
    public List<Map<String, Object>> searchSongs(@RequestParam String keyword) {
        return fuzzySearchService.searchSongs(keyword);
    }
}
