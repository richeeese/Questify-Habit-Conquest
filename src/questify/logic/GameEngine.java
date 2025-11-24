package logic;

import models.Player;
import models.Task;
import models.DailyTask;

public class GameEngine {

    // --- State and Logic Dependencies ---
    private Player player;
    private TaskManager taskManager;

    // Constructor
    public GameEngine() {
        this.player = new Player("QuestHero");
        this.taskManager = new TaskManager();
    }

    // --- Public Getters ---
    public Player getPlayer() {
        return player;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    // ------------------------------------------------------------------
    // ⚔️ GAMEPLAY LOGIC
    // ------------------------------------------------------------------

    public void playerCompletesTask(Task completedTask) {
        if (completedTask == null || !completedTask.isCompleted()) {
            return;
        }

        int baseExp = completedTask.getExpReward();
        // Simple INT multiplier formula
        int finalExp = baseExp + (int) (baseExp * (player.getIntelligence() * 0.1));

        if (player.addExp(finalExp)) {
            handleLevelUp();
        }
    }

    private void handleLevelUp() {
        if (player.getLevel() % 5 == 0) {
            player.setStatPoints(player.getStatPoints() + 2);
            System.out.println("🌟 Level " + player.getLevel() + " Bonus! Gained 2 extra stat points.");
        } else {
            player.setStatPoints(player.getStatPoints() + 1);
        }
        System.out.println("You have " + player.getStatPoints() + " points to allocate.");
    }

    /**
     * Called by UI to spend points.
     */
    public void upgradePlayerStat(String statName) {
        player.allocateStat(statName);
    }

    // ------------------------------------------------------------------
    // 🌅 INTERACTIVE END DAY LOGIC (Matches ConsoleMenu)
    // ------------------------------------------------------------------

    /**
     * Step 1: UI calls this to process a single failed task.
     * 
     * @param task     The specific DailyTask that failed.
     * @param useDodge True if user chose to use a charge; False otherwise.
     */
    public void resolveFailedTask(DailyTask task, boolean useDodge) {
        int penalty = 5; // Base damage

        if (useDodge && player.hasDodgeAvailable()) {
            // User chose to dodge AND has charges left
            player.decrementDodge();
            System.out.println("🛡️ USED DODGE! You skipped the penalty for: " + task.getDescription());
            System.out.println("   (Dodges remaining: " + player.getDodgeCharges() + ")");
        } else {
            // User chose NOT to dodge, or has no charges left
            player.takeDamage(penalty);
            System.out.println("❌ TOOK DAMAGE: " + task.getDescription() + " (-" + penalty + " HP)");
        }
    }

    /**
     * Step 2: UI calls this after asking about all tasks to finish the day.
     */
    public void completeDayReset() {
        // Check for Game Over
        if (player.isDefeated()) {
            System.out.println("💀 GAME OVER. Your hero was defeated by procrastination.");
        } else {
            System.out.println("✅ Day Complete.");
        }

        // Reset system for tomorrow
        taskManager.resetDailyTasks();
        player.refreshDodges(); // Give back the free dodge
        System.out.println("🌞 New Day Starts! Tasks and Dodge Charges reset.");
    }
}