package com.example.smartpantrymanager.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

// Service is responsible for the CRUD operations involving ingredients
public class IngredientService {

    private DatabaseHelper dbHelper;

    public IngredientService(DatabaseHelper databaseHelper) {
        this.dbHelper = databaseHelper;
    }

    public Ingredient getIngredient(int primaryKey) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tIngredients WHERE pkIngredients = ?",
                new String[]{String.valueOf(primaryKey)}
        );

        Ingredient ingredient = null;

        // if the ingredient exists
        if (cursor.moveToFirst()) {

            ingredient = new Ingredient(
                    cursor.getInt(cursor.getColumnIndexOrThrow("pkIngredients")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sName"))
            );
        }

        cursor.close();

        return ingredient;
    }

    public Ingredient getIngredient(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tIngredients WHERE sName = ?",
                new String[]{name}
        );

        Ingredient ingredient = null;

        // if the ingredient exists
        if (cursor.moveToFirst()) {

            ingredient = new Ingredient(
                    cursor.getInt(cursor.getColumnIndexOrThrow("pkIngredients")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sName"))
            );
        }

        cursor.close();

        return ingredient;
    }

    public List<Ingredient> getAllIngredients() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tIngredients", null);

        List<Ingredient> allIngredients = new ArrayList<>();

        while(cursor.moveToNext()) {

            Ingredient ingredient  = new Ingredient(
                    cursor.getInt(cursor.getColumnIndexOrThrow("pkIngredients")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sName"))
            );

            allIngredients.add(ingredient);
        }

        cursor.close();
        return allIngredients;
    }

    public int addIngredient(Ingredient ingredient) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("sName", ingredient.getName());

        long primaryKey = db.insert("tIngredients", null, values);

        // casting as an int
        return (int) primaryKey;
    }

    public boolean updateIngredient(Ingredient ingredient) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("sName", ingredient.getName());

        int rowsUpdated = db.update("tIngredients", values, "pkIngredients = ?",
                new String[]{String.valueOf(ingredient.getPrimaryKey())}
        );

        return rowsUpdated > 0;
    }

    public boolean deleteIngredient(int primaryKey) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsDeleted = db.delete("tIngredients", "pkIngredients = ?",
                new String[]{String.valueOf(primaryKey)}
        );

        return rowsDeleted > 0;
    }


}