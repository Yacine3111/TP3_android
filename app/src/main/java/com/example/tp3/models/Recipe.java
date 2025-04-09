package com.example.tp3.models;

import java.util.List;
import java.util.UUID;

public class Recipe {
    private UUID id;
    private String name;
    private int portions;

    private List<Ingredient> ingredients;
    private List<Step> steps;

    public Recipe(String name, int portions, List<Ingredient> ingredients, List<Step> steps) {
        this.portions = portions;
        id = UUID.randomUUID();
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }
}


