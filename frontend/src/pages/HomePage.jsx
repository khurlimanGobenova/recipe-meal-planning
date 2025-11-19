import React, { useEffect, useState } from "react";
import axios from "axios";

export default function DashboardPage() {
  const [recipes, setRecipes] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:8080/api/recipes")
      .then(res => setRecipes(res.data))
      .catch(err => console.error(err));
  }, []);

  return (
    <div className="app-page-container">
      <h1 className="page-title">Dashboard</h1>
      <div className="recipe-suggestions-grid">
        {recipes.map(r => (
          <div key={r.recipe_id} className="recipe-suggestion-item">
            <div className="recipe-image-suggestion">🍽️</div>
            <div className="recipe-name-suggestion">{r.title}</div>
          </div>
        ))}
      </div>
    </div>
  );
}
