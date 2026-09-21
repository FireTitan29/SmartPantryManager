package com.example.smartpantrymanager.controllers;

import android.util.Log;

import com.example.smartpantrymanager.common.MeasurementConverter;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.Measurement;
import com.example.smartpantrymanager.models.PantryItem;
import com.example.smartpantrymanager.services.AppSettingsService;
import com.example.smartpantrymanager.services.MeasurementService;
import com.example.smartpantrymanager.services.PantryService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        List<PantryItem> formattedItems = new ArrayList<>();

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

                boolean isVolumeMetric = MeasurementConverter.isVolumeMetric(itemMeasurement.getAbbreviation());
                boolean isWeightMetric = MeasurementConverter.isWeightMetric(itemMeasurement.getAbbreviation());

                // Using the metrics set in the appsettings
                if (isVolumeMetric) {

                    item.setMeasurementName(appSettingsService.getAppSettings().getSetting("Volume"));

                } else if (isWeightMetric) {

                    item.setMeasurementName(appSettingsService.getAppSettings().getSetting("Weight"));

                } else {

                    item.setMeasurementName(" Units");
                }

                if (appSettingsService.getAppSettings().getSetting("Notifications").equals("On")) {

                    // Notification of expiring food items in pantry logic
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    dateFormat.setLenient(false);

                    Date expireDate = dateFormat.parse(item.getExpiryDate());
                    Date todayDate = dateFormat.parse(dateFormat.format(new Date()));

                    long difference = expireDate.getTime() - todayDate.getTime();
                    long daysUntilExpiry = difference / (1000 * 60 * 60 * 24);

                    // used for the notification icon in the pantry activity
                    if (daysUntilExpiry >= 0 && daysUntilExpiry <= 7) {

                        item.setExpiringSoon(true);

                    }
                }

                formattedItems.add(item);

            }

            return formattedItems;

        } catch (Exception ex) {

            Log.d("GetPantryItems", ex.toString());
            return items;
        }
    }

}
