package com.example.tp3.utils;

import android.content.Context;

import com.example.tp3.models.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipeStorage {
    private static final String TAG = "RecipeStorage";
    private static final String RECIPES_DIR = "recipes";
    private static final String EXTENSION = ".json";

    private final Context context;
    private final Gson gson;

    public RecipeStorage(Context context) {
        this.context = context;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }


    public boolean saveRecipe(Recipe recipe) {
        return false;
    }

    public Recipe loadRecipe(UUID id) {
        return null;
    }

    public boolean deleteRecipe(UUID id) {
        return false;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        return recipes;
    }
}
