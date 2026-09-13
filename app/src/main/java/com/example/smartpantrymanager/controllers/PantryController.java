package com.example.smartpantrymanager.controllers;

import android.util.Log;

import com.example.smartpantrymanager.common.MeasurementConverter;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.AppSettings;
import com.example.smartpantrymanager.models.Measurement;
import com.example.smartpantrymanager.models.PantryItem;
import com.example.smartpantrymanager.services.AppSettingsService;
import com.example.smartpantrymanager.services.MeasurementService;
import com.example.smartpantrymanager.services.PantryService;

import java.util.ArrayList;
import java.util.List;

public class PantryController {

    private PantryService pantryService;
    private AppSettingsService appSettingsService;
    private MeasurementService measurementService;

    // Constructor
    public PantryController(DatabaseHelper databaseHelper) {
        this.pantryService = new PantryService(databaseHelper);
        this.appSettingsService = new AppSettingsService(databaseHelper);
        this.measurementService = new MeasurementService(databaseHelper);
    }

    // Functions

    // Gets pantry items and converts them to the measurements in the db to the measurement
    // preference of the user. Db measurements are stored in ml and g
    public List<PantryItem> getPantryItems() {

        List<PantryItem> items = new ArrayList<>();

        List<Measurement> measurements = measurementService.getAllMeasurements();

        try {

            items = pantryService.getAllPantryItems();

            for (int i = 0; i < items.size(); i ++) {

                PantryItem item = items.get(i);

                Measurement itemMeasurement = measurements.stream()
                        .filter(m -> m.getPrimaryKey() == item.getMeasurementId())
                        .findFirst()
                        .orElseThrow();

                float newValue = MeasurementConverter.convert(itemMeasurement.getAbbreviation(), appSettingsService, item.getQuantity());

                item.setQuantity(newValue);

            }

            return items;

        } catch (Exception ex) {

            Log.d("GetPantryItems", ex.toString());
            return items;
        }
    }

    public boolean deletePantryItem(int primaryKey) {

        try {

            pantryService.deletePantryItem(primaryKey);
            return true;

        } catch (Exception ex) {

            Log.d("GetPantryItems", ex.toString());
            return false;
        }
    }



}
