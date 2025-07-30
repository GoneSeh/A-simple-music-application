// src/components/MusicPlayerUI.js

import React, { useEffect, useRef, useState } from "react";
import "../styles/MusicPlayer.css";

const MusicPlayerUI = ({ playlist }) => {
  const audioRef = useRef(null);
  const progressRef = useRef(null);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [isRotating, setIsRotating] = useState(false);
  const [rotation, setRotation] = useState(0);
  const [rotationIntervalId, setRotationIntervalId] = useState(null);

  const currentSong = playlist[currentIndex];

  const playPause = () => {
    if (!audioRef.current) return;
    if (audioRef.current.paused) {
      audioRef.current.play();
      startRotation();
    } else {
      audioRef.current.pause();
      pauseRotation();
    }
  };

  const startRotation = () => {
    if (!isRotating) {
      setIsRotating(true);
      const id = setInterval(() => {
        setRotation((prev) => prev + 1);
      }, 50);
      setRotationIntervalId(id);
    }
  };

  const pauseRotation = () => {
    clearInterval(rotationIntervalId);
    setIsRotating(false);
  };

  const nextSong = () => {
    setCurrentIndex((prev) => (prev + 1) % playlist.length);
  };

  const prevSong = () => {
    setCurrentIndex((prev) => (prev - 1 + playlist.length) % playlist.length);
  };

  const selectSong = (index) => {
    setCurrentIndex(index);
  };

  useEffect(() => {
    const audio = audioRef.current;
    if (audio) {
      const handleCanPlayThrough = () => {
        audio.play().then(() => {
          startRotation();
        }).catch(error => {
          console.error("Playback failed:", error);
        });
      };

      audio.addEventListener('canplaythrough', handleCanPlayThrough);
      audio.load();

      return () => {
        audio.removeEventListener('canplaythrough', handleCanPlayThrough);
      };
    }
  }, [currentIndex]);

  const handleProgressChange = (e) => {
    audioRef.current.currentTime = e.target.value;
  };

  return (
    <div className="music-player-container">
      <div className="music-player">
        <div className="album-cover">
          <img
            id="rotatingImage"
            src={currentSong.cover}
            alt=""
            style={{ transform: `rotate(${rotation}deg)` }}
          />
          <span className="point"></span>
        </div>

        <h2>{currentSong.title}</h2>
        <p>{currentSong.artist}</p>

        <audio
          ref={audioRef}
          onEnded={nextSong}
          onTimeUpdate={() => {
            if (progressRef.current && audioRef.current) {
              progressRef.current.value = audioRef.current.currentTime;
              progressRef.current.max = audioRef.current.duration;
            }
          }}
        >
          <source src={currentSong.src} type="audio/mpeg" />
        </audio>

        <input
          type="range"
          defaultValue="0"
          ref={progressRef}
          onChange={handleProgressChange}
          id="progress"
        />

        <div className="controls">
          <button className="backward" onClick={prevSong}>
            <i className="fa-solid fa-backward"></i>
          </button>
          <button className="play-pause-btn" onClick={playPause}>
            <i
              className={`fa-solid ${
                audioRef.current?.paused ? "fa-play" : "fa-pause"
              }`}
              id="controlIcon"
            ></i>
          </button>
          <button className="forward" onClick={nextSong}>
            <i className="fa-solid fa-forward"></i>
          </button>
        </div>
      </div>

      {/* Playlist Table */}
      <div className="playlist-table">
        <h3>Playlist</h3>
        <table>
          <thead>
            <tr>
              <th>#</th>
              <th>Title</th>
              <th>Artist</th>
            </tr>
          </thead>
          <tbody>
            {playlist.map((song, index) => (
              <tr
                key={index}
                onClick={() => selectSong(index)}
                className={index === currentIndex ? "active" : ""}
              >
                <td>{index + 1}</td>
                <td>{song.title}</td>
                <td>{song.artist}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default MusicPlayerUI;
