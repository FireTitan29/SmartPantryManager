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
}
