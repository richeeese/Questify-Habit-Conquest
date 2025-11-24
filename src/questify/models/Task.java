package models;

public class Task {

    // --- 1. ATTRIBUTES: Add the missing 'difficulty' field ---
    private String description;
    private boolean completed;
    private String difficulty; // NEW: The difficulty string for this specific task

    // --- 2. CONSTANTS: Define the reward values ---
    private final int expEasy = 5;
    private final int expMedium = 10;
    private final int expHard = 15;

    // --- 3. CONSTRUCTOR: Correctly initializes all fields ---
    public Task(String description, String difficulty) {
        this.description = description;
        this.completed = false; // Task starts incomplete
        this.difficulty = difficulty.toLowerCase();
    }

    // --- 4. METHODS: Implement Logic and Getters ---

    // Getter for the Task description
    public String getDescription() {
        return description;
    }

    // Setter for the description (used by TaskManager's update method)
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter for completion status
    public boolean isCompleted() {
        return completed;
    }

    // Setter for completion status (used by DailyTask or TaskManager for cleanup)
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // Method to change completion status
    public void toggleCompleted() {
        this.completed = !this.completed;
    }

    /**
     * Calculates and returns the EXP reward based on difficulty.
     * FIX: This method must return an 'int', not a 'boolean' (which 'completed'
     * is).
     */
    public int getExpReward() {
        switch (this.difficulty) {
            case "easy":
                return expEasy;
            case "hard":
                return expHard;
            // Defaults to medium for any other input
            case "medium":
            default:
                return expMedium;
        }
    }

    // Override toString for clean list display in the UI
    @Override
    public String toString() {
        String status = completed ? " [Done]" : " [Pending]";
        return description + status + " (" + difficulty.toUpperCase() + ")";
    }
}