import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import '../styles/Navbar.css';

function Navbar() {
  const location = useLocation();
  const navigate = useNavigate();
  
  const isActive = (path) => {
    return location.pathname === path ? 'active' : '';
  };

  const handleLogout = () => {
    localStorage.removeItem('user');
    localStorage.removeItem('isAuthenticated');
    navigate('/');
  };

  const user = JSON.parse(localStorage.getItem('user') || '{}');

  return (
    <nav className="navbar">
      <div className="container">
        <div className="navbar-content">
          <Link to="/home" className="navbar-brand">
            <span className="logo">🍽️</span>
            <span className="brand-text">Heal Meal</span>
          </Link>
          
          <div className="navbar-links">
            <Link to="/home" className={`nav-link ${isActive('/home')}`}>
              Home
            </Link>
            <Link to="/recipes" className={`nav-link ${isActive('/recipes')}`}>
              Recipes
            </Link>
            <Link to="/meal-plan" className={`nav-link ${isActive('/meal-plan')}`}>
              Meal Plan
            </Link>
            <Link to="/shopping-list" className={`nav-link ${isActive('/shopping-list')}`}>
              Shopping List
            </Link>
            <Link to="/profile" className={`nav-link ${isActive('/profile')}`}>
              Profile
            </Link>
            
            <div className="navbar-user">
              <span className="user-name">{user.name || 'User'}</span>
              <button onClick={handleLogout} className="btn-logout">
                Logout
              </button>
            </div>
          </div>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;