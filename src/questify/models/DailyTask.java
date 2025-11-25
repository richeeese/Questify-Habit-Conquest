package models;

public class DailyTask extends Task {
    private int streak;
    
    public DailyTask(String description, String difficulty) {
        super(description, difficulty); // Pass arguments to the base Task constructor
        this.streak = 0; // Daily tasks start with a streak of 0
    }

    // End-of-day logic called by TaskManager
    public void endDayMaintenance() {
        if (isCompleted()) {
            this.streak++;
        } else {
            this.streak = 0;
        }
        
        // Always reset completion status for the new day
        setCompleted(false);
    }
    
    // --- Getters and Setters ---
    public int getStreak() { return streak; }
    public void setStreak(int streak) { this.streak = streak; }

    @Override
    public String toString() {
        String base = super.toString();
        // Custom format for ConsoleMenu (ListTasks uses its own formatting, but good to have)
        return base + String.format(" | Streak: %d", this.streak);
    }
}