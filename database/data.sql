--Sample data for heal meal database

USE heal_meal_db;

--User names (50)

INSERT INTO user (name, email, password, diet_type, preferences) VALUES
('Alisher Karimov', 'alisher.karimov@email.com', 'pass123', 'balanced', 'Loves spicy food'),
('Nigora Abdullayeva', 'nigora.a@email.com', 'pass123', 'vegetarian', 'Prefers low-sodium meals'),
('Dmitry Ivanov', 'dmitry.ivanov@email.com', 'pass123', 'vegan', 'Allergic to nuts'),
('Zilola Rahimova', 'zilola.r@email.com', 'pass123', 'keto', 'High protein preference'),
('Sergey Petrov', 'sergey.petrov@email.com', 'pass123', 'paleo', 'Prefers organic ingredients'),
('Madina Yusupova', 'madina.y@email.com', 'pass123', 'balanced', 'Gluten-free diet'),
('Ruslan Tashkentov', 'ruslan.t@email.com', 'pass123', 'vegetarian', 'Loves Italian cuisine'),
('Olga Smirnova', 'olga.smirnova@email.com', 'pass123', 'low-carb', 'Diabetic-friendly meals'),
('Aziz Nazarov', 'aziz.nazarov@email.com', 'pass123', 'balanced', 'Family meals for 4'),
('Svetlana Kuznetsova', 'svetlana.k@email.com', 'pass123', 'vegan', 'Prefers raw foods'),
('Bobur Murodov', 'bobur.murodov@email.com', 'pass123', 'keto', 'Bodybuilder diet'),
('Natasha Popova', 'natasha.popova@email.com', 'pass123', 'mediterranean', 'Heart-healthy focus'),
('Sardor Ismoilov', 'sardor.i@email.com', 'pass123', 'balanced', 'Quick meals under 30min'),
('Marina Sokolova', 'marina.sokolova@email.com', 'pass123', 'pescatarian', 'Loves seafood'),
('Jamshid Mahmudov', 'jamshid.m@email.com', 'pass123', 'paleo', 'CrossFit athlete'),
('Gulnora Aliyeva', 'gulnora.a@email.com', 'pass123', 'vegetarian', 'Indian cuisine lover'),
('Alexander Volkov', 'alexander.v@email.com', 'pass123', 'low-carb', 'Pre-diabetic'),
('Dilnoza Tursunova', 'dilnoza.t@email.com', 'pass123', 'vegan', 'Environmental activist'),
('Timur Sharipov', 'timur.sharipov@email.com', 'pass123', 'balanced', 'Meal prep for week'),
('Irina Morozova', 'irina.morozova@email.com', 'pass123', 'keto', 'Weight loss journey'),
('Ravshan Ergashev', 'ravshan.e@email.com', 'pass123', 'mediterranean', 'Greek food fan'),
('Tatyana Novikova', 'tatyana.n@email.com', 'pass123', 'balanced', 'Senior citizen meals'),
('Otabek Umarov', 'otabek.umarov@email.com', 'pass123', 'vegetarian', 'College student budget'),
('Elena Lebedeva', 'elena.lebedeva@email.com', 'pass123', 'paleo', 'Whole30 follower'),
('Farhod Mirzayev', 'farhod.mirzayev@email.com', 'pass123', 'balanced', 'Food blogger'),
('Anastasia Orlova', 'anastasia.o@email.com', 'pass123', 'vegan', 'Animal rights advocate'),
('Jasur Khasanov', 'jasur.khasanov@email.com', 'pass123', 'keto', 'Epilepsy management'),
('Yuliya Kozlova', 'yuliya.kozlova@email.com', 'pass123', 'low-carb', 'PCOS management'),
('Kamil Safarov', 'kamil.safarov@email.com', 'pass123', 'balanced', 'Restaurant chef'),
('Valentina Zhukova', 'valentina.z@email.com', 'pass123', 'pescatarian', 'Coastal living'),
('Dilshod Nematov', 'dilshod.n@email.com', 'pass123', 'mediterranean', 'Longevity focus'),
('Anzhelika Belyayeva', 'anzhelika.b@email.com', 'pass123', 'vegetarian', 'Yoga instructor'),
('Rustam Akbarov', 'rustam.akbarov@email.com', 'pass123', 'paleo', 'Autoimmune protocol'),
('Larisa Medvedeva', 'larisa.medvedeva@email.com', 'pass123', 'balanced', 'Busy mom of 3'),
('Sherzod Boymurodov', 'sherzod.b@email.com', 'pass123', 'vegan', 'Marathon runner'),
('Lyudmila Romanova', 'lyudmila.r@email.com', 'pass123', 'keto', 'Carnivore adjacent'),
('Bekzod Juraev', 'bekzod.juraev@email.com', 'pass123', 'balanced', 'Fitness trainer'),
('Vera Andreeva', 'vera.andreeva@email.com', 'pass123', 'vegetarian', 'Ethical eating'),
('Sanjar Ibragimov', 'sanjar.i@email.com', 'pass123', 'low-carb', 'Type 2 diabetes'),
('Oksana Filippova', 'oksana.filippova@email.com', 'pass123', 'mediterranean', 'Spanish heritage'),
('Nodir Akhmedov', 'nodir.akhmedov@email.com', 'pass123', 'balanced', 'Meal delivery service'),
('Galina Makarova', 'galina.makarova@email.com', 'pass123', 'pescatarian', 'Sushi enthusiast'),
('Ulugbek Sultanov', 'ulugbek.sultanov@email.com', 'pass123', 'paleo', 'Hunter gatherer'),
('Kseniya Vinogradova', 'kseniya.v@email.com', 'pass123', 'vegan', 'Plant-based doctor'),
('Jahongir Fayzullayev', 'jahongir.f@email.com', 'pass123', 'keto', 'Biohacker'),
('Polina Zaitseva', 'polina.zaitseva@email.com', 'pass123', 'balanced', 'Culinary student'),
('Abbos Ubaydullayev', 'abbos.u@email.com', 'pass123', 'vegetarian', 'Buddhist monk'),
('Natalya Nikitina', 'natalya.nikitina@email.com', 'pass123', 'low-carb', 'Insulin resistance'),
('Bahrom Qodirov', 'bahrom.qodirov@email.com', 'pass123', 'mediterranean', 'Heart attack survivor'),
('Mariya Fedorova', 'mariya.fedorova@email.com', 'pass123', 'balanced', 'Cooking show host');

--user health goals

INSERT INTO user_health_goal (user_id, daily_calorie_target, protein_target, carb_target, fat_target, goal_type, start_date) VALUES
(1, 2200, 150, 200, 80, 'maintenance', '2025-01-01'),
(4, 1500, 120, 30, 100, 'weight_loss', '2025-01-01'),
(11, 2800, 200, 50, 150, 'muscle_gain', '2025-01-15'),
(15, 2500, 180, 150, 90, 'athletic_performance', '2025-02-01'),
(20, 1600, 100, 40, 110, 'weight_loss', '2025-01-10');

--Ingredients

INSERT INTO ingredient (name, unit, calories_per_unit, protein, fat, carbs) VALUES
-- Proteins
('Chicken Breast', 'gram', 1.65, 0.31, 0.036, 0),
('Ground Beef (90% lean)', 'gram', 1.76, 0.25, 0.10, 0),
('Salmon Fillet', 'gram', 2.08, 0.20, 0.13, 0),
('Tofu (firm)', 'gram', 0.76, 0.08, 0.048, 0.019),
('Eggs (large)', 'piece', 72, 6.3, 4.8, 0.4),
('Greek Yogurt (plain)', 'gram', 0.59, 0.10, 0.004, 0.036),
('Shrimp', 'gram', 0.99, 0.24, 0.013, 0),
('Turkey Breast', 'gram', 1.35, 0.30, 0.007, 0),
('Tuna (canned in water)', 'gram', 1.16, 0.26, 0.008, 0),
('Cottage Cheese (low-fat)', 'gram', 0.72, 0.11, 0.01, 0.038),
('Pork Tenderloin', 'gram', 1.43, 0.26, 0.035, 0),
('Cod Fillet', 'gram', 0.82, 0.18, 0.007, 0),
('Black Beans (cooked)', 'gram', 1.32, 0.089, 0.005, 0.238),
('Chickpeas (cooked)', 'gram', 1.64, 0.086, 0.026, 0.271),
('Lentils (cooked)', 'gram', 1.16, 0.090, 0.004, 0.201),

