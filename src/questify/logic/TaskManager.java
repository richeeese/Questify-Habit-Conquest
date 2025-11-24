package logic;

import models.Task; // Corrected import
import models.DailyTask; // Corrected import
import models.Player; // Corrected import
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages the collection of all tasks (To-dos and Dailies).
 * Handles adding, retrieving, toggling, and end-of-day maintenance.
 */
public class TaskManager {

    private final List<Task> allTasks;


    /**
     * Initializes the TaskManager and populates the initial task list.
 * @param player The active player (required for Main.java dependency structure).
 */
    public TaskManager(Player player) {
        this.allTasks = new ArrayList<>();

        addInitialTasks();
    }

    // --- Private Initialization ---

    private void addInitialTasks() {
        // Initial Daily Tasks
        addTask(new DailyTask("Wake up by 8 AM", "Easy"));
addTask(new DailyTask("Read for 30 minutes", "Medium"));
        addTask(new DailyTask("Exercise for 1 hour", "Hard"));
// Initial To-do Tasks (Uses base Task class)
        addTask(new Task("Finish Java Core Module", "Hard"));
addTask(new Task("Prepare presentation slides", "Medium"));
        addTask(new Task("Clean out email inbox", "Easy"));
}

    // --- Public Task Management Methods ---

    /**
     * Adds a new Task (Daily or To-do) to the master list.
 * @param task The Task object to add.
     */
    public void addTask(Task task) { allTasks.add(task);
}

    /**
     * @return The full list of all active tasks.
 */
    public List<Task> getAllTasks() { return allTasks;
}

    /**
     * Retrieves a task by its index in the internal list.
 * @param index The 0-based index of the task.
     * @return The Task object or null if the index is out of bounds.
 */
    public Task getTaskByIndex(int index) {
        if (index >= 0 && index < allTasks.size()) {
            return allTasks.get(index);
}
        return null;
}

    /**
     * Toggles the completion status of a task using its index.
 * @param index The 0-based index of the task to toggle.
 * @return The Task object that was toggled, or null if invalid index.
 */
    public Task toggleCompletion(int index) {
        Task task = getTaskByIndex(index);
if (task != null) {
            task.toggleCompleted();
            return task;
// Returned to GameEngine for reward processing
        }
        return null;
}

    /**
     * Finds all DailyTasks that were *not* completed (used for penalties).
 * @return A list of incomplete DailyTask objects.
     */
    public List<DailyTask> getIncompleteDailyTasks() {
        return allTasks.stream()
                .filter(t -> t instanceof DailyTask)
                .map(t -> (DailyTask) t)
                .filter(t -> !t.isCompleted())
                .collect(Collectors.toList());
}

    // --- End-of-Day Maintenance ---

    /**
     * Resets the completion status, runs streak maintenance on Dailies, 
     * and removes completed To-dos.
 */
    public void resetDailyTasks() {
        // 1. Run maintenance on all Dailies (resets completion, updates streak)
        for (Task task : allTasks) {
            if (task instanceof DailyTask) {
                // Cast and call the specific DailyTask maintenance method
                ((DailyTask) task).endDayMaintenance();
}
        }
        
        // 2. Cleanup: Remove completed To-dos (base Task objects)
        // Checks: NOT a DailyTask AND isCompleted
        allTasks.removeIf(t -> !(t instanceof DailyTask) && t.isCompleted());
}
}