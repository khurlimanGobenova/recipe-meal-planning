// src/components/Navbar.jsx
import React from "react";
import { Link, useLocation } from "react-router-dom";
import "./Navbar.css";

export default function Navbar() {
  const location = useLocation();

  const navItems = [
    { name: "Home", path: "/" },
    { name: "Dashboard", path: "/dashboard" },
    { name: "Calendar", path: "/calendar" },
    { name: "Recipes", path: "/recipes" },
    { name: "Shopping List", path: "/shopping-list" },
    { name: "Profile", path: "/profile" },
  ];

  return (
    <nav className="navbar">
      <div className="navbar-content">
        {/* App Title */}
        <div className="navbar-title">🥗 Meal Planner</div>

        {/* Navigation Links */}
        <div className="nav-links">
          {navItems.map((item) => (
            <Link
              key={item.name}
              to={item.path}
              className={`nav-button ${
                location.pathname === item.path
                  ? "nav-button-active"
                  : "nav-button-inactive"
              }`}
            >
              {item.name}
            </Link>
          ))}
        </div>
      </div>
    </nav>
  );
}