-- Vegetables
('Broccoli', 'gram', 0.34, 0.028, 0.004, 0.066),
('Spinach', 'gram', 0.23, 0.029, 0.004, 0.036),
('Kale', 'gram', 0.49, 0.043, 0.009, 0.087),
('Carrots', 'gram', 0.41, 0.009, 0.002, 0.096),
('Bell Pepper (red)', 'gram', 0.31, 0.010, 0.003, 0.06),
('Tomatoes', 'gram', 0.18, 0.009, 0.002, 0.039),
('Cauliflower', 'gram', 0.25, 0.019, 0.003, 0.049),
('Zucchini', 'gram', 0.17, 0.012, 0.003, 0.033),
('Cucumber', 'gram', 0.15, 0.007, 0.001, 0.036),
('Lettuce (romaine)', 'gram', 0.17, 0.012, 0.003, 0.033),
('Asparagus', 'gram', 0.20, 0.022, 0.001, 0.039),
('Green Beans', 'gram', 0.31, 0.018, 0.002, 0.070),
('Brussels Sprouts', 'gram', 0.43, 0.034, 0.003, 0.089),
('Sweet Potato', 'gram', 0.86, 0.016, 0.001, 0.201),
('Mushrooms', 'gram', 0.22, 0.031, 0.003, 0.033),

-- Fruits
('Banana', 'piece', 105, 1.3, 0.4, 27),
('Apple', 'piece', 95, 0.5, 0.3, 25),
('Blueberries', 'gram', 0.57, 0.007, 0.003, 0.144),
('Strawberries', 'gram', 0.32, 0.007, 0.003, 0.077),
('Avocado', 'piece', 240, 3, 22, 13),
('Orange', 'piece', 62, 1.2, 0.2, 15.4),
('Mango', 'gram', 0.60, 0.008, 0.004, 0.150),
('Pineapple', 'gram', 0.50, 0.005, 0.001, 0.131),
('Grapes', 'gram', 0.69, 0.007, 0.002, 0.181),
('Watermelon', 'gram', 0.30, 0.006, 0.002, 0.076),

-- Grains & Carbs
('Brown Rice (cooked)', 'gram', 1.12, 0.026, 0.009, 0.230),
('Quinoa (cooked)', 'gram', 1.20, 0.043, 0.019, 0.211),
('Oats (dry)', 'gram', 3.89, 0.169, 0.069, 0.664),
('Whole Wheat Bread', 'slice', 80, 4, 1, 14),
('White Rice (cooked)', 'gram', 1.30, 0.027, 0.003, 0.281),
('Pasta (whole wheat, cooked)', 'gram', 1.24, 0.053, 0.011, 0.260),
('Sweet Potato', 'gram', 0.86, 0.016, 0.001, 0.201),
('Potato', 'gram', 0.77, 0.020, 0.001, 0.171),

-- Nuts & Seeds
('Almonds', 'gram', 5.79, 0.211, 0.497, 0.219),
('Walnuts', 'gram', 6.54, 0.152, 0.654, 0.137),
('Chia Seeds', 'gram', 4.86, 0.167, 0.309, 0.424),
('Flax Seeds', 'gram', 5.34, 0.183, 0.423, 0.289),
('Peanut Butter', 'tablespoon', 94, 3.6, 8, 3.6),
('Cashews', 'gram', 5.53, 0.182, 0.437, 0.303),

-- Dairy & Alternatives
('Milk (2%)', 'cup', 122, 8.1, 4.8, 11.7),
('Almond Milk (unsweetened)', 'cup', 30, 1, 2.5, 1),
('Cheddar Cheese', 'gram', 4.03, 0.249, 0.333, 0.013),
('Mozzarella (part-skim)', 'gram', 2.54, 0.246, 0.157, 0.031),
('Feta Cheese', 'gram', 2.64, 0.143, 0.213, 0.044),

-- Oils & Fats
('Olive Oil', 'tablespoon', 119, 0, 13.5, 0),
('Coconut Oil', 'tablespoon', 117, 0, 13.6, 0),
('Butter', 'tablespoon', 102, 0.1, 11.5, 0.01),
('Avocado Oil', 'tablespoon', 124, 0, 14, 0),

-- Condiments & Seasonings
('Garlic', 'clove', 4, 0.2, 0, 1),
('Onion', 'gram', 0.40, 0.011, 0.001, 0.093),
('Ginger', 'gram', 0.80, 0.018, 0.008, 0.178),
('Soy Sauce', 'tablespoon', 11, 1, 0, 1),
('Honey', 'tablespoon', 64, 0.1, 0, 17.3),
('Maple Syrup', 'tablespoon', 52, 0, 0.01, 13.4),
('Lemon Juice', 'tablespoon', 3, 0.1, 0, 1),
('Balsamic Vinegar', 'tablespoon', 14, 0, 0, 2.7),
('Salt', 'teaspoon', 0, 0, 0, 0),
('Black Pepper', 'teaspoon', 6, 0.2, 0.1, 1.5);


