import React, { useState } from "react";
import FeaturedSlider from "./FeaturedSlider";
import MusicPlayerUI from "./MusicPlayerUI";
import playlistData from "../music/playlists"; // Assume this is your playlist data
//import FeaturedArtists from "./FeaturedArtists";
//import RecommendedAlbums from "./RecommendedAlbums";

const DiscoverPage = () => {
  const [selectedPlaylist, setSelectedPlaylist] = useState(null);
  const [selectedPlaylistId, setSelectedPlaylistId] = useState(null);

  const handleListenNow = (playlistId) => {
    setSelectedPlaylist(playlistData[playlistId]);
    setSelectedPlaylistId(playlistId);
  };

  return (
    <div style={{ padding: 20, color: "white" }}>
      <h1>Welcome to Music Information Management System 🎉</h1>
      <FeaturedSlider onListenNow={handleListenNow} />
      
      
      {/* Other components */}
      {selectedPlaylist && (
        <MusicPlayerUI
          key={selectedPlaylistId}
          playlist={selectedPlaylist}
        />
      )}


      
    </div>
  );
};

export default DiscoverPage;
