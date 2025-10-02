package com.example.json_to_sql.model;

import jakarta.persistence.*;

@Entity
@Table(name = "allsong")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    private String title;
    private int length;
    private String disambiguation;
    private boolean video;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }
}
