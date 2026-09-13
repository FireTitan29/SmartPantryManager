package com.example.smartpantrymanager.controllers;

import android.util.Log;

import com.example.smartpantrymanager.common.MeasurementConverter;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.Ingredient;
import com.example.smartpantrymanager.models.Measurement;
import com.example.smartpantrymanager.models.Recipe;
import com.example.smartpantrymanager.models.RecipeIngredient;
import com.example.smartpantrymanager.services.AppSettingsService;
import com.example.smartpantrymanager.services.IngredientService;
import com.example.smartpantrymanager.services.MeasurementService;
import com.example.smartpantrymanager.services.RecipeService;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailController {

    private RecipeService recipeService;
    private IngredientService ingredientService;
    private MeasurementService measurementService;
    private AppSettingsService appSettingsService;


    // Constructor
    public RecipeDetailController(DatabaseHelper databaseHelper) {

        this.recipeService = new RecipeService(databaseHelper);
        this.ingredientService = new IngredientService(databaseHelper);
        this.measurementService = new MeasurementService(databaseHelper);
        this.appSettingsService = new AppSettingsService(databaseHelper);
    }

    // Functions

    // Gets the recipe as is in db
    public Recipe getRecipe(int primaryKey) {

        try {

            return recipeService.getRecipe(primaryKey);

        } catch (Exception ex) {

            Log.d("GetRecipe", ex.toString());
            return null;
        }
    }

    // gets recipe's ingredients in the format for the user based on the appsettings
    public List<String> getFormattedIngredients(Recipe recipe) {

        List<String> formattedIngredients = new ArrayList<>();

        try {

            if (recipe == null) {

                return formattedIngredients;
            }

            for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {

                Ingredient ingredient = ingredientService.getIngredient(recipeIngredient.getIngredientId());

                Measurement measurement = measurementService.getMeasurement(recipeIngredient.getMeasurementId());

                if (ingredient == null || measurement == null) {

                    continue;
                }

                float quantity = MeasurementConverter.convert(measurement.getAbbreviation(), appSettingsService, recipeIngredient.getQuantity());

                String formattedIngredient =
                                quantity
                                + " "
                                + getDisplayMeasurement(measurement)
                                + " "
                                + ingredient.getName();

                formattedIngredients.add(formattedIngredient);
            }

        } catch (Exception ex) {

            Log.d("FormatIngredients", ex.toString());
        }

        return formattedIngredients;
    }

    // Private function that gets the correct measurement unit
    private String getDisplayMeasurement(Measurement measurement) {

        if (measurement.getAbbreviation().equals("g") || measurement.getAbbreviation().equals("kg") || measurement.getAbbreviation().equals("mg")) {

            return appSettingsService.getAppSettings().getWeightUnit();

        }

        if (measurement.getAbbreviation().equals("ml") || measurement.getAbbreviation().equals("l")) {

            return appSettingsService.getAppSettings().getVolumeUnit();
        }

        return measurement.getAbbreviation();
    }
}