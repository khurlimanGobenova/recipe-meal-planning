import React, { useEffect, useState } from "react";
import axios from "axios";

export default function ShoppingList() {
  const [lists, setLists] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:8080/api/shoppinglists/mealplan/1") // replace with mealplan ID
      .then(res => setLists(res.data))
      .catch(err => console.error(err));
  }, []);

  return (
    <div className="app-page-container">
      <h1 className="page-title">Shopping List</h1>
      {lists.length === 0 ? (
        <div className="empty-list-message">No items in your shopping list.</div>
      ) : (
        lists.map(list => (
          <div key={list.list_id} className="shopping-list-max-width">
            <div className="list-items-container">
              {list.items?.map(item => (
                <div key={item.item_id} className={`list-item ${item.is_purchased ? "list-item-checked" : ""}`}>
                  <div className={`checkbox ${item.is_purchased ? "checkbox-checked" : "checkbox-unchecked"}`}></div>
                  <div className={`item-details ${item.is_purchased ? "item-details-checked" : ""}`}>
                    <span className="item-name">{item.name}</span>
                    <span className="item-amount">{item.total_quantity} {item.unit}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        ))
      )}
    </div>
  );
}