INSERT INTO ingredient (name, unit, calories_per_unit, protein, fat, carbs) VALUES
('Beef Sirloin', 'gram', 2.71, 0.253, 0.176, 0),
('Lamb Chop', 'gram', 2.94, 0.246, 0.215, 0),
('Duck Breast', 'gram', 1.37, 0.188, 0.056, 0),
('Venison', 'gram', 1.57, 0.304, 0.031, 0),
('Tilapia', 'gram', 1.29, 0.263, 0.026, 0),
('Halibut', 'gram', 1.11, 0.223, 0.023, 0),
('Scallops', 'gram', 0.69, 0.120, 0.006, 0.021),
('Lobster', 'gram', 0.77, 0.161, 0.009, 0.022),
('Crab Meat', 'gram', 0.97, 0.195, 0.012, 0),
('Mussels', 'gram', 0.86, 0.118, 0.022, 0.037),
('Octopus', 'gram', 0.82, 0.149, 0.011, 0.022),
('Sardines', 'gram', 2.08, 0.249, 0.116, 0),
('Anchovies', 'gram', 2.10, 0.289, 0.097, 0),
('Mackerel', 'gram', 2.62, 0.239, 0.178, 0),
('Herring', 'gram', 1.58, 0.179, 0.090, 0),
('Bacon', 'slice', 43, 3, 3.3, 0.1),
('Sausage (Italian)', 'gram', 3.46, 0.167, 0.303, 0.028),
('Ham (lean)', 'gram', 1.45, 0.207, 0.060, 0.015),
('Prosciutto', 'gram', 2.46, 0.266, 0.151, 0.010),
('Salami', 'gram', 4.07, 0.227, 0.337, 0.015),
('Tempeh', 'gram', 1.93, 0.190, 0.108, 0.092),
('Seitan', 'gram', 3.70, 0.750, 0.018, 0.140),
('Edamame', 'gram', 1.22, 0.116, 0.051, 0.099),
('Navy Beans', 'gram', 1.40, 0.083, 0.006, 0.262),
('Kidney Beans', 'gram', 1.27, 0.084, 0.005, 0.225),
('Pinto Beans', 'gram', 1.43, 0.090, 0.009, 0.264),
('Lima Beans', 'gram', 1.15, 0.079, 0.004, 0.208),
('Split Peas', 'gram', 1.16, 0.082, 0.004, 0.208),
('Green Peas', 'gram', 0.81, 0.054, 0.004, 0.144),
('Snow Peas', 'gram', 0.42, 0.027, 0.002, 0.075),
('Arugula', 'gram', 0.25, 0.026, 0.007, 0.037),
('Bok Choy', 'gram', 0.13, 0.015, 0.002, 0.023),
('Swiss Chard', 'gram', 0.19, 0.018, 0.002, 0.037),
('Collard Greens', 'gram', 0.32, 0.030, 0.006, 0.058),
('Cabbage', 'gram', 0.25, 0.013, 0.001, 0.058),
('Red Cabbage', 'gram', 0.31, 0.014, 0.002, 0.072),
('Eggplant', 'gram', 0.25, 0.010, 0.002, 0.059),
('Radish', 'gram', 0.16, 0.007, 0.001, 0.034),
('Turnip', 'gram', 0.28, 0.009, 0.001, 0.064),
('Beets', 'gram', 0.43, 0.016, 0.002, 0.096),
('Celery', 'gram', 0.14, 0.007, 0.002, 0.030),
('Leeks', 'gram', 0.61, 0.015, 0.003, 0.143),
('Shallots', 'gram', 0.72, 0.025, 0.001, 0.167),
('Scallions', 'gram', 0.32, 0.018, 0.002, 0.073),
('Parsnips', 'gram', 0.75, 0.012, 0.003, 0.179),
('Rutabaga', 'gram', 0.36, 0.011, 0.002, 0.087),
('Jicama', 'gram', 0.38, 0.007, 0.001, 0.089),
('Artichoke', 'gram', 0.47, 0.032, 0.002, 0.108),
('Fennel', 'gram', 0.31, 0.012, 0.002, 0.073),
('Okra', 'gram', 0.33, 0.019, 0.002, 0.074),
('Squash (butternut)', 'gram', 0.45, 0.010, 0.001, 0.119),
('Squash (acorn)', 'gram', 0.56, 0.011, 0.001, 0.149),
('Pumpkin', 'gram', 0.26, 0.010, 0.001, 0.069),
('Corn (sweet)', 'gram', 0.86, 0.032, 0.012, 0.189),
('Peas and Carrots', 'gram', 0.38, 0.026, 0.003, 0.084),
('Peach', 'piece', 58, 1.4, 0.4, 14.3),
('Pear', 'piece', 101, 0.6, 0.2, 27),
('Plum', 'piece', 30, 0.5, 0.2, 7.5),
('Apricot', 'piece', 17, 0.5, 0.1, 3.9),
('Cherry', 'gram', 0.50, 0.008, 0.002, 0.126),
('Kiwi', 'piece', 42, 0.8, 0.4, 10.1),
('Papaya', 'gram', 0.43, 0.005, 0.003, 0.109),
('Cantaloupe', 'gram', 0.34, 0.008, 0.002, 0.083),
('Honeydew Melon', 'gram', 0.36, 0.005, 0.001, 0.091),
('Grapefruit', 'piece', 52, 0.9, 0.2, 13.1),
('Pomegranate', 'gram', 0.83, 0.017, 0.012, 0.187),
('Dates', 'piece', 20, 0.2, 0, 5.3),
('Figs', 'piece', 37, 0.4, 0.2, 9.6),
('Prunes', 'piece', 20, 0.2, 0, 5.4),
('Raisins', 'gram', 2.99, 0.031, 0.005, 0.792),
('Cranberries (dried)', 'gram', 3.08, 0.001, 0.013, 0.825),
('Coconut (shredded)', 'gram', 3.54, 0.033, 0.335, 0.235),
('Raspberries', 'gram', 0.52, 0.012, 0.007, 0.119),
('Blackberries', 'gram', 0.43, 0.014, 0.005, 0.096),
('Barley (cooked)', 'gram', 1.23, 0.023, 0.004, 0.283),
('Bulgur (cooked)', 'gram', 0.83, 0.031, 0.002, 0.189),
('Couscous (cooked)', 'gram', 1.12, 0.038, 0.002, 0.231),
('Farro (cooked)', 'gram', 1.70, 0.065, 0.013, 0.352),
('Millet (cooked)', 'gram', 1.19, 0.035, 0.010, 0.237),
('Buckwheat (cooked)', 'gram', 0.92, 0.034, 0.006, 0.199),
('Amaranth (cooked)', 'gram', 1.02, 0.039, 0.016, 0.188),
('Teff (cooked)', 'gram', 1.01, 0.039, 0.007, 0.198),
('Sorghum (cooked)', 'gram', 1.39, 0.112, 0.033, 0.298),
('Wild Rice (cooked)', 'gram', 1.01, 0.040, 0.003, 0.213),
('Jasmine Rice (cooked)', 'gram', 1.41, 0.026, 0.004, 0.314),
('Basmati Rice (cooked)', 'gram', 1.21, 0.025, 0.003, 0.264),
('Arborio Rice (cooked)', 'gram', 1.30, 0.024, 0.002, 0.285),
('Risotto Rice (cooked)', 'gram', 1.29, 0.025, 0.003, 0.282),
('Corn Tortilla', 'piece', 52, 1.4, 0.7, 10.7),
('Flour Tortilla', 'piece', 90, 2.4, 2.5, 15),
('Pita Bread', 'piece', 165, 5.5, 0.7, 33.4),
('Naan Bread', 'piece', 262, 8.7, 5.2, 45.4),
('Sourdough Bread', 'slice', 93, 3.8, 0.6, 18.0),
('Rye Bread', 'slice', 83, 2.7, 1.1, 15.5),
('Pumpernickel Bread', 'slice', 80, 2.7, 1.0, 15.2),
('Bagel', 'piece', 245, 9.0, 1.4, 47.9),
('English Muffin', 'piece', 134, 4.4, 1.0, 26.2),
('Croissant', 'piece', 231, 4.7, 12, 26.1),
('Biscuit', 'piece', 212, 4.2, 9.8, 27),
('Pecans', 'gram', 6.91, 0.092, 0.720, 0.139),
('Pistachios', 'gram', 5.60, 0.202, 0.451, 0.277),
('Macadamia Nuts', 'gram', 7.18, 0.079, 0.757, 0.138),
('Brazil Nuts', 'gram', 6.56, 0.141, 0.665, 0.120),
('Hazelnuts', 'gram', 6.28, 0.150, 0.608, 0.167),
('Pine Nuts', 'gram', 6.73, 0.137, 0.684, 0.132),
('Sunflower Seeds', 'gram', 5.84, 0.207, 0.517, 0.200),
('Pumpkin Seeds', 'gram', 5.59, 0.302, 0.490, 0.104),
('Sesame Seeds', 'gram', 5.73, 0.177, 0.498, 0.232),
('Hemp Seeds', 'gram', 5.53, 0.317, 0.489, 0.088),
('Poppy Seeds', 'gram', 5.25, 0.179, 0.416, 0.286),
('Almond Butter', 'tablespoon', 98, 3.4, 9, 3),
('Cashew Butter', 'tablespoon', 94, 2.8, 7.9, 4.4),
('Tahini', 'tablespoon', 89, 2.6, 8.1, 3.2),
('Cream Cheese', 'tablespoon', 51, 0.9, 5.1, 0.8),
('Ricotta Cheese', 'gram', 1.74, 0.114, 0.130, 0.030),
('Parmesan Cheese', 'gram', 4.31, 0.381, 0.286, 0.042),
('Blue Cheese', 'gram', 3.53, 0.214, 0.286, 0.023),
('Goat Cheese', 'gram', 3.64, 0.216, 0.298, 0.025),
('Brie Cheese', 'gram', 3.34, 0.208, 0.277, 0.005),
('Swiss Cheese', 'gram', 3.80, 0.269, 0.278, 0.054),
('Provolone Cheese', 'gram', 3.51, 0.254, 0.268, 0.021),
('Monterey Jack Cheese', 'gram', 3.73, 0.249, 0.302, 0.007),
('Colby Cheese', 'gram', 3.94, 0.235, 0.321, 0.028),
('Sour Cream', 'tablespoon', 23, 0.4, 2.3, 0.5),
('Heavy Cream', 'tablespoon', 51, 0.4, 5.4, 0.4),
('Half and Half', 'tablespoon', 20, 0.4, 1.7, 0.6),
('Whipped Cream', 'tablespoon', 8, 0.1, 0.7, 0.4),
('Condensed Milk', 'tablespoon', 61, 1.5, 1.7, 10.4),
('Evaporated Milk', 'tablespoon', 21, 1.0, 1.2, 1.6),
('Buttermilk', 'cup', 98, 8.1, 2.2, 11.7),
('Coconut Milk (canned)', 'cup', 445, 4.6, 48.2, 6.4),
('Oat Milk', 'cup', 120, 3, 5, 16),
('Soy Milk', 'cup', 80, 7, 4, 4),
('Cashew Milk', 'cup', 25, 1, 2, 1),
('Rice Milk', 'cup', 113, 0.7, 2.3, 22.4),
('Kefir', 'cup', 104, 9.2, 2.5, 12),
('Canola Oil', 'tablespoon', 124, 0, 14, 0),
('Vegetable Oil', 'tablespoon', 120, 0, 13.6, 0),
('Sesame Oil', 'tablespoon', 120, 0, 13.6, 0),
('Peanut Oil', 'tablespoon', 119, 0, 13.5, 0),
('Sunflower Oil', 'tablespoon', 120, 0, 13.6, 0),
('Grapeseed Oil', 'tablespoon', 120, 0, 13.6, 0),
('Walnut Oil', 'tablespoon', 120, 0, 13.6, 0),
('Ghee', 'tablespoon', 112, 0, 12.7, 0),
('Lard', 'tablespoon', 115, 0, 12.8, 0),
('Shortening', 'tablespoon', 113, 0, 12.8, 0),
('Mayonnaise', 'tablespoon', 94, 0.1, 10.3, 0.1),
('Mustard', 'teaspoon', 3, 0.2, 0.2, 0.3),
('Ketchup', 'tablespoon', 17, 0.2, 0, 4.5),
('BBQ Sauce', 'tablespoon', 29, 0.3, 0.1, 7),
('Hot Sauce', 'teaspoon', 1, 0, 0, 0.1),
('Worcestershire Sauce', 'tablespoon', 13, 0, 0, 3.3),
('Fish Sauce', 'tablespoon', 6, 0.9, 0, 0.7),
('Teriyaki Sauce', 'tablespoon', 16, 1.1, 0, 2.9),
('Hoisin Sauce', 'tablespoon', 35, 0.5, 0.5, 7),
('Sriracha', 'teaspoon', 5, 0.1, 0.1, 1.1),
('Salsa', 'tablespoon', 4, 0.1, 0, 1),
('Pesto', 'tablespoon', 80, 2, 7.5, 2.3),
('Hummus', 'tablespoon', 25, 1.2, 1.4, 3),
('Guacamole', 'tablespoon', 23, 0.3, 2, 1.2),
('Tomato Sauce', 'cup', 70, 3.3, 0.4, 16.5),
('Marinara Sauce', 'cup', 130, 4, 4, 20),
('Alfredo Sauce', 'cup', 480, 12, 40, 20),
('Ranch Dressing', 'tablespoon', 73, 0.4, 7.7, 1.4),
('Italian Dressing', 'tablespoon', 43, 0.1, 4.2, 1.5),
('Caesar Dressing', 'tablespoon', 78, 0.5, 8.4, 0.5),
('Vinaigrette', 'tablespoon', 72, 0, 8, 0.8),
('Blue Cheese Dressing', 'tablespoon', 77, 0.7, 8, 1.1),
('Thousand Island Dressing', 'tablespoon', 59, 0.2, 5.6, 2.3),
('Cumin', 'teaspoon', 8, 0.4, 0.5, 0.9),
('Paprika', 'teaspoon', 6, 0.3, 0.3, 1.2),
('Turmeric', 'teaspoon', 8, 0.2, 0.2, 1.4),
('Cinnamon', 'teaspoon', 6, 0.1, 0, 2.1),
('Oregano', 'teaspoon', 3, 0.1, 0.1, 0.7),
('Basil', 'teaspoon', 1, 0.1, 0, 0.1),
('Thyme', 'teaspoon', 3, 0.1, 0.1, 0.7),
('Rosemary', 'teaspoon', 4, 0.1, 0.2, 0.8),
('Parsley', 'tablespoon', 1, 0.1, 0, 0.2),
('Cilantro', 'tablespoon', 1, 0.1, 0, 0.1),
('Dill', 'tablespoon', 1, 0.1, 0, 0.2),
('Mint', 'tablespoon', 2, 0.1, 0, 0.5),
('Sage', 'teaspoon', 2, 0.1, 0.1, 0.4),
('Bay Leaf', 'piece', 2, 0, 0.1, 0.5),
('Cardamom', 'teaspoon', 6, 0.2, 0.1, 1.4),
('Cloves', 'teaspoon', 7, 0.1, 0.4, 1.3),
('Nutmeg', 'teaspoon', 12, 0.1, 0.8, 1.1),
('Vanilla Extract', 'teaspoon', 12, 0, 0, 0.5),
('Cocoa Powder', 'tablespoon', 12, 1, 0.7, 3),
('Dark Chocolate', 'gram', 5.98, 0.048, 0.428, 0.457),
('Milk Chocolate', 'gram', 5.35, 0.078, 0.297, 0.591),
('White Chocolate', 'gram', 5.39, 0.059, 0.323, 0.593),
('Brown Sugar', 'tablespoon', 52, 0, 0, 13.5),
('White Sugar', 'tablespoon', 49, 0, 0, 12.6),
('Powdered Sugar', 'tablespoon', 31, 0, 0, 8),
('Molasses', 'tablespoon', 58, 0, 0, 15),
('Agave Nectar', 'tablespoon', 60, 0, 0, 16),
('Stevia', 'teaspoon', 0, 0, 0, 0),
('Flour (all-purpose)', 'cup', 455, 12.9, 1.2, 95.4),
('Flour (whole wheat)', 'cup', 407, 16.4, 2.2, 87),
('Flour (almond)', 'cup', 640, 24, 56, 24),
('Flour (coconut)', 'cup', 480, 16, 16, 72),
('Cornstarch', 'tablespoon', 30, 0, 0, 7.3),
('Baking Powder', 'teaspoon', 2, 0, 0, 1.3),
('Baking Soda', 'teaspoon', 0, 0, 0, 0),
('Yeast', 'teaspoon', 7, 0.9, 0.1, 0.9),
('Gelatin', 'teaspoon', 8, 2, 0, 0),
('Cornmeal', 'cup', 442, 9.9, 4.4, 93.8),
('Panko Breadcrumbs', 'cup', 110, 3.7, 0.8, 20.4),
('Regular Breadcrumbs', 'cup', 427, 13.4, 5.8, 78.3);

