// Layout.js
import React from 'react';
import Sidebar from './Sidebar';
import '../styles/Layout.css';
import { Outlet } from 'react-router-dom';

const Layout = ({ children }) => {
  return (
    <div className="layout">
      <Sidebar />
      <div className="main-content">
        {children}
      </div>
    </div>
  );
};

export default Layout;
