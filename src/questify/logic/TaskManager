package src.com.questify.logic;

import src.com.questify.models.Task;
import java.util.ArrayList;
import java.util.List; // Prefer List for method signatures

// TaskManager is now a non-static instance class
public class TaskManager {

    private ArrayList<Task> tasks = new ArrayList<>();

    // READ / LIST (Returns the list for the UI/Engine to process)
    public List<Task> getAllTasks() {
        return tasks; // Return the list
    }

    // CREATE
    // Takes the description as an argument, allowing the UI to handle input
    public void addTask(String description) {
        // You can add validation/logic here, like checking for duplicates
        if (description != null && !description.trim().isEmpty()) {
            tasks.add(new Task(description));
        }
    }

    // READ: Get a specific task (used by Engine/UI)
    public Task getTaskByIndex(int index) {
        if (index >= 0 && index < tasks.size()) {
            return tasks.get(index);
        }
        return null;
    }

    // TOGGLE COMPLETION (Requires an index/ID to find the task)
    public boolean toggleCompletion(int index) {
        Task task = getTaskByIndex(index);
        if (task != null) {
            task.toggleCompleted();
            // In Questify, this is where you'd return the task 
            // to the GameEngine for EXP calculation.
            return true;
        }
        return false;
    }

    // UPDATE
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
    
    // QUESTIFY SPECIFIC: Helper for GameEngine
    public List<Task> getIncompleteDailyTasks() {
        // Since you only have Task for now, this would just return incomplete tasks.
        // Later, you'd filter for DailyTask objects.
        List<Task> incomplete = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                incomplete.add(task);
            }
        }
        return incomplete;
    }
}