// src/App.jsx
import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Recipes from './pages/Recipes';
import Profile from './pages/Profile';
import MealPlan from './pages/MealPlan';
import ShoppingList from './pages/ShoppingList';
import StartPage from './pages/StartPage';
import './styles/global.css';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public Route - Login/Signup */}
        <Route path="/" element={<StartPage />} />
        
        {/* Protected Routes - No ProtectedRoute wrapper, just direct routes */}
        <Route path="/home" element={
          <>
            <Navbar />
            <Home />
          </>
        } />
        
        <Route path="/recipes" element={
          <>
            <Navbar />
            <Recipes />
          </>
        } />
        
        <Route path="/meal-plan" element={
          <>
            <Navbar />
            <MealPlan />
          </>
        } />
        
        <Route path="/shopping-list" element={
          <>
            <Navbar />
            <ShoppingList />
          </>
        } />
        
        <Route path="/profile" element={
          <>
            <Navbar />
            <Profile />
          </>
        } />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
