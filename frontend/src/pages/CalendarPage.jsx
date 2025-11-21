import React, { useEffect, useState } from "react";
import axios from "axios";

export default function CalendarPage() {
  const [mealPlans, setMealPlans] = useState([]);
  const [recipes, setRecipes] = useState([]);

  useEffect(() => {
    // Load user's meal plans
    axios.get("http://localhost:8080/api/mealplans/user/1")
      .then(res => setMealPlans(res.data))
      .catch(err => console.error(err));

    // Load available recipes for selection
    axios.get("http://localhost:8080/api/recipes")
      .then(res => setRecipes(res.data))
      .catch(err => console.error(err));
  }, []);

  const handleAddMeal = (mealplanId, date, mealType, recipeId) => {
    axios.post("http://localhost:8080/api/mealentries", {
      mealplan_id: mealplanId,
      recipe_id: recipeId,
      date,
      meal_type: mealType
    })
    .then(() => {
      // Refresh meal plans after adding
      return axios.get("http://localhost:8080/api/mealplans/user/1");
    })
    .then(res => setMealPlans(res.data))
    .catch(err => console.error(err));
  };

  return (
    <div className="app-page-container">
      <h1 className="page-title">Calendar</h1>
      <div className="calendar-grid">
        {mealPlans.map(plan => (
          <div key={plan.mealplan_id} className="calendar-day-card">
            <div className="calendar-header">{plan.title}</div>
            <div className="meal-slots">
              {["Breakfast", "Lunch", "Dinner"].map(mealType => (
                <div key={mealType} className="meal-item">
                  <span>{mealType}</span>
                  <select
                    onChange={(e) =>
                      handleAddMeal(plan.mealplan_id, plan.start_date, mealType.toLowerCase(), e.target.value)
                    }
                  >
                    <option value="">Add Recipe</option>
                    {recipes.map(r => (
                      <option key={r.recipe_id} value={r.recipe_id}>{r.title}</option>
                    ))}
                  </select>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
