package com.example.tp3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tp3.databinding.ItemIngredientDetailBinding;
import com.example.tp3.models.Ingredient;

import java.util.List;
import java.util.Locale;

public class IngredientsDetailAdapter extends ArrayAdapter<Ingredient> {
    private final boolean useMetric;
    private final int prefPortions;
    private final int recipePortions;

    public IngredientsDetailAdapter(Context context, List<Ingredient> ingredients, int recipePortions) {
        super(context, 0, ingredients);

        // Récupérer les préférences d'unités
        useMetric = true;
        prefPortions = 1;

        // Récupérer la portion de la recette
        this.recipePortions = recipePortions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItemIngredientDetailBinding binding;
        if (convertView == null) {
            binding = ItemIngredientDetailBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = binding.getRoot();
        } else {
            binding = ItemIngredientDetailBinding.bind(convertView);
        }

        Ingredient ingredient = getItem(position);
        if (ingredient != null) {
            // Afficher le nom de l'ingrédient
            binding.textIngredientName.setText(ingredient.getName());

            // Formatter la quantité avec l'unité
            String quantityText = formatQuantity(ingredient);

            binding.textIngredientQuantity.setText(quantityText);
        }

        return convertView;
    }

    private String formatQuantity(Ingredient ingredient) {
        // Si aucune quantité, retourner chaîne vide
        if (ingredient.getQuantity() <= 0) {
            return "";
        }

        String unitString = ingredient.getUnit().toString();
        double quantity = ingredient.getQuantity();

        // CONVERSION ICI

        // Formatter la quantité (avec ou sans décimales selon besoin)
        String quantityFormatted;
        if (quantity == Math.floor(quantity)) {
            quantityFormatted = String.format(Locale.getDefault(), "%.0f", quantity);
        } else {
            quantityFormatted = String.format(Locale.getDefault(), "%.1f", quantity);
        }

        // Retourner la quantité formatée avec l'unité
        return quantityFormatted + (unitString.isEmpty() ? "" : " " + unitString);
    }
}
