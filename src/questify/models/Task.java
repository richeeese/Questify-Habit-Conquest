package models;

import java.io.Serializable;
import java.util.UUID;

public class Task implements Serializable {

    private final String id;
    private String description;
    private boolean completed;
    private final String difficulty;
    private final int expReward;

    public Task(String description, String difficulty) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.completed = false;
        this.difficulty = difficulty;
        this.expReward = calculateExpReward(difficulty);
    }

    private int calculateExpReward(String difficulty) {
        return switch (difficulty.toLowerCase()) {
            case "medium" -> 10;
            case "hard" -> 15;
            default -> 5; // Easy
        };
    }

    public void toggleCompleted() {
        this.completed = !this.completed;
    }

    // --- Getters and Setters ---
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getExpReward() {
        return expReward;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s - EXP: %d)",
                this.completed ? "X" : " ",
                this.description,
                this.difficulty,
                this.expReward);
    }
}