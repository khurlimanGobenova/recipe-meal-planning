// src/pages/StartPage.jsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../styles/StartPage.css";

const StartPage = () => {
  const navigate = useNavigate();
  const [isLogin, setIsLogin] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const [loginData, setLoginData] = useState({
    email: "",
    password: "",
  });

  const [signupData, setSignupData] = useState({
    name: "",
    email: "",
    password: "",
    diet_type: "",
  });

  // ============================
  // LOGIN
  // ============================
  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      console.log('Attempting login with:', loginData.email);
      
      // POST to backend login endpoint
      const response = await axios.post('http://localhost:8080/api/users/login', {
        email: loginData.email,
        password: loginData.password
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      });

      console.log('Login response:', response.data);

      // Check if userId is returned
      if (response.data && response.data.userId) {
        // Store user data in localStorage
        localStorage.setItem('userId', response.data.userId);
        localStorage.setItem('isAuthenticated', 'true');
        
        // Store full user data if available
        if (response.data.name || loginData.email) {
          localStorage.setItem('user', JSON.stringify({
            id: response.data.userId,
            name: response.data.name,
            email: loginData.email
          }));
        }
        
        console.log('Login successful, redirecting to /home');
        navigate('/home');
      } else {
        setError('Login failed. No user ID returned.');
      }
    } catch (err) {
      console.error('Login error:', err);
      console.error('Error response:', err.response);
      
      const message = err.response?.data?.error || 
                     err.response?.data?.message || 
                     err.message ||
                     'Invalid email or password';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  // ============================
  // SIGNUP
  // ============================
  const handleSignUp = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      console.log('Attempting signup with:', signupData.email);
      
      // POST to backend signup endpoint
      const response = await axios.post('http://localhost:8080/api/users/signup', {
        name: signupData.name,
        email: signupData.email,
        password: signupData.password,
        diet_type: signupData.diet_type
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      });

      console.log('Signup response:', response.data);

      // Check if userId is returned
      if (response.data && response.data.userId) {
        // Store user data in localStorage
        localStorage.setItem('userId', response.data.userId);
        localStorage.setItem('isAuthenticated', 'true');
        localStorage.setItem('user', JSON.stringify({
          id: response.data.userId,
          name: signupData.name,
          email: signupData.email,
          diet_type: signupData.diet_type
        }));
        
        console.log('Signup successful, redirecting to /home');
        navigate('/home');
      } else {
        setError('Signup failed. No user ID returned.');
      }
    } catch (err) {
      console.error('Signup error:', err);
      console.error('Error response:', err.response);
      
      const message = err.response?.data?.error || 
                     err.response?.data?.message || 
                     err.message ||
                     'Registration failed. Please try again.';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="startpage-container">
      <div className="intro-section">
        <h1>HEAL MEAL</h1>
        <p>Plan meals, track nutrition, and reach your goals effortlessly.</p>
      </div>

      <div className="auth-section">
        {/* Toggle Login/Signup */}
        <div className="auth-toggle">
          <button
            className={isLogin ? "active" : ""}
            onClick={() => {
              setIsLogin(true);
              setError("");
            }}
          >
            Login
          </button>
          <button
            className={!isLogin ? "active" : ""}
            onClick={() => {
              setIsLogin(false);
              setError("");
            }}
          >
            Sign Up
          </button>
        </div>

        {/* Error Message */}
        {error && <div className="auth-error">{error}</div>}

        {isLogin ? (
          <form className="auth-form" onSubmit={handleLogin}>
            <h2>Login</h2>
            <input
              type="email"
              placeholder="Email"
              value={loginData.email}
              onChange={(e) =>
                setLoginData({ ...loginData, email: e.target.value })
              }
              required
            />
            <input
              type="password"
              placeholder="Password"
              value={loginData.password}
              onChange={(e) =>
                setLoginData({ ...loginData, password: e.target.value })
              }
              required
            />
            <button type="submit" disabled={loading}>
              {loading ? "Logging in..." : "Login"}
            </button>
          </form>
        ) : (
          <form className="auth-form" onSubmit={handleSignUp}>
            <h2>Sign Up</h2>
            <input
              type="text"
              placeholder="Name"
              value={signupData.name}
              onChange={(e) =>
                setSignupData({ ...signupData, name: e.target.value })
              }
              required
            />
            <input
              type="email"
              placeholder="Email"
              value={signupData.email}
              onChange={(e) =>
                setSignupData({ ...signupData, email: e.target.value })
              }
              required
            />
            <input
              type="password"
              placeholder="Password"
              value={signupData.password}
              onChange={(e) =>
                setSignupData({ ...signupData, password: e.target.value })
              }
              required
            />
            <select
              value={signupData.diet_type}
              onChange={(e) =>
                setSignupData({ ...signupData, diet_type: e.target.value })
              }
              required
            >
              <option value="">Select Diet Type</option>
              <option value="vegan">Vegan</option>
              <option value="vegetarian">Vegetarian</option>
              <option value="keto">Keto</option>
              <option value="balanced">Balanced</option>
            </select>
            <button type="submit" disabled={loading}>
              {loading ? "Creating account..." : "Create Account"}
            </button>
          </form>
        )}
      </div>
    </div>
  );
};

export default StartPage;
