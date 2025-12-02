/**
 * api.js
 * API service layer for Heal Meal frontend
 * Handles all HTTP requests to the Java backend
 */

const API_BASE_URL = 'http://localhost:8080/api';

/**
 * Generic fetch wrapper with error handling
 */
async function fetchAPI(endpoint, options = {}) {
  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error(`API Error (${endpoint}):`, error);
    throw error;
  }
}

/**
 * Recipe API calls
 */
export const recipeAPI = {
  // Get all recipes
  getAll: async () => {
    return await fetchAPI('/recipes');
  },

  // Get recipe by ID
  getById: async (id) => {
    return await fetchAPI(`/recipe/${id}`);
  },

  // Search recipes
  search: async (query) => {
    return await fetchAPI(`/search?q=${encodeURIComponent(query)}`);
  },

  // Get recipes by category
  getByCategory: async (category) => {
    return await fetchAPI(`/recipes?category=${encodeURIComponent(category)}`);
  },
};

/**
 * Meal Plan API calls
 */
export const mealPlanAPI = {
  // Get all meal plans
  getAll: async () => {
    return await fetchAPI('/mealplans');
  },

  // Get meal plan by ID
  getById: async (id) => {
    return await fetchAPI(`/mealplan/${id}`);
  },

  // Create new meal plan (for future implementation)
  create: async (mealPlanData) => {
    return await fetchAPI('/mealplan', {
      method: 'POST',
      body: JSON.stringify(mealPlanData),
    });
  },
}; 
app.get('/api / mealentries', async (req, res) => {
const { mealPlanId } = req.query;
if (!mealPlanId) return res.status(400).json({ error: 'mealPlanId required' });

try {
  const [rows] = await pool.query(
    `SELECT me.mealentry_id AS id,
              me.date AS planDate,
              me.meal_type AS mealType,
              r.recipe_id,
              r.title AS recipeTitle
       FROM mealentry me
       JOIN recipe r ON me.recipe_id = r.recipe_id
       WHERE me.mealplan_id = ?`,
    [mealPlanId]
  );
  res.json(rows);
} catch (err) {
  console.error(err);
  res.status(500).json({ error: 'Failed to fetch meal entries' });
}
});

// Add a new meal entry
app.post('/api/mealentries', async (req, res) => {
  const { mealPlanId, planDate, mealType, recipeId } = req.body;
  if (!mealPlanId || !planDate || !mealType || !recipeId) {
    return res.status(400).json({ error: 'Missing required fields' });
  }

  try {
    const [result] = await pool.query(
      `INSERT INTO mealentry (mealplan_id, recipe_id, date, meal_type)
       VALUES (?, ?, ?, ?)`,
      [mealPlanId, recipeId, planDate, mealType]
    );

    const [rows] = await pool.query(
      `SELECT me.mealentry_id AS id,
              me.date AS planDate,
              me.meal_type AS mealType,
              r.recipe_id,
              r.title AS recipeTitle
       FROM mealentry me
       JOIN recipe r ON me.recipe_id = r.recipe_id
       WHERE me.mealentry_id = ?`,
      [result.insertId]
    );

    res.json(rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Failed to add meal entry' });
  }
});

// Delete a meal entry
app.delete('/api/mealentries/:id', async (req, res) => {
  const { id } = req.params;
  try {
    await pool.query('DELETE FROM mealentry WHERE mealentry_id = ?', [id]);
    res.json({ message: 'Meal entry deleted successfully' });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Failed to delete meal entry' });
  }
});


/**
 * Ingredient API calls
 */
export const ingredientAPI = {
  // Get all ingredients
  getAll: async () => {
    return await fetchAPI('/ingredients');
  },

  // Search ingredients
  search: async (query) => {
    return await fetchAPI(`/ingredients?search=${encodeURIComponent(query)}`);
  },
};

/**
 * Stats API calls
 */
export const statsAPI = {
  // Get database statistics
  getStats: async () => {
    return await fetchAPI('/stats');
  },
};

/**
 * User API calls (for future implementation)
 */
export const userAPI = {
  // Get all users
  getAll: async () => {
    return await fetchAPI('/users');
  },

  // Get user by ID
  getById: async (id) => {
    return await fetchAPI(`/user/${id}`);
  },
};

// Export all APIs
export default {
  recipes: recipeAPI,
  mealPlans: mealPlanAPI,
  ingredients: ingredientAPI,
  stats: statsAPI,
  users: userAPI,
};