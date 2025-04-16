package com.example.tp3.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.example.tp3.databinding.ItemIngredientDetailBinding;
import com.example.tp3.models.Ingredient;
import com.example.tp3.models.Unit;

import java.util.List;
import java.util.Locale;

public class IngredientsDetailAdapter extends ArrayAdapter<Ingredient> {
    private final boolean useMetric;
    private final int prefPortions;
    private final int recipePortions;


    public IngredientsDetailAdapter(Context context, List<Ingredient> ingredients, int recipePortions) {
        super(context, 0, ingredients);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        // Récupérer les préférences d'unités
        useMetric = sharedPreferences.getString("unite_type", "").equals("metric");
        prefPortions = sharedPreferences.getInt("portions",1);

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

        quantity = prefPortions*quantity/recipePortions;

        if(ingredient.getUnit()==Unit.None){
            return " "+quantity;
        }
        else if(useMetric){
            switch (unitString){
                case"oz":
                    converte(28,Unit.Gram,Unit.Gram.toString(),quantity,ingredient);
                    break;

                case "c. à thé" :
                    converte(5,Unit.Milliliter,Unit.Milliliter.toString(),quantity,ingredient);
                    break;

                case "c. à soupe":
                    converte(15,Unit.Milliliter,Unit.Milliliter.toString(),quantity,ingredient);
                    break;
                case "t":
                    if(quantity<=2){
                        converte(284,Unit.Milliliter,Unit.Milliliter.toString(),quantity,ingredient);
                    }else{
                        converte(3.52,Unit.Liter,Unit.Liter.toString(),quantity,ingredient);
                    }
                    break;
            }
        }
        else{
            switch (unitString){
                case "g":
                   converte(28,Unit.Ounce,Unit.Ounce.toString(),quantity,ingredient);
                    break;
                case "ml":
                        if(quantity<=15){
                            converte(5,Unit.Teaspoon,Unit.Teaspoon.toString(),quantity,ingredient);
                        } else if (quantity<=60) {
                            converte(15,Unit.Tablespoon,Unit.Tablespoon.toString(),quantity,ingredient);
                        }else{
                            converte(284,Unit.Cup,Unit.Cup.toString(),quantity,ingredient);
                        }
                    break;
                case "l":
                    converte(3.52,Unit.Cup,Unit.Cup.toString(),quantity,ingredient);
            }
        }


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

    public void converte(double ratio, Unit unit,String unitString,double quantity, Ingredient ingredient){
        ingredient.setUnit(unit);
        unitString = unit.toString();

        //litre est le seul qu'on doit multiplier pour les metric je vérifie donc si
        //on convertit à partir de litre.
        //je prend le ratio pour savoir si on convertit avec des litres, car on passe
        //les nouvelle unit en param on ne peut donc pas vérifier avec unit
        if (ratio == 3.52) {
            quantity = useMetric ? quantity / ratio : quantity * ratio;
        } else {
            quantity = useMetric ? quantity * ratio : quantity / ratio;
        }
        ingredient.setQuantity(quantity);
    }
}
