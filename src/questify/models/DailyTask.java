package models;

// 'extends' to inherit from Task 
public class DailyTask extends Task {

    // unique attributes for DailyTask
    private int streak;
    private boolean completedToday; // Tracks if it was completed since the last 'End Day'

    // Constructor
    public DailyTask(String description, String difficulty) {
        // Call the parent (Task) constructor to initialize description and difficulty
        super(description, difficulty);
        this.streak = 0;
        this.completedToday = false;
    }

    // 1. Override the parent's method to add daily specific logic
    @Override
    public void toggleCompleted() {
        super.toggleCompleted(); // Calls the parent method to flip the 'completed' state
        this.completedToday = super.isCompleted(); // Update the daily tracker
    }

    /*
     * Called by the GameEngine at the start of a new day.
     * Handles streak increment or reset and prepares for the next day.
     */
    public void endDayMaintenance() {
        if (this.completedToday) {
            // Success: Increment the streak
            this.streak++;
        } else {
            // Failure: Reset the streak
            this.streak = 0;
        }

        // Reset the daily tracker and inherited 'completed' status for the new day
        this.completedToday = false;
        super.setCompleted(false); // Task must be set incomplete for the new day
    }

    // --- Getters for Logic/UI ---

    /**
     * Calculates the bonus damage/reward based on the current streak.
     * Since the cap was removed, it returns the full streak value.
     */
    public int getStreakBonusDamage() {
        // Returns the full streak as the bonus, as requested
        return this.streak;
    }

    // Getter for GameEngine's End Day check
    public boolean isCompletedToday() {
        return completedToday;
    }

    // Getter for the streak itself
    public int getStreak() {
        return streak;
    }

    // Override toString for enhanced display in the UI
    @Override
    public String toString() {
        // Uses the parent's toString and adds the streak info
        return super.toString() + " | Streak: " + this.streak + " (Bonus: +" + getStreakBonusDamage() + ")";
    }
}