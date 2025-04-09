package com.example.tp3.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.tp3.R;
import com.example.tp3.adapters.RecipesAdapter;
import com.example.tp3.databinding.FragmentRecipesBinding;
import com.example.tp3.models.Recipe;
import com.example.tp3.utils.RecipeStorage;

import java.util.List;

public class RecipesFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_id";
    private RecipesAdapter recipesAdapter;
    private List<Recipe> recipes;
    private RecipeStorage recipeStorage;
    private @NonNull FragmentRecipesBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeStorage = new RecipeStorage(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Charger les recettes
        loadRecipes();

        // Configurer le bouton d'ajout de recette
        binding.fabAddRecipe.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_to_create_recipe);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recharger les recettes à chaque retour au fragment
        loadRecipes();
    }

    private void loadRecipes() {
        // Récupérer toutes les recettes du stockage
        recipes = recipeStorage.getAllRecipes();

        // Mettre à jour l'affichage en fonction du nombre de recettes
        if (recipes.isEmpty()) {
            binding.textEmptyRecipes.setVisibility(View.VISIBLE);
            binding.listRecipes.setVisibility(View.GONE);
        } else {
            binding.textEmptyRecipes.setVisibility(View.GONE);
            binding.listRecipes.setVisibility(View.VISIBLE);

            // Configurer l'adaptateur
            recipesAdapter = new RecipesAdapter(requireContext(), recipes, recipeStorage,
                    this::navigateToRecipeDetail,
                    recipeToBeDeleted -> {
                    });
            binding.listRecipes.setAdapter(recipesAdapter);
        }
    }

    private void navigateToRecipeDetail(Recipe recipe) {
        Bundle args = new Bundle();
        args.putString(ARG_RECIPE_ID, recipe.getId().toString());

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_recipe_detail, args);
    }
}