package com.example.tp3.models;

public class Step {
    private int Index;
    private String Description;

    public Step(int index, String description) {
        Index = index;
        Description = description;
    }
    
    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
