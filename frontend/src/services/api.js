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