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
        // Default tasks removed as requested. Game starts empty.
    }

    // --- Public Task Management Methods ---

    public void addTask(Task task) {
        allTasks.add(task);
    }

    public void addTask(String description, String difficulty) {
        this.allTasks.add(new Task(description, difficulty));
    }

    public void addDailyTask(String description, String difficulty) {
        this.allTasks.add(new DailyTask(description, difficulty));
    }

    public List<Task> getAllTasks() {
        return allTasks;
    }

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

    public void removeTask(int index) {
        if (index >= 0 && index < allTasks.size()) {
            Task task = allTasks.get(index);
            allTasks.remove(index);
            System.out.println("🗑️ Quest removed: " + task.getDescription());
        } else {
            System.out.println("Invalid Quest Number.");
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
        // 1. Handle Daily Tasks (Reset them, don't delete them)
        for (Task task : allTasks) {
            if (task instanceof DailyTask) {
                // This updates streaks and sets completed = false
                ((DailyTask) task).endDayMaintenance();
            }
        }

        // 2. Handle To-Do Tasks (Delete them if they are finished)
        // "Remove if it is NOT a DailyTask AND it IS Completed"
        allTasks.removeIf(t -> !(t instanceof DailyTask) && t.isCompleted());

        // OPTIONAL: If you want ALL To-Dos to vanish (even failed ones), use this
        // instead:
        // allTasks.removeIf(t -> !(t instanceof DailyTask));
    }
}