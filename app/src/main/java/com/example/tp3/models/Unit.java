package com.example.tp3.models;

import androidx.annotation.NonNull;

public enum Unit {
    None("", null),
    Gram("g", true),
    Ounce("oz", false),
    Milliliter("ml", true),
    Liter("l", true),
    Cup("t", false),
    Teaspoon("c. à thé", false),
    Tablespoon("c. à soupe", false);

    private final String friendlyName;

    private final Boolean isMetric;

    Unit(String friendlyName, Boolean isMetric) {
        this.friendlyName = friendlyName;
        this.isMetric = isMetric;
    }

    @NonNull
    @Override
    public String toString() {
        return friendlyName;
    }

    public Boolean isMetric() {
        return isMetric;
    }
}
