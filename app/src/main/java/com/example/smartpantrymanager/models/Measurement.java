package com.example.smartpantrymanager.models;

public class Measurement {


    private int pkMeasurements;
    private String sType;
    private  String sAbbreviation;

    public Measurement() {

    }

    public Measurement(int primaryKey, String type, String abbreviation) {

        this.pkMeasurements = primaryKey;
        this.sType = type;
        this.sAbbreviation = abbreviation;

    }

    // Getters and Setters
    public int getPrimaryKey() {
        return pkMeasurements;
    }

    public void setPrimaryKey(int primaryKey) {
        this.pkMeasurements = primaryKey;
    }

    public String getType() {
        return sType;
    }

    public void setType(String type) {
        this.sType = type;
    }

    public String getAbbreviation() {
        return  sAbbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.sAbbreviation = abbreviation;
    }
}
