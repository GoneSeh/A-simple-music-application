package com.example.json_to_sql.service;

import com.example.json_to_sql.model.Song;
import com.example.json_to_sql.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> searchSongs(String keyword) {
        return songRepository.searchSongs(keyword);
    }
}
