package com.example.tp3.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tp3.databinding.ItemRecipeBinding;
import com.example.tp3.models.Recipe;
import com.example.tp3.utils.RecipeStorage;

import java.util.List;
import java.util.Locale;

public class RecipesAdapter extends ArrayAdapter<Recipe> {

    private final RecipeStorage recipeStorage;
    private final OnRecipeDeletedListener deleteListener;

    private final OnRecipeClickedListener clickListener;

    public RecipesAdapter(Context context, List<Recipe> recipes, RecipeStorage recipeStorage, OnRecipeClickedListener clickListener, OnRecipeDeletedListener deleteListener) {
        super(context, 0, recipes);
        this.recipeStorage = recipeStorage;
        this.deleteListener = deleteListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItemRecipeBinding binding;
        if (convertView == null) {
            binding = ItemRecipeBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = binding.getRoot();
        } else {
            binding = ItemRecipeBinding.bind(convertView);
        }

        Recipe recipe = getItem(position);
        if (recipe != null) {


            // Configurer le nom de la recette
            binding.textRecipeName.setText(recipe.getName());

            // Configurer les informations supplémentaires
            int ingredientCount = recipe.getIngredients() != null ? recipe.getIngredients().size() : 0;
            int stepCount = recipe.getSteps() != null ? recipe.getSteps().size() : 0;
            String infoText = String.format(Locale.getDefault(),
                    "%d portion%s, %d ingrédient%s, %d étape%s",
                    recipe.getPortions(), recipe.getPortions() > 1 ? "s" : "",
                    ingredientCount, ingredientCount > 1 ? "s" : "",
                    stepCount, stepCount > 1 ? "s" : "");
            binding.textRecipeInfo.setText(infoText);

            // Configurer le bouton de suppression
            binding.btnDeleteRecipe.setOnClickListener(v -> confirmDeleteRecipe(recipe));
            binding.getRoot().setOnClickListener(v -> clickListener.onRecipeClicked(recipe));
        }

        return convertView;
    }

    private void confirmDeleteRecipe(Recipe recipe) {
        new AlertDialog.Builder(getContext())
                .setTitle("Supprimer la recette")
                .setMessage("Êtes-vous sûr de vouloir supprimer la recette \"" + recipe.getName() + "\" ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    // Supprimer la recette
                    boolean deleted = recipeStorage.deleteRecipe(recipe.getId());
                    if (deleted) {
                        // Retirer de l'adaptateur
                        remove(recipe);
                        notifyDataSetChanged();

                        // Notifier le fragment que la suppression a eu lieu
                        if (deleteListener != null) {
                            deleteListener.onRecipeDeleted(recipe);
                        }

                        Toast.makeText(getContext(), "Recette supprimée", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    public interface OnRecipeDeletedListener {
        void onRecipeDeleted(Recipe recipe);
    }

    public interface OnRecipeClickedListener {
        void onRecipeClicked(Recipe recipe);
    }
}

