package com.example.smartpantrymanager.models;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private int pkRecipes;
    private String sName;
    private String sDescription;
    private String sInstructions;
    private int bArchived;

    // not in db, just a part of class
    private List<RecipeIngredient> ingredientList = new ArrayList<>();

    public Recipe() {

    }

    public Recipe(int primaryKey, String name, String description, String instructions, int archived) {

        this.pkRecipes = primaryKey;
        this.sName = name;
        this.sDescription = description;
        this.sInstructions = instructions;
        this.bArchived = archived;

    }

    public Recipe(int primaryKey, String name, String description, String instructions, int archived, List<RecipeIngredient> ingredientList) {

        this(primaryKey, name, description, instructions, archived);
        this.ingredientList = ingredientList;

    }

    // Getters and Setters
    public int getPrimaryKey() {
        return pkRecipes;
    }

    public void setPrimaryKey(int primaryKey) {
        this.pkRecipes = primaryKey;
    }

    public String getName() {
        return sName;
    }

    public void setName(String name) {
        this.sName = name;
    }

    public String getDescription() {
        return sDescription;
    }

    public void setDescription(String description) {
        this.sDescription = description;
    }

    public String getInstructions() {
        return sInstructions;
    }

    public void setInstructions(String instructions) {
        this.sInstructions = instructions;
    }

    public int getArchived() {
        return bArchived;
    }

    public void setArchived(int archived) {
        this.bArchived = archived;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredientList;
    }

    public void setIngredients(List<RecipeIngredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void addIngredient(RecipeIngredient ingredient) {
        ingredientList.add(ingredient);
    }

    public void removeIngredient(RecipeIngredient ingredient) {
        ingredientList.remove(ingredient);
    }

}