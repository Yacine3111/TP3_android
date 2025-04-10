package com.example.tp3.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.tp3.models.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipeStorage {
    private static final String TAG = "RecipeStorage";
    private static final String RECIPES_DIR = "recipes";
    private static final String EXTENSION = ".json";

    private final Context context;
    private final Gson gson;

    private final File recipeDirectory;

    public RecipeStorage(Context context) {
        this.context = context;
        this.gson = new GsonBuilder().setPrettyPrinting().create();


        recipeDirectory= new File(context.getFilesDir(),RECIPES_DIR);
        recipeDirectory.mkdir();


    }


    public boolean saveRecipe(Recipe recipe) {
        String fileName = recipe.getId()+EXTENSION;
        try{

            File file=new File(recipeDirectory,fileName);
            file.createNewFile();



            FileOutputStream fos = new FileOutputStream(file);
            String recipeJson= gson.toJson(recipe);
            fos.write(recipeJson.getBytes());
            fos.close();
            return true;
        }catch (FileNotFoundException e){
            Log.d(TAG,String.format("Le fichier %s n'existe pas",fileName));
        }catch (IOException e){
            Log.d(TAG,String.format("Erreur en écrivant dans le fichier %s",fileName));
        }
        return false;
    }

    public Recipe loadRecipe(UUID id) {
        String fileName= id+EXTENSION;
        try{
            File file = new File(RECIPES_DIR,fileName);


            FileInputStream fis = context.openFileInput(fileName);

            fis.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG,String.format("Le fichier %s n'existe pas",fileName));
        }catch (IOException e){
            Log.d(TAG,String.format("Erreur en lisant dans le fichier %s",fileName));
        }

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
