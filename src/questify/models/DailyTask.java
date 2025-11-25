package models;

public class DailyTask extends Task {
    private int streak;

    public DailyTask(String description, String difficulty) {
        super(description, difficulty);
        this.streak = 0;
    }

    public void endDayMaintenance() {
        if (isCompleted()) {
            this.streak++;
        } else {
            this.streak = 0;
        }

        setCompleted(false);
    }

    // --- Getters and Setters ---
    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    @Override
    public String toString() {
        String base = super.toString();
        return base + String.format(" | Streak: %d", this.streak);
    }
}