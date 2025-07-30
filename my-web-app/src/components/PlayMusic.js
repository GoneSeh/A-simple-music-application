// src/components/PlayMusic.js
/* global $ */ // Let ESLint know that $ is a global variable (from jQuery)

import React, { useEffect } from "react";
import { loadScript, loadStyle } from "../loadAssets";
// eslint-disable-next-line no-unused-vars
import $ from "jquery"; // jQuery is used indirectly via jQuery plugins
import "../App.css"; // jPlayer-related CSS is in App.css

function PlayMusic() {
  useEffect(() => {
    (async function initJPlayer() {
      try {
        console.log("Loading local jPlayer assets...");
        // IMPORTANT: Load jQuery first!
        await loadScript("/libs/jquery.min.js");
        // Load jQuery UI CSS and JS from local libs folder
        await loadStyle("/libs/jquery-ui.css");
        await loadScript("/libs/jquery-ui.min.js");
        // Load jPlayer and jPlayer Playlist from local libs
        await loadScript("/libs/jquery.jplayer.min.js");
        await loadScript("/libs/jplayer.playlist.min.js");
        console.log("Assets loaded. Initializing jPlayer...");
        initPlayer();
      } catch (err) {
        console.error("Error loading jPlayer scripts:", err);
      }
    })();

    function initPlayer() {
      console.log("Initializing jPlayer...");
      const playlist = [
        {
          title: "Song 01",
          artist: "Artist A",
          mp3: "/music/Rose_Ft_Bruno_Mars_-_APT.mp3"
        },
        {
          title: "Song 02",
          artist: "Artist B",
          mp3: "/music/Sabrina Carpenter  Nonsense Official Audio.mp3"
        },
        {
          title: "Song 03",
          artist: "Artist C",
          mp3: "/music/The Kid LAROI Justin Bieber STAY.mp3"
        }
      ];

      const cssSelector = {
        jPlayer: "#jquery_jplayer",
        cssSelectorAncestor: ".music-player"
      };

      const options = {
        swfPath: "/libs/", // Use local path for jplayer.swf if needed
        supplied: "mp3",
        volume: 0.5
      };

      if (window.jPlayerPlaylist) {
        const myPlaylist = new window.jPlayerPlaylist(cssSelector, playlist, options);
        console.log("jPlayerPlaylist created:", myPlaylist);
      } else {
        console.error("jPlayerPlaylist not available. Check local libs.");
      }
    }
  }, []);

  return (
    <div className="music-player-page" style={{ textAlign: "center", marginTop: "40px" }}>
      <h2>Play Music (song in react only, not link with database yet yet MP3s)</h2>
      <div className="music-player" style={{ margin: "0 auto" }}>
        {/* jPlayer container */}
        <div id="jquery_jplayer" className="jp-jplayer"></div>

        <div className="info">
          <div className="left">
            <a href="#!" aria-label="Shuffle" className="icon-shuffle"></a>
            <a href="#!" aria-label="Favorite" className="icon-heart"></a>
          </div>
          <div className="center">
            <div className="jp-playlist">
              <ul>
                <li></li>
              </ul>
            </div>
          </div>
          <div className="right">
            <a href="#!" aria-label="Repeat" className="icon-repeat"></a>
            <a href="#!" aria-label="Share" className="icon-share"></a>
          </div>
          <div className="progress"></div>
        </div>

        <div className="controls">
          <div className="current jp-current-time">00:00</div>
          <div className="play-controls">
            <a href="#!" aria-label="Previous" className="icon-previous jp-previous" title="previous"></a>
            <a href="#!" aria-label="Play" className="icon-play jp-play" title="play"></a>
            <a href="#!" aria-label="Pause" className="icon-pause jp-pause" title="pause"></a>
            <a href="#!" aria-label="Next" className="icon-next jp-next" title="next"></a>
          </div>
          <div className="volume-level"></div>
        </div>
      </div>
    </div>
  );
}

export default PlayMusic;
