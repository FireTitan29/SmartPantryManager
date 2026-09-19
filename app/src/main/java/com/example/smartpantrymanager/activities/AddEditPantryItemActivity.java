package com.example.smartpantrymanager.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.common.Validation;
import com.example.smartpantrymanager.controllers.AddEditPantryItemsController;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.Measurement;
import com.example.smartpantrymanager.models.PantryItem;
import com.example.smartpantrymanager.services.MeasurementService;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class AddEditPantryItemActivity extends AppCompatActivity {
    private TextView textViewAddPantryTitle;
    private TextView textViewAddPantrySubtitle;
    private EditText editTextIngredientName;
    private EditText editTextQuantity;
    private Spinner spinnerMeasurement;
    private EditText editTextExpiryDate;
    private Button buttonSavePantryItem;
    private Button buttonCancel;
    private Button buttonDeletePantryItem;
    private int pantryItemId;
    private boolean editMode;
    private AddEditPantryItemsController addEditPantryItemsController;
    private MeasurementService measurementService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        addEditPantryItemsController = new AddEditPantryItemsController(databaseHelper);
        measurementService = new MeasurementService(databaseHelper);

        setContentView(R.layout.activity_add_edit_pantry_item);
        editTextIngredientName = findViewById(R.id.editTextIngredientName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        spinnerMeasurement = findViewById(R.id.spinnerMeasurement);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        buttonSavePantryItem = findViewById(R.id.buttonSavePantryItem);
        buttonDeletePantryItem = findViewById(R.id.buttonDeletePantryItem);

        textViewAddPantryTitle = findViewById(R.id.textViewAddPantryTitle);
        textViewAddPantrySubtitle = findViewById(R.id.textViewAddPantrySubtitle);

        // Determine if we are editing or adding a new item
        pantryItemId = getIntent().getIntExtra("pantryItemId", -1);
        editMode = pantryItemId != -1;

        loadMeasurements();

        if (editMode) {
            loadPantryItem();

            // Update the UI
            textViewAddPantryTitle.setText("Edit Ingredient");
            textViewAddPantrySubtitle.setText("Update your pantry item");
            buttonSavePantryItem.setText("Save Changes");

            buttonDeletePantryItem.setVisibility(View.VISIBLE);

        } else {

            textViewAddPantryTitle.setText("Add Ingredient");
            textViewAddPantrySubtitle.setText("Add something to your pantry");
            buttonSavePantryItem.setText("Add Ingredient");

            buttonDeletePantryItem.setVisibility(View.GONE);
        }

        buttonSavePantryItem.setOnClickListener(v -> validateForm());

        // Cancel Button goes back to pantry
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(v -> {finish();});

        // Delete Button
        buttonDeletePantryItem.setOnClickListener(v -> {

            boolean deleted = addEditPantryItemsController.deletePantryItem(pantryItemId);

            if (deleted) {

                finish();

            } else {

                buttonDeletePantryItem.setError("Could not delete ingredient");
            }
        });

    }

    // Validates the form and adds the item to the pantry
    private void validateForm() {

        boolean valid = true;

        String name = editTextIngredientName.getText().toString().trim();

        if (!Validation.isValidString(name)) {

            editTextIngredientName.setError("Ingredient name is required");
            valid = false;
        }

        String quantityText = editTextQuantity.getText().toString().trim();

        if (!Validation.isValidString(quantityText)) {

            editTextQuantity.setError("Quantity is required");
            valid = false;

        } else {

            try {

                float quantity = Float.parseFloat(quantityText);

                // Using the Validation common class I created to validate the form
                if (!Validation.isPositiveFloat(quantity)) {editTextQuantity.setError("Quantity must be greater than 0");

                    valid = false;
                }

            } catch (NumberFormatException ex) {

                editTextQuantity.setError("Enter a valid quantity");
                valid = false;
            }
        }

        String expiryDate = editTextExpiryDate.getText().toString().trim();

        if  (Validation.isValidString(expiryDate)) {

            if (!Validation.isValidDate(expiryDate)) {

                editTextExpiryDate.setError("Enter a valid date");
                valid = false;
            }

        }

        // Only add the item if validation comes back correct and happy
        if (valid) {

            String measurement = spinnerMeasurement.getSelectedItem().toString();

            // Creating a new pantry item
            PantryItem newItem = new PantryItem();
            newItem.setName(name);
            newItem.setQuantity(Float.parseFloat(quantityText));
            newItem.setExpiryDate(expiryDate);
            newItem.setMeasurementId(measurementService.getMeasurementId(measurement));

            boolean successful = false;

            if (editMode) {

                newItem.setPrimaryKey(pantryItemId);
                successful = addEditPantryItemsController.updatePantryItem(newItem);

            } else {

                successful = addEditPantryItemsController.addPantryItem(newItem);
            }


            if (successful) {

                finish();

            } else {
                // In case something goes wrong
                buttonSavePantryItem.setError("Could not add ingredient");
            }

        }
    }

    // helper to load the measurements into the spinner
    private void loadMeasurements() {

        List<Measurement> measurements =  measurementService.getAllMeasurements();
        List<String> measurementNames = new ArrayList<>();

        for (Measurement measurement : measurements) {

            measurementNames.add(measurement.getAbbreviation());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, measurementNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMeasurement.setAdapter(adapter);
    }

    // Loads the selected Pantry Item when a user chooses to edit
    private void loadPantryItem() {

        PantryItem pantryItem = addEditPantryItemsController.getPantryItem(pantryItemId);

        if (pantryItem == null) {

            finish();
            return;
        }

        editTextIngredientName.setText(pantryItem.getName());
        editTextQuantity.setText(String.valueOf(pantryItem.getQuantity()));
        editTextExpiryDate.setText(pantryItem.getExpiryDate());

        for (int i = 0; i < spinnerMeasurement.getCount(); i++) {

            String measurement = spinnerMeasurement.getItemAtPosition(i).toString();

            if (measurement.equals(pantryItem.getMeasurementName())) {
                spinnerMeasurement.setSelection(i);
                break;
            }
        }
    }


}