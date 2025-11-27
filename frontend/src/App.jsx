import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import ProtectedRoute from './components/ProtectedRoute';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Recipes from './pages/Recipes';
import Profile from './pages/Profile';
import MealPlan from './pages/MealPlan';
import ShoppingList from './pages/ShoppingList';
import './styles/global.css';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        
        {/* Protected Routes */}
        <Route path="/home" element={
          <ProtectedRoute>
            <>
              <Navbar />
              <Home />
            </>
          </ProtectedRoute>
        } />
        
        <Route path="/recipes" element={
          <ProtectedRoute>
            <>
              <Navbar />
              <Recipes />
            </>
          </ProtectedRoute>
        } />
        
        <Route path="/meal-plan" element={
          <ProtectedRoute>
            <>
              <Navbar />
              <MealPlan />
            </>
          </ProtectedRoute>
        } />
        
        <Route path="/shopping-list" element={
          <ProtectedRoute>
            <>
              <Navbar />
              <ShoppingList />
            </>
          </ProtectedRoute>
        } />
        
        <Route path="/profile" element={
          <ProtectedRoute>
            <>
              <Navbar />
              <Profile />
            </>
          </ProtectedRoute>
        } />
      </Routes>
    </BrowserRouter>
  );
}

export default App;