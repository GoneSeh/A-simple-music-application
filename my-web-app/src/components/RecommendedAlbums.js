// RecommendedAlbums.js
import React from "react";
import "../styles/Albums.css";

import darkside from '../images/darkside.png'
import endless from '../images/endlessalbum.png'
import born from '../images/borntodie.jpg'
import speaknow from '../images/speaknowalbum.jpg'
import view from '../images/viewalbum.jpg'


const albums = [
  {
    title: "Views",
    artist: "Drake",
    image:
      view,
  },
  {
    title: "Speak Now",
    artist: "Taylor Swift",
    image:
      speaknow,
  },
  {
    title: "Born to Die",
    artist: "Lana Del Rey",
    image:
      born,
  },
  {
    title: "Endless Summer Vacation",
    artist: "Miley Cyrus",
    image:
      endless,
  },
  {
    title: "The Dark Side of The Moon",
    artist: "Pink Floyd",
    image:
      darkside,
  },
];

const RecommendedAlbums = () => {
  return (
    <div className="albums">
      <h1>Recommended Albums</h1>
      <div className="album-container containers">
        {albums.map((album, index) => (
          <div className="album" key={index}>
            <div className="album-frame">
              <img src={album.image} alt={album.title} />
            </div>
            <div>
              <h2>{album.title}</h2>
              <p>{album.artist}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default RecommendedAlbums;
