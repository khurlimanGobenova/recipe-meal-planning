import React from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import RecipesPage from './pages/RecipesPage'
import StartPage from './pages/StartPage'

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<StartPage />} />
        <Route path="/recipes" element={<RecipesPage />} />
      </Routes>
    </Router>
  )
}

export default App