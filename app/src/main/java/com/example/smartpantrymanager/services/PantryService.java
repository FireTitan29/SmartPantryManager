package com.example.smartpantrymanager.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.PantryItem;

import java.util.ArrayList;
import java.util.List;

public class PantryService {

    private DatabaseHelper dbHelper;

    public PantryService(DatabaseHelper databaseHelper) {
        this.dbHelper = databaseHelper;
    }

    public int addPantryItem(PantryItem pantryItem) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("sName", pantryItem.getName());
        values.put("fQuantity", pantryItem.getQuantity());
        values.put("fkMeasurements", pantryItem.getMeasurementId());
        values.put("dtExpiryDate", pantryItem.getExpiryDate());

        long primaryKey = db.insert(
                "tPantryItems",
                null,
                values
        );

        return (int) primaryKey;
    }

    public PantryItem getPantryItem(int primaryKey) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tPantryItems WHERE pkPantryItems = ?",
                new String[]{String.valueOf(primaryKey)}
        );

        PantryItem pantryItem = null;

        if (cursor.moveToFirst()) {

            pantryItem = new PantryItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow("pkPantryItems")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sName")),
                    cursor.getFloat(cursor.getColumnIndexOrThrow("fQuantity")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("fkMeasurements")),
                    cursor.getString(cursor.getColumnIndexOrThrow("dtExpiryDate"))
            );
        }

        cursor.close();

        return pantryItem;
    }


    public List<PantryItem> getAllPantryItems() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tPantryItems", null);

        List<PantryItem> allPantryItems = new ArrayList<>();

        while (cursor.moveToNext()) {

            PantryItem pantryItem = new PantryItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow("pkPantryItems")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sName")),
                    cursor.getFloat(cursor.getColumnIndexOrThrow("fQuantity")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("fkMeasurements")),
                    cursor.getString(cursor.getColumnIndexOrThrow("dtExpiryDate"))
            );

            allPantryItems.add(pantryItem);
        }

        cursor.close();

        return allPantryItems;
    }


    public boolean updatePantryItem(PantryItem pantryItem) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("sName", pantryItem.getName());
        values.put("fQuantity", pantryItem.getQuantity());
        values.put("fkMeasurements", pantryItem.getMeasurementId());
        values.put("dtExpiryDate", pantryItem.getExpiryDate());

        int rowsUpdated = db.update(
                "tPantryItems",
                values,
                "pkPantryItems = ?",
                new String[]{
                        String.valueOf(pantryItem.getPrimaryKey())
                }
        );

        return rowsUpdated > 0;
    }


    public boolean deletePantryItem(int primaryKey) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsDeleted = db.delete(
                "tPantryItems",
                "pkPantryItems = ?",
                new String[]{String.valueOf(primaryKey)}
        );

        return rowsDeleted > 0;
    }
}