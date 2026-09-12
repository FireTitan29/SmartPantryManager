package com.example.smartpantrymanager.services;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.Measurement;

import java.util.ArrayList;
import java.util.List;


// Measurements will be static in the db, so they will be set once by the application
public class MeasurementService {

    private DatabaseHelper dbHelper;

    public MeasurementService(DatabaseHelper databaseHelper) {
        this.dbHelper = databaseHelper;
    }

    public Measurement getMeasurement(int primaryKey) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tMeasurements WHERE pkMeasurements = ?",
                new String[]{String.valueOf(primaryKey)}
        );

        Measurement measurement = null;

        if (cursor.moveToFirst()) {

            measurement = new Measurement(
                    cursor.getInt(cursor.getColumnIndexOrThrow("pkMeasurements")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sType")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sAbbreviation"))
            );
        }

        cursor.close();

        return measurement;
    }

    public List<Measurement> getAllMeasurements() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tMeasurements", null
        );

        List<Measurement> allMeasurements = new ArrayList<>();

        while (cursor.moveToNext()) {

            Measurement measurement = new Measurement(
                    cursor.getInt(cursor.getColumnIndexOrThrow("pkMeasurements")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sType")),
                    cursor.getString(cursor.getColumnIndexOrThrow("sAbbreviation"))
            );

            allMeasurements.add(measurement);
        }

        cursor.close();

        return allMeasurements;
    }
}