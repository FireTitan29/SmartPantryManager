package com.example.smartpantrymanager.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.controllers.RecipeDetailController;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.models.Recipe;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private ImageView imageRecipe;
    private TextView textViewRecipeName;
    private TextView textViewRecipeDescription;
    private TextView textViewRecipeIngredients;
    private TextView textViewRecipeInstructions;
    private Button buttonBackToRecipes;

    private RecipeDetailController recipeDetailController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        recipeDetailController = new RecipeDetailController(databaseHelper);

        imageRecipe = findViewById(R.id.imageRecipe);
        textViewRecipeName = findViewById(R.id.textViewRecipeName);
        textViewRecipeDescription = findViewById(R.id.textViewRecipeDescription);
        textViewRecipeIngredients = findViewById(R.id.textViewRecipeIngredients);
        textViewRecipeInstructions = findViewById(R.id.textViewRecipeInstructions);
        buttonBackToRecipes = findViewById(R.id.buttonBackToRecipes);

        buttonBackToRecipes.setOnClickListener(v -> finish());

        loadRecipe();
    }

    private void loadRecipe() {

        int recipeId = getIntent().getIntExtra("recipeId", -1);

        if (recipeId == -1) {
            finish();
            return;
        }

        Recipe recipe = recipeDetailController.getRecipe(recipeId);

        if (recipe == null) {
            finish();
            return;
        }

        textViewRecipeName.setText(recipe.getName());
        textViewRecipeDescription.setText(recipe.getDescription());
        textViewRecipeInstructions.setText(recipe.getInstructions());

        loadRecipeIngredients(recipe);
        loadRecipeImage(recipe);
    }

    private void loadRecipeIngredients(Recipe recipe) {

        List<String> ingredients = recipeDetailController.getFormattedIngredients(recipe);

        StringBuilder formattedIngredients = new StringBuilder();

        for (String ingredient : ingredients) {

            formattedIngredients
                    .append(ingredient)
                    .append("\n");
        }

        textViewRecipeIngredients.setText(formattedIngredients.toString());
    }

    private void loadRecipeImage(Recipe recipe) {

        int imageResource = getRecipeImage(recipe.getImageName());

        imageRecipe.setImageResource(imageResource);
    }

    // Helper To get the image
    private int getRecipeImage(String imageName) {

        switch (imageName) {

            case "American_Pancakes.jpg":
                return R.drawable.american_pancakes;

            case "Apple_Crumble.jpg":
                return R.drawable.apple_crumble;

            case "Bacon_and_Eggs.jpg":
                return R.drawable.bacon_and_eggs;

            case "Bacon_Pasta.jpg":
                return R.drawable.bacon_pasta;

            case "Banana_Oats.jpg":
                return R.drawable.banana_oats;

            case "Banana_Pancakes.jpg":
                return R.drawable.banana_pancakes;

            case "Breakfast_Pancakes.jpg":
                return R.drawable.breakfast_pancakes;

            case "Breakfast_Sandwich.jpg":
                return R.drawable.breakfast_sandwich;

            case "Cheese_Burger.jpg":
                return R.drawable.cheese_burger;

            case "Cheese_Toastie.jpg":
                return R.drawable.cheese_toastie;

            case "Chicken_and_Potato.jpg":
                return R.drawable.chicken_and_potato;

            case "Chicken_and_Rice.jpg":
                return R.drawable.chicken_and_rice;

            case "Chicken_Burger.jpg":
                return R.drawable.chicken_burger;

            case "Chicken_Rice_Bowl.jpg":
                return R.drawable.chicken_rice_bowl;

            case "Chicken_Sandwich.jpg":
                return R.drawable.chicken_sandwich;

            case "Chicken_Tomato_Pasta.jpg":
                return R.drawable.chicken_tomato_pasta;

            case "Chocolate_Brownies.jpg":
                return R.drawable.chocolate_brownies;

            case "Chocolate_Cake.jpg":
                return R.drawable.chocolate_cake;

            case "Chocolate_Milkshake.jpg":
                return R.drawable.chocolate_milkshake;

            case "Cottage_Pie.jpg":
                return R.drawable.cottage_pie;

            case "Creamy_Chicken_Pasta.jpg":
                return R.drawable.creamy_chicken_pasta;

            case "Ham_and_Cheese_Sandwich.jpg":
                return R.drawable.ham_and_cheese_sandwich;

            case "Loaded_Fries.jpg":
                return R.drawable.loaded_fries;

            case "Mac_and_Cheese.jpg":
                return R.drawable.mac_and_cheese;

            case "Mince_Sandwich.jpg":
                return R.drawable.mince_sandwich;

            case "Sausage_and_Mash.jpg":
                return R.drawable.sausage_and_mash;

            case "Scrambled_Eggs_On_Toast.jpg":
                return R.drawable.scrambled_eggs_on_toast;

            case "Spaghetti_and_Meatballs.jpg":
                return R.drawable.spaghetti_and_meatballs;

            case "Spaghetti_Bolognese.jpg":
                return R.drawable.spaghetti_bolognese;

            case "Tomato_Cheese_Pasta.jpg":
                return R.drawable.tomato_cheese_pasta;

            default:
                return R.drawable.spaghetti_bolognese;
        }
    }
}