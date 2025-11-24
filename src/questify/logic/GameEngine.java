package logic;

import models.Player;
import models.Task;
import models.DailyTask;
import models.Boss;
import java.util.List;

public class GameEngine {

    private Player player;
    private TaskManager taskManager;
    private Boss currentBoss;
    private boolean isBossQuestActive = true;

    public GameEngine(Player player, TaskManager taskManager) {
        this.player = player;
        this.taskManager = taskManager;
        // Start with the Level 10 Boss available immediately
        this.currentBoss = new Boss(10); 
    }

    public Boss getCurrentBoss() { return currentBoss;
    }

    // Task completion now ONLY grants EXP and checks for level up (passive combat removed)
    public void playerCompletesTask(Task completedTask) {
        if (completedTask == null || !completedTask.isCompleted()) return;
        
        int baseExp = completedTask.getExpReward();
        // Note: Intel is used as a direct multiplier for EXP gain
        int finalExp = baseExp * player.getIntel(); 

        if (player.addExp(finalExp)) handleLevelUp();
    }
    
    // NEW: Active Combat Method
    public void initiateCombat() {
        if (player.isDefeated()) {
            System.out.println("\n💔 You are defeated! You must recover before fighting again.");
            return;
        }
        if (currentBoss == null || !isBossQuestActive || currentBoss.isDefeated()) {
            System.out.println("\nNothing to fight! No boss is currently active.");
            return;
        }

        System.out.println("\n⚔️ You enter combat with " + currentBoss.getName() + " (HP: " + currentBoss.getCurrHp() + "/" + currentBoss.getMaxHp() + ")");

        // 1. PLAYER ATTACK PHASE
        // Player accuracy based on DEX: Max 80% base + 1% per 10 DEX points
        double hitChance = 0.80 + (player.getDex() / 1000.0);
        
        if (Math.random() < hitChance) {
            int playerDamage = player.getStr();
            int damageDealt = currentBoss.takeDamage(playerDamage);
            System.out.println("    ➡️ Your attack HITS! You dealt " + damageDealt + " damage.");

            // 1.1 Check Boss Defeat
            if (currentBoss.isDefeated()) { 
                handleBossDefeat();
                return; // End combat immediately
            }
        } else {
            System.out.println("    ➡️ Your attack MISSES! The boss dodges your strike.");
        }

        // 2. BOSS ATTACK PHASE
        // Boss hits player
        System.out.println("    ⬅️ " + currentBoss.getName() + " attacks you!");
        
        // Player dodge chance based on DEX: 10% base + 1% per 10 DEX points
        double dodgeChance = 0.10 + (player.getDex() / 1000.0);
        
        if (Math.random() < dodgeChance) {
            System.out.println("    🛡️ You DODGE! No damage taken.");
        } else {
            int bossAttack = currentBoss.getAttackPower();
            player.takeDamage(bossAttack);
            System.out.println("    💔 You took " + bossAttack + " damage. HP: " + player.getCurrHp() + "/" + player.getMaxHp());
            
            // 2.1 Check Player Defeat
            if (player.isDefeated()) {
                System.out.println("💀 GAME OVER. You were defeated by " + currentBoss.getName() + ".");
                this.isBossQuestActive = false;
                return;
            }
        }
        
        System.out.println("Combat Round End. Boss HP: " + currentBoss.getCurrHp());
    }
    
    // Handler for a successful level up (from EXP)
    private void handleLevelUp() {
        System.out.println("\n✨ LEVEL UP! You are now Level " + player.getLevel() + "!");
        if (player.getLevel() % 5 == 0) player.setStatPoints(player.getStatPoints() + 2);
        else player.setStatPoints(player.getStatPoints() + 1);
        System.out.println("You have " + player.getStatPoints() + " points to allocate.");
        
        // Check for new boss spawn condition
        checkBossSpawn();
    }

    // Handler for Boss Defeat Rewards
    private void handleBossDefeat() {
        System.out.println("🎉 Boss Defeated!");

        // 1. Grant Boss EXP (may trigger a regular level up and call handleLevelUp())
        if (player.addExp(currentBoss.getExpReward())) handleLevelUp();
        System.out.println("   + " + currentBoss.getExpReward() + " Bonus EXP!");

        // 2. Force +1 Level (Boss Reward)
        player.gainLevel();
        
        // 3. Grant normal stat points for the forced level
        System.out.println("\n✨ BONUS LEVEL UP! You are now Level " + player.getLevel() + "!");
        int normalStatBonus = (player.getLevel() % 5 == 0) ? 2 : 1;
        player.setStatPoints(player.getStatPoints() + normalStatBonus);
        
        // 4. Grant bonus +3 Stat Points (Boss Reward)
        player.setStatPoints(player.getStatPoints() + 3);
        
        System.out.println("   + 1 Bonus Level!");
        System.out.println("   + 3 Bonus Stat Points!");
        System.out.println("You have " + player.getStatPoints() + " points to allocate.");

        this.isBossQuestActive = false;
        System.out.println("*** Boss Rewards Processed. ***");
    }
    
    // Checks if a new boss should be created (every 10 levels)
    private void checkBossSpawn() {
        // Bosses appear at level 10, 20, 30, etc.
        if (player.getLevel() > 1 && player.getLevel() % 10 == 0) {
            int bossLevel = player.getLevel();
            this.currentBoss = new Boss(bossLevel);
            this.isBossQuestActive = true; 
            System.out.println("\n*** 🚨 New Threat Emerges! 🚨 ***");
            System.out.println("A Level " + bossLevel + " Boss, " + currentBoss.getName() + ", has appeared!");
        }
    }

    public void endDay() {
        System.out.println("\n--- 🌅 Day End Maintenance ---");
        int totalDamage = 0;
        int penalty = 5;

        List<DailyTask> incompleteDailies = taskManager.getIncompleteDailyTasks();
        for (DailyTask daily : incompleteDailies) {
            // Player dodge chance based on DEX (10% base + 1% per 50 DEX points)
            boolean dodged = (Math.random() < (0.10 + (player.getDex() / 500.0)));
            if (!dodged) {
                totalDamage += penalty;
                System.out.println("❌ Failed Daily Task: " + daily.getDescription() + " - Taking " + penalty + " damage.");
            } else System.out.println("💨 You dodged the penalty from: " + daily.getDescription());
        }

        if (totalDamage > 0) {
            player.takeDamage(totalDamage);
            System.out.println("💔 Total Penalty Damage Taken: " + totalDamage + ". HP: " + player.getCurrHp() + "/" + player.getMaxHp());
            if (player.isDefeated()) System.out.println("💀 GAME OVER. Your hero was defeated.");
        } else System.out.println("✅ All daily obligations met! No damage taken.");

        taskManager.resetDailyTasks();
        System.out.println("🌞 New Day starts! Daily tasks reset and streaks updated.");
    }
}