// src/components/SharePlaylists.js
import React, { useState } from "react";
import playlistData from "../music/playlists";

function SharePlaylists() {
  const [selected, setSelected] = useState("");
  const [songs, setSongs] = useState([]);

  const handleSelect = (e) => {
    const name = e.target.value;
    setSelected(name);
    setSongs(playlistData[name] || []);
  };

  const handleExport = () => {
    if (!selected || songs.length === 0) return;

    const textContent = songs
      .map((song, index) => `${index + 1}. ${song.title} - ${song.artist}\n${song.src}`)
      .join("\n\n");

    const blob = new Blob([textContent], { type: "text/plain" });
    const url = URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.download = `${selected}_playlist.txt`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  };

  return (
    <div className="view-songs-container" style={{ textAlign: "center" }}>
      <h2>🎵 Share or Export the Playlist 🎵</h2>

      <select value={selected} onChange={handleSelect} className="input-field">
        <option value="">-- Choose Playlist --</option>
        {Object.keys(playlistData).map((key) => (
          <option key={key} value={key}>
            {key.replace(/([A-Z])/g, " $1").replace(/^./, str => str.toUpperCase())}
          </option>
        ))}
      </select>

      {songs.length > 0 && (
        <>
          <h3 style={{ marginTop: "20px" }}>🎧 Songs in {selected}</h3>
          <ul style={{ listStyle: "none", paddingLeft: 0 }}>
            {songs.map((song, index) => (
              <li key={index}>
                <strong>{song.title}</strong> by {song.artist}
              </li>
            ))}
          </ul>
          <button className="button" onClick={handleExport}>
            📥 Export Playlist (.txt)
          </button>
        </>
      )}
    </div>
  );
}

export default SharePlaylists;