--Healthy recipes

INSERT INTO recipe (title, description, prep_time, cook_time, servings, difficulty, category, created_by) VALUES
-- Breakfast Recipes
('High-Protein Overnight Oats', 'Creamy oats with Greek yogurt and berries, perfect for meal prep', 10, 0, 1, 'Easy', 'Breakfast', 1),
('Veggie-Packed Omelet', 'Fluffy omelet filled with spinach, tomatoes, and mushrooms', 5, 10, 1, 'Easy', 'Breakfast', 2),
('Quinoa Breakfast Bowl', 'Protein-rich quinoa topped with fruits and nuts', 5, 15, 2, 'Easy', 'Breakfast', 3),
('Avocado Toast with Eggs', 'Whole wheat toast topped with mashed avocado and poached eggs', 5, 8, 1, 'Easy', 'Breakfast', 4),
('Berry Protein Smoothie', 'Refreshing smoothie with mixed berries, Greek yogurt, and protein powder', 5, 0, 1, 'Easy', 'Breakfast', 5),
('Sweet Potato Hash', 'Savory breakfast hash with sweet potatoes, bell peppers, and eggs', 10, 20, 2, 'Medium', 'Breakfast', 6),
('Chia Seed Pudding', 'Creamy pudding made with chia seeds, almond milk, and berries', 5, 0, 2, 'Easy', 'Breakfast', 7),
('Greek Yogurt Parfait', 'Layered parfait with Greek yogurt, granola, and fresh fruits', 5, 0, 1, 'Easy', 'Breakfast', 8),
('Whole Wheat Pancakes', 'Fluffy pancakes made with whole wheat flour and topped with fruit', 10, 15, 4, 'Medium', 'Breakfast', 9),
('Spinach Feta Scramble', 'Scrambled eggs with spinach, feta cheese, and cherry tomatoes', 5, 8, 1, 'Easy', 'Breakfast', 10),

