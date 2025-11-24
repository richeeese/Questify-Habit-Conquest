package logic;

import models.Task;
import models.DailyTask;
import java.util.ArrayList;
import java.util.List;

// TaskManager is now a non-static instance class
public class TaskManager {

    // Private list holds both Task and DailyTask objects due to inheritance
    private ArrayList<Task> tasks = new ArrayList<>();

    // READ / LIST (Returns the list for the UI/Engine to process)
    public List<Task> getAllTasks() {
        return tasks; // Return the list
    }

    // ---------------------- CREATE METHODS ----------------------

    public void addTask(String description, String difficulty) {
        if (description != null && !description.trim().isEmpty()) {
            // Pass description and difficulty to the Task constructor
            tasks.add(new Task(description, difficulty));
        }
    }
    // Creates a DailyTask. Requires difficulty.

    public void addDailyTask(String description, String difficulty) {
        if (description != null && !description.trim().isEmpty()) {
            // Use the DailyTask constructor
            tasks.add(new DailyTask(description, difficulty));
        }
    }

    // ---------------------- READ & TOGGLE ----------------------

    // READ: Get a specific task (used by Engine/UI)
    public Task getTaskByIndex(int index) {
        // Use 0-based index for internal array list
        if (index >= 0 && index < tasks.size()) {
            return tasks.get(index);
        }
        return null;
    }

    /**
     * Toggles completion and returns the Task object.
     * This is crucial because the GameEngine needs the Task's details
     * (like difficulty/EXP) to calculate rewards.
     * 
     * @return The Task object that was toggled, or null if the index was invalid.
     */
    public Task toggleCompletion(int index) {
        Task task = getTaskByIndex(index);
        if (task != null) {
            task.toggleCompleted();
            // Return the task object for the GameEngine to process EXP/damage
            return task;
        }
        return null;
    }

    public boolean updateTask(int index, String newDescription) {
        Task task = getTaskByIndex(index);
        if (task != null) {
            task.setDescription(newDescription);
            return true;
        }
        return false;
    }

    // DELETE
    public boolean deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            return true;
        }
        return false;
    }

    public List<DailyTask> getIncompleteDailyTasks() {
        List<DailyTask> incompleteDailies = new ArrayList<>();

        for (Task task : tasks) {
            // 1. Check if the object is an instance of the DailyTask subclass
            // 2. Check if the task is NOT completed
            if (task instanceof DailyTask && !task.isCompleted()) {
                // Safely cast the general Task object to the specific DailyTask type
                incompleteDailies.add((DailyTask) task);
            }
        }
        return incompleteDailies;
    }

    // Helper for GameEngine's endDay logic: resets all dailies for a new day.

    public void resetDailyTasks() {
        for (Task task : tasks) {
            if (task instanceof DailyTask) {
                DailyTask daily = (DailyTask) task;
                // Run the maintenance logic for streaks
                daily.endDayMaintenance();
            }
        }
    }
}