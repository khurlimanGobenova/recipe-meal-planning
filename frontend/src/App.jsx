import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar.jsx";
import Dashboard from "./pages/Dashboard.jsx";
import Profile from "./pages/ProfilePage.jsx";
import Calendar from "./pages/CalendarPage.jsx";
import ShoppingList from "./pages/ShoppingList.jsx"
import "./styles/App.css";

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <div className="content-wrapper">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/calendar" element={<Calendar />} />
          <Route path="/shopping-list" element={<ShoppingList />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;