-- Lunch Recipes
('Grilled Chicken Salad', 'Mixed greens with grilled chicken breast, vegetables, and balsamic vinaigrette', 15, 15, 2, 'Easy', 'Lunch', 11),
('Mediterranean Quinoa Bowl', 'Quinoa bowl with chickpeas, cucumber, tomatoes, and tahini dressing', 10, 15, 2, 'Easy', 'Lunch', 12),
('Turkey and Avocado Wrap', 'Whole wheat wrap filled with turkey, avocado, lettuce, and tomato', 10, 0, 1, 'Easy', 'Lunch', 13),
('Lentil Soup', 'Hearty soup with lentils, carrots, celery, and aromatic herbs', 15, 30, 4, 'Easy', 'Lunch', 14),
('Tuna Poke Bowl', 'Fresh tuna served over brown rice with edamame and vegetables', 20, 0, 2, 'Medium', 'Lunch', 15),
('Chicken Caesar Salad', 'Classic Caesar salad with grilled chicken and homemade dressing', 15, 15, 2, 'Easy', 'Lunch', 16),
('Black Bean Burrito Bowl', 'Mexican-inspired bowl with black beans, rice, vegetables, and salsa', 15, 20, 2, 'Easy', 'Lunch', 17),
('Caprese Sandwich', 'Fresh mozzarella, tomatoes, and basil on ciabatta bread', 10, 0, 1, 'Easy', 'Lunch', 18),
('Asian Chicken Lettuce Wraps', 'Ground chicken mixture served in crisp lettuce cups', 15, 10, 2, 'Medium', 'Lunch', 19),
('Greek Chicken Pita', 'Grilled chicken in pita bread with tzatziki and vegetables', 15, 15, 2, 'Easy', 'Lunch', 20),

-- Dinner Recipes
('Baked Salmon with Asparagus', 'Herb-crusted salmon fillets with roasted asparagus', 10, 25, 2, 'Medium', 'Dinner', 21),
('Grilled Chicken and Vegetables', 'Marinated chicken breast with colorful grilled vegetables', 20, 20, 4, 'Easy', 'Dinner', 22),
('Beef Stir-Fry', 'Tender beef strips with broccoli, bell peppers in savory sauce', 15, 15, 4, 'Medium', 'Dinner', 23),
('Vegetarian Chili', 'Hearty chili made with beans, tomatoes, and spices', 15, 45, 6, 'Easy', 'Dinner', 24),
('Lemon Herb Chicken', 'Juicy chicken thighs with lemon, garlic, and fresh herbs', 10, 35, 4, 'Easy', 'Dinner', 25),
('Shrimp Pasta Primavera', 'Whole wheat pasta with shrimp and seasonal vegetables', 15, 20, 4, 'Medium', 'Dinner', 26),
('Turkey Meatballs', 'Lean turkey meatballs in marinara sauce', 20, 25, 6, 'Medium', 'Dinner', 27),
('Tofu Curry', 'Creamy coconut curry with tofu and vegetables', 15, 25, 4, 'Medium', 'Dinner', 28),
('Grilled Steak with Sweet Potato', 'Perfectly grilled steak with baked sweet potato', 10, 20, 2, 'Medium', 'Dinner', 29),
('Baked Cod with Green Beans', 'Flaky cod fillet with sautéed green beans and lemon', 10, 20, 2, 'Easy', 'Dinner', 30),
('Chicken Fajitas', 'Sizzling chicken fajitas with peppers and onions', 15, 20, 4, 'Easy', 'Dinner', 31),
('Pork Tenderloin with Brussels Sprouts', 'Roasted pork tenderloin with caramelized Brussels sprouts', 15, 35, 4, 'Medium', 'Dinner', 32),
('Eggplant Parmesan', 'Breaded eggplant layered with marinara and mozzarella', 30, 45, 6, 'Hard', 'Dinner', 33),
('Salmon Teriyaki', 'Glazed salmon with teriyaki sauce served over rice', 10, 20, 2, 'Easy', 'Dinner', 34),
('Chicken Tikka Masala', 'Tender chicken in creamy tomato-based curry sauce', 30, 30, 6, 'Medium', 'Dinner', 35),

-- Snacks & Sides
('Roasted Chickpeas', 'Crispy roasted chickpeas seasoned with spices', 5, 30, 4, 'Easy', 'Snack', 36),
('Veggie Sticks with Hummus', 'Fresh vegetable sticks served with homemade hummus', 10, 0, 2, 'Easy', 'Snack', 37),
('Energy Balls', 'No-bake energy balls made with oats, dates, and nuts', 15, 0, 12, 'Easy', 'Snack', 38),
('Baked Kale Chips', 'Crispy kale chips seasoned with sea salt', 5, 15, 2, 'Easy', 'Snack', 39),
('Greek Yogurt Dip', 'Creamy dip made with Greek yogurt and herbs', 5, 0, 4, 'Easy', 'Snack', 40),
('Quinoa Salad', 'Refreshing quinoa salad with vegetables and lemon dressing', 15, 15, 4, 'Easy', 'Side', 41),
('Roasted Vegetables', 'Colorful medley of roasted seasonal vegetables', 15, 30, 4, 'Easy', 'Side', 42),
('Cauliflower Rice', 'Low-carb alternative to rice, seasoned and sautéed', 10, 10, 4, 'Easy', 'Side', 43),
('Sweet Potato Fries', 'Crispy baked sweet potato fries with herbs', 10, 25, 4, 'Easy', 'Side', 44),
('Steamed Broccoli', 'Simple steamed broccoli with garlic and lemon', 5, 10, 4, 'Easy', 'Side', 45),

