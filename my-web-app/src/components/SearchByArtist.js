import React, { useState } from "react";

function SearchByArtist() {
  const [artist, setArtist] = useState("");
  const [results, setResults] = useState([]);

  const handleSearch = async () => {
    if (!artist.trim()) return;

    try {
      const response = await fetch(`http://localhost:8080/api/fartists/search?keyword=${artist}`);
      if (response.ok) {
        const data = await response.json();
        setResults(data);
      }
    } catch (error) {
      console.error("Error fetching artists:", error);
    }
  };

  return (
    <div className="search-page">
      <h2>Search By Artist</h2>
      <input type="text" placeholder="Enter artist name" value={artist} onChange={(e) => setArtist(e.target.value)} />
      <button onClick={handleSearch}>Search</button>
      {results.length > 0 && (
        <ul>
          {results.map((item) => (
            <li key={item.artist_id}>{item.artist_name} - {item.artist_sort_name}</li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default SearchByArtist;
