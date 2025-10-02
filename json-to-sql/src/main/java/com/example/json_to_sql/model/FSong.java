package com.example.json_to_sql.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fsongs")
public class FSong {

    @Id
    @Column(name = "song_id")
    private String songId;
    
    private String title;
    private String disambiguation;
    private int length;
    
    // Assuming your JSON columns are stored as text; you can later parse these if needed.
    @Column(columnDefinition = "json")
    private String tags;
    
    @Column(columnDefinition = "json")
    private String relation;
    
    private String language;
    private String url;

    // Getters and Setters

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
