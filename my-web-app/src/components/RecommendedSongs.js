// RecommendedSongs.js
import React from "react";
import "../styles/Songs.css";

import blankspace from '../images/blankspace.png';
import onedance from '../images/onedance.png';
import pawnitall from '../images/pawnitall.jpg';
import losecontrol from '../images/losecontrol.png';
import betheone from '../images/betheone.jpg';
import delicate from '../images/delicated.jpg';
import lastchristmas from '../images/lastchristmas.png';
import paradise from '../images/paradise.jpg';
import easyonme from '../images/easyonme.jpg';



const songs = [
  {
    title: "Blank Space",
    artist: "Taylor Swift",
    duration: "4:33",
    image:
      blankspace,
  },
  {
    title: "One Dance",
    artist: "Drake",
    duration: "4:03",
    image:
      onedance,
  },
  {
    title: "Pawn It All",
    artist: "Alicia Keys",
    duration: "3:10",
    image:
      pawnitall,
  },
  {
    title: "Lose Control",
    artist: "Teddy Swims",
    duration: "3:30",
    image:
      losecontrol,
  },
  {
    title: "Be The One",
    artist: "Dua Lipa",
    duration: "3:24",
    image:
      betheone,
  },
  {
    title: "Delicate",
    artist: "Taylor Swift",
    duration: "3:54",
    image:
      delicate,
  },
  {
    title: "Last Christmas",
    artist: "Wham!",
    duration: "4:39",
    image:
      lastchristmas,
  },
  {
    title: "Paradise",
    artist: "Coldplay",
    duration: "4:20",
    image:
      paradise,
  },
  {
    title: "Easy On Me",
    artist: "Adele",
    duration: "3:45",
    image:
      easyonme,
  },
];

const RecommendedSongs = () => {
  return (
    <div className="recommended-songs">
      <h1>Recommended Songs</h1>
      <div className="song-container">
        {songs.map((song, index) => (
          <div className="song" key={index}>
            <div className="song-img">
              <img src={song.image} alt={song.title} />
              <div className="overlay">
                <i className="fa-solid fa-play"></i>
              </div>
            </div>
            <div className="song-title">
              <h2>{song.title}</h2>
              <p>{song.artist}</p>
            </div>
            <span>{song.duration}</span>
          </div>
        ))}
      </div>
    </div>
  );
}

export default RecommendedSongs;
