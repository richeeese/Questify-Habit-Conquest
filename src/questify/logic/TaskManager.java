package logic;

import models.Player;
import models.Task;
import models.DailyTask;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {

    private final List<Task> allTasks;

    public TaskManager(Player player) {
        this.allTasks = new ArrayList<>();
        addInitialTasks();
    }

    private void addInitialTasks() {
        addTask("Wake up by 8 AM", "Easy");
        addDailyTask("Read for 30 minutes", "Medium");
        addDailyTask("Exercise for 1 hour", "Hard");
        addTask("Finish Java Core Module", "Hard");
        addTask("Prepare presentation slides", "Medium");
        addTask("Clean out email inbox", "Easy");
    }

    // --- Public Task Management Methods ---

    // Overloaded to accept objects (used by initialization)
    public void addTask(Task task) { allTasks.add(task); } 
    
    // Overloaded to create and add base Task (To-Do)
    public void addTask(String description, String difficulty) {
        this.allTasks.add(new Task(description, difficulty));
    }
    
    // Creates and adds DailyTask
    public void addDailyTask(String description, String difficulty) {
        this.allTasks.add(new DailyTask(description, difficulty));
    }

    public List<Task> getAllTasks() { return allTasks; }

    public Task getTaskByIndex(int index) {
        if (index >= 0 && index < allTasks.size()) {
            return allTasks.get(index);
        }
        return null;
    }

    public Task toggleCompletion(int index) {
        Task task = getTaskByIndex(index);
        if (task != null) {
            task.toggleCompleted();
            return task;
        }
        return null;
    }
    
    // NEW: Removes a To-Do Task
    public void removeTask(int index) {
        if (index >= 0 && index < allTasks.size()) {
            Task task = allTasks.get(index);
            if (task instanceof DailyTask) {
                System.out.println("Cannot remove a Daily Task. It will reset at the end of the day.");
            } else {
                allTasks.remove(index);
                System.out.println("🗑️ To-Do Quest removed: " + task.getDescription());
            }
        } else {
            System.out.println("Invalid Quest Number for removal.");
        }
    }

    public List<DailyTask> getIncompleteDailyTasks() {
        return allTasks.stream()
                .filter(t -> t instanceof DailyTask)
                .map(t -> (DailyTask) t)
                .filter(t -> !t.isCompleted())
                .collect(Collectors.toList());
    }

    // --- End-of-Day Maintenance ---
    public void resetDailyTasks() {
        // 1. Run maintenance on all Dailies (resets completion, updates streak)
        for (Task task : allTasks) {
            if (task instanceof DailyTask) {
                ((DailyTask) task).endDayMaintenance();
            }
        }
        
        // 2. Cleanup: Remove completed To-dos (base Task objects)
        allTasks.removeIf(t -> !(t instanceof DailyTask) && t.isCompleted());
    }
}