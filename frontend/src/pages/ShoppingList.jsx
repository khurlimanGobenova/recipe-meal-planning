import React, { useState, useEffect } from 'react';
import '../styles/ShoppingList.css';

function ShoppingList() {
  const [ingredients, setIngredients] = useState([]);
  const [checkedItems, setCheckedItems] = useState(new Set());
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetch('http://localhost:8080/api/ingredients')
      .then(res => res.json())
      .then(data => {
        setIngredients(data.slice(0, 20)); // Show first 20
        setLoading(false);
      })
      .catch(err => {
        console.error('Error:', err);
        setLoading(false);
      });
  }, []);

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

  return (
    <div className="page">
      <div className="container">
        <div className="page-header">
          <div>
            <h1>Shopping List</h1>
            <p>Your ingredients to buy</p>
          </div>
          <button className="btn btn-primary">Generate from Meal Plan</button>
        </div>

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
        <div className="search-section">
          <input
            type="text"
            placeholder="Search ingredients..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
        </div>

        {/* Shopping List */}
        {loading ? (
          <div className="loading">Loading ingredients...</div>
        ) : (
          <div className="shopping-list-card">
            <div className="list-header">
              <h3>Ingredients</h3>
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
                    <span className="item-unit">{item.unit}</span>
                  </div>
                  <div className="item-nutrition">
                    {item.calories} cal
                  </div>
                </div>
              ))}
            </div>

            {filteredIngredients.length === 0 && (
              <div className="empty-state">
                <p>No ingredients found</p>
              </div>
            )}
          </div>
        )}

        {/* Quick Add */}
        <div className="quick-add-card">
          <h3>Quick Add Item</h3>
          <div className="quick-add-form">
            <input
              type="text"
              placeholder="Enter item name..."
              className="quick-add-input"
            />
            <button className="btn btn-primary">Add</button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ShoppingList;