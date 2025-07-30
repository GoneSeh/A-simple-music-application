import React from "react";
import { Link } from "react-router-dom";

function Home() {
  return (
    <div>
      <h1>Welcome to the Music Management System</h1>
      <p>Select an option to get started:</p>
      <ul>
        <li>
          <Link to="/search-artist">Search by Artist</Link>
        </li>
        <li>
          <Link to="/search-song">Search by Song</Link>
        </li>
        <li>
          <Link to="/search-album">Search by Album</Link>
        </li>
      </ul>
    </div>
  );
}

export default Home;
