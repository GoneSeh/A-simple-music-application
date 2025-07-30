// FeaturedArtists.js
import React from "react";
import "../styles/Artists.css";

import taylor from '../images/taylor.jpg';
import dualipa from '../images/dualipa.jpg';
import alica from '../images/Alica.jpg';
import jimin from '../images/jimin.jpg';
import maroon5 from '../images/Maroon5.jpg';
import weekend from '../images/theweekend.jpg';
import dragon from '../images/Imagingdragon.jpg';
import blie from '../images/b_elis.jpg';

const artists = [
  {
    name: "Taylor Swift",
    image:
      taylor,
  },
  {
    name: "The Weeknd",
    image:
      weekend,
  },
  {
    name: "Dua Lipa",
    image:
      dualipa,
  },
  {
    name: "Jimin",
    image:
      jimin,
  },
  {
    name: "Alicia Keys",
    image:
      alica,
  },
  {
    name: "Maroon 5",
    image:
      maroon5,
  },
  {
    name: "Imagine Dragons",
    image:
      dragon,
  },
  {
    name: "Billie Eilish",
    image:
      blie,
  },
];

const FeaturedArtists = () => {
  return (
    <div className="artists">
      <h1>Top English Artist 2025</h1>
      <div className="artist-container containers">
        {artists.map((artist, index) => (
          <div className="artist" key={index}>
            <div className="artist-img-container">
              <img src={artist.image} alt={artist.name} />
            </div>
            <p>{artist.name}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default FeaturedArtists;