-- More Dinner Recipes
('Chicken Burrito Bowl', 'Mexican-inspired bowl with chicken, rice, beans, and toppings', 20, 20, 4, 'Easy', 'Dinner', 1),
('Baked Tilapia with Vegetables', 'Flaky tilapia baked with zucchini and tomatoes', 10, 25, 2, 'Easy', 'Dinner', 2),
('Turkey Chili', 'Lean ground turkey chili with beans and spices', 15, 40, 6, 'Easy', 'Dinner', 3),
('Vegetable Stir-Fry with Tofu', 'Colorful vegetable medley with crispy tofu', 15, 15, 4, 'Easy', 'Dinner', 4),
('Grilled Pork Chops', 'Juicy pork chops marinated and grilled to perfection', 15, 15, 2, 'Medium', 'Dinner', 5),
('Stuffed Bell Peppers', 'Bell peppers filled with ground turkey and quinoa', 20, 40, 4, 'Medium', 'Dinner', 6),
('Lemon Garlic Shrimp', 'Quick sautéed shrimp with lemon, garlic, and herbs', 10, 10, 2, 'Easy', 'Dinner', 7),
('Chicken Enchiladas', 'Rolled tortillas filled with chicken and topped with sauce', 25, 30, 6, 'Medium', 'Dinner', 8),
('Beef and Broccoli', 'Classic Chinese dish with tender beef and broccoli', 15, 15, 4, 'Medium', 'Dinner', 9),
('Baked Chicken Thighs', 'Crispy-skinned chicken thighs with herb seasoning', 10, 45, 4, 'Easy', 'Dinner', 10),
('Moroccan Chickpea Stew', 'Aromatic stew with chickpeas, tomatoes, and spices', 15, 30, 6, 'Medium', 'Dinner', 11),
('Zucchini Noodles with Pesto', 'Low-carb zucchini noodles tossed in fresh pesto', 15, 10, 2, 'Easy', 'Dinner', 12),
('Honey Mustard Salmon', 'Glazed salmon with honey mustard sauce', 10, 20, 2, 'Easy', 'Dinner', 13),
('Thai Curry Chicken', 'Spicy Thai curry with chicken and vegetables', 15, 25, 4, 'Medium', 'Dinner', 14),
('Meatloaf with Vegetables', 'Classic meatloaf packed with hidden vegetables', 20, 60, 6, 'Medium', 'Dinner', 15),
('Balsamic Chicken', 'Chicken breasts in tangy balsamic glaze', 10, 25, 4, 'Easy', 'Dinner', 16),
('Veggie Burger', 'Homemade vegetarian burger with beans and vegetables', 20, 20, 4, 'Medium', 'Dinner', 17),
('Teriyaki Beef Bowl', 'Sliced beef with teriyaki sauce over rice', 15, 15, 2, 'Easy', 'Dinner', 18),
('Lemon Pepper Tilapia', 'Seasoned tilapia with bright lemon pepper flavor', 5, 15, 2, 'Easy', 'Dinner', 19),
('Chicken Parmesan', 'Breaded chicken cutlet with marinara and cheese', 20, 30, 4, 'Medium', 'Dinner', 20),

-- More Breakfast Recipes
('Protein Waffles', 'High-protein waffles made with protein powder', 10, 15, 4, 'Easy', 'Breakfast', 21),
('Breakfast Burrito', 'Scrambled eggs with vegetables wrapped in tortilla', 10, 10, 1, 'Easy', 'Breakfast', 22),
('Banana Oat Muffins', 'Healthy muffins made with bananas and oats', 15, 25, 12, 'Easy', 'Breakfast', 23),
('Smoked Salmon Bagel', 'Whole wheat bagel with cream cheese and smoked salmon', 5, 0, 1, 'Easy', 'Breakfast', 24),
('Egg White Frittata', 'Light frittata with vegetables and herbs', 15, 25, 4, 'Medium', 'Breakfast', 25),

-- More Lunch Recipes
('Chicken Pesto Pasta', 'Whole wheat pasta with grilled chicken and pesto', 10, 20, 4, 'Easy', 'Lunch', 26),
('Veggie Sushi Bowl', 'Deconstructed sushi with vegetables and brown rice', 20, 15, 2, 'Medium', 'Lunch', 27),
('Spinach Salad with Salmon', 'Fresh spinach salad topped with grilled salmon', 15, 15, 2, 'Easy', 'Lunch', 28),
('Chicken Noodle Soup', 'Comforting soup with chicken, noodles, and vegetables', 15, 30, 6, 'Easy', 'Lunch', 29),
('Falafel Wrap', 'Crispy falafel in whole wheat wrap with tahini', 20, 20, 2, 'Medium', 'Lunch', 30),

-- Healthy Desserts
('Chocolate Protein Brownies', 'Fudgy brownies made with protein powder', 15, 25, 9, 'Easy', 'Dessert', 31),
('Baked Apples', 'Warm cinnamon baked apples with oat topping', 10, 30, 4, 'Easy', 'Dessert', 32),
('Banana Ice Cream', 'Creamy ice cream made from frozen bananas', 5, 0, 2, 'Easy', 'Dessert', 33),
('Chia Seed Pudding Parfait', 'Layered dessert with chia pudding and fruits', 10, 0, 2, 'Easy', 'Dessert', 34),
('Greek Yogurt Cheesecake', 'Lighter cheesecake made with Greek yogurt', 20, 60, 8, 'Hard', 'Dessert', 35),

-- Post-Workout Meals
('Protein Pancakes', 'High-protein pancakes perfect post-workout', 10, 15, 2, 'Easy', 'Post-Workout', 36),
('Turkey and Sweet Potato Bowl', 'Ground turkey with roasted sweet potato and greens', 15, 25, 2, 'Easy', 'Post-Workout', 37),
('Chocolate Peanut Butter Smoothie', 'Protein-packed smoothie with chocolate and PB', 5, 0, 1, 'Easy', 'Post-Workout', 38),
('Tuna and Avocado Bowl', 'Quick bowl with tuna, avocado, and brown rice', 10, 15, 1, 'Easy', 'Post-Workout', 39),
('Chicken and Quinoa Bowl', 'Balanced bowl with chicken, quinoa, and vegetables', 15, 25, 2, 'Easy', 'Post-Workout', 40),

-- Vegan Recipes
('Vegan Buddha Bowl', 'Nourishing bowl with tofu, vegetables, and tahini', 20, 20, 2, 'Easy', 'Vegan', 18),
('Lentil Dal', 'Creamy Indian lentil curry', 10, 30, 4, 'Easy', 'Vegan', 26),
('Vegan Tacos', 'Black bean and vegetable tacos', 15, 15, 4, 'Easy', 'Vegan', 10),
('Chickpea Curry', 'Spiced chickpea curry in tomato sauce', 15, 25, 4, 'Medium', 'Vegan', 3),
('Vegan Pad Thai', 'Rice noodles with tofu and vegetables', 20, 15, 4, 'Medium', 'Vegan', 35),

-- Keto Recipes
('Keto Egg Muffins', 'Low-carb egg muffins with cheese and bacon', 10, 25, 6, 'Easy', 'Keto', 4),
('Cauliflower Pizza', 'Pizza with cauliflower crust', 20, 30, 4, 'Medium', 'Keto', 11),
('Keto Chicken Alfredo', 'Creamy chicken alfredo with zucchini noodles', 15, 20, 4, 'Easy', 'Keto', 20),
('Bacon-Wrapped Asparagus', 'Crispy bacon wrapped around asparagus spears', 10, 20, 4, 'Easy', 'Keto', 29),
('Keto Beef Tacos', 'Low-carb tacos with lettuce wraps', 15, 15, 4, 'Easy', 'Keto', 37);


--Recipe ingredients

-- Recipe 1: High-Protein Overnight Oats
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(1, 38, 50, 'gram'),
(1, 6, 100, 'gram'),
(1, 52, 120, 'cup'),
(1, 29, 50, 'gram'),
(1, 50, 10, 'gram');

-- Recipe 2: Veggie-Packed Omelet
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(2, 5, 3, 'piece'),
(2, 17, 30, 'gram'),
(2, 21, 50, 'gram'),
(2, 25, 30, 'gram'),
(2, 60, 5, 'gram');

-- Recipe 3: Quinoa Breakfast Bowl
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(3, 37, 100, 'gram'),
(3, 27, 1, 'piece'),
(3, 29, 50, 'gram'),
(3, 45, 10, 'gram'),
(3, 65, 5, 'tablespoon');

-- Recipe 4: Avocado Toast with Eggs
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(4, 39, 2, 'slice'),
(4, 31, 0.5, 'piece'),
(4, 5, 2, 'piece'),
(4, 73, 1, 'teaspoon');

