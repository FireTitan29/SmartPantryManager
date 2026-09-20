package com.example.smartpantrymanager.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Decided to use SQL Lite so that I don't need to worry about connection string etc, it's
// easier for the marker to just run the app and I can include the db file in the submission

// Naming Convention explained:
// t = Table - example: tAppSettings
// pk = Primary Key
// fk = Foreign Key
// s = String / Text - example: sName
// i = Int / Number - example: iAge
// f = Float - example: fQuantity
// dt = Date (but due to SQL lite limitations, it will be a text value) - Example dtExpiryDate
// b = Boolean / Bit (but due to SQL lite limitations, it will be an Integer value) -  Example: bArchived

public class DatabaseHelper  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;


    // Tables we need for the app to run
    private static final String TABLE_PANTRY_ITEMS = "tPantryItems";
    private static final String TABLE_RECIPES = "tRecipes";
    private static final String TABLE_INGREDIENTS = "tIngredients";

    // Linker table for linking ingredients to recipes
    private static final String TABLE_RECIPE_INGREDIENTS = "tRecipeIngredients";
    private static final String TABLE_MEASUREMENTS = "tMeasurements";
    private static final String TABLE_APP_SETTINGS = "tAppSettings";

    // Table Creation Scripts
    private static final String CREATE_TABLE_INGREDIENTS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_INGREDIENTS +
                    "(pkIngredients INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "sName TEXT NOT NULL )";

    private static final String CREATE_TABLE_MEASUREMENTS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_MEASUREMENTS +
                    "(pkMeasurements INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "sType TEXT NOT NULL," +
                    "sAbbreviation TEXT NOT NULL)";

    private static final String CREATE_TABLE_RECIPES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPES +
                    "(pkRecipes INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sName TEXT NOT NULL," +
                    "sDescription TEXT," +
                    "sInstructions TEXT NOT NULL," +
                    "bArchived INTEGER NOT NULL DEFAULT 0)";

    private static final String CREATE_TABLE_RECIPE_INGREDIENTS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPE_INGREDIENTS +
                    "(fkRecipes INTEGER NOT NULL, " +
                    "fkIngredients INTEGER NOT NULL, " +
                    "fQuantity REAL NOT NULL, " +
                    "fkMeasurements INTEGER NOT NULL, " +

                    "PRIMARY KEY(fkRecipes, fkIngredients), " +

                    "FOREIGN KEY(fkRecipes) REFERENCES " +
                    TABLE_RECIPES + "(pkRecipes), " +

                    "FOREIGN KEY(fkIngredients) REFERENCES " +
                    TABLE_INGREDIENTS + "(pkIngredients), " +

                    "FOREIGN KEY(fkMeasurements) REFERENCES " +
                    TABLE_MEASUREMENTS + "(pkMeasurements))";

    private static final String CREATE_TABLE_PANTRY_ITEMS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_PANTRY_ITEMS +
                    "(pkPantryItems INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "sName TEXT NOT NULL, " +
                    "fQuantity REAL NOT NULL, " +
                    "fkMeasurements INTEGER NOT NULL, " +
                    "dtExpiryDate TEXT, " +
                    "FOREIGN KEY(fkMeasurements) REFERENCES " +
                    TABLE_MEASUREMENTS + "(pkMeasurements))";

    private static final String CREATE_TABLE_APP_SETTINGS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_APP_SETTINGS +
                    "(pkAppSettings INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "sWeightUnit TEXT NOT NULL, " +
                    "sVolumeUnit TEXT NOT NULL)";

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String[] create_table_scripts = {
                                    CREATE_TABLE_INGREDIENTS,
                                    CREATE_TABLE_MEASUREMENTS,
                                    CREATE_TABLE_RECIPES,
                                    CREATE_TABLE_RECIPE_INGREDIENTS,
                                    CREATE_TABLE_PANTRY_ITEMS,
                                    CREATE_TABLE_APP_SETTINGS
                                };

        for (String script : create_table_scripts) {

            db.execSQL(script);
        }

        insertTestMeasurements(db);
        insertTestPantryItems(db);
        insertIngredients(db);
        insertRecipes(db);
        printDbTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void printDbTables(SQLiteDatabase db) {

        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table'", null);

        while (cursor.moveToNext()) {

            String sTableName = cursor.getString(cursor.getColumnIndexOrThrow("name"));

            Log.d("DATABASE", sTableName);
        }

        cursor.close();
    }


    // Seed Test data
    private void insertTestMeasurements(SQLiteDatabase db) {

        db.execSQL("INSERT INTO " + TABLE_MEASUREMENTS +  " (pkMeasurements, sType, sAbbreviation) VALUES (1, 'Weight', 'kg')");

        db.execSQL("INSERT INTO " + TABLE_MEASUREMENTS + " (pkMeasurements, sType, sAbbreviation) VALUES (2, 'Weight', 'g')");

        db.execSQL("INSERT INTO " + TABLE_MEASUREMENTS + " (pkMeasurements, sType, sAbbreviation) VALUES (3, 'Weight', 'mg')");

        db.execSQL("INSERT INTO " + TABLE_MEASUREMENTS + " (pkMeasurements, sType, sAbbreviation) VALUES (4, 'Volume', 'l')");

        db.execSQL("INSERT INTO " + TABLE_MEASUREMENTS + " (pkMeasurements, sType, sAbbreviation) VALUES (5, 'Volume', 'ml')");

        db.execSQL("INSERT INTO " + TABLE_MEASUREMENTS + " (pkMeasurements, sType, sAbbreviation) VALUES (6, 'Count', 'Units')");

        db.execSQL("INSERT INTO " + TABLE_APP_SETTINGS + " (pkAppSettings, sWeightUnit, sVolumeUnit) VALUES (1, 'g', 'l')");
    }

    // Seed Test Data
    private void insertTestPantryItems(SQLiteDatabase db) {

        db.execSQL("INSERT INTO " + TABLE_PANTRY_ITEMS +
                " (sName, fQuantity, fkMeasurements, dtExpiryDate) " +
                "VALUES ('Milk', 2000, 5, '2026-09-25')");

        db.execSQL("INSERT INTO " + TABLE_PANTRY_ITEMS +
                " (sName, fQuantity, fkMeasurements, dtExpiryDate) " +
                "VALUES ('Eggs', 12, 6, '2026-09-30')");

        db.execSQL("INSERT INTO " + TABLE_PANTRY_ITEMS +
                " (sName, fQuantity, fkMeasurements, dtExpiryDate) " +
                "VALUES ('Flour', 500, 2, '2027-01-15')");

        db.execSQL("INSERT INTO " + TABLE_PANTRY_ITEMS +
                " (sName, fQuantity, fkMeasurements, dtExpiryDate) " +
                "VALUES ('Sugar', 250, 2, '2027-02-01')");

        db.execSQL("INSERT INTO " + TABLE_PANTRY_ITEMS +
                " (sName, fQuantity, fkMeasurements, dtExpiryDate) " +
                "VALUES ('Bread', 1, 6, '2026-09-22')");
    }

    // Seed Recipe Ingredients
    private void insertIngredients(SQLiteDatabase db) {

        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Pasta')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Mince')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Tomato')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Onion')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Garlic')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Oil')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Chicken')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Milk')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Cheese')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Bacon')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Burger Bun')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Lettuce')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Potato')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Flour')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Eggs')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Sugar')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Butter')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Cocoa')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Banana')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Apple')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Bread')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Oats')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Rice')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Ham')");
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " (sName) VALUES ('Sausage')");
    }

    // Helper function will make it easier to insert recipes, we return an int because we need
    // to know what pk the recipe has to insert the ingredients
    private int insertRecipe(SQLiteDatabase db, String name, String description, String instructions) {

        android.content.ContentValues values = new android.content.ContentValues();

        values.put("sName", name);
        values.put("sDescription", description);
        values.put("sInstructions", instructions);
        values.put("bArchived", 0);

        return (int) db.insert(TABLE_RECIPES, null, values);
    }

    // Helper function will make it easier to insert recipes ingredients because there are a lot
    private void insertRecipeIngredient(SQLiteDatabase db, int recipeId, int ingredientId, float quantity, int measurementId) {

        android.content.ContentValues values = new android.content.ContentValues();

        values.put("fkRecipes", recipeId);
        values.put("fkIngredients", ingredientId);
        values.put("fQuantity", quantity);
        values.put("fkMeasurements", measurementId);

        db.insert(TABLE_RECIPE_INGREDIENTS, null, values);
    }

    // Inserting many different styles of recipes, some of the choices I've made is simplifying them
    // because I needed some overlap for testing purposes, and because this is a project and I wanted
    // to make sure I spend my time wisely. Other than that, the reciepes are split into Pastas, American Style,
    // Desserts, Breakfasts, Lunches, and Home style meals
    private void insertRecipes(SQLiteDatabase db) {

        // Pastas
        int spaghettiBolognese = insertRecipe(db, "Spaghetti Bolognese",
                "A simple spaghetti dish with mince and tomato sauce.",
                "Cook pasta. Fry onion, garlic and mince. Add tomatoes and simmer. Serve with pasta.");
        insertRecipeIngredient(db, spaghettiBolognese, 1, 200, 2); // Pasta
        insertRecipeIngredient(db, spaghettiBolognese, 2, 250, 2); // Mince
        insertRecipeIngredient(db, spaghettiBolognese, 3, 200, 2); // Tomato
        insertRecipeIngredient(db, spaghettiBolognese, 4, 100, 2); // Onion
        insertRecipeIngredient(db, spaghettiBolognese, 5, 10, 2);  // Garlic
        insertRecipeIngredient(db, spaghettiBolognese, 6, 20, 5);  // Oil

        int creamyChickenPasta = insertRecipe(db, "Creamy Chicken Pasta",
                "Pasta with chicken in a simple creamy cheese sauce.",
                "Cook pasta. Cook chicken with onion and garlic. Add milk and cheese and stir until creamy. Combine with pasta.");
        insertRecipeIngredient(db, creamyChickenPasta, 1, 200, 2);
        insertRecipeIngredient(db, creamyChickenPasta, 7, 250, 2);
        insertRecipeIngredient(db, creamyChickenPasta, 8, 150, 5);
        insertRecipeIngredient(db, creamyChickenPasta, 9, 100, 2);
        insertRecipeIngredient(db, creamyChickenPasta, 4, 100, 2);
        insertRecipeIngredient(db, creamyChickenPasta, 5, 10, 2);

        int tomatoCheesePasta = insertRecipe(db, "Tomato Cheese Pasta",
                "A simple pasta dish with tomato and cheese.",
                "Cook pasta. Fry onion and garlic, add tomatoes and simmer. Stir through pasta and top with cheese.");
        insertRecipeIngredient(db, tomatoCheesePasta, 1, 200, 2);
        insertRecipeIngredient(db, tomatoCheesePasta, 3, 250, 2);
        insertRecipeIngredient(db, tomatoCheesePasta, 9, 100, 2);
        insertRecipeIngredient(db, tomatoCheesePasta, 4, 100, 2);
        insertRecipeIngredient(db, tomatoCheesePasta, 5, 10, 2);
        insertRecipeIngredient(db, tomatoCheesePasta, 6, 20, 5);

        int baconPasta = insertRecipe(db, "Bacon Pasta",
                "Pasta with bacon, cheese and a creamy sauce.",
                "Cook pasta. Fry bacon, onion and garlic. Add milk and cheese, then mix with pasta.");
        insertRecipeIngredient(db, baconPasta, 1, 200, 2);
        insertRecipeIngredient(db, baconPasta, 10, 150, 2);
        insertRecipeIngredient(db, baconPasta, 9, 100, 2);
        insertRecipeIngredient(db, baconPasta, 8, 100, 5);
        insertRecipeIngredient(db, baconPasta, 4, 100, 2);
        insertRecipeIngredient(db, baconPasta, 5, 10, 2);

        int chickenTomatoPasta = insertRecipe(db, "Chicken Tomato Pasta",
                "Pasta with chicken and a simple tomato sauce.",
                "Cook pasta. Cook chicken with onion and garlic. Add tomatoes and simmer before combining with pasta.");
        insertRecipeIngredient(db, chickenTomatoPasta, 1, 200, 2);
        insertRecipeIngredient(db, chickenTomatoPasta, 7, 200, 2);
        insertRecipeIngredient(db, chickenTomatoPasta, 3, 200, 2);
        insertRecipeIngredient(db, chickenTomatoPasta, 4, 100, 2);
        insertRecipeIngredient(db, chickenTomatoPasta, 5, 10, 2);
        insertRecipeIngredient(db, chickenTomatoPasta, 6, 20, 5);


        // American
        int cheeseburger = insertRecipe(db, "Cheeseburger",
                "A simple homemade cheeseburger.",
                "Shape and cook mince into a burger patty. Place in bun with cheese, tomato, lettuce and onion.");
        insertRecipeIngredient(db, cheeseburger, 11, 1, 6);
        insertRecipeIngredient(db, cheeseburger, 2, 150, 2);
        insertRecipeIngredient(db, cheeseburger, 9, 50, 2);
        insertRecipeIngredient(db, cheeseburger, 3, 50, 2);
        insertRecipeIngredient(db, cheeseburger, 12, 30, 2);
        insertRecipeIngredient(db, cheeseburger, 4, 30, 2);

        int chickenBurger = insertRecipe(db, "Chicken Burger",
                "A simple chicken burger with cheese and salad.",
                "Cook chicken in oil. Place chicken in bun with cheese, tomato and lettuce.");
        insertRecipeIngredient(db, chickenBurger, 11, 1, 6);
        insertRecipeIngredient(db, chickenBurger, 7, 150, 2);
        insertRecipeIngredient(db, chickenBurger, 9, 50, 2);
        insertRecipeIngredient(db, chickenBurger, 3, 50, 2);
        insertRecipeIngredient(db, chickenBurger, 12, 30, 2);
        insertRecipeIngredient(db, chickenBurger, 6, 10, 5);

        int americanPancakes = insertRecipe(db, "American Pancakes",
                "Fluffy homemade pancakes.",
                "Mix flour, milk, eggs and sugar. Fry portions in butter until golden.");
        insertRecipeIngredient(db, americanPancakes, 14, 150, 2);
        insertRecipeIngredient(db, americanPancakes, 8, 200, 5);
        insertRecipeIngredient(db, americanPancakes, 15, 2, 6);
        insertRecipeIngredient(db, americanPancakes, 16, 30, 2);
        insertRecipeIngredient(db, americanPancakes, 17, 30, 2);

        int macAndCheese = insertRecipe(db, "Mac and Cheese",
                "Simple pasta with a creamy cheese sauce.",
                "Cook pasta. Melt butter, add flour and milk to make a sauce. Stir in cheese and pasta.");
        insertRecipeIngredient(db, macAndCheese, 1, 200, 2);
        insertRecipeIngredient(db, macAndCheese, 9, 150, 2);
        insertRecipeIngredient(db, macAndCheese, 8, 200, 5);
        insertRecipeIngredient(db, macAndCheese, 17, 30, 2);
        insertRecipeIngredient(db, macAndCheese, 14, 30, 2);

        int loadedFries = insertRecipe(db, "Loaded Fries",
                "Crispy potatoes topped with mince, tomato and cheese.",
                "Cook potato until crispy. Cook mince with onion. Add mince, tomato and cheese over the potatoes.");
        insertRecipeIngredient(db, loadedFries, 13, 300, 2);
        insertRecipeIngredient(db, loadedFries, 9, 100, 2);
        insertRecipeIngredient(db, loadedFries, 2, 150, 2);
        insertRecipeIngredient(db, loadedFries, 3, 50, 2);
        insertRecipeIngredient(db, loadedFries, 4, 50, 2);
        insertRecipeIngredient(db, loadedFries, 6, 20, 5);


        // Desserts
        int chocolateCake = insertRecipe(db, "Chocolate Cake",
                "A simple homemade chocolate cake.",
                "Mix ingredients together. Pour into a cake tin and bake until cooked.");
        insertRecipeIngredient(db, chocolateCake, 14, 150, 2);
        insertRecipeIngredient(db, chocolateCake, 16, 100, 2);
        insertRecipeIngredient(db, chocolateCake, 18, 30, 2);
        insertRecipeIngredient(db, chocolateCake, 15, 2, 6);
        insertRecipeIngredient(db, chocolateCake, 8, 150, 5);
        insertRecipeIngredient(db, chocolateCake, 17, 50, 2);

        int bananaPancakes = insertRecipe(db, "Banana Pancakes",
                "Simple pancakes made with banana.",
                "Mash bananas and mix with remaining ingredients. Fry small pancakes in butter.");
        insertRecipeIngredient(db, bananaPancakes, 19, 2, 6);
        insertRecipeIngredient(db, bananaPancakes, 15, 2, 6);
        insertRecipeIngredient(db, bananaPancakes, 14, 100, 2);
        insertRecipeIngredient(db, bananaPancakes, 8, 100, 5);
        insertRecipeIngredient(db, bananaPancakes, 16, 20, 2);
        insertRecipeIngredient(db, bananaPancakes, 17, 20, 2);

        int chocolateBrownies = insertRecipe(db, "Chocolate Brownies",
                "Simple homemade chocolate brownies.",
                "Mix ingredients together. Pour into a baking tray and bake until set.");
        insertRecipeIngredient(db, chocolateBrownies, 14, 120, 2);
        insertRecipeIngredient(db, chocolateBrownies, 16, 100, 2);
        insertRecipeIngredient(db, chocolateBrownies, 18, 40, 2);
        insertRecipeIngredient(db, chocolateBrownies, 15, 2, 6);
        insertRecipeIngredient(db, chocolateBrownies, 17, 80, 2);

        int appleCrumble = insertRecipe(db, "Apple Crumble",
                "Baked apples with a simple crumble topping.",
                "Slice apples and place in a baking dish. Mix flour, sugar and butter into crumbs. Place over apples and bake.");
        insertRecipeIngredient(db, appleCrumble, 20, 3, 6);
        insertRecipeIngredient(db, appleCrumble, 14, 100, 2);
        insertRecipeIngredient(db, appleCrumble, 16, 80, 2);
        insertRecipeIngredient(db, appleCrumble, 17, 60, 2);

        int chocolateMilkshake = insertRecipe(db, "Chocolate Milkshake",
                "A simple chocolate and banana milkshake.",
                "Blend all ingredients until smooth.");
        insertRecipeIngredient(db, chocolateMilkshake, 8, 300, 5);
        insertRecipeIngredient(db, chocolateMilkshake, 19, 1, 6);
        insertRecipeIngredient(db, chocolateMilkshake, 18, 20, 2);
        insertRecipeIngredient(db, chocolateMilkshake, 16, 20, 2);


        // Breakfasts
        int baconAndEggs = insertRecipe(db, "Bacon and Eggs",
                "A simple breakfast of bacon, eggs and toast.",
                "Fry bacon and eggs. Toast bread and serve together.");
        insertRecipeIngredient(db, baconAndEggs, 10, 100, 2);
        insertRecipeIngredient(db, baconAndEggs, 15, 2, 6);
        insertRecipeIngredient(db, baconAndEggs, 21, 2, 6);
        insertRecipeIngredient(db, baconAndEggs, 17, 10, 2);

        int breakfastSandwich = insertRecipe(db, "Breakfast Sandwich",
                "A simple breakfast sandwich with eggs, bacon and cheese.",
                "Cook eggs and bacon. Place in bread with cheese and tomato.");
        insertRecipeIngredient(db, breakfastSandwich, 21, 2, 6);
        insertRecipeIngredient(db, breakfastSandwich, 15, 2, 6);
        insertRecipeIngredient(db, breakfastSandwich, 10, 80, 2);
        insertRecipeIngredient(db, breakfastSandwich, 9, 50, 2);
        insertRecipeIngredient(db, breakfastSandwich, 3, 50, 2);

        int scrambledEggs = insertRecipe(db, "Scrambled Eggs on Toast",
                "Scrambled eggs served on toast.",
                "Beat eggs with milk. Cook slowly in butter and serve on toast.");
        insertRecipeIngredient(db, scrambledEggs, 15, 2, 6);
        insertRecipeIngredient(db, scrambledEggs, 8, 50, 5);
        insertRecipeIngredient(db, scrambledEggs, 21, 2, 6);
        insertRecipeIngredient(db, scrambledEggs, 17, 20, 2);

        int bananaOats = insertRecipe(db, "Banana Oats",
                "Warm oats with banana and milk.",
                "Cook oats with milk. Add sliced banana and sugar.");
        insertRecipeIngredient(db, bananaOats, 22, 100, 2);
        insertRecipeIngredient(db, bananaOats, 8, 250, 5);
        insertRecipeIngredient(db, bananaOats, 19, 1, 6);
        insertRecipeIngredient(db, bananaOats, 16, 20, 2);

        int breakfastPancakes = insertRecipe(db, "Breakfast Pancakes",
                "Simple homemade breakfast pancakes.",
                "Mix ingredients and fry pancakes in butter.");
        insertRecipeIngredient(db, breakfastPancakes, 14, 150, 2);
        insertRecipeIngredient(db, breakfastPancakes, 8, 200, 5);
        insertRecipeIngredient(db, breakfastPancakes, 15, 2, 6);
        insertRecipeIngredient(db, breakfastPancakes, 16, 30, 2);
        insertRecipeIngredient(db, breakfastPancakes, 17, 30, 2);


        // Lunches
        int chickenSandwich = insertRecipe(db, "Chicken Sandwich",
                "A simple chicken sandwich with salad.",
                "Cook chicken. Place chicken, lettuce, tomato and cheese between bread.");
        insertRecipeIngredient(db, chickenSandwich, 21, 2, 6);
        insertRecipeIngredient(db, chickenSandwich, 7, 150, 2);
        insertRecipeIngredient(db, chickenSandwich, 12, 30, 2);
        insertRecipeIngredient(db, chickenSandwich, 3, 50, 2);
        insertRecipeIngredient(db, chickenSandwich, 9, 50, 2);

        int hamCheeseSandwich = insertRecipe(db, "Ham and Cheese Sandwich",
                "A simple ham and cheese sandwich.",
                "Add ham, cheese, tomato and lettuce between bread.");
        insertRecipeIngredient(db, hamCheeseSandwich, 21, 2, 6);
        insertRecipeIngredient(db, hamCheeseSandwich, 24, 100, 2);
        insertRecipeIngredient(db, hamCheeseSandwich, 9, 50, 2);
        insertRecipeIngredient(db, hamCheeseSandwich, 3, 50, 2);
        insertRecipeIngredient(db, hamCheeseSandwich, 12, 30, 2);

        int chickenRiceBowl = insertRecipe(db, "Chicken Rice Bowl",
                "Chicken served with rice and vegetables.",
                "Cook rice. Cook chicken with onion and tomato. Serve over rice.");
        insertRecipeIngredient(db, chickenRiceBowl, 23, 200, 2);
        insertRecipeIngredient(db, chickenRiceBowl, 7, 200, 2);
        insertRecipeIngredient(db, chickenRiceBowl, 3, 100, 2);
        insertRecipeIngredient(db, chickenRiceBowl, 4, 50, 2);
        insertRecipeIngredient(db, chickenRiceBowl, 6, 20, 5);

        int minceSandwich = insertRecipe(db, "Mince Sandwich",
                "Cooked mince served in bread with cheese and tomato.",
                "Cook mince with onion. Place in bread with tomato and cheese.");
        insertRecipeIngredient(db, minceSandwich, 21, 2, 6);
        insertRecipeIngredient(db, minceSandwich, 2, 150, 2);
        insertRecipeIngredient(db, minceSandwich, 3, 50, 2);
        insertRecipeIngredient(db, minceSandwich, 4, 50, 2);
        insertRecipeIngredient(db, minceSandwich, 9, 50, 2);

        int cheeseToastie = insertRecipe(db, "Cheese Toastie",
                "A simple toasted cheese sandwich.",
                "Butter bread. Add cheese and tomato. Toast until golden and cheese has melted.");
        insertRecipeIngredient(db, cheeseToastie, 21, 2, 6);
        insertRecipeIngredient(db, cheeseToastie, 9, 100, 2);
        insertRecipeIngredient(db, cheeseToastie, 17, 20, 2);
        insertRecipeIngredient(db, cheeseToastie, 3, 50, 2);


        // Home-Style Meals
        int cottagePie = insertRecipe(db, "Cottage Pie",
                "Mince and vegetables topped with mashed potato.",
                "Cook mince with onion and tomato. Mash potatoes. Place potatoes over mince, top with cheese and bake.");
        insertRecipeIngredient(db, cottagePie, 2, 250, 2);
        insertRecipeIngredient(db, cottagePie, 13, 300, 2);
        insertRecipeIngredient(db, cottagePie, 4, 100, 2);
        insertRecipeIngredient(db, cottagePie, 3, 100, 2);
        insertRecipeIngredient(db, cottagePie, 9, 50, 2);

        int chickenAndRice = insertRecipe(db, "Chicken and Rice",
                "Simple chicken cooked with onion and tomato served with rice.",
                "Cook rice. Fry chicken and onion, add tomato and cook through. Serve with rice.");
        insertRecipeIngredient(db, chickenAndRice, 7, 250, 2);
        insertRecipeIngredient(db, chickenAndRice, 23, 200, 2);
        insertRecipeIngredient(db, chickenAndRice, 4, 100, 2);
        insertRecipeIngredient(db, chickenAndRice, 3, 100, 2);
        insertRecipeIngredient(db, chickenAndRice, 6, 20, 5);

        int sausageMash = insertRecipe(db, "Sausage and Mash",
                "Sausages served with mashed potatoes and onion.",
                "Cook sausages. Boil and mash potatoes with milk and butter. Fry onion and serve together.");
        insertRecipeIngredient(db, sausageMash, 25, 3, 6);
        insertRecipeIngredient(db, sausageMash, 13, 300, 2);
        insertRecipeIngredient(db, sausageMash, 8, 100, 5);
        insertRecipeIngredient(db, sausageMash, 17, 30, 2);
        insertRecipeIngredient(db, sausageMash, 4, 100, 2);

        int spaghettiMeatballs = insertRecipe(db, "Spaghetti and Meatballs",
                "Spaghetti served with simple homemade meatballs and tomato sauce.",
                "Form mince into meatballs and cook. Fry onion and garlic, add tomatoes and simmer. Serve with pasta and meatballs.");
        insertRecipeIngredient(db, spaghettiMeatballs, 1, 200, 2);
        insertRecipeIngredient(db, spaghettiMeatballs, 2, 250, 2);
        insertRecipeIngredient(db, spaghettiMeatballs, 3, 200, 2);
        insertRecipeIngredient(db, spaghettiMeatballs, 4, 100, 2);
        insertRecipeIngredient(db, spaghettiMeatballs, 5, 10, 2);
        insertRecipeIngredient(db, spaghettiMeatballs, 6, 20, 5);

        int chickenPotato = insertRecipe(db, "Chicken and Potato",
                "Chicken and potatoes cooked with onion and tomato.",
                "Cook potatoes until soft. Cook chicken with onion and tomato. Combine and serve.");
        insertRecipeIngredient(db, chickenPotato, 7, 250, 2);
        insertRecipeIngredient(db, chickenPotato, 13, 300, 2);
        insertRecipeIngredient(db, chickenPotato, 4, 100, 2);
        insertRecipeIngredient(db, chickenPotato, 3, 100, 2);
        insertRecipeIngredient(db, chickenPotato, 6, 20, 5);
    }

}

