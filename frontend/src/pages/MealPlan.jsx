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

  const [showCreateModal, setShowCreateModal] = useState(false);
  const [newPlanTitle, setNewPlanTitle] = useState("");
  const [newPlanStartDate, setNewPlanStartDate] = useState("");
  const [newPlanEndDate, setNewPlanEndDate] = useState("");

  const currentUser = JSON.parse(localStorage.getItem("currentUser") || "{}");

  // Fetch meal plans for current user
  useEffect(() => {
    if (currentUser.userId) {
      fetch(`http://localhost:8080/api/mealplans?userId=${currentUser.userId}`)
        .then((res) => res.json())
        .then((data) => {
          setMealPlans(data);
          setLoadingPlans(false);
        })
        .catch((err) => {
          console.error("Error fetching meal plans:", err);
          setLoadingPlans(false);
        });
    } else {
      setLoadingPlans(false);
    }
  }, [currentUser.userId]);

  // Fetch entries when a plan is selected
  useEffect(() => {
    if (selectedPlan) {
      setLoadingEntries(true);
      fetch(`http://localhost:8080/api/mealentries?mealPlanId=${selectedPlan.id}`)
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

  // Get dates for the week
  const getWeekDates = (startDate) => {
    const dates = [];
    const start = new Date(startDate + "T00:00:00");
    for (let i = 0; i < 7; i++) {
      const date = new Date(start);
      date.setDate(start.getDate() + i);
      dates.push(date.toISOString().split("T")[0]);
    }
    return dates;
  };

  // Create new meal plan
  const createMealPlan = (e) => {
    e.preventDefault();
    
    if (!newPlanTitle || !newPlanStartDate || !newPlanEndDate) {
      alert("Please fill in all fields");
      return;
    }

    fetch("http://localhost:8080/api/mealplans", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        userId: currentUser.userId,
        title: newPlanTitle,
        startDate: newPlanStartDate,
        endDate: newPlanEndDate,
      }),
    })
      .then((res) => res.json())
      .then((newPlan) => {
        setMealPlans([newPlan, ...mealPlans]);
        setShowCreateModal(false);
        setNewPlanTitle("");
        setNewPlanStartDate("");
        setNewPlanEndDate("");
        alert("Meal plan created successfully!");
      })
      .catch((err) => {
        console.error("Error creating meal plan:", err);
        alert("Failed to create meal plan");
      });
  };

  // Open recipe picker
  const openRecipePicker = (date, mealType) => {
    setPickerContext({ date, mealType });
    setShowRecipePicker(true);
  };

  // Add selected recipe
  const addMealEntry = (recipeId) => {
    const { date, mealType } = pickerContext;
    const payload = {
      mealPlanId: selectedPlan.id,
      planDate: date,
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

  const mealTypes = ["Breakfast", "Lunch", "Dinner"];
  
  // Get week dates if a plan is selected
  const weekDates = selectedPlan ? getWeekDates(selectedPlan.startDate) : [];

  return (
    <div className="page">
      <div className="container">
        <div className="page-header">
          <div>
            <h1>Meal Plans</h1>
            <p>Plan your meals for the week</p>
          </div>
          <button 
            className="btn btn-primary"
            onClick={() => setShowCreateModal(true)}
          >
            + New Meal Plan
          </button>
        </div>

        {loadingPlans ? (
          <div className="loading">Loading meal plans...</div>
        ) : mealPlans.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">📅</div>
            <h3>No Meal Plans Yet</h3>
            <p>Create your first meal plan to get started</p>
            <button 
              className="btn btn-primary mt-2"
              onClick={() => setShowCreateModal(true)}
            >
              Create Meal Plan
            </button>
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
                {weekDates.map((date, index) => (
                  <div key={date} className="day-column">
                    <div className="day-header">
                      {new Date(date + "T00:00:00").toLocaleDateString("en-US", { weekday: "short" })}
                      <br />
                      <small>{new Date(date + "T00:00:00").toLocaleDateString("en-US", { month: "short", day: "numeric" })}</small>
                    </div>
                    {mealTypes.map((meal) => {
                      const entries = mealEntries.filter((e) => 
                        e.planDate === date && e.mealType === meal
                      );
                      return (
                        <div key={meal} className="meal-slot">
                          <div className="meal-type">{meal}</div>
                          {entries.length > 0 ? (
                            entries.map((entry) => (
                              <div key={entry.id} className="meal-assigned">
                                <span>{entry.recipeTitle}</span>
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
                              onClick={() => openRecipePicker(date, meal)}
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

        {/* Create Meal Plan Modal */}
        {showCreateModal && (
          <div className="modal-overlay" onClick={() => setShowCreateModal(false)}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
              <h3>Create New Meal Plan</h3>
              <form onSubmit={createMealPlan}>
                <div className="form-group">
                  <label>Plan Title</label>
                  <input
                    type="text"
                    value={newPlanTitle}
                    onChange={(e) => setNewPlanTitle(e.target.value)}
                    placeholder="e.g., My Weekly Plan"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Start Date</label>
                  <input
                    type="date"
                    value={newPlanStartDate}
                    onChange={(e) => setNewPlanStartDate(e.target.value)}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>End Date (7 days recommended)</label>
                  <input
                    type="date"
                    value={newPlanEndDate}
                    onChange={(e) => setNewPlanEndDate(e.target.value)}
                    required
                  />
                </div>
                <div className="modal-actions">
                  <button type="button" className="btn btn-secondary" onClick={() => setShowCreateModal(false)}>
                    Cancel
                  </button>
                  <button type="submit" className="btn btn-primary">
                    Create Plan
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        {/* Recipe Picker Modal */}
        {showRecipePicker && (
          <div className="modal-overlay" onClick={() => setShowRecipePicker(false)}>
            <div className="modal-content recipe-picker" onClick={(e) => e.stopPropagation()}>
              <h3>Select a Recipe</h3>
              <div className="recipe-list">
                {recipes.map((r) => (
                  <div 
                    key={r.id} 
                    className="recipe-item"
                    onClick={() => addMealEntry(r.id)}
                  >
                    <div className="recipe-item-title">{r.title}</div>
                    <div className="recipe-item-meta">
                      <span>{r.category}</span>
                      <span>⏱️ {r.totalTime}min</span>
                    </div>
                  </div>
                ))}
              </div>
              <button
                className="btn btn-secondary mt-2"
                onClick={() => setShowRecipePicker(false)}
              >
                Cancel
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default MealPlan;
