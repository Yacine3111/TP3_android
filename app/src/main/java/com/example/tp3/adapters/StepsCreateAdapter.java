package com.example.tp3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tp3.R;
import com.example.tp3.databinding.ItemStepCreateBinding;
import com.example.tp3.models.Step;

import java.util.List;
import java.util.Locale;

public class StepsCreateAdapter extends ArrayAdapter<Step> {
    private ItemStepCreateBinding binding;
    public StepsCreateAdapter(Context context, List<Step> steps) {
        super(context, 0, steps);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_step_create, parent, false);
        }
        binding=ItemStepCreateBinding.bind(convertView);

        TextView textStepNumber = binding.textStepNumber;
        EditText editStepDescription = binding.editStepDescription;
        ImageButton btnDeleteStep = binding.btnDeleteStep;

        Step step = getItem(position);
        if (step != null) {
            // Configurer le numéro d'étape
            textStepNumber.setText(String.format(Locale.getDefault(), "Étape %d", position + 1));

            // Configurer la description
            editStepDescription.setText(step.getDescription());
            editStepDescription.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    step.setDescription(editStepDescription.getText().toString());
                }
            });
            // Configurer le bouton de suppression
            btnDeleteStep.setOnClickListener(v -> {
                Step stepToDelete = getItem(position);
                remove(stepToDelete);
                // Mettre à jour les indices
                for (int i = position; i < getCount(); i++) {
                    Step item = getItem(i);
                    if (item != null) {
                        item.setIndex(i + 1);
                    }
                }
                notifyDataSetChanged();
            });
        }
        return convertView;
    }
}
