import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Recipes.css';

function Recipes() {
  const [recipes, setRecipes] = useState([]);
  const [filteredRecipes, setFilteredRecipes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [selectedDifficulty, setSelectedDifficulty] = useState('all');
  
  const navigate = useNavigate();

  const categories = ['all', 'breakfast', 'lunch', 'dinner', 'snack', 'dessert'];
  const difficulties = ['all', 'easy', 'medium', 'hard'];

  useEffect(() => {
    fetch('http://localhost:8080/api/recipes')
      .then(res => res.json())
      .then(data => {
        setRecipes(data);
        setFilteredRecipes(data);
        setLoading(false);
      })
      .catch(err => {
        console.error('Error:', err);
        setLoading(false);
      });
  }, []);

  useEffect(() => {
    let filtered = recipes;

    // Filter by search term
    if (searchTerm) {
      filtered = filtered.filter(recipe =>
        recipe.title.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    // Filter by category
    if (selectedCategory !== 'all') {
      filtered = filtered.filter(recipe =>
        recipe.category.toLowerCase() === selectedCategory
      );
    }

    // Filter by difficulty
    if (selectedDifficulty !== 'all') {
      filtered = filtered.filter(recipe =>
        recipe.difficulty.toLowerCase() === selectedDifficulty
      );
    }

    setFilteredRecipes(filtered);
  }, [searchTerm, selectedCategory, selectedDifficulty, recipes]);

  return (
    <div className="page">
      <div className="container">
        <div className="recipes-header">
          <h1>Recipes</h1>
          <p>Discover healthy and delicious recipes</p>
        </div>

        {/* Filters */}
        <div className="filters-section">
          {/* Search */}
          <div className="search-box">
            <input
              type="text"
              placeholder="Search recipes..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
            />
          </div>

          {/* Category Filter */}
          <div className="filter-group">
            <label>Category</label>
            <div className="filter-buttons">
              {categories.map(category => (
                <button
                  key={category}
                  onClick={() => setSelectedCategory(category)}
                  className={`filter-btn ${selectedCategory === category ? 'active' : ''}`}
                >
                  {category}
                </button>
              ))}
            </div>
          </div>

          {/* Difficulty Filter */}
          <div className="filter-group">
            <label>Difficulty</label>
            <div className="filter-buttons">
              {difficulties.map(difficulty => (
                <button
                  key={difficulty}
                  onClick={() => setSelectedDifficulty(difficulty)}
                  className={`filter-btn ${selectedDifficulty === difficulty ? 'active' : ''}`}
                >
                  {difficulty}
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* Results Count */}
        <div className="results-info">
          {filteredRecipes.length} recipe{filteredRecipes.length !== 1 ? 's' : ''} found
        </div>

        {/* Recipes Grid */}
        {loading ? (
          <div className="loading">Loading recipes...</div>
        ) : filteredRecipes.length === 0 ? (
          <div className="empty-state">
            <p>No recipes found. Try adjusting your filters.</p>
          </div>
        ) : (
          <div className="grid grid-3">
            {filteredRecipes.map(recipe => (
              <div
                key={recipe.id}
                onClick={() => navigate(`/recipes/${recipe.id}`)}
                className="recipe-card"
              >
                <div className="recipe-header">
                  <span className="recipe-category">{recipe.category}</span>
                  <span className={`difficulty-badge ${recipe.difficulty}`}>
                    {recipe.difficulty}
                  </span>
                </div>
                
                <h3>{recipe.title}</h3>
                
                <div className="recipe-meta">
                  <span className="meta-item">
                    ⏱️ {recipe.totalTime} min
                  </span>
                  <span className="meta-item">
                    ⭐ {recipe.rating ? recipe.rating.toFixed(1) : 'N/A'}
                  </span>
                </div>
                
                <button className="btn-view">
                  View Recipe →
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default Recipes;