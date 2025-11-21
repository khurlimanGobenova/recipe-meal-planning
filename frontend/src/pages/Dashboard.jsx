// src/pages/Dashboard.jsx
import React, { useEffect, useState } from "react";
import { PieChart, Pie, Cell, Legend } from "recharts";
import axios from "axios";
import "../styles/Dashboard.css";

const Dashboard = () => {
  const [dailyNutrition, setDailyNutrition] = useState({});
  const [weeklyNutrition, setWeeklyNutrition] = useState({});
  const [mealSuggestions, setMealSuggestions] = useState([]);

  const COLORS = ["#2f855a", "#3182ce", "#d69e2e", "#e53e3e"];

  useEffect(() => {
    // Fetch nutrition totals
    axios.get("http://localhost:8080/api/nutrition/user/1")
      .then(res => {
        setDailyNutrition(res.data.daily);
        setWeeklyNutrition(res.data.weekly);
      })
      .catch(err => console.error(err));

    // Fetch meal suggestions
    axios.get("http://localhost:8080/api/recipes/suggestions/1")
      .then(res => setMealSuggestions(res.data))
      .catch(err => console.error(err));
  }, []);

  const dailyData = [
    { name: "Calories", value: dailyNutrition.calories || 0 },
    { name: "Protein", value: dailyNutrition.protein || 0 },
    { name: "Carbs", value: dailyNutrition.carbs || 0 },
    { name: "Fat", value: dailyNutrition.fat || 0 },
  ];

  const weeklyData = [
    { name: "Calories", value: weeklyNutrition.calories || 0 },
    { name: "Protein", value: weeklyNutrition.protein || 0 },
    { name: "Carbs", value: weeklyNutrition.carbs || 0 },
    { name: "Fat", value: weeklyNutrition.fat || 0 },
  ];

  return (
    <div className="dashboard-container">
      <h1 className="page-title">Welcome to Your Dashboard</h1>

      {/* Nutrition Tracking */}
      <div className="nutrition-section">
        <div className="chart-container">
          <h2>Daily Nutrition</h2>
          <PieChart width={300} height={300}>
            <Pie
              data={dailyData}
              cx={150}
              cy={150}
              innerRadius={60}
              outerRadius={100}
              paddingAngle={5}
              dataKey="value"
            >
              {dailyData.map((entry, index) => (
                <Cell key={index} fill={COLORS[index % COLORS.length]} />
              ))}
            </Pie>
            <Legend />
          </PieChart>
        </div>

        <div className="chart-container">
          <h2>Weekly Nutrition</h2>
          <PieChart width={300} height={300}>
            <Pie
              data={weeklyData}
              cx={150}
              cy={150}
              innerRadius={60}
              outerRadius={100}
              paddingAngle={5}
              dataKey="value"
            >
              {weeklyData.map((entry, index) => (
                <Cell key={index} fill={COLORS[index % COLORS.length]} />
              ))}
            </Pie>
            <Legend />
          </PieChart>
        </div>
      </div>

      {/* Meal Suggestions */}
      <div className="meal-suggestions">
        <h2>Meal Suggestions</h2>
        <div className="meal-cards">
          {mealSuggestions.map((meal) => (
            <div key={meal.recipe_id} className="meal-card">
              <h3>{meal.title}</h3>
              <p>{meal.category}</p>
              <button>Add to Calendar</button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
