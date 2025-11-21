import React from "react";
import { Routes, Route } from "react-router-dom";
import Navbar from "./Navbar.jsx";

import StartPage from "./StartPage.jsx";
import DashboardPage from "./Dashboard.jsx";
import ProfilePage from "./ProfilePage.jsx";
import CalendarPage from "./CalendarPage.jsx";
import ShoppingList from "./ShoppingList.jsx";

import "./App.css";

function App() {
  return (
    <>
      <Navbar />
      <div className="content-wrapper">
        <Routes>
          <Route path="/" element={<StartPage />} />
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/calendar" element={<CalendarPage />} />
          <Route path="/shopping-list" element={<ShoppingList />} />
        </Routes>
      </div>
    </>
  );
}

export default App;
