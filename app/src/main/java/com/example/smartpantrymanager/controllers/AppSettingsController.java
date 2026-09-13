package com.example.smartpantrymanager.controllers;

import android.util.Log;

import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.AppSettings;
import com.example.smartpantrymanager.services.AppSettingsService;

public class AppSettingsController {

    private AppSettingsService appSettingsService;

    // Constructor
    public AppSettingsController(DatabaseHelper databaseHelper) {
        this.appSettingsService = new AppSettingsService(databaseHelper);
    }

    // Functions
    public AppSettings getSettings() {

        try {

            return appSettingsService.getAppSettings();

        } catch (Exception ex) {

            Log.d("GetSettings", ex.toString());
            return null;
        }
    }

    public boolean updateSettings(AppSettings appSettings) {

        try {

            if (appSettings == null) {
                return false;
            }

            return appSettingsService.updateAppSettings(appSettings);

        } catch (Exception ex) {

            Log.d("UpdateSettings", ex.toString());
            return false;
        }
    }
}