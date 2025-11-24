package com.questify.models;

public class Task {
    private String description;
    private boolean completed;
    // ... (rest of methods)
    
    // You should add the attributes needed for Questify, e.g.,
    private int expEasy = 5;
    private int expMedium = 10;
    private int expHard = 15; // Default reward
    
    // Add public getters for models
    public int getExpReward() {
        return expReward;
    }
}