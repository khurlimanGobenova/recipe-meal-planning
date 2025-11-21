// src/pages/ProfilePage.jsx
import React, { useEffect, useState } from "react";
import axios from "axios";
import "./ProfilePage.css";

export default function ProfilePage() {
  const [user, setUser] = useState({});
  const [mealPlans, setMealPlans] = useState([]);
  const [passwordData, setPasswordData] = useState({ oldPassword: "", newPassword: "" });

  useEffect(() => {
    // Fetch user info
    axios.get("http://localhost:8080/api/users/1") // replace 1 with logged-in user ID
      .then(res => setUser(res.data))
      .catch(err => console.error(err));

    // Fetch meal plan history
    axios.get("http://localhost:8080/api/mealplans/user/1") // replace 1 with logged-in user ID
      .then(res => setMealPlans(res.data))
      .catch(err => console.error(err));
  }, []);

  const handlePasswordChange = (e) => {
    e.preventDefault();
    axios.put("http://localhost:8080/api/users/1/password", passwordData) // replace 1 with logged-in user ID
      .then(() => {
        alert("Password updated successfully!");
        setPasswordData({ oldPassword: "", newPassword: "" });
      })
      .catch(err => console.error(err));
  };

  return (
    <div className="profile-container">
      <h1 className="page-title">Profile</h1>

      <div className="profile-cards-wrapper">
        {/* User Info */}
        <div className="profile-card">
          <h2>User Information</h2>
          <p><strong>Name:</strong> {user.name}</p>
          <p><strong>Email:</strong> {user.email}</p>
          <p><strong>Diet Type:</strong> {user.diet_type}</p>
        </div>

        {/* Change Password */}
        <div className="profile-card">
          <h2>Change Password</h2>
          <form className="password-form" onSubmit={handlePasswordChange}>
            <input
              type="password"
              placeholder="Old Password"
              value={passwordData.oldPassword}
              onChange={(e) => setPasswordData({ ...passwordData, oldPassword: e.target.value })}
              required
            />
            <input
              type="password"
              placeholder="New Password"
              value={passwordData.newPassword}
              onChange={(e) => setPasswordData({ ...passwordData, newPassword: e.target.value })}
              required
            />
            <button type="submit">Update Password</button>
          </form>
        </div>

        {/* Meal Plan History */}
        <div className="profile-card">
          <h2>Meal Plan History</h2>
          {mealPlans.length === 0 ? (
            <p>No meal plans found.</p>
          ) : (
            <ul className="mealplan-list">
              {mealPlans.map(plan => (
                <li key={plan.mealplan_id} className="mealplan-item">
                  <span className="mealplan-title">{plan.title}</span>
                  <span className="mealplan-dates">
                    {plan.start_date} → {plan.end_date}
                  </span>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
}
