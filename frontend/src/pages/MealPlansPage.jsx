import React, { useState, useEffect } from 'react';


const CalendarIcon = (props) => (/* ... SVG definition ... */ <svg {...props} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>);
const UserIcon = (props) => (/* ... SVG definition ... */ <svg {...props} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>);
const BookIcon = (props) => (/* ... SVG definition ... */ <svg {...props} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20"></path></svg>);
const ShoppingCartIcon = (props) => (/* ... SVG definition ... */ <svg {...props} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="9" cy="21" r="1"></circle><circle cx="20" cy="21" r="1"></circle><path d="M1 1h4l2.68 12.08a2 2 0 0 0 2 1.92h9.72a2 2 0 0 0 2-1.92L23 6H6"></path></svg>);
const TrendingUpIcon = (props) => (/* ... SVG definition ... */ <svg {...props} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><polyline points="22 7 13.5 15.5 10 12 2 20"></polyline><polyline points="16 7 22 7 22 13"></polyline></svg>);
const ClockIcon = (props) => (/* ... SVG definition ... */ <svg {...props} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg>);
const HeartIcon = (props) => (/* ... SVG definition ... */ <svg {...props} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"></path></svg>);
const ChefHatIcon = (props) => (/* ... SVG definition ... */ <svg {...props} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M12 3a2 2 0 1 0 0 4c1.1 0 2 .9 2 2v2H7a2 2 0 1 0 0 4h10c.5 0 .9.2 1.3.6l.8.8c.4.4.8 1 1.2 1.6H3.4c.4-.6.8-1.2 1.2-1.6l.8-.8c.4-.4.8-.6 1.3-.6h10V9c0-1.1-.9-2-2-2a2 2 0 0 0-2-2Z"></path><path d="M12 15a4 4 0 0 0-4 4v2h8v-2a4 4 0 0 0-4-4Z"></path><path d="M3 22h18"></path></svg>);
const PlusIcon = (props) => (/* ... SVG definition ... */ <svg {...props} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>);
const Trash2Icon = (props) => (/* ... SVG definition ... */ <svg {...props} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M3 6h18"></path><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"></path><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"></path><line x1="10" y1="11" x2="10" y2="17"></line><line x1="14" y1="11" x2="14" y2="17"></line></svg>);
const CheckIcon = (props) => (/* ... SVG definition ... */ <svg {...props} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><polyline points="20 6 9 17 4 12"></polyline></svg>);
const XIcon = (props) => (/* ... SVG definition ... */ <svg {...props} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>);


const MealPlannerApp = () => {
  const [currentPage, setCurrentPage] = useState('landing');
  const [isLogin, setIsLogin] = useState(true);
  const [user, setUser] = useState(null);
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [mealPlan, setMealPlan] = useState({});
  const [shoppingList, setShoppingList] = useState([]);
  const [customItem, setCustomItem] = useState('');

  // Sample data and functions (omitted for brevity, they are unchanged)
  const sampleRecipes = [
    { id: 1, name: 'Grilled Chicken Salad', calories: 350, protein: 35, carbs: 15, fats: 18, time: 25, image: '🥗', ingredients: ['Chicken breast 200g', 'Mixed greens 100g', 'Olive oil 15ml', 'Cherry tomatoes 50g'] },
    { id: 2, name: 'Salmon with Vegetables', calories: 420, protein: 38, carbs: 20, fats: 22, time: 30, image: '🐟', ingredients: ['Salmon fillet 150g', 'Broccoli 100g', 'Carrots 80g', 'Lemon 1pc'] },
    { id: 3, name: 'Quinoa Buddha Bowl', calories: 380, protein: 15, carbs: 45, fats: 16, time: 20, image: '🥙', ingredients: ['Quinoa 100g', 'Chickpeas 80g', 'Avocado 50g', 'Spinach 50g'] },
    { id: 4, name: 'Beef Stir Fry', calories: 450, protein: 40, carbs: 30, fats: 20, time: 35, image: '🍜', ingredients: ['Beef strips 180g', 'Bell peppers 100g', 'Soy sauce 20ml', 'Rice 100g'] },
    { id: 5, name: 'Greek Yogurt Parfait', calories: 280, protein: 20, carbs: 35, fats: 8, time: 10, image: '🥣', ingredients: ['Greek yogurt 200g', 'Granola 50g', 'Berries 100g', 'Honey 15g'] },
    { id: 6, name: 'Turkey Sandwich', calories: 320, protein: 28, carbs: 38, fats: 10, time: 15, image: '🥪', ingredients: ['Whole wheat bread 2 slices', 'Turkey breast 100g', 'Lettuce 30g', 'Tomato 50g'] }
  ];

  const userData = {
    name: 'Sarah Johnson',
    email: 'sarah.j@email.com',
    dailyGoal: { calories: 2000, protein: 150, carbs: 200, fats: 65 },
    weeklyGoal: { calories: 14000, protein: 1050, carbs: 1400, fats: 455 },
    currentDay: { calories: 1250, protein: 95, carbs: 130, fats: 42 },
    currentWeek: { calories: 8400, protein: 640, carbs: 890, fats: 285 },
    mealHistory: [
      { date: '2024-11-13', meals: ['Grilled Chicken Salad', 'Salmon with Vegetables'] },
      { date: '2024-11-12', meals: ['Quinoa Buddha Bowl', 'Beef Stir Fry'] }
    ],
    favoriteRecipes: ['Grilled Chicken Salad', 'Salmon with Vegetables', 'Greek Yogurt Parfait']
  };

  useEffect(() => {
    if (user) {
      generateShoppingList();
    }
  }, [mealPlan, user]);

  const handleAuth = (e) => {
    e.preventDefault();
    setUser(userData);
    setCurrentPage('dashboard');
  };

  const generateShoppingList = () => {
    const ingredients = {};
    Object.values(mealPlan).forEach(day => {
      ['breakfast', 'lunch', 'dinner'].forEach(mealType => {
        if (day[mealType]) {
          day[mealType].ingredients.forEach(ing => {
            const match = ing.match(/^(.+?)\s*(\d+.*)?$/);
            const item = match ? match[1].trim() : ing.trim();
            const amount = match && match[2] ? match[2].trim() : '';
            
            const key = item; 
            if (!ingredients[key]) {
              ingredients[key] = { item: key, amount: amount, checked: false };
            }
          });
        }
      });
    });
    setShoppingList(Object.values(ingredients));
  };

  const addMealToPlan = (date, mealType, recipe) => {
    const dateKey = date.toISOString().split('T')[0];
    setMealPlan(prev => ({
      ...prev,
      [dateKey]: {
        ...prev[dateKey],
        [mealType]: recipe
      }
    }));
  };

  const removeMealFromPlan = (date, mealType) => {
    const dateKey = date.toISOString().split('T')[0];
    setMealPlan(prev => {
      const updated = { ...prev };
      if (updated[dateKey]) {
        delete updated[dateKey][mealType];
        if (Object.keys(updated[dateKey]).length === 0) {
          delete updated[dateKey];
        }
      }
      return updated;
    });
  };

  const CircularProgress = ({ value, max, label, color }) => {
    const percentage = (value / max) * 100;
    const circumference = 2 * Math.PI * 45;
    const strokeDashoffset = circumference - (Math.min(percentage, 100) / 100) * circumference; 

    return (
      <div className="progress-wrapper">
        <svg width="120" height="120" className="progress-svg">
          {/* Background Circle */}
          <circle cx="60" cy="60" r="45" stroke="#e5e7eb" strokeWidth="10" fill="none" />
          {/* Progress Circle */}
          <circle 
            cx="60" cy="60" r="45" 
            stroke={color} 
            strokeWidth="10" 
            fill="none"
            strokeDasharray={circumference}
            strokeDashoffset={strokeDashoffset}
            strokeLinecap="round"
          />
        </svg>
        <div className="progress-text">
          <div className="progress-value">{value}</div>
          <div className="progress-label">{label}</div>
          <div className="progress-max">of {max}</div>
        </div>
      </div>
    );
  };
  
  // Landing Page
  if (currentPage === 'landing') {
    return (
      <div className="landing-page-container">
        <div className="content-wrapper">
          <div className="landing-grid">
            {/* Info Section */}
            <div className="info-section">
              <div className="logo-group">
                <ChefHatIcon className="icon-large icon-green" />
                <h1 className="main-title">MealFlow</h1>
              </div>
              
              <p className="subtitle">
                Your personal meal planning assistant for a healthier lifestyle
              </p>

              <div className="feature-list">
                <div className="feature-item">
                  <TrendingUpIcon className="icon-small icon-green feature-icon" />
                  <div>
                    <h3 className="feature-title">Nutrition Tracking</h3>
                    <p className="feature-text">Monitor your daily and weekly calorie intake with visual diagrams</p>
                  </div>
                </div>

                <div className="feature-item">
                  <CalendarIcon className="icon-small icon-blue feature-icon" />
                  <div>
                    <h3 className="feature-title">Meal Planning</h3>
                    <p className="feature-text">Plan your meals for the entire week with our intuitive calendar</p>
                  </div>
                </div>

                <div className="feature-item">
                  <ShoppingCartIcon className="icon-small icon-purple feature-icon" />
                  <div>
                    <h3 className="feature-title">Smart Shopping Lists</h3>
                    <p className="feature-text">Auto-generated shopping lists based on your meal plans</p>
                  </div>
                </div>

                <div className="feature-item">
                  <BookIcon className="icon-small icon-orange feature-icon" />
                  <div>
                    <h3 className="feature-title">Recipe Collection</h3>
                    <p className="feature-text">Access hundreds of healthy recipes with nutritional information</p>
                  </div>
                </div>
              </div>
            </div>

            {/* Auth Section */}
            <div className="auth-card card">
              <div className="auth-switch">
                <div className="auth-switch-group">
                  <button
                    onClick={() => setIsLogin(true)}
                    className={`auth-switch-button ${isLogin ? 'auth-active' : ''}`}
                  >
                    Login
                  </button>
                  <button
                    onClick={() => setIsLogin(false)}
                    className={`auth-switch-button ${!isLogin ? 'auth-active' : ''}`}
                  >
                    Sign Up
                  </button>
                </div>
              </div>

              <form onSubmit={handleAuth} className="auth-form">
                {!isLogin && (
                  <div className="form-group">
                    <label className="form-label">Full Name</label>
                    <input type="text" className="form-input" placeholder="John Doe"/>
                  </div>
                )}

                <div className="form-group">
                  <label className="form-label">Email</label>
                  <input type="email" className="form-input" placeholder="your@email.com"/>
                </div>

                <div className="form-group">
                  <label className="form-label">Password</label>
                  <input type="password" className="form-input" placeholder="••••••••"/>
                </div>

                {!isLogin && (
                  <div className="form-group">
                    <label className="form-label">Confirm Password</label>
                    <input type="password" className="form-input" placeholder="••••••••"/>
                  </div>
                )}

                <button type="submit" className="primary-button full-width">
                  {isLogin ? 'Login' : 'Create Account'}
                </button>
              </form>

              {isLogin && (
                <div className="forgot-password-link">
                  <a href="#">Forgot password?</a>
                </div>
              )}
            </div>
          </div>

          {/* Recipe Preview Section */}
          <div className="featured-recipes-section">
            <h2 className="section-title">Featured Recipes</h2>
            <div className="recipe-preview-grid">
              {sampleRecipes.slice(0, 3).map(recipe => (
                <div key={recipe.id} className="recipe-card">
                  <div className="recipe-image-placeholder">
                    {recipe.image}
                  </div>
                  <div className="recipe-details">
                    <h3 className="recipe-name">{recipe.name}</h3>
                    <div className="recipe-meta">
                      <span>{recipe.calories} cal</span>
                      <span className="recipe-time"><ClockIcon className="icon-tiny" />{recipe.time}min</span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    );
  }

  // Main App Navigation
  const NavBar = () => (
    <nav className="navbar">
      <div className="content-wrapper">
        <div className="navbar-content">
          <div className="logo-group">
            <ChefHatIcon className="icon-medium icon-green" />
            <span className="navbar-title">MealFlow</span>
          </div>
          
          <div className="nav-links">
            <button
              onClick={() => setCurrentPage('dashboard')}
              className={`nav-button ${currentPage === 'dashboard' ? 'nav-button-active' : 'nav-button-inactive'}`}
            >
              <TrendingUpIcon className="icon-small" />
              <span>Dashboard</span>
            </button>
            <button
              onClick={() => setCurrentPage('calendar')}
              className={`nav-button ${currentPage === 'calendar' ? 'nav-button-active' : 'nav-button-inactive'}`}
            >
              <CalendarIcon className="icon-small" />
              <span>Calendar</span>
            </button>
            <button
              onClick={() => setCurrentPage('profile')}
              className={`nav-button ${currentPage === 'profile' ? 'nav-button-active' : 'nav-button-inactive'}`}
            >
              <UserIcon className="icon-small" />
              <span>Profile</span>
            </button>
            <button
              onClick={() => setCurrentPage('recipes')}
              className={`nav-button ${currentPage === 'recipes' ? 'nav-button-active' : 'nav-button-inactive'}`}
            >
              <BookIcon className="icon-small" />
              <span>Recipes</span>
            </button>
            <button
              onClick={() => setCurrentPage('shopping')}
              className={`nav-button ${currentPage === 'shopping' ? 'nav-button-active' : 'nav-button-inactive'}`}
            >
              <ShoppingCartIcon className="icon-small" />
              <span>Shopping List</span>
            </button>
          </div>
        </div>
      </div>
    </nav>
  );

  // Dashboard Page
  if (currentPage === 'dashboard') {
    return (
      <div className="app-page-container">
        <NavBar />
        <div className="content-wrapper">
          <h1 className="page-title">Welcome back, {user.name}!</h1>

          {/* Nutrition Trackers */}
          <div className="dashboard-grid mb-4">
            {/* Daily Nutrition */}
            <div className="card">
              <h2 className="card-title text-center">Today's Nutrition</h2>
              <div className="progress-grid">
                <CircularProgress value={user.currentDay.calories} max={user.dailyGoal.calories} label="Calories" color="#10b981" />
                <CircularProgress value={user.currentDay.protein} max={user.dailyGoal.protein} label="Protein (g)" color="#3b82f6" />
                <CircularProgress value={user.currentDay.carbs} max={user.dailyGoal.carbs} label="Carbs (g)" color="#f59e0b" />
                <CircularProgress value={user.currentDay.fats} max={user.dailyGoal.fats} label="Fats (g)" color="#ef4444" />
              </div>
            </div>

            {/* Weekly Nutrition */}
            <div className="card">
              <h2 className="card-title text-center">This Week's Progress</h2>
              <div className="progress-grid">
                <CircularProgress value={user.currentWeek.calories} max={user.weeklyGoal.calories} label="Calories" color="#10b981" />
                <CircularProgress value={user.currentWeek.protein} max={user.weeklyGoal.protein} label="Protein (g)" color="#3b82f6" />
                <CircularProgress value={user.currentWeek.carbs} max={user.weeklyGoal.carbs} label="Carbs (g)" color="#f59e0b" />
                <CircularProgress value={user.currentWeek.fats} max={user.weeklyGoal.fats} label="Fats (g)" color="#ef4444" />
              </div>
            </div>
          </div>

          {/* Meal Suggestions */}
          <div className="card">
            <h2 className="card-title">Suggested Meals for You</h2>
            <div className="recipe-suggestions-grid">
              {sampleRecipes.map(recipe => (
                <div key={recipe.id} className="recipe-suggestion-item">
                  <div className="recipe-image-suggestion">
                    {recipe.image}
                  </div>
                  <div className="p-4">
                    <h3 className="recipe-name-suggestion">{recipe.name}</h3>
                    <div className="macro-grid">
                      <div className="macro-item"><span>Calories:</span><span className="macro-value">{recipe.calories}</span></div>
                      <div className="macro-item"><span>Protein:</span><span className="macro-value">{recipe.protein}g</span></div>
                      <div className="macro-item"><span>Carbs:</span><span className="macro-value">{recipe.carbs}g</span></div>
                      <div className="macro-item"><span>Fats:</span><span className="macro-value">{recipe.fats}g</span></div>
                    </div>
                    <div className="suggestion-footer">
                      <span className="recipe-time-suggestion"><ClockIcon className="icon-tiny" />{recipe.time} min</span>
                      <button className="text-link">View Recipe</button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    );
  }

  // ... (Profile, Calendar, Recipes, and Shopping pages are similarly refactored) ...
  
  // Profile Page
  if (currentPage === 'profile') {
    return (
      <div className="app-page-container">
        <NavBar />
        <div className="content-wrapper">
          <div className="profile-grid">
            {/* Personal Info */}
            <div className="card">
              <div className="profile-header">
                <div className="avatar-placeholder">
                  {user.name.split(' ').map(n => n[0]).join('')}
                </div>
                <h2 className="profile-name">{user.name}</h2>
                <p className="profile-email">{user.email}</p>
              </div>

              <div className="form-group-list">
                <div className="form-group">
                  <label className="form-label">Full Name</label>
                  <input type="text" defaultValue={user.name} className="form-input"/>
                </div>
                <div className="form-group">
                  <label className="form-label">Email</label>
                  <input type="email" defaultValue={user.email} className="form-input"/>
                </div>
                <div className="form-group">
                  <label className="form-label">Daily Calorie Goal</label>
                  <input type="number" defaultValue={user.dailyGoal.calories} className="form-input"/>
                </div>
                <button className="primary-button full-width">
                  Save Changes
                </button>
              </div>
            </div>

            {/* Meal History */}
            <div className="card">
              <h3 className="card-subtitle">Recent Meal History</h3>
              <div className="history-list">
                {user.mealHistory.map((day, idx) => (
                  <div key={idx} className="history-item">
                    <div className="history-date">{day.date}</div>
                    <div className="history-meals">
                      {day.meals.map((meal, mIdx) => (
                        <div key={mIdx} className="meal-entry">
                          <CheckIcon className="icon-tiny icon-green" />
                          <span className="meal-name">{meal}</span>
                        </div>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Favorite Recipes */}
            <div className="card">
              <h3 className="card-subtitle favorite-title">
                <HeartIcon className="icon-medium icon-red" />
                Most Prepared Recipes
              </h3>
              <div className="favorite-list">
                {user.favoriteRecipes.map((recipe, idx) => {
                  const recipeData = sampleRecipes.find(r => r.name === recipe);
                  return (
                    <div key={idx} className="favorite-item">
                      <div className="favorite-image">{recipeData?.image}</div>
                      <div className="favorite-details">
                        <div className="favorite-name">{recipe}</div>
                        <div className="favorite-meta">{recipeData?.calories} cal • {recipeData?.time} min</div>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
  
  // Calendar Page
  if (currentPage === 'calendar') {
    const weekDays = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    const getWeekDates = () => {
      const dates = [];
      const today = new Date();
      const day = today.getDay();
      const diff = today.getDate() - day;
      for (let i = 0; i < 7; i++) {
        const date = new Date(today.getFullYear(), today.getMonth(), diff + i);
        dates.push(date);
      }
      return dates;
    };

    const weekDates = getWeekDates();

    return (
      <div className="app-page-container">
        <NavBar />
        <div className="content-wrapper">
          <h1 className="page-title">Weekly Meal Planner</h1>
          
          <div className="calendar-grid">
            {weekDates.map((date, idx) => {
              const dateKey = date.toISOString().split('T')[0];
              const dayPlan = mealPlan[dateKey] || {};
              
              return (
                <div key={idx} className="calendar-day-card">
                  <div className="calendar-header">
                    <div className="calendar-day-name">{weekDays[date.getDay()]}</div>
                    <div className="calendar-date-number">{date.getDate()}/{date.getMonth() + 1}</div>
                  </div>
                  
                  <div className="meal-slots">
                    {/* Breakfast */}
                    <div className="meal-slot">
                      <div className="meal-label">BREAKFAST</div>
                      {dayPlan.breakfast ? (
                        <div className="meal-item meal-breakfast group">
                          <button
                            onClick={() => removeMealFromPlan(date, 'breakfast')}
                            className="meal-remove-button"
                          >
                            <XIcon className="icon-tiny" />
                          </button>
                          <div className="meal-emoji">{dayPlan.breakfast.image}</div>
                          <div className="meal-name-small">{dayPlan.breakfast.name}</div>
                          <div className="meal-cal-small">{dayPlan.breakfast.calories} cal</div>
                        </div>
                      ) : (
                        <div className="meal-placeholder">
                          <select
                            onChange={(e) => {
                              const recipe = sampleRecipes.find(r => r.id === parseInt(e.target.value));
                              if (recipe) addMealToPlan(date, 'breakfast', recipe);
                            }}
                            className="meal-select"
                            defaultValue=""
                          >
                            <option value="">+ Add</option>
                            {sampleRecipes.map(r => (<option key={r.id} value={r.id}>{r.name}</option>))}
                          </select>
                        </div>
                      )}
                    </div>

                    {/* Lunch */}
                    <div className="meal-slot">
                      <div className="meal-label">LUNCH</div>
                      {dayPlan.lunch ? (
                        <div className="meal-item meal-lunch group">
                          <button onClick={() => removeMealFromPlan(date, 'lunch')} className="meal-remove-button">
                            <XIcon className="icon-tiny" />
                          </button>
                          <div className="meal-emoji">{dayPlan.lunch.image}</div>
                          <div className="meal-name-small">{dayPlan.lunch.name}</div>
                          <div className="meal-cal-small">{dayPlan.lunch.calories} cal</div>
                        </div>
                      ) : (
                        <div className="meal-placeholder">
                          <select
                            onChange={(e) => {
                              const recipe = sampleRecipes.find(r => r.id === parseInt(e.target.value));
                              if (recipe) addMealToPlan(date, 'lunch', recipe);
                            }}
                            className="meal-select"
                            defaultValue=""
                          >
                            <option value="">+ Add</option>
                            {sampleRecipes.map(r => (<option key={r.id} value={r.id}>{r.name}</option>))}
                          </select>
                        </div>
                      )}
                    </div>

                    {/* Dinner */}
                    <div className="meal-slot">
                      <div className="meal-label">DINNER</div>
                      {dayPlan.dinner ? (
                        <div className="meal-item meal-dinner group">
                          <button onClick={() => removeMealFromPlan(date, 'dinner')} className="meal-remove-button">
                            <XIcon className="icon-tiny" />
                          </button>
                          <div className="meal-emoji">{dayPlan.dinner.image}</div>
                          <div className="meal-name-small">{dayPlan.dinner.name}</div>
                          <div className="meal-cal-small">{dayPlan.dinner.calories} cal</div>
                        </div>
                      ) : (
                        <div className="meal-placeholder">
                          <select
                            onChange={(e) => {
                              const recipe = sampleRecipes.find(r => r.id === parseInt(e.target.value));
                              if (recipe) addMealToPlan(date, 'dinner', recipe);
                            }}
                            className="meal-select"
                            defaultValue=""
                          >
                            <option value="">+ Add</option>
                            {sampleRecipes.map(r => (<option key={r.id} value={r.id}>{r.name}</option>))}
                          </select>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    );
  }
  
  // Recipes Page
  if (currentPage === 'recipes') {
    return (
      <div className="app-page-container">
        <NavBar />
        <div className="content-wrapper">
          <h1 className="page-title">Recipe Collection</h1>
          
          <div className="recipe-collection-grid">
            {sampleRecipes.map(recipe => (
              <div key={recipe.id} className="recipe-collection-card">
                <div className="recipe-image-collection">
                  {recipe.image}
                </div>
                <div className="recipe-collection-details">
                  <h3 className="recipe-collection-name">{recipe.name}</h3>
                  
                  <div className="macro-info-grid">
                    <div className="macro-box box-green">
                      <div className="macro-label">Calories</div>
                      <div className="macro-data">{recipe.calories}</div>
                    </div>
                    <div className="macro-box box-blue">
                      <div className="macro-label">Protein</div>
                      <div className="macro-data">{recipe.protein}g</div>
                    </div>
                    <div className="macro-box box-yellow">
                      <div className="macro-label">Carbs</div>
                      <div className="macro-data">{recipe.carbs}g</div>
                    </div>
                    <div className="macro-box box-red">
                      <div className="macro-label">Fats</div>
                      <div className="macro-data">{recipe.fats}g</div>
                    </div>
                  </div>

                  <div className="ingredient-section">
                    <div className="ingredient-title">Ingredients:</div>
                    <ul className="ingredient-list">
                      {recipe.ingredients.map((ing, idx) => (
                        <li key={idx} className="ingredient-item">
                          <span className="ingredient-bullet">•</span>
                          {ing}
                        </li>
                      ))}
                    </ul>
                  </div>

                  <div className="recipe-collection-footer">
                    <span className="recipe-time-collection">
                      <ClockIcon className="icon-tiny" />
                      {recipe.time} minutes
                    </span>
                    <button className="primary-button-small">
                      Add to Plan
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  // Shopping List Page
  if (currentPage === 'shopping') {
    return (
      <div className="app-page-container">
        <NavBar />
        <div className="content-wrapper">
          <div className="shopping-list-max-width">
            <div className="shopping-list-header">
              <h1 className="page-title-small">Shopping List</h1>
              <div className="completion-status">
                {shoppingList.filter(item => item.checked).length} of {shoppingList.length} items
              </div>
            </div>

            {/* Add Custom Item */}
            <div className="card mb-20">
              <h3 className="card-subtitle">Add Custom Item</h3>
              <div className="add-item-form">
                <input
                  type="text"
                  value={customItem}
                  onChange={(e) => setCustomItem(e.target.value)}
                  placeholder="Enter item name..."
                  className="form-input flex-grow"
                />
                <button
                  onClick={() => {
                    if (customItem.trim()) {
                      setShoppingList([...shoppingList, { item: customItem, amount: '', checked: false }]);
                      setCustomItem('');
                    }
                  }}
                  className="primary-button-add"
                >
                  <PlusIcon className="icon-small" />
                  <span>Add</span>
                </button>
              </div>
            </div>

            {/* Shopping List Items */}
            <div className="card">
              <h3 className="card-subtitle">Items from Meal Plan</h3>
              {shoppingList.length === 0 ? (
                <div className="empty-list-message">
                  <ShoppingCartIcon className="icon-huge text-gray" />
                  <p>No items yet. Add meals to your calendar to generate a shopping list!</p>
                </div>
              ) : (
                <div className="list-items-container">
                  {shoppingList.map((item, idx) => (
                    <div
                      key={idx}
                      className={`list-item ${item.checked ? 'list-item-checked' : ''}`}
                    >
                      <button
                        onClick={() => {
                          const updated = [...shoppingList];
                          updated[idx].checked = !updated[idx].checked;
                          setShoppingList(updated);
                        }}
                        className={`checkbox ${item.checked ? 'checkbox-checked' : 'checkbox-unchecked'}`}
                      >
                        {item.checked && <CheckIcon className="icon-tiny icon-white" />}
                      </button>
                      <div className={`item-details ${item.checked ? 'item-details-checked' : ''}`}>
                        <span className="item-name">{item.item}</span>
                        {item.amount && <span className="item-amount">{item.amount}</span>}
                      </div>
                      <button
                        onClick={() => {
                          setShoppingList(shoppingList.filter((_, i) => i !== idx));
                        }}
                        className="delete-button"
                      >
                        <Trash2Icon className="icon-small" />
                      </button>
                    </div>
                  ))}
                </div>
              )}
            </div>

            {shoppingList.length > 0 && (
              <div className="shopping-actions">
                <button className="primary-button flex-grow">
                  Export List
                </button>
                <button
                  onClick={() => setShoppingList([])}
                  className="secondary-button flex-grow"
                >
                  Clear All
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    );
  }


  return null;
};

export default MealPlannerApp;