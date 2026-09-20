package com.example.smartpantrymanager.controllers;

import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.Recipe;
import com.example.smartpantrymanager.services.RecipeService;

import java.util.List;

public class SuggestedRecipesController {

    private RecipeService recipeService;

    public SuggestedRecipesController(DatabaseHelper databaseHelper) {
        recipeService = new RecipeService(databaseHelper);
    }

    // Gets recipes from the service which is ordered by name asc
    public List<Recipe> getRecipes() {
        return recipeService.getActiveRecipes();
    }

    public Recipe getRecipe(int primaryKey) {
        return recipeService.getRecipe(primaryKey);
    }
}