-- Recipe 5: Berry Protein Smoothie
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(5, 6, 150, 'gram'),
(5, 29, 100, 'gram'),
(5, 30, 50, 'gram'),
(5, 52, 200, 'cup'),
(5, 27, 0.5, 'piece');

-- Recipe 6: Sweet Potato Hash
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(6, 24, 200, 'gram'),
(6, 20, 100, 'gram'),
(6, 64, 50, 'gram'),
(6, 5, 2, 'piece'),
(6, 60, 10, 'gram');

-- Recipe 7: Chia Seed Pudding
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(7, 48, 30, 'gram'),
(7, 53, 240, 'cup'),
(7, 29, 50, 'gram'),
(7, 65, 10, 'tablespoon');

-- Recipe 8: Greek Yogurt Parfait
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(8, 6, 200, 'gram'),
(8, 38, 30, 'gram'),
(8, 30, 50, 'gram'),
(8, 29, 30, 'gram'),
(8, 65, 10, 'tablespoon');

-- Recipe 9: Whole Wheat Pancakes
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(9, 39, 4, 'slice'),
(9, 5, 2, 'piece'),
(9, 52, 120, 'cup'),
(9, 65, 20, 'tablespoon'),
(9, 28, 1, 'piece');

-- Recipe 10: Spinach Feta Scramble
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(10, 5, 3, 'piece'),
(10, 17, 50, 'gram'),
(10, 56, 30, 'gram'),
(10, 21, 50, 'gram');

-- Recipe 11: Grilled Chicken Salad
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(11, 1, 150, 'gram'),
(11, 24, 50, 'gram'),
(11, 17, 40, 'gram'),
(11, 21, 50, 'gram'),
(11, 23, 50, 'gram'),
(11, 71, 20, 'tablespoon'),
(11, 60, 15, 'gram');

-- Recipe 12: Mediterranean Quinoa Bowl
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(12, 37, 100, 'gram'),
(12, 14, 100, 'gram'),
(12, 23, 50, 'gram'),
(12, 21, 50, 'gram'),
(12, 20, 50, 'gram'),
(12, 56, 30, 'gram');

-- Recipe 13: Turkey and Avocado Wrap
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(13, 8, 100, 'gram'),
(13, 31, 0.5, 'piece'),
(13, 24, 30, 'gram'),
(13, 21, 50, 'gram'),
(13, 39, 1, 'slice');

-- Recipe 14: Lentil Soup
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(14, 15, 200, 'gram'),
(14, 19, 100, 'gram'),
(14, 64, 50, 'gram'),
(14, 61, 3, 'clove'),
(14, 62, 50, 'gram');

-- Recipe 15: Tuna Poke Bowl
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(15, 9, 150, 'gram'),
(15, 36, 100, 'gram'),
(15, 31, 0.5, 'piece'),
(15, 23, 50, 'gram'),
(15, 63, 10, 'tablespoon');

-- Recipe 16: Chicken Caesar Salad
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(16, 1, 150, 'gram'),
(16, 24, 100, 'gram'),
(16, 54, 20, 'gram'),
(16, 60, 10, 'gram');

-- Recipe 17: Black Bean Burrito Bowl
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(17, 13, 150, 'gram'),
(17, 36, 100, 'gram'),
(17, 20, 50, 'gram'),
(17, 21, 50, 'gram'),
(17, 31, 0.5, 'piece');

-- Recipe 18: Caprese Sandwich
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(18, 55, 50, 'gram'),
(18, 21, 100, 'gram'),
(18, 39, 2, 'slice'),
(18, 60, 15, 'gram');

-- Recipe 19: Asian Chicken Lettuce Wraps
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(19, 1, 200, 'gram'),
(19, 24, 8, 'gram'),
(19, 19, 50, 'gram'),
(19, 62, 10, 'gram'),
(19, 63, 20, 'tablespoon');

-- Recipe 20: Greek Chicken Pita
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(20, 1, 150, 'gram'),
(20, 39, 1, 'slice'),
(20, 6, 50, 'gram'),
(20, 23, 50, 'gram'),
(20, 21, 50, 'gram');

-- Recipe 21: Baked Salmon with Asparagus
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(21, 3, 200, 'gram'),
(21, 26, 150, 'gram'),
(21, 60, 15, 'gram'),
(21, 70, 10, 'tablespoon');

-- Recipe 22: Grilled Chicken and Vegetables
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(22, 1, 400, 'gram'),
(22, 16, 200, 'gram'),
(22, 20, 150, 'gram'),
(22, 23, 100, 'gram'),
(22, 60, 20, 'gram');

-- Recipe 23: Beef Stir-Fry
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(23, 2, 300, 'gram'),
(23, 16, 200, 'gram'),
(23, 20, 100, 'gram'),
(23, 63, 30, 'tablespoon'),
(23, 62, 15, 'gram');

-- Recipe 24: Vegetarian Chili
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(24, 13, 200, 'gram'),
(24, 14, 100, 'gram'),
(24, 21, 200, 'gram'),
(24, 20, 100, 'gram'),
(24, 62, 50, 'gram');

-- Recipe 25: Lemon Herb Chicken
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(25, 1, 400, 'gram'),
(25, 70, 30, 'tablespoon'),
(25, 61, 4, 'clove'),
(25, 60, 15, 'gram');

-- Recipe 26: Shrimp Pasta Primavera
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(26, 7, 300, 'gram'),
(26, 40, 200, 'gram'),
(26, 16, 100, 'gram'),
(26, 21, 100, 'gram'),
(26, 60, 20, 'gram');

-- Recipe 27: Turkey Meatballs
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(27, 8, 500, 'gram'),
(27, 5, 1, 'piece'),
(27, 62, 50, 'gram'),
(27, 61, 3, 'clove');

-- Recipe 28: Tofu Curry
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(28, 4, 400, 'gram'),
(28, 16, 150, 'gram'),
(28, 20, 100, 'gram'),
(28, 62, 15, 'gram');

-- Recipe 29: Grilled Steak with Sweet Potato
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(29, 2, 200, 'gram'),
(29, 24, 200, 'gram'),
(29, 60, 10, 'gram');

-- Recipe 30: Baked Cod with Green Beans
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES
(30, 12, 200, 'gram'),
(30, 27, 150, 'gram'),
(30, 70, 15, 'tablespoon'),
(30, 61, 2, 'clove');

--Recipe tags

INSERT INTO recipe_tag (recipe_id, tag_name) VALUES
-- Breakfast tags
(1, 'high-protein'), (1, 'meal-prep'), (1, 'vegetarian'),
(2, 'high-protein'), (2, 'low-carb'), (2, 'vegetarian'),
(3, 'vegan'), (3, 'gluten-free'), (3, 'high-fiber'),
(4, 'vegetarian'), (4, 'healthy-fats'), (4, 'quick'),
(5, 'high-protein'), (5, 'post-workout'), (5, 'vegetarian'),
(6, 'vegetarian'), (6, 'paleo-friendly'), (6, 'whole30'),
(7, 'vegan'), (7, 'meal-prep'), (7, 'high-fiber'),
(8, 'high-protein'), (8, 'quick'), (8, 'vegetarian'),
(9, 'vegetarian'), (9, 'family-friendly'), (9, 'whole-grain'),
(10, 'high-protein'), (10, 'low-carb'), (10, 'vegetarian'),

