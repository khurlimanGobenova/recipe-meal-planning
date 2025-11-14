-- HEAL MEAL SCHEMA DEFINITION
DROP DATABASE IF EXISTS meal_planner_db;
CREATE DATABASE meal_planner_db;
USE meal_planner_db;

--USER (stores dietary preferences
CREATE TABLE user (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    diet_type VARCHAR(50),
    preferences TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email)
) ENGINE=InnoDB;

-- USER HEALTH GOAL - tracks users fitness goals
CREATE TABLE user_health_goal (
    goal_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    daily_calorie_target INT,
    protein_target FLOAT,
    carb_target FLOAT,
    fat_target FLOAT,
    goal_type VARCHAR(50),
    start_date DATE,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

--INGREDIENT - stores data of ingredients with nutrition info
CREATE TABLE ingredient (
    ingredient_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    calories_per_unit FLOAT DEFAULT 0,
    protein FLOAT DEFAULT 0,
    fat FLOAT DEFAULT 0,
    carbs FLOAT DEFAULT 0,
    INDEX idx_name (name)
) ENGINE=InnoDB;

--RECIPE - stores recipe  created by users
CREATE TABLE recipe (
    recipe_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    prep_time INT,
    cook_time INT,
    servings INT NOT NULL DEFAULT 1,
    difficulty VARCHAR(20),
    category VARCHAR(50),
    created_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES user(user_id) ON DELETE CASCADE,
    INDEX idx_title (title),
    INDEX idx_category (category),
    INDEX idx_created_by (created_by)
) ENGINE=InnoDB;

--RECIPE INGREDIENT - m-m relationship between recipe and ingredients
CREATE TABLE recipe_ingredient (
    recipe_id INT NOT NULL,
    ingredient_id INT NOT NULL,
    quantity FLOAT NOT NULL,
    unit VARCHAR(20) NOT NULL,
    PRIMARY KEY (recipe_id, ingredient_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(ingredient_id) ON DELETE CASCADE
) ENGINE=InnoDB;

--RECIPE TAG - used for filtering ingredients
CREATE TABLE recipe_tag (
    recipe_id INT NOT NULL,
    tag_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (recipe_id, tag_name),
    FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id) ON DELETE CASCADE,
    INDEX idx_tag (tag_name)
) ENGINE=InnoDB;

--RATING -includes comments from the users
CREATE TABLE rating (
    rating_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    recipe_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    rated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_recipe (user_id, recipe_id)
) ENGINE=InnoDB;

--MEAL PLAN - user's meal planning schedule
CREATE TABLE meal_plan (
    mealplan_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    title VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    INDEX idx_user_dates (user_id, start_date, end_date)
) ENGINE=InnoDB;

--MEAL ENTRY - individual meal entry within a meal plan
CREATE TABLE meal_entry (
    mealentry_id INT PRIMARY KEY AUTO_INCREMENT,
    mealplan_id INT NOT NULL,
    recipe_id INT NOT NULL,
    date DATE NOT NULL,
    meal_type VARCHAR(20) NOT NULL,
    FOREIGN KEY (mealplan_id) REFERENCES meal_plan(mealplan_id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id) ON DELETE CASCADE,
    INDEX idx_mealplan_date (mealplan_id, date)
) ENGINE=InnoDB;

--SHOPPING LIST - generated from meal plans
CREATE TABLE shopping_list (
    list_id INT PRIMARY KEY AUTO_INCREMENT,
    mealplan_id INT NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (mealplan_id) REFERENCES meal_plan(mealplan_id) ON DELETE CASCADE
) ENGINE=InnoDB;

--SHOPPING LIST ITEMS - separate items from the list
CREATE TABLE shoppingList_item (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    list_id INT NOT NULL,
    ingredient_id INT NOT NULL,
    total_quantity FLOAT NOT NULL,
    unit VARCHAR(20) NOT NULL,
    is_purchased BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (list_id) REFERENCES shopping_list(list_id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(ingredient_id) ON DELETE CASCADE
) ENGINE=InnoDB;

