package com.example.smartpantrymanager.common;

import com.example.smartpantrymanager.models.AppSettings;
import com.example.smartpantrymanager.services.AppSettingsService;

import java.util.ArrayList;
import java.util.List;

public final class MeasurementConverter {

    private MeasurementConverter() {

    }

    public static float convert(String measurement, AppSettingsService appSettingsService, float value) throws Exception {

        if (measurement.equals("Units")) {
            return value;
        }

        AppSettings appSettings = appSettingsService.getAppSettings();

        List<String> weight = List.of("kg", "g", "mg");
        List<String> volume = List.of("l", "ml");


        int measurement_index;
        int appsetting_index;

        // Convert Weight
        if (weight.contains(measurement)) {

            measurement_index = weight.indexOf(measurement);
            appsetting_index = weight.indexOf(appSettings.getWeightUnit());

        } else {

            measurement_index = volume.indexOf(measurement);
            appsetting_index = volume.indexOf(appSettings.getVolumeUnit());
        }

        int modifyBy = 0;

        // Multiply / divide by 1000 to get to the correct measurement value

        if (measurement_index <= appsetting_index) {

            modifyBy = appsetting_index - measurement_index;

            for (int i = 0; i < modifyBy; i++) {

                value *= 1000;
            }

        } else {

            modifyBy = measurement_index - appsetting_index;

            for (int i = 0; i < modifyBy; i++) {

                value /= 1000;
            }
        }

        return value;
    }

    // database values are always stored in grams and ml
    public static float convertToDatabase(String measurement, float value) throws Exception {

        if (measurement.equals("Units")) {
            return value;
        }

        List<String> weight = List.of("kg", "g", "mg");
        List<String> volume = List.of("l", "ml");

        int measurementIndex;
        int databaseIndex;

        // Convert Weight to grams
        if (weight.contains(measurement)) {

            measurementIndex = weight.indexOf(measurement);
            databaseIndex = weight.indexOf("g");

            // Convert Volume to millilitres
        } else if (volume.contains(measurement)) {

            measurementIndex = volume.indexOf(measurement);
            databaseIndex = volume.indexOf("ml");

        } else {

            throw new Exception("Unknown measurement: " + measurement);
        }

        int modifyBy;

        // Going from larger → smaller
        if (measurementIndex < databaseIndex) {

            modifyBy = databaseIndex - measurementIndex;

            for (int i = 0; i < modifyBy; i++) {
                value *= 1000;
            }

        // Going from smaller → larger
        } else {

            modifyBy = measurementIndex - databaseIndex;

            for (int i = 0; i < modifyBy; i++) {
                value /= 1000;
            }
        }

        return value;
    }
}