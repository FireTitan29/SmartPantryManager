package com.example.smartpantrymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.adapters.PantryAdapter;
import com.example.smartpantrymanager.controllers.PantryController;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.PantryItem;
import com.example.smartpantrymanager.services.MeasurementService;

import java.util.List;

public class PantryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantryItems;
    private PantryController pantryController;
    private Button buttonAddIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pantry);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        pantryController = new PantryController(databaseHelper);

        recyclerViewPantryItems = findViewById(R.id.recyclerViewPantryItems);

        recyclerViewPantryItems.setLayoutManager(new LinearLayoutManager(this));

        loadPantryItems();

        // Adding the button and making it clickable to open the edit/add activity
        buttonAddIngredient = findViewById(R.id.btnAddIngredient);

        buttonAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(this, AddEditPantryItemActivity.class);
            startActivity(intent);
        });

    }

    // When finish adding the recycler will load the new item
    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems();
    }

    // Helper to load the pantry items
    private void loadPantryItems() {
        List<PantryItem> pantryItems = pantryController.getPantryItems();

        PantryAdapter pantryAdapter = new PantryAdapter(pantryItems, item -> {
            Intent intent = new Intent(this, AddEditPantryItemActivity.class);
            intent.putExtra("pantryItemId", item.getPrimaryKey());
            startActivity(intent);
        });

        recyclerViewPantryItems.setAdapter(pantryAdapter);
    }
}