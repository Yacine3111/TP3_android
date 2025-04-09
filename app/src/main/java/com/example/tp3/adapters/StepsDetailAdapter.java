package com.example.tp3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tp3.databinding.ItemStepDetailBinding;
import com.example.tp3.models.Step;

import java.util.List;
import java.util.Locale;

public class StepsDetailAdapter extends ArrayAdapter<Step> {

    public StepsDetailAdapter(Context context, List<Step> steps) {
        super(context, 0, steps);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItemStepDetailBinding binding;
        if (convertView == null) {
            binding = ItemStepDetailBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = binding.getRoot();
        } else {
            binding = ItemStepDetailBinding.bind(convertView);
        }

        Step step = getItem(position);
        if (step != null) {
            // Afficher le numéro de l'étape
            binding.textStepNumber.setText(String.format(Locale.getDefault(), "%d.", position + 1));

            // Afficher la description de l'étape
            binding.textStepDescription.setText(step.getDescription());
        }

        return convertView;
    }
}