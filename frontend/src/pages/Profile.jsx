import React, { useState, useEffect } from 'react';
import '../styles/Profile.css';

function Profile() {
  const [user, setUser] = useState({
    name: 'John Doe',
    email: 'john.doe@email.com',
    diet_type: 'vegetarian',
    calorieGoal: 2000,
    proteinGoal: 150,
    carbsGoal: 200,
    fatsGoal: 65
  });

  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [mealPlans, setMealPlans] = useState([]);

  useEffect(() => {
    // Fetch meal plans for calendar
    fetch('http://localhost:8080/api/mealplans')
      .then(res => res.json())
      .then(data => setMealPlans(data))
      .catch(err => console.error('Error:', err));
  }, []);

  const getDaysInMonth = (date) => {
    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    
    const days = [];
    // Empty cells for days before month starts
    for (let i = 0; i < firstDay; i++) {
      days.push(null);
    }
    // Days of the month
    for (let i = 1; i <= daysInMonth; i++) {
      days.push(i);
    }
    return days;
  };

  const monthNames = ['January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'];

  const previousMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1));
  };

  const nextMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1));
  };

  const hasMealPlan = (day) => {
    const dateStr = `${currentMonth.getFullYear()}-${String(currentMonth.getMonth() + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
    return mealPlans.some(plan => {
      const startDate = new Date(plan.startDate).toISOString().split('T')[0];
      const endDate = new Date(plan.endDate).toISOString().split('T')[0];
      return dateStr >= startDate && dateStr <= endDate;
    });
  };

  return (
    <div className="page">
      <div className="container">
        <h1>Profile</h1>

        <div className="profile-grid">
          {/* User Info Card */}
          <div className="card profile-info">
            <h2>Personal Information</h2>
            <div className="info-group">
              <label>Name</label>
              <p>{user.name}</p>
            </div>
            <div className="info-group">
              <label>Email</label>
              <p>{user.email}</p>
            </div>
            <div className="info-group">
              <label>Diet Type</label>
              <p className="diet-badge">{user.diet_type}</p>
            </div>
            <button className="btn btn-secondary mt-2">Edit Profile</button>
          </div>

          {/* Nutrition Goals Card */}
          <div className="card nutrition-goals">
            <h2>Daily Nutrition Goals</h2>
            <div className="goal-item">
              <div className="goal-label">
                <span>Calories</span>
                <span className="goal-value">{user.calorieGoal} kcal</span>
              </div>
              <div className="goal-bar">
                <div className="goal-progress" style={{width: '75%'}}></div>
              </div>
            </div>
            <div className="goal-item">
              <div className="goal-label">
                <span>Protein</span>
                <span className="goal-value">{user.proteinGoal}g</span>
              </div>
              <div className="goal-bar">
                <div className="goal-progress" style={{width: '60%'}}></div>
              </div>
            </div>
            <div className="goal-item">
              <div className="goal-label">
                <span>Carbs</span>
                <span className="goal-value">{user.carbsGoal}g</span>
              </div>
              <div className="goal-bar">
                <div className="goal-progress" style={{width: '80%'}}></div>
              </div>
            </div>
            <div className="goal-item">
              <div className="goal-label">
                <span>Fats</span>
                <span className="goal-value">{user.fatsGoal}g</span>
              </div>
              <div className="goal-bar">
                <div className="goal-progress" style={{width: '70%'}}></div>
              </div>
            </div>
          </div>
        </div>

        {/* Calendar Section */}
        <div className="card calendar-section">
          <div className="calendar-header">
            <h2>Meal Plan Calendar</h2>
            <div className="calendar-controls">
              <button onClick={previousMonth} className="btn-icon">←</button>
              <span className="current-month">
                {monthNames[currentMonth.getMonth()]} {currentMonth.getFullYear()}
              </span>
              <button onClick={nextMonth} className="btn-icon">→</button>
            </div>
          </div>

          <div className="calendar">
            <div className="calendar-grid">
              {['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map(day => (
                <div key={day} className="calendar-day-header">{day}</div>
              ))}
              {getDaysInMonth(currentMonth).map((day, index) => (
                <div
                  key={index}
                  className={`calendar-day ${day ? '' : 'empty'} ${hasMealPlan(day) ? 'has-meal-plan' : ''}`}
                >
                  {day && (
                    <>
                      <span className="day-number">{day}</span>
                      {hasMealPlan(day) && <span className="meal-indicator">🍽️</span>}
                    </>
                  )}
                </div>
              ))}
            </div>
          </div>

          <div className="calendar-legend">
            <span className="legend-item">
              <span className="legend-dot has-plan"></span>
              Meal plan scheduled
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Profile;