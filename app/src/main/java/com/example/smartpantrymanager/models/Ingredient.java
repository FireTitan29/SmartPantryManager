package com.example.smartpantrymanager.models;

public class Ingredient {

    private int pkIngredients;
    private String sName;

    public Ingredient() {

    }

    public Ingredient(int primaryKey, String name) {

        this.pkIngredients = primaryKey;
        this.sName = name;

    }

    // Getters and Setters
    public int getPrimaryKey() {
        return this.pkIngredients;
    }

    public String getName() {
        return this.sName;
    }

    public void setName(String name) {
        this.sName = name;
    }

    public void setPrimaryKey(int primaryKey) {
        this.pkIngredients = primaryKey;
    }

}
