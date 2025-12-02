USE heal_meal_db;
-- Find recipes with less than 500 calories per serving
SELECT 
    r.recipe_id,
    r.title,
    r.category,
    r.servings,
    ROUND(SUM(i.calories_per_unit * ri.quantity) / r.servings, 2) AS calories_per_serving
FROM recipe r
JOIN recipeingredient ri ON r.recipe_id = ri.recipe_id
JOIN ingredient i ON ri.ingredient_id = i.ingredient_id
GROUP BY r.recipe_id, r.title, r.category, r.servings
HAVING calories_per_serving < 500
ORDER BY calories_per_serving ASC
LIMIT 20;


-- QUERY 2: List all recipes that satisfy a given diet type

-- Find all vegetarian recipes (no meat ingredients)
SELECT DISTINCT
    r.recipe_id,
    r.title,
    r.category,
    r.difficulty,
    r.prep_time + r.cook_time AS total_time
FROM recipe r
WHERE r.recipe_id NOT IN (
    SELECT ri.recipe_id
    FROM recipeingredient ri
    JOIN ingredient i ON ri.ingredient_id = i.ingredient_id
    WHERE i.name IN ('chicken', 'beef', 'pork', 'fish', 'lamb', 'turkey', 'bacon', 'ham')
)
ORDER BY r.title;

-- Alternative: Find recipes with specific tags
SELECT 
    r.recipe_id,
    r.title,
    r.category,
    rt.tag_name
FROM recipe r
JOIN recipetag rt ON r.recipe_id = rt.recipe_id
WHERE rt.tag_name IN ('vegan', 'vegetarian', 'gluten-free', 'keto')
ORDER BY rt.tag_name, r.title;


-- QUERY 3: Aggregate ingredients for shopping list from meal plan

-- Generate shopping list for a specific meal plan
SELECT 
    i.ingredient_id,
    i.name AS ingredient_name,
    i.unit,
    SUM(ri.quantity) AS total_quantity,
    ROUND(SUM(i.calories_per_unit * ri.quantity), 2) AS total_calories
FROM mealentry me
JOIN recipe r ON me.recipe_id = r.recipe_id
JOIN recipeingredient ri ON r.recipe_id = ri.recipe_id
JOIN ingredient i ON ri.ingredient_id = i.ingredient_id
WHERE me.mealplan_id = 1  -- Replace with specific meal plan ID
GROUP BY i.ingredient_id, i.name, i.unit
ORDER BY i.name;

-- Shopping list for a specific date range
SELECT 
    i.ingredient_id,
    i.name AS ingredient_name,
    SUM(ri.quantity) AS total_quantity,
    i.unit,
    CONCAT(ROUND(SUM(ri.quantity), 2), ' ', i.unit) AS shopping_amount
FROM mealentry me
JOIN recipe r ON me.recipe_id = r.recipe_id
JOIN recipeingredient ri ON r.recipe_id = ri.recipe_id
JOIN ingredient i ON ri.ingredient_id = i.ingredient_id
WHERE me.date BETWEEN '2024-01-01' AND '2024-01-07'
GROUP BY i.ingredient_id, i.name, i.unit
ORDER BY i.name;


-- QUERY 4: Show top-rated recipes for a specific category

-- Top 10 rated breakfast recipes
SELECT 
    r.recipe_id,
    r.title,
    r.category,
    r.difficulty,
    COUNT(ra.rating_id) AS num_ratings,
    ROUND(AVG(ra.rating), 2) AS avg_rating,
    r.prep_time + r.cook_time AS total_time
FROM recipe r
LEFT JOIN rating ra ON r.recipe_id = ra.recipe_id
WHERE r.category = 'breakfast'
GROUP BY r.recipe_id, r.title, r.category, r.difficulty, r.prep_time, r.cook_time
HAVING COUNT(ra.rating_id) >= 1  -- At least 1 rating
ORDER BY avg_rating DESC, num_ratings DESC
LIMIT 10;

-- Top rated recipes across all categories
SELECT 
    r.category,
    r.recipe_id,
    r.title,
    ROUND(AVG(ra.rating), 2) AS avg_rating,
    COUNT(ra.rating_id) AS num_ratings
