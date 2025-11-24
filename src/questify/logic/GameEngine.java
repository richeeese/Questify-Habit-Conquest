package logic;

import models.Player;
import models.Task;
import models.DailyTask;
import models.Boss;
//ignore

public class GameEngine {

    private Player player;
    private TaskManager taskManager;
    private Boss currentBoss;

    public GameEngine(Player player, TaskManager taskManager) {
        this.player = player;
        this.taskManager = taskManager;
        // Start with the Level 10 Boss available immediately
        this.currentBoss = new Boss(10); 
    }

    // --- Core Getters for UI ---
    public Player getPlayer() { return player; }
    public TaskManager getTaskManager() { return taskManager; }
    public Boss getCurrentBoss() { return currentBoss; }

    // Task completion now ONLY grants EXP and checks for level up
    public void playerCompletesTask(Task completedTask) {
        if (completedTask == null || !completedTask.isCompleted()) return;
        
        int baseExp = completedTask.getExpReward();
        // Intel is used as a direct multiplier for EXP gain
        int finalExp = baseExp * player.getIntel(); 

        if (player.addExp(finalExp)) handleLevelUp();
    }
    
    // NEW: Stat Allocation (called by ConsoleMenu)
    public void upgradePlayerStat(String stat) {
        if (player.getStatPoints() <= 0) {
            System.out.println("No stat points available.");
            return;
        }
        
        switch (stat.toLowerCase()) {
            case "str":
                player.setStr(player.getStr() + 1);
                break;
            case "def": 
                player.setDef(player.getDef() + 1);
                break;
            case "int":
            case "intel":
                player.setIntel(player.getIntel() + 1);
                break;
            case "dex":
                player.setDex(player.getDex() + 1);
                break;
            default:
                System.out.println("Invalid stat name. Points not spent.");
                return; // Exit without spending points
        }
        player.setStatPoints(player.getStatPoints() - 1);
        System.out.println("✨ Stat Upgraded! Points remaining: " + player.getStatPoints());
    }
    
    // Active Combat Method
    public void initiateCombat() {
        if (player.isDefeated()) {
            System.out.println("\n💔 You are defeated! You must rest before fighting again.");
            return;
        }
        if (currentBoss == null || currentBoss.isDefeated()) {
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
            System.out.println("    ➡️ Your attack HITS! You dealt " + damageDealt + " damage.");

            // 1.1 Check Boss Defeat
            if (currentBoss.isDefeated()) { 
                handleBossDefeat();
                return; // End combat immediately
            }
        } else {
            System.out.println("    ➡️ Your attack MISSES! The boss dodges your strike.");
        }

        // 2. BOSS ATTACK PHASE
        System.out.println("    ⬅️ " + currentBoss.getName() + " attacks you!");
        
        // Player dodge chance based on DEX: 10% base + 1% per 10 DEX points
        double dodgeChance = 0.10 + (player.getDex() / 1000.0);
        
        if (Math.random() < dodgeChance) {
            System.out.println("    🛡️ You DODGE! No damage taken.");
        } else {
            int bossAttack = currentBoss.getAttackPower();
            player.takeDamage(bossAttack);
            System.out.println("    💔 You took " + bossAttack + " damage. HP: " + player.getCurrHp() + "/" + player.getMaxHp());
            
            // 2.1 Check Player Defeat
            if (player.isDefeated()) {
                System.out.println("💀 GAME OVER. You were defeated by " + currentBoss.getName() + ".");
                return;
            }
        }
        
        System.out.println("Combat Round End. Boss HP: " + currentBoss.getCurrHp());
    }
    
    // Handler for a successful level up (from EXP or Boss reward)
    private void handleLevelUp() {
        System.out.println("\n✨ LEVEL UP! You are now Level " + player.getLevel() + "!");
        System.out.println("You have " + player.getStatPoints() + " points to allocate.");
        
        // Check for new boss spawn condition
        checkBossSpawn();
    }

