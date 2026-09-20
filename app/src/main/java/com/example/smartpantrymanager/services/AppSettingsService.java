package com.example.smartpantrymanager.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.AppSettings;

import java.util.Map;

public class AppSettingsService {

    private DatabaseHelper dbHelper;

    public AppSettingsService(DatabaseHelper databaseHelper) {
        this.dbHelper = databaseHelper;
    }


    public AppSettings getAppSettings() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tAppSettings",
                null
        );

        AppSettings appSettings = new AppSettings();

        if (cursor.moveToFirst()) {

            do {

                String name = cursor.getString(cursor.getColumnIndexOrThrow("sName"));

                String value = cursor.getString(cursor.getColumnIndexOrThrow("sValue"));

                appSettings.setSetting(name, value);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return appSettings;
    }


    public boolean updateAppSettings(AppSettings appSettings) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        boolean successful = true;

        for (Map.Entry<String, String> setting : appSettings.getSettings().entrySet()) {

            ContentValues values = new ContentValues();
            values.put("sValue", setting.getValue());

            int rowsUpdated = db.update("tAppSettings", values, "sName = ?", new String[]{setting.getKey()});

            if (rowsUpdated == 0) {

                successful = false;
            }
        }

        return successful;
    }
}