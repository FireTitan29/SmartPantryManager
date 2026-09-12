package com.example.smartpantrymanager.models;

public class RecipeIngredient {

    private int fkRecipes;
    private int fkIngredients;
    private float fQuantity;
    private int fkMeasurements;

    public RecipeIngredient() {

    }

    public RecipeIngredient(int recipeId, int ingredientId, float quantity, int measurementId) {
        this.fkRecipes = recipeId;
        this.fkIngredients = ingredientId;
        this.fQuantity = quantity;
        this.fkMeasurements = measurementId;
    }

    // Getters and setters
    public int getRecipeId() {
        return fkRecipes;
    }

    public void setRecipeId(int recipeId) {
        this.fkRecipes = recipeId;
    }

    public int getIngredientId() {
        return fkIngredients;
    }

    public void setIngredientId(int ingredientId) {
        this.fkIngredients = ingredientId;
    }

    public float getQuantity() {
        return fQuantity;
    }

    public void setQuantity(float quantity) {
        this.fQuantity = quantity;
    }

    public int getMeasurementId() {
        return fkMeasurements;
    }

    public void setMeasurementId(int measurementId) {
        this.fkMeasurements = measurementId;
    }
}