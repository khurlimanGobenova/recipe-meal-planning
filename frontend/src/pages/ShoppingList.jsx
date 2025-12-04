import React, { useState, useEffect } from 'react';
import '../styles/ShoppingList.css';

function ShoppingList() {
  const [ingredients, setIngredients] = useState([]);
  const [mealPlans, setMealPlans] = useState([]);
  const [selectedPlanId, setSelectedPlanId] = useState(null);
  const [checkedItems, setCheckedItems] = useState(new Set());
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  const currentUser = JSON.parse(localStorage.getItem("currentUser") || "{}");

  // Fetch user's meal plans
  useEffect(() => {
    if (currentUser.userId) {
      fetch(`http://localhost:8080/api/mealplans?userId=${currentUser.userId}`)
        .then(res => res.json())
        .then(data => {
          setMealPlans(data);
          if (data.length > 0) {
            setSelectedPlanId(data[0].id);
          }
        })
        .catch(err => console.error('Error fetching meal plans:', err));
    }
  }, [currentUser.userId]);

  // Generate shopping list when meal plan is selected
  useEffect(() => {
    if (selectedPlanId) {
      setLoading(true);
      fetch(`http://localhost:8080/api/shopping-list?mealPlanId=${selectedPlanId}`)
        .then(res => res.json())
        .then(data => {
          setIngredients(data);
          setLoading(false);
        })
        .catch(err => {
          console.error('Error generating shopping list:', err);
          setLoading(false);
        });
    }
  }, [selectedPlanId]);

  const toggleItem = (id) => {
    const newChecked = new Set(checkedItems);
    if (newChecked.has(id)) {
      newChecked.delete(id);
    } else {
      newChecked.add(id);
    }
    setCheckedItems(newChecked);
  };

  const filteredIngredients = ingredients.filter(item =>
    item.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const checkedCount = checkedItems.size;
  const totalCount = filteredIngredients.length;

  const selectedPlan = mealPlans.find(p => p.id === selectedPlanId);

  return (
    <div className="page">
      <div className="container">
        <div className="page-header">
          <div>
            <h1>Shopping List</h1>
            <p>Ingredients from your meal plan</p>
          </div>
        </div>

        {/* Meal Plan Selector */}
        {mealPlans.length > 0 && (
          <div className="meal-plan-selector">
            <label>Select Meal Plan: </label>
            <select 
              value={selectedPlanId || ''}
              onChange={(e) => setSelectedPlanId(parseInt(e.target.value))}
              className="plan-select"
            >
              {mealPlans.map(plan => (
                <option key={plan.id} value={plan.id}>
                  {plan.title} ({new Date(plan.startDate).toLocaleDateString()})
                </option>
              ))}
            </select>
          </div>
        )}

        {selectedPlan && (
          <div className="plan-info">
            <h3>Shopping list for: {selectedPlan.title}</h3>
            <p>{new Date(selectedPlan.startDate).toLocaleDateString()} - {new Date(selectedPlan.endDate).toLocaleDateString()}</p>
          </div>
        )}

        {/* Progress */}
        <div className="shopping-progress">
          <div className="progress-text">
            <span>{checkedCount} of {totalCount} items</span>
            <span className="progress-percent">
              {totalCount > 0 ? Math.round((checkedCount / totalCount) * 100) : 0}%
            </span>
          </div>
          <div className="progress-bar-container">
            <div 
              className="progress-bar-fill"
              style={{width: `${totalCount > 0 ? (checkedCount / totalCount) * 100 : 0}%`}}
            ></div>
          </div>
        </div>

        {/* Search */}
        {ingredients.length > 0 && (
          <div className="search-section">
            <input
              type="text"
              placeholder="Search ingredients..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
            />
          </div>
        )}

        {/* Shopping List */}
        {loading ? (
          <div className="loading">Generating shopping list...</div>
        ) : ingredients.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">🛒</div>
            <h3>No ingredients yet</h3>
            <p>Add recipes to your meal plan to generate a shopping list</p>
          </div>
        ) : (
          <div className="shopping-list-card">
            <div className="list-header">
              <h3>Ingredients ({totalCount})</h3>
              <button 
                className="btn btn-secondary btn-sm"
                onClick={() => setCheckedItems(new Set())}
              >
                Clear All
              </button>
            </div>

            <div className="shopping-items">
              {filteredIngredients.map(item => (
                <div
                  key={item.id}
                  className={`shopping-item ${checkedItems.has(item.id) ? 'checked' : ''}`}
                  onClick={() => toggleItem(item.id)}
                >
                  <div className="item-checkbox">
                    <input
                      type="checkbox"
                      checked={checkedItems.has(item.id)}
                      onChange={() => {}}
                    />
                  </div>
                  <div className="item-details">
                    <span className="item-name">{item.name}</span>
                    <span className="item-quantity">{item.quantity} {item.unit}</span>
                  </div>
                </div>
              ))}
            </div>

            {filteredIngredients.length === 0 && searchTerm && (
              <div className="empty-state">
                <p>No ingredients found matching "{searchTerm}"</p>
              </div>
            )}
          </div>
        )}

        {/* Info Card */}
        <div className="info-card">
          <h3>💡 Tip</h3>
          <p>The shopping list automatically aggregates all ingredients from your selected meal plan. Check off items as you shop!</p>
        </div>
      </div>
    </div>
  );
}

export default ShoppingList;
