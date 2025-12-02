import React, { useState, useEffect } from "react";
import "../styles/MealPlan.css";

function MealPlan() {
  const [mealPlans, setMealPlans] = useState([]);
  const [loadingPlans, setLoadingPlans] = useState(true);
  const [selectedPlan, setSelectedPlan] = useState(null);
  const [mealEntries, setMealEntries] = useState([]);
  const [loadingEntries, setLoadingEntries] = useState(false);

  const [recipes, setRecipes] = useState([]);
  const [showRecipePicker, setShowRecipePicker] = useState(false);
  const [pickerContext, setPickerContext] = useState(null);

  // Fetch all meal plans
  useEffect(() => {
    fetch("http://localhost:8080/api/mealplans")
      .then((res) => res.json())
      .then((data) => {
        setMealPlans(data);
        setLoadingPlans(false);
      })
      .catch((err) => {
        console.error("Error fetching meal plans:", err);
        setLoadingPlans(false);
      });
  }, []);

  // Fetch entries when a plan is selected
  useEffect(() => {
    if (selectedPlan) {
      setLoadingEntries(true);
      fetch(
        `http://localhost:8080/api/mealentries?mealPlanId=${selectedPlan.id}`
      )
        .then((res) => res.json())
        .then((data) => {
          setMealEntries(data);
          setLoadingEntries(false);
        })
        .catch((err) => {
          console.error("Error fetching meal entries:", err);
          setLoadingEntries(false);
        });
    }
  }, [selectedPlan]);

  // Fetch recipes once
  useEffect(() => {
    fetch("http://localhost:8080/api/recipes")
      .then((res) => res.json())
      .then((data) => setRecipes(data))
      .catch((err) => console.error("Error fetching recipes:", err));
  }, []);

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      month: "short",
      day: "numeric",
      year: "numeric",
    });
  };

  // Open recipe picker
  const openRecipePicker = (day, mealType) => {
    setPickerContext({ day, mealType });
    setShowRecipePicker(true);
  };

  // Add selected recipe
  const addMealEntry = (recipeId) => {
    const { day, mealType } = pickerContext;
    const payload = {
      mealPlanId: selectedPlan.id,
      planDate: day,
      mealType,
      recipeId,
    };

    fetch("http://localhost:8080/api/mealentries", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    })
      .then((res) => res.json())
      .then((newEntry) => {
        setMealEntries([...mealEntries, newEntry]);
        setShowRecipePicker(false);
      })
      .catch((err) => console.error("Error adding meal entry:", err));
  };

  // Remove a recipe from a slot
  const removeMealEntry = (entryId) => {
    fetch(`http://localhost:8080/api/mealentries/${entryId}`, {
      method: "DELETE",
    })
      .then(() => {
        setMealEntries(mealEntries.filter((e) => e.id !== entryId));
      })
      .catch((err) => console.error("Error deleting meal entry:", err));
  };

  const daysOfWeek = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday",
  ];
  const mealTypes = ["Breakfast", "Lunch", "Dinner", "Snack"];

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

        {loadingPlans ? (
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
            {mealPlans.map((plan) => (
              <div
                key={plan.id}
                className={`meal-plan-card ${
                  selectedPlan?.id === plan.id ? "selected" : ""
                }`}
                onClick={() => setSelectedPlan(plan)}
              >
                <div className="plan-header">
                  <h3>{plan.title}</h3>
                  <span className="plan-status active">Active</span>
                </div>
                <div className="plan-dates">
                  <div className="date-item">
                    <span className="date-label">Start</span>
                    <span className="date-value">
                      {formatDate(plan.startDate)}
                    </span>
                  </div>
                  <div className="date-separator">→</div>
                  <div className="date-item">
                    <span className="date-label">End</span>
                    <span className="date-value">
                      {formatDate(plan.endDate)}
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {selectedPlan && (
          <div className="weekly-planner">
            <h2>Weekly Meal Planner: {selectedPlan.title}</h2>
            {loadingEntries ? (
              <div className="loading">Loading entries...</div>
            ) : (
              <div className="week-grid">
                {daysOfWeek.map((day) => (
                  <div key={day} className="day-column">
                    <div className="day-header">{day}</div>
                    {mealTypes.map((meal) => {
                      const entries = mealEntries.filter((e) => {
                        const weekday = new Date(e.planDate).toLocaleDateString(
                          "en-US",
                          { weekday: "long" }
                        );
                        return weekday === day && e.mealType === meal;
                      });
                      return (
                        <div key={meal} className="meal-slot">
                          <div className="meal-type">{meal}</div>
                          {entries.length > 0 ? (
                            entries.map((entry) => (
                              <div key={entry.id} className="meal-assigned">
                                <span>
                                  {entry.recipeTitle ||
                                    `Recipe #${entry.recipeId}`}
                                </span>
                                <button
                                  className="btn btn-danger btn-sm"
                                  onClick={() => removeMealEntry(entry.id)}
                                >
                                  ✕
                                </button>
                              </div>
                            ))
                          ) : (
                            <button
                              className="add-meal-btn"
                              onClick={() => openRecipePicker(day, meal)}
                            >
                              + Add
                            </button>
                          )}
                        </div>
                      );
                    })}
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {/* Recipe Picker Modal */}
        {showRecipePicker && (
          <div className="recipe-picker">
            <h3>Select a Recipe</h3>
            <ul>
              {recipes.map((r) => (
                <li key={r.recipe_id}>
                  <button onClick={() => addMealEntry(r.recipe_id)}>
                    {r.title}
                  </button>
                </li>
              ))}
            </ul>
            <button
              className="btn btn-secondary mt-2"
              onClick={() => setShowRecipePicker(false)}
            >
              Cancel
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default MealPlan;
