package com.example.smartpantrymanager.models;

public class AppSettings {

    private int pkAppSettings;
    private String sWeightUnit;
    private String sVolumeUnit;


    public AppSettings() {

    }


    public AppSettings(int primaryKey, String weightUnit, String volumeUnit) {

        this.pkAppSettings = primaryKey;
        this.sWeightUnit = weightUnit;
        this.sVolumeUnit = volumeUnit;

    }


    // Getters and Setters

    public int getPrimaryKey() {
        return pkAppSettings;
    }

    public void setPrimaryKey(int primaryKey) {
        this.pkAppSettings = primaryKey;
    }


    public String getWeightUnit() {
        return sWeightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.sWeightUnit = weightUnit;
    }


    public String getVolumeUnit() {
        return sVolumeUnit;
    }

    public void setVolumeUnit(String volumeUnit) {
        this.sVolumeUnit = volumeUnit;
    }
}