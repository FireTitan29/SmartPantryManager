package com.example.smartpantrymanager.controllers;

import com.example.smartpantrymanager.common.MeasurementConverter;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.Ingredient;
import com.example.smartpantrymanager.models.Measurement;
import com.example.smartpantrymanager.models.PantryItem;
import com.example.smartpantrymanager.models.Recipe;
import com.example.smartpantrymanager.models.RecipeIngredient;
import com.example.smartpantrymanager.services.IngredientService;
import com.example.smartpantrymanager.services.MeasurementService;
import com.example.smartpantrymanager.services.RecipeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuggestedRecipesController {

    private RecipeService recipeService;
    private PantryController pantryController;
    private IngredientService ingredientService;
    private MeasurementService measurementService;

    public SuggestedRecipesController(DatabaseHelper databaseHelper) {
        recipeService = new RecipeService(databaseHelper);
        pantryController = new PantryController(databaseHelper);
        ingredientService = new IngredientService(databaseHelper);
        measurementService = new MeasurementService(databaseHelper);
    }

    // Gets all of the recipes that match whats in your pantry
    public List<Recipe> getRecipes() {


        List<Recipe> allRecipes = recipeService.getActiveRecipes();
        List<PantryItem> allPantryItems = pantryController.getPantryItems();


        List<String> normalisedPantryItems = new ArrayList<>();

        // Looping through all the pantry items and normalising them
        for (int i = 0; i < allPantryItems.size(); i++) {

            String pantryItemName = allPantryItems.get(i).getName();

            String normalisedPantryItem = normaliseIngredientName(pantryItemName);

            normalisedPantryItems.add(normalisedPantryItem);
        }

        Map<Integer, List<String>> recipeIngredientsLookup = new HashMap<>();
        Map<Integer, Recipe> recipeLookup = new HashMap<>();

        for (Recipe recipe : allRecipes) {

            recipeLookup.put(recipe.getPrimaryKey(), recipe);
        }

        // also looping through all of the recipes and also normalising them
        // this is not the most efficient way to do it O(n^2), but its an assignment
        // and this is the easiest way I can think of doing it
        for (int i = 0; i < allRecipes.size(); i++) {

            Recipe recipe = allRecipes.get(i);

            List<RecipeIngredient> ingredientsList = recipe.getIngredients();

            List<String> normalisedIngredientNames =  new ArrayList<>();


            for (int j = 0; j < ingredientsList.size(); j++) {

                String normalisedIngredientName = normaliseIngredientName(ingredientService
                                                                                .getIngredient(ingredientsList
                                                                                .get(j).getIngredientId())
                                                                                .getName());

                normalisedIngredientNames.add(normalisedIngredientName);
            }

            recipeIngredientsLookup.put(recipe.getPrimaryKey(), normalisedIngredientNames);
        }

        List<Recipe> matchingRecipes = new ArrayList<>();

        // again not the most efficent way, this is like O(n^3) which is terrible optimisation wise,
        // but because this is an assignment and I want to keep the logic there from what I've done before
        // without major rewrites, I'll keep it this way
        for (Map.Entry<Integer, List<String>> recipe : recipeIngredientsLookup.entrySet()) {

            Integer recipeId = recipe.getKey();
            List<String> ingredients = recipe.getValue();

            int ingredientsSize = ingredients.size();
            int matches = 0;

            // Implementation of matching logic
            for (String ingredient : ingredients) {

                // ingredient exists
                if (normalisedPantryItems.contains(ingredient)) {

                    PantryItem pantryItem = null;
                    RecipeIngredient recipeIngredient = null;

                    // Find pantry item
                    for (PantryItem item : allPantryItems) {

                        String pantryName = normaliseIngredientName(item.getName());

                        if (pantryName.equals(ingredient)) {

                            pantryItem = item;
                            break;
                        }
                    }

                    // Find recipe ingredient
                    Recipe currentRecipe = recipeLookup.get(recipeId);

                    for (RecipeIngredient item : currentRecipe.getIngredients()) {

                        String recipeIngredientName = normaliseIngredientName(ingredientService.getIngredient(item.getIngredientId()).getName());

                        if (recipeIngredientName.equals(ingredient)) {

                            recipeIngredient = item;
                            break;
                        }
                    }

                    if (pantryItem != null && recipeIngredient != null) {

                        if (measurementMatches(pantryItem, recipeIngredient)) {
                            matches++;
                        }
                    }
                }
            }

            if (matches == ingredientsSize) {

                matchingRecipes.add(recipeLookup.get(recipeId));
            }
        }

        // proof of concept
        return matchingRecipes;
    }

    public Recipe getRecipe(int primaryKey) {

        return recipeService.getRecipe(primaryKey);
    }

    // Super basic normalisation helper, in case users enter purals
    // there were some issues with this though... for instance... cheeses when I was just removing es
    // hence the new ies and oes rules
    // this covers MOST issues I've found, but there could be more, like spelling mistakes etc.
    private String normaliseIngredientName(String name) {

        name = name.trim().toLowerCase();

        if (name.endsWith("ies")) {

            name = name.substring(0, name.length() - 3) + "y";

        } else if (name.endsWith("oes")) {

            name = name.substring(0, name.length() - 2);

        } else if (name.endsWith("s") && !name.endsWith("ss")) {

            name = name.substring(0, name.length() - 1);
        }

        return name;
    }

    // Here's my rules for matching because matching units and volume/weight gets weird
    // Units Rule:
    // If we have a unit in our pantry, the recipe asks for ml or g, we assume we have enough...
    // so if it wants 200g of Onion, and we have 1 Unit of Onion, then we assume there is enough

    // Weight & Volume:
    // Pantry Items need to be more than or equal to the recipe
    // We don't compare weight and volume, so 300g of Onion vs 300ml of Onion will FAIL... so this
    // does require the user to use their brain a little
    private boolean measurementMatches(PantryItem pantryItem, RecipeIngredient recipeIngredient) {

        try {
            Measurement pantryMeasurement = measurementService.getMeasurement(pantryItem.getMeasurementId());

            Measurement recipeMeasurement = measurementService.getMeasurement(recipeIngredient.getMeasurementId());

            if (pantryMeasurement == null || recipeMeasurement == null) {
                return false;
            }

            String pantryUnit = pantryItem.getMeasurementName().trim();
            String recipeUnit = recipeMeasurement.getAbbreviation();

            // If both are Units then we compare the quantities normally
            if (pantryUnit.equalsIgnoreCase("Units") && recipeUnit.equalsIgnoreCase("Units")) {

                return pantryItem.getQuantity() >= recipeIngredient.getQuantity();
            }

            // If either side is Units then we assume there is enough
            if (pantryUnit.equalsIgnoreCase("Units") || recipeUnit.equalsIgnoreCase("Units")) {

                return true;
            }

            // Weight
            if (MeasurementConverter.isWeightMetric(pantryUnit) && MeasurementConverter.isWeightMetric(recipeUnit)) {

                float pantryQuantity = MeasurementConverter.convertToDatabase(pantryUnit, pantryItem.getQuantity());

                float recipeQuantity = MeasurementConverter.convertToDatabase(recipeUnit, recipeIngredient.getQuantity());

                return pantryQuantity >= recipeQuantity;
            }

            // Volume
            if (MeasurementConverter.isVolumeMetric(pantryUnit) && MeasurementConverter.isVolumeMetric(recipeUnit)) {

                float pantryQuantity = MeasurementConverter.convertToDatabase(pantryUnit, pantryItem.getQuantity());

                float recipeQuantity = MeasurementConverter.convertToDatabase(recipeUnit, recipeIngredient.getQuantity());

                return pantryQuantity >= recipeQuantity;
            }

            // Weight vs volume, or some other incompatible combination
            return false;

        } catch (Exception ex) {
            return false;
        }
    }
}