// App.js
import React from "react";
import { Routes, Route } from "react-router-dom";
import DiscoverPage from './components/DiscoverPage';
import SearchBySong from "./components/SearchBySong";
import SearchByArtist from "./components/SearchByArtist";
import SearchByLyrics from "./components/SearchByLyrics";
import ViewArtists from "./components/ViewArtists";
import ViewSongs from "./components/ViewSongs";
import AddArtist from "./components/AddArtist";
//import PlayMusic from "./components/PlayMusic";
import Layout from './components/Layout';
import MusicPlayerUI from "./components/MusicPlayerUI";
import SharePlaylists from "./components/SharePlaylists";

function App() {
  return (
    <Layout>
    <Routes>
      <Route path="/" element={<DiscoverPage />} />
      <Route path="/discover" element={<DiscoverPage />} />
      <Route path="/search/song" element={<SearchBySong />} />
      <Route path="/search/artist" element={<SearchByArtist />} />
      <Route path="/search/lyrics" element={<SearchByLyrics />} />
      <Route path="/view/artists" element={<ViewArtists />} />
      <Route path="/view/songs" element={<ViewSongs />} />
      <Route path="/add/artist" element={<AddArtist />} />
      <Route path="/playlist" element={<MusicPlayerUI />} />
      <Route path="/share" element={<SharePlaylists />} />
    </Routes>
    </Layout>
   
  );
}

export default App;
