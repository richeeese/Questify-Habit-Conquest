package logic;

import models.Player;
import models.Task;
import models.DailyTask;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {

    // references to player quest log
    private Player player;

    public TaskManager(Player player) {
        this.player = player;
    }

    // --- Public Task Management Methods ---

    public void addTask(Task task) {
        getTasks().add(task);
    }

    public void addTask(String description, String difficulty) {
        getTasks().add(new Task(description, difficulty));
    }

    public void addDailyTask(String description, String difficulty) {
        getTasks().add(new DailyTask(description, difficulty));
    }

    public List<Task> getTasks() {
        return player.getQuestLog();
    }

    public List<Task> getAllTasks() {
        return getTasks();
    }

    public Task getTaskByIndex(int index) {
        if (index >= 0 && index < getTasks().size()) {
            return getTasks().get(index);
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
        if (index >= 0 && index < getTasks().size()) {
            Task task = getTasks().get(index);
            getTasks().remove(index);
            System.out.println("🗑️ Quest removed: " + task.getDescription());
        } else {
            System.out.println("Invalid Quest Number.");
        }
    }

    public List<DailyTask> getIncompleteDailyTasks() {
        return getTasks().stream()
                .filter(t -> t instanceof DailyTask)
                .map(t -> (DailyTask) t)
                .filter(t -> !t.isCompleted())
                .collect(Collectors.toList());
    }

    // --- End-of-Day Maintenance ---
    public void resetDailyTasks() {
        // 1. Handle Daily Tasks
        for (Task task : getTasks()) {
            if (task instanceof DailyTask) {
                // This updates streaks and sets completed = false
                ((DailyTask) task).endDayMaintenance();
            }
        }

        // 2. Handle To-Do Tasks (Delete them if they are finished)
        getTasks().removeIf(t -> !(t instanceof DailyTask) && t.isCompleted());
    }
}