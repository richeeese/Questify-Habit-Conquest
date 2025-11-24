package logic;

import models.Player;
import models.Task;
import models.DailyTask;
// import com.questify.models.Boss; // Will be added when Boss class is ready
// import com.questify.logic.CombatSystem; // Will be added when CombatSystem is ready
import java.util.List;

public class GameEngine {

    // --- State and Logic Dependencies ---
    private Player player;
    private TaskManager taskManager;
    // private Boss currentBoss;
    // private CombatSystem combatSystem;

    // Constructor: Initializes the primary components
    public GameEngine() {
        // Initialize Core Dependencies
        this.player = new Player("QuestHero"); // Create the starting player
        this.taskManager = new TaskManager();
        // this.combatSystem = new CombatSystem(); // Initialize when ready
        // this.currentBoss = new Boss("The Procrastinator", 500, 50); // Initialize
        // Boss when ready
    }

    // --- Public Getters for UI and Main ---

    public Player getPlayer() {
        return player;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    // ------------------------------------------------------------------
    // ⚔️ CORE GAME LOOP METHODS
    // ------------------------------------------------------------------

    /**
     * Handles the full reward/penalty processing when a user toggles a task to
     * "complete."
     * This is called by the UI when a user completes a task.
     * 
     * @param completedTask The Task object returned by
     *                      TaskManager.toggleCompletion.
     */
    public void playerCompletesTask(Task completedTask) {
        // Only process if the task is now marked complete (toggled ON)
        if (completedTask == null || !completedTask.isCompleted()) {
            return;
        }

        // 1. Calculate Base EXP, applying the INT multiplier
        int baseExp = completedTask.getExpReward();
        int finalExp = baseExp * player.getIntelligence();

        // 2. Grant EXP to the player
        if (player.addExp(finalExp)) {
            // Level up occurred inside player.addExp
            handleLevelUp(); // Check for stat point bonuses
        }

        // 3. Boss Damage Logic (Will use CombatSystem later)
        // if (currentBoss != null && isBossQuestActive) {
        // int damage = player.getStrength();

        // Apply streak bonus if it's a DailyTask
        // if (completedTask instanceof DailyTask) {
        // DailyTask daily = (DailyTask) completedTask;
        // damage += daily.getStreakBonusDamage();
        // }
        // combatSystem.dealDamageToBoss(currentBoss, damage);
        // }
    }

    /**
     * Checks for stat point bonuses and notifies the UI/Player.
     */
    private void handleLevelUp() {
        // Player model already increased level and reset HP/Mana

        // Grant bonus stat points if level is a multiple of 5
        if (player.getLevel() % 5 == 0) {
            player.setStatPoints(player.getStatPoints() + 2); // Grant 2 bonus points
            System.out.println("🌟 Level " + player.getLevel() + " Bonus! Gained 2 extra stat points.");
        } else {
            player.setStatPoints(player.getStatPoints() + 1); // Grant 1 standard point
        }

        System.out.println("You have " + player.getStatPoints() + " points to allocate.");
        // UI (ConsoleMenu) will be responsible for prompting the user to allocate
        // points
    }

    /**
     * Manages the end of the day, applying damage for incomplete Dailies and
     * resetting streaks.
     * This is the core penalty mechanic.
     */
    public void endDay() {
        System.out.println("\n--- 🌅 Day End Maintenance ---");
        int totalDamage = 0;

        // 1. Check for failed Daily Tasks
        List<DailyTask> incompleteDailies = taskManager.getIncompleteDailyTasks();

        for (DailyTask daily : incompleteDailies) {
            // Apply penalty damage (e.g., base damage is 5, adjust as needed)
            int penalty = 5;

            // Check for DEX dodge ability (This would use CombatSystem later)
            boolean dodged = false; // combatSystem.tryToDodge(player.getDexterity());

            if (!dodged) {
                totalDamage += penalty;
                System.out.println(
                        "❌ Failed Daily Task: " + daily.getDescription() + " - Taking " + penalty + " damage.");
            } else {
                System.out.println("💨 You deftly dodged the penalty from: " + daily.getDescription());
            }
        }

        // 2. Apply total damage to the player (Damage reduction applied inside
        // player.takeDamage or CombatSystem)
        if (totalDamage > 0) {
            player.takeDamage(totalDamage);
            if (player.isDefeated()) {
                System.out.println("💀 GAME OVER. Your hero was defeated by procrastination.");
                // handlePlayerDefeat();
            }
        } else {
            System.out.println("✅ All daily obligations met! No damage taken.");
        }

        // 3. Reset all daily tasks for the new day and clean up
        taskManager.resetDailyTasks();
        System.out.println("🌞 New Day starts! Daily tasks reset and streaks updated.");
    }
}