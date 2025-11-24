package questify.models;

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

    // Getter for GameEngine's End Day check
    public boolean isCompletedToday() {
        return completedToday;
    }
}