FROM recipe r
JOIN rating ra ON r.recipe_id = ra.recipe_id
GROUP BY r.category, r.recipe_id, r.title
HAVING COUNT(ra.rating_id) >= 3
ORDER BY r.category, avg_rating DESC;


-- QUERY 5: Daily nutritional totals for user's meal plan

-- Calculate daily nutrition for a specific user's meal plan
SELECT 
    me.date,
    me.meal_type,
    COUNT(DISTINCT me.recipe_id) AS num_recipes,
    ROUND(SUM(i.calories_per_unit * ri.quantity / r.servings), 2) AS total_calories,
    ROUND(SUM(i.protein * ri.quantity / r.servings), 2) AS total_protein_g,
    ROUND(SUM(i.carbs * ri.quantity / r.servings), 2) AS total_carbs_g,
    ROUND(SUM(i.fat * ri.quantity / r.servings), 2) AS total_fat_g
FROM mealentry me
JOIN recipe r ON me.recipe_id = r.recipe_id
JOIN recipeingredient ri ON r.recipe_id = ri.recipe_id
JOIN ingredient i ON ri.ingredient_id = i.ingredient_id
WHERE me.mealplan_id = 1  -- Replace with specific meal plan
GROUP BY me.date, me.meal_type
ORDER BY me.date, 
    CASE me.meal_type
        WHEN 'breakfast' THEN 1
        WHEN 'lunch' THEN 2
        WHEN 'dinner' THEN 3
        WHEN 'snack' THEN 4
    END;

-- Daily summary (all meals combined per day)
SELECT 
    me.date,
    ROUND(SUM(i.calories_per_unit * ri.quantity / r.servings), 2) AS daily_calories,
    ROUND(SUM(i.protein * ri.quantity / r.servings), 2) AS daily_protein_g,
    ROUND(SUM(i.carbs * ri.quantity / r.servings), 2) AS daily_carbs_g,
    ROUND(SUM(i.fat * ri.quantity / r.servings), 2) AS daily_fat_g
FROM mealentry me
JOIN recipe r ON me.recipe_id = r.recipe_id
JOIN recipeingredient ri ON r.recipe_id = ri.recipe_id
JOIN ingredient i ON ri.ingredient_id = i.ingredient_id
WHERE me.mealplan_id = 1
GROUP BY me.date
ORDER BY me.date;


-- QUERY 6: Find all recipes using a specific ingredient

-- Find all recipes that use chicken
SELECT 
    r.recipe_id,
    r.title,
    r.category,
    ri.quantity,
    ri.unit,
    r.prep_time + r.cook_time AS total_time,
    r.difficulty
FROM recipe r
JOIN recipeingredient ri ON r.recipe_id = ri.recipe_id
JOIN ingredient i ON ri.ingredient_id = i.ingredient_id
WHERE i.name LIKE '%chicken%'
ORDER BY r.title;

-- Find recipes using multiple specific ingredients (e.g., tomato AND onion)
SELECT 
    r.recipe_id,
    r.title,
    r.category,
    COUNT(DISTINCT i.ingredient_id) AS matching_ingredients
FROM recipe r
JOIN recipeingredient ri ON r.recipe_id = ri.recipe_id
JOIN ingredient i ON ri.ingredient_id = i.ingredient_id
WHERE i.name IN ('tomato', 'onion', 'garlic')
GROUP BY r.recipe_id, r.title, r.category
HAVING matching_ingredients >= 2
ORDER BY matching_ingredients DESC, r.title;

-- QUERY 7: Count recipes per user and find most active creators

-- Most active recipe creators
SELECT 
    u.user_id,
    u.name,
    u.email,
    u.diet_type,
    COUNT(r.recipe_id) AS recipe_count,
    ROUND(AVG(r.prep_time + r.cook_time), 2) AS avg_total_time
FROM user u
JOIN recipe r ON u.user_id = r.created_by
GROUP BY u.user_id, u.name, u.email, u.diet_type
ORDER BY recipe_count DESC, u.name
LIMIT 20;