-- Lunch tags
(11, 'high-protein'), (11, 'low-carb'), (11, 'gluten-free'),
(12, 'vegan'), (12, 'high-fiber'), (12, 'mediterranean'),
(13, 'high-protein'), (13, 'quick'), (13, 'healthy-fats'),
(14, 'vegan'), (14, 'high-fiber'), (14, 'budget-friendly'),
(15, 'high-protein'), (15, 'low-carb'), (15, 'omega-3'),
(16, 'high-protein'), (16, 'low-carb'),
(17, 'vegan'), (17, 'high-fiber'), (17, 'mexican'),
(18, 'vegetarian'), (18, 'quick'), (18, 'italian'),
(19, 'low-carb'), (19, 'high-protein'), (19, 'asian'),
(20, 'high-protein'), (20, 'mediterranean'), (20, 'greek'),

-- Dinner tags
(21, 'high-protein'), (21, 'omega-3'), (21, 'low-carb'),
(22, 'high-protein'), (22, 'paleo'), (22, 'whole30'),
(23, 'high-protein'), (23, 'low-carb'), (23, 'asian'),
(24, 'vegan'), (24, 'high-fiber'), (24, 'budget-friendly'),
(25, 'high-protein'), (25, 'low-carb'), (25, 'paleo'),
(26, 'high-protein'), (26, 'whole-grain'), (26, 'omega-3'),
(27, 'high-protein'), (27, 'low-carb'), (27, 'italian'),
(28, 'vegan'), (28, 'indian'), (28, 'spicy'),
(29, 'high-protein'), (29, 'low-carb'), (29, 'paleo'),
(30, 'high-protein'), (30, 'low-carb'), (30, 'omega-3');


--Rating system and comments

INSERT INTO rating (user_id, recipe_id, rating, comment) VALUES
(1, 21, 5, 'Perfect salmon recipe! Easy to make and delicious.'),
(2, 1, 5, 'Love these overnight oats. Great for meal prep!'),
(3, 24, 4, 'Hearty and filling. Added extra spices.'),
(4, 29, 5, 'Best steak recipe ever! Sweet potato was perfect.'),
(5, 5, 5, 'My go-to post-workout smoothie!'),
(6, 11, 4, 'Delicious salad. Very filling and nutritious.'),
(7, 12, 5, 'Mediterranean flavors are amazing!'),
(8, 22, 5, 'Family loved this! Will make again.'),
(9, 3, 4, 'Quinoa bowl is great but takes time to prep.'),
(10, 2, 5, 'Perfect omelet every time with this recipe.'),
(11, 26, 4, 'Shrimp pasta was good but needed more sauce.'),
(12, 30, 5, 'Simple and healthy. Love the lemon flavor.'),
(13, 4, 5, 'My favorite breakfast! Quick and nutritious.'),
(14, 14, 4, 'Excellent soup for meal prep. Very hearty.'),
(15, 23, 5, 'Beef stir-fry is now a weekly staple!'),
(16, 25, 5, 'Lemon herb chicken is so flavorful!'),
(17, 28, 4, 'Curry was good but a bit too spicy for me.'),
(18, 18, 5, 'Classic Caprese never disappoints!'),
(19, 19, 5, 'Lettuce wraps are perfect for low-carb diet.'),
(20, 27, 4, 'Turkey meatballs are healthy and tasty.');


--Meal Plans

INSERT INTO meal_plan (user_id, start_date, end_date, title) VALUES
(1, '2025-11-10', '2025-11-16', 'Week 1 - Balanced Diet'),
(1, '2025-11-17', '2025-11-23', 'Week 2 - Lean & Green'),
(4, '2025-11-10', '2025-11-16', 'Keto Week 1'),
(4, '2025-11-17', '2025-11-23', 'Keto Week 2'),
(11, '2025-11-10', '2025-11-16', 'Muscle Gain Plan'),
(15, '2025-11-10', '2025-11-16', 'Athletic Performance'),
(2, '2025-11-10', '2025-11-16', 'Vegetarian Week'),
(3, '2025-11-10', '2025-11-16', 'Vegan Meal Plan'),
(5, '2025-11-10', '2025-11-16', 'Paleo Week 1'),
(6, '2025-11-10', '2025-11-16', 'Gluten-Free Week');

--Meal Entries

-- User 1 - Week 1 Meal Plan
INSERT INTO meal_entry (mealplan_id, recipe_id, date, meal_type) VALUES
-- Monday
(1, 1, '2025-11-10', 'Breakfast'),
(1, 11, '2025-11-10', 'Lunch'),
(1, 21, '2025-11-10', 'Dinner'),
(1, 37, '2025-11-10', 'Snack'),
-- Tuesday
(1, 2, '2025-11-11', 'Breakfast'),
(1, 12, '2025-11-11', 'Lunch'),
(1, 22, '2025-11-11', 'Dinner'),
(1, 36, '2025-11-11', 'Snack'),
-- Wednesday
(1, 3, '2025-11-12', 'Breakfast'),
(1, 13, '2025-11-12', 'Lunch'),
(1, 23, '2025-11-12', 'Dinner'),
(1, 38, '2025-11-12', 'Snack'),
-- Thursday
(1, 4, '2025-11-13', 'Breakfast'),
(1, 14, '2025-11-13', 'Lunch'),
(1, 24, '2025-11-13', 'Dinner'),
(1, 39, '2025-11-13', 'Snack'),
-- Friday
(1, 5, '2025-11-14', 'Breakfast'),
(1, 15, '2025-11-14', 'Lunch'),
(1, 25, '2025-11-14', 'Dinner'),
(1, 40, '2025-11-14', 'Snack'),
-- Saturday
(1, 8, '2025-11-15', 'Breakfast'),
(1, 16, '2025-11-15', 'Lunch'),
(1, 26, '2025-11-15', 'Dinner'),
-- Sunday
(1, 9, '2025-11-16', 'Breakfast'),
(1, 17, '2025-11-16', 'Lunch'),
(1, 27, '2025-11-16', 'Dinner');

-- User 4 - Keto Meal Plan (Week 1)
INSERT INTO meal_entry (mealplan_id, recipe_id, date, meal_type) VALUES
-- Monday
(3, 2, '2025-11-10', 'Breakfast'),
(3, 11, '2025-11-10', 'Lunch'),
(3, 29, '2025-11-10', 'Dinner'),
-- Tuesday
(3, 10, '2025-11-11', 'Breakfast'),
(3, 16, '2025-11-11', 'Lunch'),
(3, 21, '2025-11-11', 'Dinner'),
-- Wednesday
(3, 2, '2025-11-12', 'Breakfast'),
(3, 19, '2025-11-12', 'Lunch'),
(3, 30, '2025-11-12', 'Dinner'),
-- Thursday
(3, 10, '2025-11-13', 'Breakfast'),
(3, 11, '2025-11-13', 'Lunch'),
(3, 23, '2025-11-13', 'Dinner'),
-- Friday
(3, 2, '2025-11-14', 'Breakfast'),
(3, 16, '2025-11-14', 'Lunch'),
(3, 29, '2025-11-14', 'Dinner'),
-- Saturday
(3, 10, '2025-11-15', 'Breakfast'),
(3, 19, '2025-11-15', 'Lunch'),
(3, 21, '2025-11-15', 'Dinner'),
-- Sunday
(3, 2, '2025-11-16', 'Breakfast'),
(3, 11, '2025-11-16', 'Lunch'),
(3, 30, '2025-11-16', 'Dinner');

--Shopping list
INSERT INTO shopping_list (mealplan_id) VALUES
(1), (2), (3), (4), (5);

--Shopping list items

INSERT INTO shoppinglist_item (list_id, ingredient_id, total_quantity, unit, is_purchased) VALUES
(1, 1, 600, 'gram', FALSE),
(1, 3, 200, 'gram', FALSE),
(1, 5, 10, 'piece', FALSE),
(1, 6, 400, 'gram', FALSE),
(1, 16, 300, 'gram', FALSE),
(1, 17, 150, 'gram', FALSE),
(1, 21, 400, 'gram', FALSE),
(1, 24, 200, 'gram', FALSE),
(1, 38, 150, 'gram', FALSE),
(1, 60, 50, 'gram', FALSE);

