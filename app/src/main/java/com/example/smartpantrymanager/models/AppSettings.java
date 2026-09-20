package com.example.smartpantrymanager.models;

import java.util.HashMap;
import java.util.Map;

public class AppSettings {

    private int pkAppSettings;
    private Map<String, String> settings;

    public AppSettings() {
        settings = new HashMap<>();
    }

    public AppSettings(int primaryKey, Map<String, String> settings) {
        this.pkAppSettings = primaryKey;
        this.settings = settings;
    }

    public int getPrimaryKey() {
        return pkAppSettings;
    }

    public void setPrimaryKey(int primaryKey) {
        this.pkAppSettings = primaryKey;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }

    public String getSetting(String name) {
        return settings.get(name);
    }

    public void setSetting(String name, String value) {
        settings.put(name, value);
    }
}