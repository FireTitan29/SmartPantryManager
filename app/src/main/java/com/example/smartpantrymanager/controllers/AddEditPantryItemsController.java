package com.example.smartpantrymanager.controllers;

import android.util.Log;

import com.example.smartpantrymanager.common.MeasurementConverter;
import com.example.smartpantrymanager.common.Validation;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.Measurement;
import com.example.smartpantrymanager.models.PantryItem;
import com.example.smartpantrymanager.services.AppSettingsService;
import com.example.smartpantrymanager.services.MeasurementService;
import com.example.smartpantrymanager.services.PantryService;

import java.util.ArrayList;
import java.util.List;

public class AddEditPantryItemsController {

    private PantryService pantryService;
    private AppSettingsService appSettingsService;
    private MeasurementService measurementService;

    // Constructor
    public AddEditPantryItemsController(DatabaseHelper databaseHelper) {
        this.pantryService = new PantryService(databaseHelper);
        this.appSettingsService = new AppSettingsService(databaseHelper);
        this.measurementService = new MeasurementService(databaseHelper);
    }

    // Functions

    // Get a pantry item and converts it to the measurements in the db to the measurement
    // preference of the user. Db measurements are stored in ml and g
    public PantryItem getPantryItem(int primaryKey) {

        PantryItem item = new PantryItem();

        List<Measurement> measurements = measurementService.getAllMeasurements();

        try {

            item = pantryService.getPantryItem(primaryKey);

            PantryItem finalItem = item;

            Measurement itemMeasurement = measurements.stream()
                    .filter(m -> m.getPrimaryKey() == finalItem.getMeasurementId())
                    .findFirst()
                    .orElseThrow();

            float newValue = MeasurementConverter.convert(itemMeasurement.getAbbreviation(), appSettingsService, item.getQuantity());

            item.setQuantity(newValue);

            boolean isVolumeMetric = MeasurementConverter.isVolumeMetric(itemMeasurement.getAbbreviation());
            boolean isWeightMetric = MeasurementConverter.isWeightMetric(itemMeasurement.getAbbreviation());


            // Using the metrics set in the appsettings
            if (isVolumeMetric) {

                item.setMeasurementName(appSettingsService.getAppSettings().getSetting("Volume"));

            } else if (isWeightMetric) {

                item.setMeasurementName(appSettingsService.getAppSettings().getSetting("Weight"));

            } else {

                item.setMeasurementName("Units");
            }

            return item;

        } catch (Exception ex) {

            Log.d("GetPantryItems", ex.toString());
            return item;
        }
    }

    public boolean addPantryItem(PantryItem pantryItem) {

        try {

            if (!Validation.isValidString(pantryItem.getName())) {
                return false;
            }

            if (!Validation.isPositiveFloat(pantryItem.getQuantity())) {
                return false;
            }

            Measurement measurement = measurementService.getMeasurement(pantryItem.getMeasurementId());

            if (measurement == null) {

                return false;
            }

            String measurementAbbreviation = measurement.getAbbreviation();

            float quantity = pantryItem.getQuantity();

            // Convert user input to database measurement
            if (!measurementAbbreviation.equals("g")  && !measurementAbbreviation.equals("ml")  && !measurementAbbreviation.equals("Units")) {

                quantity = MeasurementConverter.convertToDatabase(measurementAbbreviation, quantity);
            }

            pantryItem.setQuantity(quantity);

            // Set the database measurement
            int databaseMeasurementId =  measurementService.getMeasurementId(measurementAbbreviation);

            pantryItem.setMeasurementId(databaseMeasurementId);

            return pantryService.addPantryItem(pantryItem) > 0;

        } catch (Exception ex) {

            Log.d("AddPantryItem", ex.toString());
            return false;
        }
    }

    public boolean updatePantryItem(PantryItem pantryItem) {

        try {

            if (!Validation.isValidString(pantryItem.getName())) {
                return false;
            }

            if (!Validation.isPositiveFloat(pantryItem.getQuantity())) {
                return false;
            }

            Measurement measurement =  measurementService.getMeasurement(pantryItem.getMeasurementId());

            if (measurement == null) {

                return false;
            }

            String measurementAbbreviation = measurement.getAbbreviation();

            float quantity = pantryItem.getQuantity();

            // Convert user input to database measurement
            if (!measurementAbbreviation.equals("g")  && !measurementAbbreviation.equals("ml")  && !measurementAbbreviation.equals("Units")) {

                quantity = MeasurementConverter.convertToDatabase(measurementAbbreviation,  quantity);
            }

            pantryItem.setQuantity(quantity);

            // Set the database measurement
            int databaseMeasurementId = measurementService.getMeasurementId(measurementAbbreviation);

            pantryItem.setMeasurementId(databaseMeasurementId);

            return pantryService.updatePantryItem(pantryItem);

        } catch (Exception ex) {

            Log.d("UpdatePantryItem", ex.toString());
            return false;
        }
    }

    // Used to be in the pantry controller, but I have moved it here since it makes more sense
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
