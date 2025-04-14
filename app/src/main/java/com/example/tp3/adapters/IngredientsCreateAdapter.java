package com.example.tp3.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.example.tp3.R;
import com.example.tp3.databinding.ActivityMainBinding;
import com.example.tp3.databinding.FragmentRecipeCreateBinding;
import com.example.tp3.databinding.ItemIngredientCreateBinding;
import com.example.tp3.models.Ingredient;
import com.example.tp3.models.Unit;

import java.util.List;

// Adapter personnalisé pour les ingrédients
public class IngredientsCreateAdapter extends ArrayAdapter<Ingredient> {

    private final boolean useMetric;

    private ItemIngredientCreateBinding binding;
    public IngredientsCreateAdapter(Context context, List<Ingredient> ingredients) {
        super(context, 0, ingredients);

        // Récupérer les préférences d'unités
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        useMetric = sharedPreferences.getString("unite_type", "").equals("metric");
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ingredient_create, parent, false);
        }

        binding = ItemIngredientCreateBinding.bind(convertView);

        EditText editIngredientName = binding.editIngredientName;
        EditText editIngredientQuantity = binding.editIngredientQuantity;
        Spinner spinnerUnit = binding.spinnerUnit;
        ImageButton btnDeleteIngredient = binding.btnDeleteIngredient;

        Ingredient ingredient = getItem(position);
        if (ingredient != null) {
            // Configurer le nom
            editIngredientName.setText(ingredient.getName());
            editIngredientName.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    ingredient.setName(editIngredientName.getText().toString());
                }
            });

            // Configurer la quantité
            String quantityStr = ingredient.getQuantity() > 0 ? String.valueOf(ingredient.getQuantity()) : "";
            editIngredientQuantity.setText(quantityStr);
            editIngredientQuantity.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    try {
                        if (!editIngredientQuantity.getText().toString().isEmpty()) {
                            ingredient.setQuantity(Double.parseDouble(editIngredientQuantity.getText().toString()));
                        } else {
                            ingredient.setQuantity(0);
                        }
                    } catch (NumberFormatException e) {
                        ingredient.setQuantity(0);
                    }
                }
            });

            // Configurer le spinner d'unité selon les unités choisies
            updateUnitSpinner(spinnerUnit);

            // Configurer le choix de l'unité de l'ingrédient
            spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Unit unit = (Unit) parent.getItemAtPosition(position);
                    ingredient.setUnit(unit);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // Configurer le bouton de suppression
            btnDeleteIngredient.setOnClickListener(v -> {
                Ingredient ingredientToDelete = getItem(position);
                remove(ingredientToDelete);
                notifyDataSetChanged();
            });
        }

        return convertView;
    }

    private void updateUnitSpinner(Spinner spinner) {
        ArrayAdapter<Unit> unitAdapter;

        if (useMetric) {
            unitAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    new Unit[]{Unit.None, Unit.Gram, Unit.Milliliter, Unit.Liter}
            );
        } else {
            unitAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    new Unit[]{Unit.None, Unit.Ounce, Unit.Teaspoon, Unit.Tablespoon, Unit.Cup}
            );
        }

        spinner.setAdapter(unitAdapter);
    }
}
