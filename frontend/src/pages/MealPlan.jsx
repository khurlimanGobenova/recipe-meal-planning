import React, { useState, useEffect } from 'react';
import '../styles/MealPlan.css';

function MealPlan() {
  const [mealPlans, setMealPlans] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('http://localhost:8080/api/mealplans')
      .then(res => res.json())
      .then(data => {
        setMealPlans(data);
        setLoading(false);
      })
      .catch(err => {
        console.error('Error:', err);
        setLoading(false);
      });
  }, []);

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric'
    });
  };

  return (
    <div className="page">
      <div className="container">
        <div className="page-header">
          <div>
            <h1>Meal Plans</h1>
            <p>Plan your meals for the week</p>
          </div>
          <button className="btn btn-primary">+ New Meal Plan</button>
        </div>

        {loading ? (
          <div className="loading">Loading meal plans...</div>
        ) : mealPlans.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">📅</div>
            <h3>No Meal Plans Yet</h3>
            <p>Create your first meal plan to get started</p>
            <button className="btn btn-primary mt-2">Create Meal Plan</button>
          </div>
        ) : (
          <div className="meal-plans-grid">
            {mealPlans.map(plan => (
              <div key={plan.id} className="meal-plan-card">
                <div className="plan-header">
                  <h3>{plan.title}</h3>
                  <span className="plan-status active">Active</span>
                </div>
                
                <div className="plan-dates">
                  <div className="date-item">
                    <span className="date-label">Start</span>
                    <span className="date-value">{formatDate(plan.startDate)}</span>
                  </div>
                  <div className="date-separator">→</div>
                  <div className="date-item">
                    <span className="date-label">End</span>
                    <span className="date-value">{formatDate(plan.endDate)}</span>
                  </div>
                </div>

                <div className="plan-actions">
                  <button className="btn btn-secondary">View Details</button>
                  <button className="btn btn-secondary">Edit</button>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Weekly Planner Template */}
        <div className="weekly-planner">
          <h2>Weekly Meal Planner</h2>
          <div className="week-grid">
            {['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'].map(day => (
              <div key={day} className="day-column">
                <div className="day-header">{day}</div>
                
                {['Breakfast', 'Lunch', 'Dinner'].map(meal => (
                  <div key={meal} className="meal-slot">
                    <div className="meal-type">{meal}</div>
                    <button className="add-meal-btn">+ Add</button>
                  </div>
                ))}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default MealPlan;