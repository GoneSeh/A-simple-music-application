import React, { useState } from "react";

function SearchBySong() {
  const [song, setSong] = useState("");
  const [results, setResults] = useState([]);

  const handleSearch = async () => {
    if (!song.trim()) {
      alert("Please enter a song name");
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/fsongs/search?keyword=${encodeURIComponent(song)}`
      );
      if (!response.ok) {
        throw new Error("Failed to fetch songs");
      }
      const data = await response.json();
      setResults(data);
    } catch (error) {
      console.error("Error fetching data:", error);
      alert("Error fetching song data");
    }
  };

  return (
    <div className="search-page">
      <h2>Search By Song</h2>
      <input
        type="text"
        placeholder="Enter song name"
        value={song}
        onChange={(e) => setSong(e.target.value)}
        className="input-field"
      />
      <button onClick={handleSearch} className="button">
        Search
      </button>
      
      {results.length > 0 && (
        <div className="results">
          <h3>Search Results:</h3>
          <ul>
            {results.map((item, index) => (
              <li key={index}>{item.title} - {item.artist}</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}

export default SearchBySong;
