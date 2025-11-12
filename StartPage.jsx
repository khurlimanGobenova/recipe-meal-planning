// File: StartPage.jsx
import React, { useState } from 'react';
import './StartPage.css'; // Import the CSS file

export default function StartPage() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [authMode, setAuthMode] = useState('login');
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    name: ''
  });

  const handleSubmit = () => {
    console.log('Form submitted:', { authMode, formData });
    alert(`${authMode === 'login' ? 'Logging in' : 'Signing up'} with email: ${formData.email}`);
  };

  const handleInputChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  return (
    <div className="page-container">
      {/* Navbar */}
      <nav className="navbar">
        <div className="nav-content">
          <div className="logo">
            <span className="logo-icon">🍴</span>
            <span className="logo-text">HEALMEAL</span>
          </div>

          {/* Desktop Navigation */}
          <div className="nav-links">
            <a href="#auth">Log/Sign</a>
            <a href="#recipes">Recipes</a>
            <a href="#calendar">Calendar</a>
            <a href="#shopping">Shopping List</a>
            <a href="#profile">My Profile</a>
          </div>

          {/* Mobile Menu Button */}
          <button className="menu-btn" onClick={() => setIsMenuOpen(!isMenuOpen)}>
            {isMenuOpen ? "✖" : "☰"}
          </button>
        </div>

        {/* Mobile Navigation */}
        {isMenuOpen && (
          <div className="mobile-nav">
            <a href="#auth">Log/Sign</a>
            <a href="#recipes">Recipes</a>
            <a href="#calendar">Calendar</a>
            <a href="#shopping">Shopping List</a>
            <a href="#profile">My Profile</a>
          </div>
        )}
      </nav>

      {/* Hero Section */}
      <section id="home" className="hero">
        <div className="hero-content">
          <h1>HEALMEAL</h1>
          <p className="hero-subtitle">Transform Your Health, One Meal at a Time</p>
          <p className="hero-desc">Plan nutritious meals, track your progress, and achieve your wellness goals</p>
        </div>
      </section>

      {/* Meals Section */}
      <section id="meals" className="meals">
        <div className="meals-content">
          <h2>Delicious & Nutritious Options</h2>
          <div className="meals-grid">
            {[
              { name: 'Fresh Salad Bowl', color: 'meal-green' },
              { name: 'Grilled Salmon', color: 'meal-orange' },
              { name: 'Quinoa Power Bowl', color: 'meal-yellow' },
              { name: 'Berry Smoothie', color: 'meal-purple' },
              { name: 'Avocado Toast', color: 'meal-teal' },
              { name: 'Protein Bowl', color: 'meal-red' }
            ].map((meal, idx) => (
              <div key={idx} className="meal-card">
                <div className={`meal-box ${meal.color}`}>
                  <span className="meal-icon">🍴</span>
                </div>
                <p className="meal-name">{meal.name}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Auth Section */}
      <section id="auth" className="auth">
        <div className="auth-box">
          <div className="auth-toggle">
            <button onClick={() => setAuthMode('login')} className={authMode === 'login' ? 'active' : ''}>Log In</button>
            <button onClick={() => setAuthMode('signup')} className={authMode === 'signup' ? 'active' : ''}>Sign Up</button>
          </div>

          <form onSubmit={(e) => { e.preventDefault(); handleSubmit(); }} className="auth-form">
            {authMode === 'signup' && (
              <div>
                <label>Name</label>
                <input type="text" name="name" value={formData.name} onChange={handleInputChange} placeholder="Your name" />
              </div>
            )}
            <div>
              <label>Email</label>
              <input type="email" name="email" value={formData.email} onChange={handleInputChange} placeholder="your@email.com" />
            </div>
            <div>
              <label>Password</label>
              <input type="password" name="password" value={formData.password} onChange={handleInputChange} placeholder="••••••••" />
            </div>
            <button type="submit">{authMode === 'login' ? 'Log In' : 'Sign Up'}</button>
          </form>
        </div>
      </section>

      {/* Footer */}
      <footer className="footer">
        <div className="footer-content">
          {/* Left side: Logo + description */}
          <div className="footer-left">
            <div className="logo">
              <span className="logo-icon">🍴</span>
              <span className="logo-text">HEALMEAL</span>
            </div>
            <p>Your partner in healthy living and meal planning</p>
          </div>

          {/* Right side: Contact info */}
          <div className="footer-right">
            <h3>Contact Us</h3>
            <div className="contact-item">
              <span className="contact-icon">📧</span>
              <a href="mailto:info@healmeal.com">info@healmeal.com</a>
            </div>
            <div className="contact-item">
              <span className="contact-icon">📞</span>
              <a href="tel:+15551234567">+1 (555) 123-4567</a>
            </div>
            <div className="contact-item">
              <span className="contact-icon">📍</span>
              <span>123 Health Street, Wellness City</span>
            </div>
          </div>
        </div>

        <div className="footer-bottom">
          <p>&copy; 2025 HEALMEAL. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
}