-- Users with their recipe counts and average ratings
SELECT 
    u.user_id,
    u.name,
    COUNT(DISTINCT r.recipe_id) AS total_recipes,
    COUNT(DISTINCT ra.rating_id) AS total_ratings_received,
    ROUND(AVG(ra.rating), 2) AS avg_rating
FROM user u
LEFT JOIN recipe r ON u.user_id = r.created_by
LEFT JOIN rating ra ON r.recipe_id = ra.recipe_id
GROUP BY u.user_id, u.name
HAVING total_recipes > 0
ORDER BY total_recipes DESC, avg_rating DESC
LIMIT 15;

-- QUERY 8: Most commonly used ingredients across all recipes

-- Top 20 most frequently used ingredients
SELECT 
    i.ingredient_id,
    i.name,
    i.unit,
    COUNT(DISTINCT ri.recipe_id) AS used_in_recipes,
    ROUND(AVG(ri.quantity), 2) AS avg_quantity_per_recipe,
    ROUND(i.calories_per_unit, 2) AS calories_per_unit
FROM ingredient i
JOIN recipeingredient ri ON i.ingredient_id = ri.ingredient_id
GROUP BY i.ingredient_id, i.name, i.unit, i.calories_per_unit
ORDER BY used_in_recipes DESC
LIMIT 20;

-- Most used ingredients by specific user's recipes
SELECT 
    i.ingredient_id,
    i.name,
    COUNT(DISTINCT r.recipe_id) AS times_used,
    SUM(ri.quantity) AS total_quantity_used
FROM ingredient i
JOIN recipeingredient ri ON i.ingredient_id = ri.ingredient_id
JOIN recipe r ON ri.recipe_id = r.recipe_id
WHERE r.created_by = 1  -- Replace with specific user ID
GROUP BY i.ingredient_id, i.name
ORDER BY times_used DESC
LIMIT 15;

-- QUERY 9: Suggest alternative recipes based on dietary preferences

-- Find alternative breakfast recipes for a vegan user
SELECT 
    r.recipe_id,
    r.title,
    r.category,
    r.difficulty,
    ROUND(SUM(i.calories_per_unit * ri.quantity) / r.servings, 2) AS calories_per_serving,
    COUNT(DISTINCT rt.tag_name) AS matching_tags
FROM recipe r
JOIN recipeingredient ri ON r.recipe_id = ri.recipe_id
JOIN ingredient i ON ri.ingredient_id = i.ingredient_id
LEFT JOIN recipetag rt ON r.recipe_id = rt.recipe_id
WHERE r.category = 'breakfast'
    AND r.recipe_id NOT IN (
        SELECT ri2.recipe_id
        FROM recipeingredient ri2
        JOIN ingredient i2 ON ri2.ingredient_id = i2.ingredient_id
        WHERE i2.name IN ('chicken', 'beef', 'pork', 'fish', 'eggs', 'milk', 'cheese', 'butter')
    )
GROUP BY r.recipe_id, r.title, r.category, r.difficulty, r.servings
ORDER BY matching_tags DESC, calories_per_serving ASC
LIMIT 10;

-- Recommend recipes similar to user's favorites
SELECT DISTINCT
    r2.recipe_id,
    r2.title,
    r2.category,
    COUNT(DISTINCT i.ingredient_id) AS shared_ingredients
FROM recipe r1
JOIN recipeingredient ri1 ON r1.recipe_id = ri1.recipe_id
JOIN ingredient i ON ri1.ingredient_id = i.ingredient_id
JOIN recipeingredient ri2 ON i.ingredient_id = ri2.ingredient_id
JOIN recipe r2 ON ri2.recipe_id = r2.recipe_id
WHERE r1.recipe_id IN (
    SELECT recipe_id FROM rating WHERE user_id = 1 AND rating >= 4
)
AND r2.recipe_id NOT IN (
    -- Exclude already rated recipes
    SELECT recipe_id FROM rating WHERE user_id = 1
)
GROUP BY r2.recipe_id, r2.title, r2.category
HAVING shared_ingredients >= 3
ORDER BY shared_ingredients DESC
LIMIT 10;

