import React from "react";
import "../styles/Sidebar.css";
import { NavLink } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faSearch,
  faMap,
  faCirclePlay,
  faHeart,
  faUser,
  faGear,
  faRightFromBracket,
} from "@fortawesome/free-solid-svg-icons";
import seh from '../images/Seh.jpg'; // Ensure the path is correct

const Sidebar = () => {
  return (
    <div className="sidebar">
      <div className="user-info">
        <img src={seh} alt="user" />
        <p>Mr Seh</p>
      </div>
      <ul className="nav primary-nav">
        <li className="nav-item">
          <NavLink to="/discover" activeClassName="active">
            <FontAwesomeIcon icon={faMap} className="nav-icon" />
            <span className="nav-text">Playlist</span>
          </NavLink>
        </li>
        <li className="nav-item">
          <NavLink to="/view/artists" activeClassName="active">
            <FontAwesomeIcon icon={faSearch} className="nav-icon" />
            <span className="nav-text">Artist</span>
          </NavLink>
        </li>
        <li className="nav-item">
          <NavLink to="/view/songs" activeClassName="active">
            <FontAwesomeIcon icon={faSearch} className="nav-icon" />
            <span className="nav-text">Songs</span>
          </NavLink>
        </li>
        <li className="nav-item">
          <NavLink to="/share" activeClassName="active">
            <FontAwesomeIcon icon={faCirclePlay} className="nav-icon" />
            <span className="nav-text">Share</span>
          </NavLink>
        </li>
      </ul>
      <ul className="nav secondary-nav">
        <li className="nav-item">
          <NavLink to="/profile" activeClassName="active">
            <FontAwesomeIcon icon={faUser} className="nav-icon" />
            <span className="nav-text">Profile</span>
          </NavLink>
        </li>
        <li className="nav-item">
          <NavLink to="/settings" activeClassName="active">
            <FontAwesomeIcon icon={faGear} className="nav-icon" />
            <span className="nav-text">Settings</span>
          </NavLink>
        </li>
        <li className="nav-item">
          <NavLink to="/logout" activeClassName="active">
            <FontAwesomeIcon icon={faRightFromBracket} className="nav-icon" />
            <span className="nav-text">Logout</span>
          </NavLink>
        </li>
      </ul>
    </div>
  );
};

export default Sidebar;
