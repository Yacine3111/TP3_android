package com.example.tp3.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.tp3.adapters.IngredientsDetailAdapter;
import com.example.tp3.adapters.StepsDetailAdapter;
import com.example.tp3.databinding.FragmentRecipeDetailBinding;
import com.example.tp3.models.Recipe;
import com.example.tp3.utils.RecipeStorage;

import java.util.Locale;
import java.util.UUID;

public class RecipeDetailFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_id";

    private Recipe recipe;
    private RecipeStorage recipeStorage;
    private @NonNull FragmentRecipeDetailBinding binding;
    private int portions;
    private SharedPreferences sharedPrefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeStorage = new RecipeStorage(requireContext());

        // Récupérer l'ID de la recette à afficher
        if (getArguments() != null && getArguments().containsKey(ARG_RECIPE_ID)) {
            String recipeIdStr = getArguments().getString(ARG_RECIPE_ID);
            if (recipeIdStr != null) {
                try {
                    UUID recipeId = UUID.fromString(recipeIdStr);
                    recipe = recipeStorage.loadRecipe(recipeId);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(requireContext(), "ID de recette invalide", Toast.LENGTH_SHORT).show();
                }
            }
        }

        // Si la recette n'a pas pu être chargée, sortir du fragment
        if (recipe == null) {
            Toast.makeText(requireContext(), "Impossible de charger la recette", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Assurer que la recette existe
        if (recipe == null) {
            return;
        }

        // Configurer le nom de la recette
        binding.textRecipeName.setText(recipe.getName());

        // Configurer la liste des ingrédients
        IngredientsDetailAdapter ingredientAdapter = new IngredientsDetailAdapter(
                requireContext(),
                recipe.getIngredients(),
                recipe.getPortions()
        );
        binding.listIngredients.setAdapter(ingredientAdapter);

        // Configurer la liste des étapes
        StepsDetailAdapter stepAdapter = new StepsDetailAdapter(
                requireContext(),
                recipe.getSteps()
        );
        binding.listSteps.setAdapter(stepAdapter);

        // Ajuster la hauteur des ListView pour afficher tous les éléments
        setListViewHeightBasedOnChildren(binding.listIngredients);
        setListViewHeightBasedOnChildren(binding.listSteps);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        portions = sharedPrefs.getInt("portions", 4);
        binding.textPortions.setText(String.format(Locale.getDefault(), "(pour %d portions)", portions));
    }

    /**
     * Méthode utilitaire pour ajuster la hauteur d'une ListView en fonction de ses enfants
     * Cela permet d'afficher tous les éléments dans un ScrollView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView.getAdapter() == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            View listItem = listView.getAdapter().getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listView.getAdapter().getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}