-- QUERY 10: Rank cuisines/categories by popularity

-- Category popularity by meal plan usage
SELECT 
    r.category,
    COUNT(DISTINCT me.mealentry_id) AS times_planned,
    COUNT(DISTINCT r.recipe_id) AS unique_recipes,
    ROUND(AVG(ra.rating), 2) AS avg_category_rating,
    COUNT(DISTINCT me.mealplan_id) AS used_in_meal_plans
FROM recipe r
LEFT JOIN mealentry me ON r.recipe_id = me.recipe_id
LEFT JOIN rating ra ON r.recipe_id = ra.recipe_id
GROUP BY r.category
ORDER BY times_planned DESC, avg_category_rating DESC;

-- Category popularity by ratings
SELECT 
    r.category,
    COUNT(DISTINCT r.recipe_id) AS recipe_count,
    COUNT(ra.rating_id) AS total_ratings,
    ROUND(AVG(ra.rating), 2) AS avg_rating,
    SUM(CASE WHEN ra.rating >= 4 THEN 1 ELSE 0 END) AS highly_rated_count
FROM recipe r
LEFT JOIN rating ra ON r.recipe_id = ra.recipe_id
GROUP BY r.category
HAVING total_ratings > 0
ORDER BY avg_rating DESC, total_ratings DESC;


--  User health goal tracking

SELECT 
    u.user_id,
    u.name,
    uhg.daily_calorie_target,
    uhg.protein_target,
    uhg.carb_target,
    uhg.fat_target,
    ROUND(AVG(daily_nutrition.daily_calories), 2) AS avg_daily_calories,
    ROUND(AVG(daily_nutrition.daily_protein), 2) AS avg_daily_protein,
    ROUND(AVG(daily_nutrition.daily_carbs), 2) AS avg_daily_carbs,
    ROUND(AVG(daily_nutrition.daily_fat), 2) AS avg_daily_fat,
    ROUND((AVG(daily_nutrition.daily_calories) / uhg.daily_calorie_target * 100), 1) AS calorie_achievement_pct
FROM user u
JOIN userhealthgoal uhg ON u.user_id = uhg.user_id
JOIN mealplan mp ON u.user_id = mp.user_id
JOIN (
    SELECT 
        me.mealplan_id,
        me.date,
        SUM(i.calories_per_unit * ri.quantity / r.servings) AS daily_calories,
        SUM(i.protein * ri.quantity / r.servings) AS daily_protein,
        SUM(i.carbs * ri.quantity / r.servings) AS daily_carbs,
        SUM(i.fat * ri.quantity / r.servings) AS daily_fat
    FROM mealentry me
    JOIN recipe r ON me.recipe_id = r.recipe_id
    JOIN recipeingredient ri ON r.recipe_id = ri.recipe_id
    JOIN ingredient i ON ri.ingredient_id = i.ingredient_id
    GROUP BY me.mealplan_id, me.date
) AS daily_nutrition ON mp.mealplan_id = daily_nutrition.mealplan_id
GROUP BY u.user_id, u.name, uhg.daily_calorie_target, uhg.protein_target, uhg.carb_target, uhg.fat_target;


--  Recipe complexity analysis
SELECT 
    r.recipe_id,
    r.title,
    r.difficulty,
    r.prep_time,
    r.cook_time,
    r.prep_time + r.cook_time AS total_time,
    COUNT(DISTINCT ri.ingredient_id) AS ingredient_count,
    CASE 
        WHEN COUNT(DISTINCT ri.ingredient_id) <= 5 AND (r.prep_time + r.cook_time) <= 30 THEN 'Simple'
        WHEN COUNT(DISTINCT ri.ingredient_id) <= 10 AND (r.prep_time + r.cook_time) <= 60 THEN 'Moderate'
        ELSE 'Complex'
    END AS calculated_complexity
FROM recipe r
JOIN recipeingredient ri ON r.recipe_id = ri.recipe_id
GROUP BY r.recipe_id, r.title, r.difficulty, r.prep_time, r.cook_time
ORDER BY ingredient_count DESC, total_time DESC
LIMIT 20;