    // Handler for Boss Defeat Rewards
    private void handleBossDefeat() {
        System.out.println("🎉 Boss Defeated!");

        // 1. Grant Boss EXP (may trigger a regular level up and call handleLevelUp())
        if (player.addExp(currentBoss.getExpReward())) handleLevelUp();
        System.out.println("   + " + currentBoss.getExpReward() + " Bonus EXP!");

        // 2. Force +1 Level (Boss Reward)
        player.gainLevel();
        
        // 3. Grant bonus +3 Stat Points (Boss Reward)
        player.setStatPoints(player.getStatPoints() + 3);
        
        System.out.println("\n✨ BONUS LEVEL UP! You are now Level " + player.getLevel() + "!");
        System.out.println("   + 3 Bonus Stat Points!");
        System.out.println("You have " + player.getStatPoints() + " points to allocate.");

        this.currentBoss = null;
        System.out.println("*** Boss Rewards Processed. ***");
    }
    
    // Boss Spawning Logic (Corrected switch to use Java 8-11 syntax for wider compatibility)
    private void checkBossSpawn() {
        int bossLevel = player.getLevel();

        // Bosses only spawn on levels 10, 20, 30, etc. (and only if the player is > 1)
        if (bossLevel > 1 && bossLevel % 10 == 0) {
            this.currentBoss = new Boss(bossLevel); // Instantiate the base boss

            // Custom Naming and Trait Logic
            String bossName;
            
            switch (bossLevel) {
                case 10:
                    bossName = "The Procrastination Daemon";
                    //this.currentBoss.setAttackPower(this.currentBoss.getAttackPower() + 5); // Example unique trait
                    break;
                case 20:
                    bossName = "The Siren of Distraction";
                    break;
                case 30:
                    bossName = "The Fog of Burnout";
                    break;
                case 40:
                    bossName = "The Perfectionist Hydra";
                    break;
                case 50:
                    bossName = "The Time Sink Kraken";
                    break;
                default:
                    // If the player somehow levels past 50 in increments of 10
                    bossName = "The Level " + bossLevel + " Overwhelming Task";
                    break;
            }

            System.out.println("\n*** 🚨 New Threat Emerges! 🚨 ***");
            System.out.println("A Level " + bossLevel + " Boss, " + bossName + ", has appeared! Defeat it!");
        }
    }
    
    // NEW: End-of-Day Penalty Resolver (called for each failed task by ConsoleMenu)
    public void resolveFailedTask(DailyTask daily, boolean usedDodgeCharge) {
        int penalty = 5; // Base damage for failing a daily
        
        if (usedDodgeCharge) {
            player.useDodgeCharge();
            System.out.println("💨 Dodge Charge used! Penalty for '" + daily.getDescription() + "' avoided.");
            return;
        }
        
        // Calculate the chance of naturally dodging the penalty damage (Passive DEX check)
        // Dodge chance: 10% base + 1% per 50 DEX points
        double passiveDodgeChance = 0.10 + (player.getDex() / 500.0);

        if (Math.random() < passiveDodgeChance) {
            System.out.println("💨 You passively dodged the penalty from: " + daily.getDescription());
        } else {
            player.takeDamage(penalty);
            System.out.println("❌ Failed Daily Task: " + daily.getDescription() + " - Taking " + penalty + " damage. HP: " + player.getCurrHp() + "/" + player.getMaxHp());
        }
    }
    
    // NEW: Final Day Reset (called once by ConsoleMenu after all penalties)
    public void completeDayReset() {
        taskManager.resetDailyTasks();
        
        // Heal and replenish charges on rest
        player.rest(); 
        player.replenishDodgeCharge(1); // Give the player one dodge charge back

        System.out.println("\n--- 🌞 New Day Begins! ---");
        System.out.println("Daily tasks reset. You have been fully healed and regained 1 Dodge Charge.");
        System.out.println("Current HP: " + player.getCurrHp() + "/" + player.getMaxHp());
    }
}