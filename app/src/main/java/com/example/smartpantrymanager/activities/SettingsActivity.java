package com.example.smartpantrymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.controllers.AppSettingsController;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.AppSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerWeightUnit;
    private Spinner spinnerVolumeUnit;
    private MaterialSwitch switchNotifications;
    private Button buttonSaveSettings;
    private BottomNavigationView bottomNavigationView;

    private AppSettingsController appSettingsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        appSettingsController = new AppSettingsController(databaseHelper);

        spinnerWeightUnit = findViewById(R.id.spinnerWeightUnit);
        spinnerVolumeUnit = findViewById(R.id.spinnerVolumeUnit);
        switchNotifications = findViewById(R.id.switchNotifications);
        buttonSaveSettings = findViewById(R.id.buttonSaveSettings);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        setupSpinners();
        loadSettings();

        buttonSaveSettings.setOnClickListener(v -> saveSettings());

        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);

        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.navigation_settings) {
                return true;
            }

            if (item.getItemId() == R.id.navigation_pantry) {
                Intent intent = new Intent(this, PantryActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_recipes) {

                Intent intent = new Intent(this, SuggestedRecipesActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });
    }

    // Creating the spinners on the page so they hold the correct values
    private void setupSpinners() {

        List<String> weightUnits = Arrays.asList("g", "kg", "mg");

        List<String> volumeUnits = Arrays.asList("ml", "l");

        ArrayAdapter<String> weightAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weightUnits);

        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerWeightUnit.setAdapter(weightAdapter);


        ArrayAdapter<String> volumeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, volumeUnits);

        volumeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerVolumeUnit.setAdapter(volumeAdapter);
    }

    // Getting all of the settings from appsettings table
    private void loadSettings() {

        AppSettings appSettings = appSettingsController.getSettings();

        String weightUnit = appSettings.getSetting("Weight");
        String volumeUnit = appSettings.getSetting("Volume");
        String notifications = appSettings.getSetting("Notifications");

        if (weightUnit != null) {

            setSpinnerValue(spinnerWeightUnit, weightUnit);
        }

        if (volumeUnit != null) {

            setSpinnerValue(spinnerVolumeUnit, volumeUnit);
        }

        if (notifications != null) {

            switchNotifications.setChecked(notifications.equalsIgnoreCase("On"));
        }
    }

    // Helper for setting the value of the spinner
    private void setSpinnerValue(Spinner spinner, String value) {

        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();

        int position = adapter.getPosition(value);

        if (position >= 0) {
            spinner.setSelection(position);
        }
    }

    // Allows the settings to be saved and then also creates a little toast notification to alert
    // the user that the settings have been saved
    private void saveSettings() {

        AppSettings appSettings = new AppSettings();

        appSettings.setSetting("Weight", spinnerWeightUnit.getSelectedItem().toString());

        appSettings.setSetting("Volume", spinnerVolumeUnit.getSelectedItem().toString());

        appSettings.setSetting("Notifications", switchNotifications.isChecked() ? "On" : "Off");

        boolean successful = appSettingsController.updateSettings(appSettings);

        if (successful) {

            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "Failed to save settings", Toast.LENGTH_SHORT).show();
        }
    }
}