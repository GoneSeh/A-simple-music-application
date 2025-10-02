package com.example.json_to_sql.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fartists")
public class FArtist {

    @Id
    @Column(name = "artist_id")
    private String artistId;
    
    @Column(name = "artist_name")
    private String artistName;
    
    @Column(name = "artist_sort_name")
    private String artistSortName;
    
    @Column(name = "artist_disambiguation")
    private String artistDisambiguation;
    
    @Column(name = "type_id")
    private String typeId;

    // Getters and Setters
    public String getArtistId() { return artistId; }
    public void setArtistId(String artistId) { this.artistId = artistId; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    public String getArtistSortName() { return artistSortName; }
    public void setArtistSortName(String artistSortName) { this.artistSortName = artistSortName; }

    public String getArtistDisambiguation() { return artistDisambiguation; }
    public void setArtistDisambiguation(String artistDisambiguation) { this.artistDisambiguation = artistDisambiguation; }

    public String getTypeId() { return typeId; }
    public void setTypeId(String typeId) { this.typeId = typeId; }
}
