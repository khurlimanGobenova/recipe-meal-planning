import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import '../styles/Home.css';

function Home() {
  const [stats, setStats] = useState(null);
  const [recentRecipes, setRecentRecipes] = useState([]);
  const [nutritionProgress, setNutritionProgress] = useState(null);
  const [loading, setLoading] = useState(true);

  const currentUser = JSON.parse(localStorage.getItem("currentUser") || "{}");

  useEffect(() => {
    // Fetch stats
    fetch('http://localhost:8080/api/stats')
      .then(res => res.json())
      .then(data => setStats(data))
      .catch(err => console.error('Error fetching stats:', err));

    // Fetch recent recipes
    fetch('http://localhost:8080/api/recipes')
      .then(res => res.json())
      .then(data => {
        setRecentRecipes(data.slice(0, 3));
        setLoading(false);
      })
      .catch(err => {
        console.error('Error fetching recipes:', err);
        setLoading(false);
      });

    // Fetch nutrition progress if user is logged in
    if (currentUser.userId) {
      fetch(`http://localhost:8080/api/nutrition-progress?userId=${currentUser.userId}`)
        .then(res => res.json())
        .then(data => setNutritionProgress(data))
        .catch(err => console.error('Error fetching nutrition progress:', err));
    }
  }, [currentUser.userId]);

  const getNutritionColor = (percent) => {
    if (percent < 50) return '#ff6b6b';
    if (percent < 80) return '#ffd93d';
    if (percent <= 110) return '#6bcf7f';
    return '#ff9f43';
  };

  return (
    <div className="page">
      <div className="container">
        {/* Hero Section */}
        <section className="hero">
          <h1>Welcome to Heal Meal{currentUser.name ? `, ${currentUser.name}` : ''}</h1>
          <p>Plan your meals, track nutrition, and eat healthy</p>
          <div className="hero-actions">
            <Link to="/recipes" className="btn btn-primary">
              Browse Recipes
            </Link>
            <Link to="/meal-plan" className="btn btn-secondary">
              Create Meal Plan
            </Link>
          </div>
        </section>

        {/* Nutrition Progress */}
        {nutritionProgress && currentUser.userId && (
          <section className="nutrition-section">
            <h2>Today's Nutrition Progress</h2>
            <div className="nutrition-grid">
              <div className="nutrition-card">
                <div className="nutrition-header">
                  <span className="nutrition-icon">🔥</span>
                  <span className="nutrition-label">Calories</span>
                </div>
                <div className="nutrition-progress-bar">
                  <div 
                    className="nutrition-fill"
                    style={{
                      width: `${Math.min(nutritionProgress.calories.percent, 100)}%`,
                      backgroundColor: getNutritionColor(nutritionProgress.calories.percent)
                    }}
                  ></div>
                </div>
                <div className="nutrition-values">
                  <span className="current">{nutritionProgress.calories.current}</span>
                  <span className="separator">/</span>
                  <span className="goal">{nutritionProgress.calories.goal}</span>
                </div>
                <div className="nutrition-percent">{nutritionProgress.calories.percent}%</div>
              </div>

              <div className="nutrition-card">
                <div className="nutrition-header">
                  <span className="nutrition-icon">💪</span>
                  <span className="nutrition-label">Protein</span>
                </div>
                <div className="nutrition-progress-bar">
                  <div 
                    className="nutrition-fill"
                    style={{
                      width: `${Math.min(nutritionProgress.protein.percent, 100)}%`,
                      backgroundColor: getNutritionColor(nutritionProgress.protein.percent)
                    }}
                  ></div>
                </div>
                <div className="nutrition-values">
                  <span className="current">{nutritionProgress.protein.current}g</span>
                  <span className="separator">/</span>
                  <span className="goal">{nutritionProgress.protein.goal}g</span>
                </div>
                <div className="nutrition-percent">{nutritionProgress.protein.percent}%</div>
              </div>

              <div className="nutrition-card">
                <div className="nutrition-header">
                  <span className="nutrition-icon">🌾</span>
                  <span className="nutrition-label">Carbs</span>
                </div>
                <div className="nutrition-progress-bar">
                  <div 
                    className="nutrition-fill"
                    style={{
                      width: `${Math.min(nutritionProgress.carbs.percent, 100)}%`,
                      backgroundColor: getNutritionColor(nutritionProgress.carbs.percent)
                    }}
                  ></div>
                </div>
                <div className="nutrition-values">
                  <span className="current">{nutritionProgress.carbs.current}g</span>
                  <span className="separator">/</span>
                  <span className="goal">{nutritionProgress.carbs.goal}g</span>
                </div>
                <div className="nutrition-percent">{nutritionProgress.carbs.percent}%</div>
              </div>

              <div className="nutrition-card">
                <div className="nutrition-header">
                  <span className="nutrition-icon">🥑</span>
                  <span className="nutrition-label">Fat</span>
                </div>
                <div className="nutrition-progress-bar">
                  <div 
                    className="nutrition-fill"
                    style={{
                      width: `${Math.min(nutritionProgress.fat.percent, 100)}%`,
                      backgroundColor: getNutritionColor(nutritionProgress.fat.percent)
                    }}
                  ></div>
                </div>
                <div className="nutrition-values">
                  <span className="current">{nutritionProgress.fat.current}g</span>
                  <span className="separator">/</span>
                  <span className="goal">{nutritionProgress.fat.goal}g</span>
                </div>
                <div className="nutrition-percent">{nutritionProgress.fat.percent}%</div>
              </div>
            </div>
          </section>
        )}

        {/* Stats Cards */}
        {stats && (
          <section className="stats-section">
            <div className="grid grid-4">
              <div className="stat-card">
                <div className="stat-icon">📖</div>
                <div className="stat-value">{stats.totalRecipes}</div>
                <div className="stat-label">Recipes</div>
              </div>
              <div className="stat-card">
                <div className="stat-icon">🥕</div>
                <div className="stat-value">{stats.totalIngredients}</div>
                <div className="stat-label">Ingredients</div>
              </div>
              <div className="stat-card">
                <div className="stat-icon">📅</div>
                <div className="stat-value">{stats.totalMealPlans}</div>
                <div className="stat-label">Meal Plans</div>
              </div>
              <div className="stat-card">
                <div className="stat-icon">👥</div>
                <div className="stat-value">{stats.totalUsers}</div>
                <div className="stat-label">Users</div>
              </div>
            </div>
          </section>
        )}

        {/* Recent Recipes */}
        <section className="recent-recipes">
          <div className="section-header">
            <h2>Recent Recipes</h2>
            <Link to="/recipes" className="view-all">View All →</Link>
          </div>
          
          {loading ? (
            <div className="loading">Loading recipes...</div>
          ) : (
            <div className="grid grid-3">
              {recentRecipes.map(recipe => (
                <Link to={`/recipes/${recipe.id}`} key={recipe.id} className="recipe-card">
                  <div className="recipe-category">{recipe.category}</div>
                  <h3>{recipe.title}</h3>
                  <div className="recipe-meta">
                    <span className="meta-item">
                      <span className="meta-icon">⏱️</span>
                      {recipe.totalTime} min
                    </span>
                    <span className="meta-item">
                      <span className="meta-icon">⭐</span>
                      {recipe.rating ? recipe.rating.toFixed(1) : 'N/A'}
                    </span>
                  </div>
                  <div className={`difficulty-badge ${recipe.difficulty}`}>
                    {recipe.difficulty}
                  </div>
                </Link>
              ))}
            </div>
          )}
        </section>

        {/* Quick Actions */}
        <section className="quick-actions">
          <h2>Quick Actions</h2>
          <div className="grid grid-2">
            <Link to="/meal-plan" className="action-card">
              <div className="action-icon">📅</div>
              <div>
                <h3>Plan Your Week</h3>
                <p>Create a weekly meal plan</p>
              </div>
            </Link>
            <Link to="/shopping-list" className="action-card">
              <div className="action-icon">🛒</div>
              <div>
                <h3>Shopping List</h3>
                <p>Generate your shopping list</p>
              </div>
            </Link>
          </div>
        </section>
      </div>
    </div>
  );
}

export default Home;
