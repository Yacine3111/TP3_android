package com.example.tp3.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tp3.adapters.IngredientsCreateAdapter;
import com.example.tp3.adapters.StepsCreateAdapter;
import com.example.tp3.databinding.FragmentRecipeCreateBinding;
import com.example.tp3.models.Ingredient;
import com.example.tp3.models.Recipe;
import com.example.tp3.models.Step;
import com.example.tp3.models.Unit;
import com.example.tp3.utils.RecipeStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeCreateFragment extends Fragment {

    private List<Ingredient> ingredients;
    private List<Step> steps;
    private IngredientsCreateAdapter ingredientsAdapter;
    private StepsCreateAdapter stepsAdapter;
    private RecipeStorage recipeStorage;
    private FragmentRecipeCreateBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();

        recipeStorage = new RecipeStorage(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeCreateBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialiser les adaptateurs
        ingredientsAdapter = new IngredientsCreateAdapter(requireContext(), ingredients);
        binding.listIngredients.setAdapter(ingredientsAdapter);

        stepsAdapter = new StepsCreateAdapter(requireContext(), steps);
        binding.listSteps.setAdapter(stepsAdapter);

        // Configurer les boutons
        binding.btnAddIngredient.setOnClickListener(v -> addIngredient());
        binding.btnAddStep.setOnClickListener(v -> addStep());
        binding.btnSaveRecipe.setOnClickListener(v -> saveRecipe());

        int portions = 1;
        binding.textPortions.setText(String.format(Locale.getDefault(), "(pour %d portions)", portions));
    }

    private void addIngredient() {
        Ingredient newIngredient = new Ingredient(
                "",
                0.0,
                Unit.None
        );

        ingredients.add(0, newIngredient);
        ingredientsAdapter.notifyDataSetChanged();
    }

    private void addStep() {
        // Créer une nouvelle étape vide
        int newIndex = steps.size() + 1;

        // Comme la classe Step n'est pas complète dans le code fourni,
        // nous allons créer une instance en supposant qu'elle a un constructeur approprié
        Step newStep = new Step(newIndex, "");

        steps.add(newStep);
        stepsAdapter.notifyDataSetChanged();
    }

    private void saveRecipe() {
        String recipeName = binding.editRecipeName.getText().toString().trim();

        if (recipeName.isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez entrer un nom pour la recette", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ingredients.isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez ajouter au moins un ingrédient", Toast.LENGTH_SHORT).show();
            return;
        }

        if (steps.isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez ajouter au moins une étape", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérifier si tous les ingrédients ont un nom
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName() == null || ingredient.getName().isEmpty()) {
                Toast.makeText(requireContext(), "Tous les ingrédients doivent avoir un nom", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        int portions = 1;
        // Créer la recette
        Recipe recipe = new Recipe(recipeName, portions, ingredients, steps);

        // Sauvegarder la recette dans le stockage interne
        boolean saved = recipeStorage.saveRecipe(recipe);

        if (saved) {
            Toast.makeText(requireContext(), "Recette enregistrée avec succès", Toast.LENGTH_SHORT).show();
            // Naviguer vers la liste des recettes ou fermer le fragment
            requireActivity().onBackPressed();
        } else {
            Toast.makeText(requireContext(), "Erreur lors de l'enregistrement de la recette", Toast.LENGTH_SHORT).show();
        }
    }
}