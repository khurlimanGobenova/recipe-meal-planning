import React, { useState } from "react";
import "../styles/StartPage.css"; 

const StartPage = () => {
  const [isLogin, setIsLogin] = useState(true);

  return (
    <div className="startpage-container">
      {/* Intro Section */}
      <div className="intro-section">
        <h1>🥗 Health Meal Planner</h1>
        <p>
          Plan your meals, track nutrition, and achieve your health goals with ease.
        </p>
        <p>
          Our app helps you create personalized meal plans, generate shopping lists,
          and discover healthy recipes tailored to your lifestyle.
        </p>
      </div>

      {/* Auth Section */}
      <div className="auth-section">
        <div className="auth-toggle">
          <button
            className={isLogin ? "active" : ""}
            onClick={() => setIsLogin(true)}
          >
            Login
          </button>
          <button
            className={!isLogin ? "active" : ""}
            onClick={() => setIsLogin(false)}
          >
            Sign Up
          </button>
        </div>

        {isLogin ? (
          <form className="auth-form">
            <h2>Login</h2>
            <input type="email" placeholder="Email" required />
            <input type="password" placeholder="Password" required />
            <button type="submit">Login</button>
          </form>
        ) : (
          <form className="auth-form">
            <h2>Sign Up</h2>
            <input type="text" placeholder="Name" required />
            <input type="email" placeholder="Email" required />
            <input type="password" placeholder="Password" required />
            <select>
              <option value="">Select Diet Type</option>
              <option value="vegan">Vegan</option>
              <option value="vegetarian">Vegetarian</option>
              <option value="keto">Keto</option>
              <option value="balanced">Balanced</option>
            </select>
            <button type="submit">Create Account</button>
          </form>
        )}
      </div>
    </div>
  );
};

export default StartPage;
