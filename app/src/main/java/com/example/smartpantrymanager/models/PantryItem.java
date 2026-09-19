package com.example.smartpantrymanager.models;

public class PantryItem {

    private int pkPantryItems;
    private String sName;
    private float fQuantity;
    private int fkMeasurements;
    private String dtExpiryDate;

    // For frontend
    private String MeasurementName;

    public PantryItem() {

    }


    public PantryItem(int primaryKey, String name, float quantity, int measurementId, String expiryDate) {

        this.pkPantryItems = primaryKey;
        this.sName = name;
        this.fQuantity = quantity;
        this.fkMeasurements = measurementId;
        this.dtExpiryDate = expiryDate;

    }

    // Getters and Setters
    public int getPrimaryKey() {
        return pkPantryItems;
    }

    public void setPrimaryKey(int primaryKey) {
        this.pkPantryItems = primaryKey;
    }


    public String getName() {
        return sName;
    }

    public void setName(String name) {
        this.sName = name;
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

    public String getExpiryDate() {
        return dtExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.dtExpiryDate = expiryDate;
    }

    public void setMeasurementName(String measurementName) {
        this.MeasurementName = measurementName;
    }

    public String getMeasurementName() {
        return MeasurementName;
    }
}