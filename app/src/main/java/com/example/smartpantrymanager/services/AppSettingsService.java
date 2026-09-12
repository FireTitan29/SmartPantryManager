package com.example.smartpantrymanager.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.AppSettings;

public class AppSettingsService {

    private DatabaseHelper dbHelper;

    public AppSettingsService(DatabaseHelper databaseHelper) {
        this.dbHelper = databaseHelper;
    }


    public AppSettings getAppSettings() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tAppSettings LIMIT 1",
                null
        );

        AppSettings appSettings = null;

        if (cursor.moveToFirst()) {

            appSettings = new AppSettings(
                    cursor.getInt(cursor.getColumnIndexOrThrow("pkAppSettings")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sWeightUnit")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sVolumeUnit"))
            );
        }

        cursor.close();

        return appSettings;
    }


    public boolean updateAppSettings(AppSettings appSettings) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("sWeightUnit", appSettings.getWeightUnit());
        values.put("sVolumeUnit", appSettings.getVolumeUnit());

        int rowsUpdated = db.update(
                "tAppSettings",
                values,
                "pkAppSettings = ?",
                new String[]{String.valueOf(appSettings.getPrimaryKey())}
        );

        return rowsUpdated > 0;
    }
}