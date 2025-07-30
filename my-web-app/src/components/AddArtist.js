import React, { useState } from "react";

function AddArtist() {
  const [artist, setArtist] = useState({
    artistId: "",
    artistName: "",
    artistSortName: "",
    artistDisambiguation: "",
    typeId: ""
  });
  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    setArtist({ ...artist, [e.target.name]: e.target.value });
  };

  const handleAddArtist = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/fartists/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(artist)
      });
      const data = await response.text();
      if (response.ok) {
        setMessage("✅ Artist added successfully!");
        setArtist({ artistId: "", artistName: "", artistSortName: "", artistDisambiguation: "", typeId: "" }); // Reset form
      } else {
        setMessage("❌ Failed to add artist: " + data);
      }
    } catch (error) {
      setMessage("⚠️ Error: " + error.message);
    }
  };

  return (
    <div className="add-artist-container">
      <h2>Add New Artist</h2>
      <div>
        <input type="text" name="artistId" placeholder="Artist ID" value={artist.artistId} onChange={handleChange} />
      </div>
      <div>
        <input type="text" name="artistName" placeholder="Artist Name" value={artist.artistName} onChange={handleChange} />
      </div>
      <div>
        <input type="text" name="artistSortName" placeholder="Artist Sort Name" value={artist.artistSortName} onChange={handleChange} />
      </div>
      <div>
        <input type="text" name="artistDisambiguation" placeholder="Artist Disambiguation" value={artist.artistDisambiguation} onChange={handleChange} />
      </div>
      <div>
        <input type="text" name="typeId" placeholder="Type ID" value={artist.typeId} onChange={handleChange} />
      </div>
      <button onClick={handleAddArtist} className="add-artist-button">Add Artist</button>
      {message && <p>{message}</p>}
    </div>
  );
}

export default AddArtist;
