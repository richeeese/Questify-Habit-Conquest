package models;

public class DailyTask extends Task {
    private int streak;
    
    public DailyTask(String description, int expReward, int initialStreak) {
        super(description, expReward);
        this.streak = initialStreak;
    }

    // Streak bonus damage for boss combat (though passive combat is now removed, this could be a future bonus)
    public int getStreakBonusDamage() {
        return streak * 2;
    }

    // --- Getters and Setters ---
    public int getStreak() { return streak; }
    public void setStreak(int streak) { this.streak = streak; }
}