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
            if (task instanceof DailyTask) {
                System.out.println("Cannot remove a Daily Task. It will reset at the end of the day.");
            } else {
                allTasks.remove(index);
                System.out.println("🗑️ To-Do Quest removed: " + task.getDescription());
            }
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
        for (Task task : allTasks) {
            if (task instanceof DailyTask) {
                ((DailyTask) task).endDayMaintenance();
            }
        }

        // Cleanup: Remove completed To-dos
        allTasks.removeIf(t -> !(t instanceof DailyTask) && t.isCompleted());
    }
}