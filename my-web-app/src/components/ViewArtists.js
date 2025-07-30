import React, { useState, useEffect } from "react";
import "../styles/ViewArtists.css";

function ViewArtists() {
  const [artists, setArtists] = useState([]);
  const [artist, setArtist] = useState("");
  const [newArtist, setNewArtist] = useState({
    artistId: "",
    artistName: "",
    artistSortName: "",
    artistDisambiguation: "",
    typeId: "",
  });

  // Pagination
  const [currentPage, setCurrentPage] = useState(1);
  const artistsPerPage = 50;

  const indexOfLastArtist = currentPage * artistsPerPage;
  const indexOfFirstArtist = indexOfLastArtist - artistsPerPage;
  const currentArtists = artists.slice(indexOfFirstArtist, indexOfLastArtist);
  const totalPages = Math.ceil(artists.length / artistsPerPage);

  useEffect(() => {
    fetchArtists();
  }, []);

  const fetchArtists = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/fartists/all");
      if (response.ok) {
        const data = await response.json();
        setArtists(data);
      }
    } catch (error) {
      console.error("Error fetching artists:", error);
    }
  };

  const handleSearch = async () => {
    if (!artist.trim()) return;
    try {
      const response = await fetch(`http://localhost:8080/api/fartists/search?keyword=${artist}`);
      if (response.ok) {
        const data = await response.json();
        setArtists(data);
        setCurrentPage(1); // Reset pagination to page 1
      }
    } catch (error) {
      console.error("Error searching artists:", error);
    }
  };

  const handleAddArtist = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/fartists/add", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newArtist),
      });
      if (response.ok) {
        alert("Artist added successfully!");
        setNewArtist({
          artistId: "",
          artistName: "",
          artistSortName: "",
          artistDisambiguation: "",
          typeId: "",
        });
        fetchArtists();
      } else {
        alert("Failed to add artist");
      }
    } catch (error) {
      console.error("Error adding artist:", error);
    }
  };

  const handleDeleteArtist = async (artistName) => {
    const isConfirmed = window.confirm(`Are you sure you want to delete "${artistName}"?`);
    if (!isConfirmed) return;
    try {
      const response = await fetch(`http://localhost:8080/api/remove/artist?artistName=${encodeURIComponent(artistName)}`, {
        method: "DELETE",
      });
      if (response.ok) {
        alert("Artist deleted successfully!");
        fetchArtists();
      } else {
        const errorMsg = await response.text();
        alert("Failed to delete artist: " + errorMsg);
      }
    } catch (error) {
      console.error("Error deleting artist:", error);
      alert("Error deleting artist");
    }
  };

  return (
    <div className="view-artist">
      <h2>Add Artist</h2>
      <div className="add-artist-form">
        <input type="text" placeholder="Artist ID" value={newArtist.artistId} onChange={(e) => setNewArtist({ ...newArtist, artistId: e.target.value })} />
        <input type="text" placeholder="Artist Name" value={newArtist.artistName} onChange={(e) => setNewArtist({ ...newArtist, artistName: e.target.value })} />
        <input type="text" placeholder="Sort Name" value={newArtist.artistSortName} onChange={(e) => setNewArtist({ ...newArtist, artistSortName: e.target.value })} />
        <input type="text" placeholder="Disambiguation" value={newArtist.artistDisambiguation} onChange={(e) => setNewArtist({ ...newArtist, artistDisambiguation: e.target.value })} />
        <input type="text" placeholder="Type ID" value={newArtist.typeId} onChange={(e) => setNewArtist({ ...newArtist, typeId: e.target.value })} />
        <button onClick={handleAddArtist}>Add Artist</button>
      </div>

      <h2>Search Artist</h2>
<div className="search-bar">
  <input type="text" placeholder="Enter artist name" value={artist} onChange={(e) => setArtist(e.target.value)} />
  <button onClick={handleSearch}>Search</button>
</div>

      <h2>Artist List</h2>
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Sort Name</th>
            <th>Disambiguation</th>
            <th>Type ID</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {currentArtists.map((item) => (
            <tr key={item.artist_id}>
              <td>{item.artist_name}</td>
              <td>{item.artist_sort_name}</td>
              <td>{item.artist_disambiguation}</td>
              <td>{item.type_id}</td>
              <td><button className="delete-button" onClick={() => handleDeleteArtist(item.artist_name)}>Delete</button></td>
            </tr>
          ))}
        </tbody>
      </table>

      {artists.length > artistsPerPage && (
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
    </div>
  );
}

export default ViewArtists;
