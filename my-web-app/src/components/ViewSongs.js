import React, { useState, useEffect } from "react";
import "../styles/ViewSongs.css";

function ViewSongs() {
  const [songs, setSongs] = useState([]);
  const [searchTitle, setSearchTitle] = useState("");
  const [newSong, setNewSong] = useState({
    songId: "",
    title: "",
    disambiguation: "",
    length: "",
    tags: "",
    relation: "",
    language: "",
    url: ""
  });
  const [loading, setLoading] = useState(true);

  // Pagination state
  const [currentPage, setCurrentPage] = useState(1);
  const songsPerPage = 50;

  const indexOfLastSong = currentPage * songsPerPage;
  const indexOfFirstSong = indexOfLastSong - songsPerPage;
  const currentSongs = songs.slice(indexOfFirstSong, indexOfLastSong);
  const totalPages = Math.ceil(songs.length / songsPerPage);

  useEffect(() => {
    fetchSongs();
  }, []);

  const fetchSongs = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/fsongs/all");
      if (response.ok) {
        const data = await response.json();
        setSongs(data);
      }
    } catch (error) {
      console.error("Error fetching songs:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (!searchTitle.trim()) return;
    try {
      const response = await fetch(`http://localhost:8080/api/fsongs/search?keyword=${searchTitle}`);
      if (response.ok) {
        const data = await response.json();
        setSongs(data);
        setCurrentPage(1); // Reset to first page on search
      }
    } catch (error) {
      console.error("Error searching songs:", error);
    }
  };

  const handleAddSong = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/fsongs/add", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newSong),
      });
      if (response.ok) {
        alert("Song added successfully!");
        setNewSong({
          songId: "",
          title: "",
          disambiguation: "",
          length: "",
          tags: "",
          relation: "",
          language: "",
          url: ""
        });
        fetchSongs();
      } else {
        alert("Failed to add song");
      }
    } catch (error) {
      console.error("Error adding song:", error);
    }
  };

  const deleteSong = async (title) => {
    if (!window.confirm(`Are you sure you want to delete "${title}"?`)) return;
    try {
      const response = await fetch(`http://localhost:8080/api/remove/song?title=${encodeURIComponent(title)}`, {
        method: "DELETE",
      });
      if (response.ok) {
        setSongs(songs.filter(song => song.title !== title));
        alert(`"${title}" deleted successfully.`);
      } else {
        alert(`Failed to delete "${title}".`);
      }
    } catch (error) {
      console.error("Error deleting song:", error);
    }
  };

  return (
    <div className="view-songs-container">
      <h2>Add Song</h2>
      <div className="add-song-form">
        <input type="text" placeholder="Song ID" value={newSong.songId} onChange={(e) => setNewSong({ ...newSong, songId: e.target.value })} />
        <input type="text" placeholder="Title" value={newSong.title} onChange={(e) => setNewSong({ ...newSong, title: e.target.value })} />
        <input type="text" placeholder="Disambiguation" value={newSong.disambiguation} onChange={(e) => setNewSong({ ...newSong, disambiguation: e.target.value })} />
        <input type="text" placeholder="Length (in seconds)" value={newSong.length} onChange={(e) => setNewSong({ ...newSong, length: e.target.value })} />
        <input type="text" placeholder="Tags (JSON)" value={newSong.tags} onChange={(e) => setNewSong({ ...newSong, tags: e.target.value })} />
        <input type="text" placeholder="Relation (JSON)" value={newSong.relation} onChange={(e) => setNewSong({ ...newSong, relation: e.target.value })} />
        <input type="text" placeholder="Language" value={newSong.language} onChange={(e) => setNewSong({ ...newSong, language: e.target.value })} />
        <input type="text" placeholder="URL" value={newSong.url} onChange={(e) => setNewSong({ ...newSong, url: e.target.value })} />
        <button onClick={handleAddSong}>Add Song</button>
      </div>

      <h2>Search Song</h2>
<div className="search-bar">
  <input type="text" placeholder="Enter song title" value={searchTitle} onChange={(e) => setSearchTitle(e.target.value)} />
  <button onClick={handleSearch}>Search</button>
</div>


      <h2>All Songs</h2>
      {loading ? (
        <p>Loading songs...</p>
      ) : (
        <>
          <table>
            <thead>
              <tr>
                <th>Title</th>
                <th>Disambiguation</th>
                <th>Language</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {currentSongs.map((song) => (
                <tr key={song.title}>
                  <td>{song.title}</td>
                  <td>{song.disambiguation}</td>
                  <td>{song.language}</td>
                  <td>
                    <button onClick={() => deleteSong(song.title)} className="delete-button">Remove</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {songs.length > songsPerPage && (
            <div style={{ textAlign: "center", marginTop: "20px" }}>
              <button
                className="button"
                onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
                disabled={currentPage === 1}
              >
                ⬅ Prev
              </button>
              <span style={{ margin: "0 15px" }}>
                Page {currentPage} of {totalPages}
              </span>
              <button
                className="button"
                onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
                disabled={currentPage === totalPages}
              >
                Next ➡
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default ViewSongs;
