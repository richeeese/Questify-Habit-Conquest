package models;

import java.util.UUID;
public class Task {
    
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
        switch (difficulty.toLowerCase()) {
            case "medium": return 10;
case "hard": return 15;
            default: return 5; 
        }
    }
    
    public void toggleCompleted() {
        this.completed = !this.completed;
}
    
    public String getId() { return id;
}
    public String getDescription() { return description;
}
    public void setDescription(String newDescription) { this.description = newDescription;
}
    public boolean isCompleted() { return completed;
}
    public void setCompleted(boolean completed) { this.completed = completed;
}
    public String getDifficulty() { return difficulty; }
    public int getExpReward() { return expReward;
}
}