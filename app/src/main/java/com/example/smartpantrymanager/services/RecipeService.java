package com.example.smartpantrymanager.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.Recipe;
import com.example.smartpantrymanager.models.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeService {

    private DatabaseHelper dbHelper;

    public RecipeService(DatabaseHelper databaseHelper) {
        this.dbHelper = databaseHelper;
    }

    public Recipe getRecipe(int primaryKey) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tRecipes WHERE pkRecipes = ?",
                new String[]{String.valueOf(primaryKey)}
        );

        Recipe recipe = null;

        if (cursor.moveToFirst()) {

            recipe = new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow("pkRecipes")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sDescription")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sInstructions")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("bArchived")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sImageName"))
            );

            recipe.setIngredients(getRecipeIngredients(recipe.getPrimaryKey()));
        }

        cursor.close();

        return recipe;
    }

    public List<Recipe> getAllRecipes() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tRecipes",
                null
        );

        List<Recipe> allRecipes = new ArrayList<>();

        while (cursor.moveToNext()) {

            Recipe recipe = new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow("pkRecipes")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sDescription")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sInstructions")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("bArchived")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sImageName"))
            );

            recipe.setIngredients(getRecipeIngredients(recipe.getPrimaryKey()));

            allRecipes.add(recipe);
        }

        cursor.close();

        return allRecipes;
    }

    public List<Recipe> getActiveRecipes() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tRecipes WHERE bArchived = 0 ORDER BY sName ASC",
                null
        );

        List<Recipe> activeRecipes = new ArrayList<>();

        while (cursor.moveToNext()) {

            Recipe recipe = new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow("pkRecipes")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sDescription")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sInstructions")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("bArchived")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sImageName"))
            );

            recipe.setIngredients(getRecipeIngredients(recipe.getPrimaryKey()));

            activeRecipes.add(recipe);
        }

        cursor.close();

        return activeRecipes;
    }

    public boolean setArchived(int primaryKey, int archived) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("bArchived", archived);

        int rowsUpdated = db.update(
                "tRecipes",
                values,
                "pkRecipes = ?",
                new String[]{String.valueOf(primaryKey)}
        );

        return rowsUpdated > 0;
    }

    // Helper, it gets all the ingredients linked to a recipe
    private List<RecipeIngredient> getRecipeIngredients(int recipePrimaryKey) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tRecipeIngredients WHERE fkRecipes = ?",
                new String[]{String.valueOf(recipePrimaryKey)}
        );

        List<RecipeIngredient> ingredientList = new ArrayList<>();

        while (cursor.moveToNext()) {

            RecipeIngredient recipeIngredient = new RecipeIngredient(
                    cursor.getInt(cursor.getColumnIndexOrThrow("fkRecipes")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("fkIngredients")),
                    cursor.getFloat(cursor.getColumnIndexOrThrow("fQuantity")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("fkMeasurements"))
            );

            ingredientList.add(recipeIngredient);
        }

        cursor.close();

        return ingredientList;
    }
}