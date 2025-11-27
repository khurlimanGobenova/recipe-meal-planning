import React, { useState, useEffect } from 'react';
import { PieChart, Pie, Cell, Legend } from "recharts";   // ✅ Import Recharts
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
    fetch('http://localhost:8080/api/mealplans')
      .then(res => res.json())
      .then(data => setMealPlans(data))
      .catch(err => console.error('Error:', err));
  }, []);

  const COLORS = ["#2f855a", "#3182ce", "#d69e2e", "#e53e3e"];

  const nutritionData = [
    { name: "Calories", value: user.calorieGoal },
    { name: "Protein", value: user.proteinGoal },
    { name: "Carbs", value: user.carbsGoal },
    { name: "Fats", value: user.fatsGoal },
  ];

  const getDaysInMonth = (date) => {
    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    
    const days = [];
    for (let i = 0; i < firstDay; i++) {
      days.push(null);
    }
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

          {/* Nutrition Goals Card - REPLACED with PieChart */}
          {/* Nutrition Goals Card - Daily & Weekly */}
<div className="card nutrition-goals">
  <h2>Nutrition Goals</h2>
  <div className="nutrition-charts">
    {/* Daily Goals */}
    <div className="chart-container">
      <h3>Daily Goals</h3>
      <PieChart width={300} height={300}>
        <Pie
          data={[
            { name: "Calories", value: user.calorieGoal },
            { name: "Protein", value: user.proteinGoal },
            { name: "Carbs", value: user.carbsGoal },
            { name: "Fats", value: user.fatsGoal },
          ]}
          cx={150}
          cy={150}
          innerRadius={60}
          outerRadius={100}
          paddingAngle={5}
          dataKey="value"
        >
          <Cell fill="#2f855a" /> {/* Calories */}
          <Cell fill="#3182ce" /> {/* Protein */}
          <Cell fill="#d69e2e" /> {/* Carbs */}
          <Cell fill="#e53e3e" /> {/* Fats */}
        </Pie>
        <Legend />
      </PieChart>
    </div>

    {/* Weekly Goals */}
    <div className="chart-container">
      <h3>Weekly Goals</h3>
      <PieChart width={300} height={300}>
        <Pie
          data={[
            { name: "Calories", value: user.calorieGoal * 7 },
            { name: "Protein", value: user.proteinGoal * 7 },
            { name: "Carbs", value: user.carbsGoal * 7 },
            { name: "Fats", value: user.fatsGoal * 7 },
          ]}
          cx={150}
          cy={150}
          innerRadius={60}
          outerRadius={100}
          paddingAngle={5}
          dataKey="value"
        >
          <Cell fill="#2f855a" /> {/* Calories */}
          <Cell fill="#3182ce" /> {/* Protein */}
          <Cell fill="#d69e2e" /> {/* Carbs */}
          <Cell fill="#e53e3e" /> {/* Fats */}
        </Pie>
        <Legend />
      </PieChart>
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
