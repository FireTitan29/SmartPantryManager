package com.example.smartpantrymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.adapters.RecipeAdapter;
import com.example.smartpantrymanager.controllers.SuggestedRecipesController;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.Recipe;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecipes;
    private SuggestedRecipesController suggestedRecipesController;
    private BottomNavigationView bottomNavigationView;

    private  TextView textViewRecipesSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        suggestedRecipesController = new SuggestedRecipesController(databaseHelper);

        recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes);
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.navigation_recipes);

        // Navigation bar logic
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.navigation_pantry) {

                Intent intent = new Intent(this, PantryActivity.class);
                startActivity(intent);
                finish();
                return true;

            } else if (item.getItemId() == R.id.navigation_recipes) {

                return true;

            } else if (item.getItemId() == R.id.navigation_settings) {

                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });

        loadRecipes();
    }

    private void loadRecipes() {

        List<Recipe> recipes = suggestedRecipesController.getRecipes();
        // List<Recipe> recipes = new ArrayList<>();

        // UI Change to inform user that they don't have any matching recipes
        if(recipes.isEmpty()) {

            textViewRecipesSubtitle = findViewById(R.id.textViewRecipesSubtitle);
            textViewRecipesSubtitle.setText("No recipes match your pantry yet - add more ingredients to your pantry");

        }

        RecipeAdapter recipeAdapter = new RecipeAdapter(recipes, recipe -> {

            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra("recipeId", recipe.getPrimaryKey());
            startActivity(intent);

        });

        recyclerViewRecipes.setAdapter(recipeAdapter);

    }
}