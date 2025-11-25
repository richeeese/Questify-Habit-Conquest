package logic;

import models.Player;
import models.Task;
import models.DailyTask;
import models.Boss;
//import java.util.List;

public class GameEngine {

    private Player player;
    private TaskManager taskManager;
    private Boss currentBoss;

    public GameEngine(Player player, TaskManager taskManager) {
        this.player = player;
        this.taskManager = taskManager;
        // Check if a boss needs to be spawned based on starting level
        checkBossSpawn(); 
    }

    // --- Core Getters for UI ---
    public Player getPlayer() { return player; }
    public TaskManager getTaskManager() { return taskManager; }
    public Boss getCurrentBoss() { return currentBoss; }

    // --- BOSS NAMING & TIER LOGIC ---

    /**
     * Determines the level of the *current* boss challenge (10, 20, 30, etc.)
     * This is the base level of the boss that the player is currently eligible to fight
     * or whose name should be displayed for the current tier.
     * E.g., If Lvl 23 and Lvl 10 boss is defeated, the challenge is Lvl 20.
     */
    public int getCurrentChallengeLevel() {
        int playerLevel = player.getLevel();
        
        if (playerLevel < 10) return 10;
        
        // Find the next 10s tier level.
        // E.g., Lvl 12 -> (12+9)/10 = 2.1 -> 2. * 10 = 20
        // E.g., Lvl 29 -> (29+9)/10 = 3.8 -> 3. * 10 = 30
        // E.g., Lvl 20 -> (20+9)/10 = 2.9 -> 2. * 10 = 20 (If boss is active)
        int nextTierLevel = 10 * ((playerLevel + 9) / 10);
        
        if (currentBoss != null && !currentBoss.isDefeated()) {
             // If a boss is currently active, return its level.
             return currentBoss.getLevel();
        } else {
             // If Lvl 23 and the Lvl 10 boss is defeated, the challenge is Lvl 20.
             // We need to look back to the start of the current tier.
             int currentTierStart = (playerLevel / 10) * 10;
             
             if (currentTierStart == 0) return 10;
             
             // If player is Lvl 23, currentTierStart is 20. The challenge should be 20.
             // If player is Lvl 20, the next challenge is Lvl 30 (since Lvl 20 boss is defeated).
             if (playerLevel % 10 == 0 && playerLevel >= 10 && currentBoss == null) {
                  return playerLevel + 10;
             }
             
             // Check if the current level is in the 10-19 range or 20-29 range.
             if (playerLevel < nextTierLevel) {
                 return currentTierStart;
             } else {
                 return nextTierLevel;
             }
        }
    }


    /**
     * Retrieves the name of the boss for the player's CURRENT challenge level.
     * This ensures the menu shows the Lvl 20 boss name for Lvl 23 players.
     */
    public String getUpcomingBossName() {
        // If an active boss exists, always display its name.
        if (currentBoss != null && !currentBoss.isDefeated()) {
             return currentBoss.getName();
        }
        
        // Otherwise, get the name of the boss for the level tier the player is currently challenging.
        int challengeLevel = getCurrentChallengeLevel();
        return getBossNameByLevel(challengeLevel);
    }
    
    /**
     * Returns the appropriate boss name for a given target level (e.g., 10, 20, 30).
     */
    private String getBossNameByLevel(int bossLevel) {
        switch (bossLevel) {
            case 10: 
                return "The Procrastination Daemon";
            case 20:return "The Siren of Distraction";
            case 30: return "The Fog of Burnout";
            case 40: return "The Perfectionist Hydra";
            case 50: return "The Time Sink Kraken";
            default:
                // Ensure this still returns the *next* tier name if the tier is defeated
                return "The Level " + bossLevel + " Overwhelming Task";
        }
    }
    
    // --- EXPERIENCE & LEVEL UP LOGIC ---

    public void playerCompletesTask(Task completedTask) {
        if (completedTask == null || !completedTask.isCompleted()) return;
        
        int baseExp = completedTask.getExpReward();
        // Intel is used as a direct multiplier for EXP gain
        int finalExp = baseExp * player.getIntel(); 

        // addExp handles the EXP check and calls handleLevelUp internally if true
        if (player.addExp(finalExp)) {
            handleLevelUp();
        }
    }
    
    private void handleLevelUp() {
        System.out.println("\n✨ LEVEL UP! You are now Level " + player.getLevel() + "!");
        System.out.println("You have " + player.getStatPoints() + " points to allocate.");
        
        // Always check for boss spawn after a level up
        checkBossSpawn();
    }

    // --- CORE BOSS SPAWN LOGIC ---

    /**
     * Core logic for spawning a boss when the player reaches the start of a new tier.
     */
    private void checkBossSpawn() {
        int playerLevel = player.getLevel();
        
        // Bosses only spawn on levels 10, 20, 30, etc.
        if (playerLevel >= 10 && playerLevel % 10 == 0) {
            
            // 1. Check if a boss is currently active. If so, do nothing.
            if (currentBoss != null && !currentBoss.isDefeated()) {
                return;
            }
            
            // 2. If no boss is active, check if the player's level is higher than the last defeated boss.
            //    (lastBossLevel will be 0 if no boss has been spawned/defeated yet)
            int lastBossLevel = (currentBoss == null) ? 0 : currentBoss.getLevel();
            
            // If the player hit Level 20, and the last boss defeated was Level 10, spawn the Level 20 boss.
            if (playerLevel > lastBossLevel) {
                spawnBossAtLevel(playerLevel);
            }
        }
    }
    
    /**
     * Helper to instantiate and announce a new boss.
     */
    private void spawnBossAtLevel(int level) {
         this.currentBoss = new Boss(level); 
         String bossName = getBossNameByLevel(level);
         this.currentBoss.setName(bossName);
         
         // Apply custom traits for Lvl 10 boss
         if (level == 10) {
              // Assumes setAttackPower exists in Boss.java
              this.currentBoss.setAttackPower(this.currentBoss.getAttackPower() + 5); 
         }

         System.out.println("\n*** 🚨 New Threat Emerges! 🚨 ***");
         System.out.println("A Level " + level + " Boss, " + bossName + ", has appeared! Defeat it!");
    }


    // --- COMBAT & STAT ALLOCATION ---

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
                return;
        }
        player.setStatPoints(player.getStatPoints() - 1);
        System.out.println("✨ Stat Upgraded! Points remaining: " + player.getStatPoints());
    }
    
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
        double hitChance = 0.80 + (player.getDex() / 1000.0);
        
        if (Math.random() < hitChance) {
            int playerDamage = player.getStr(); 
            int damageDealt = currentBoss.takeDamage(playerDamage);
            System.out.println("    ➡️ Your attack HITS! You dealt " + damageDealt + " damage.");

            if (currentBoss.isDefeated()) { 
                handleBossDefeat();
                return;
            }
        } else {
            System.out.println("    ➡️ Your attack MISSES! The boss dodges your strike.");
        }

        // 2. BOSS ATTACK PHASE
        System.out.println("    ⬅️ " + currentBoss.getName() + " attacks you!");
        
        double dodgeChance = 0.10 + (player.getDex() / 1000.0);
        
        if (Math.random() < dodgeChance) {
            System.out.println("    🛡️ You DODGE! No damage taken.");
        } else {
            int rawBossAttack = currentBoss.getAttackPower();
            int finalDamage = Math.max(1, rawBossAttack - player.getDef());
            
            player.takeDamage(finalDamage);
            System.out.println("    💔 You took " + finalDamage + " damage. HP: " + player.getCurrHp() + "/" + player.getMaxHp());
            
            if (player.isDefeated()) {
                System.out.println("💀 GAME OVER. You were defeated by " + currentBoss.getName() + ".");
                return;
            }
        }
        
        System.out.println("Combat Round End. Boss HP: " + currentBoss.getCurrHp());
    }
    
    private void handleBossDefeat() {
        System.out.println("🎉 Boss Defeated!");

        // 1. Grant Boss EXP
        if (player.addExp(currentBoss.getExpReward())) handleLevelUp();
        System.out.println("   + " + currentBoss.getExpReward() + " Bonus EXP!");

        // 2. Force +1 Level & Stat Points (Boss Reward)
        player.gainLevel(); 
        player.setStatPoints(player.getStatPoints() + 3);
        
        System.out.println("\n✨ BONUS LEVEL UP! You are now Level " + player.getLevel() + "!");
        System.out.println("   + 3 Bonus Stat Points!");
        System.out.println("You have " + player.getStatPoints() + " points to allocate.");

        this.currentBoss = null;
        System.out.println("*** Boss Rewards Processed. ***");
        
        // Check for next boss spawn (e.g., if Lvl 19 defeated Lvl 10 boss, they hit Lvl 20 and spawn Lvl 20 boss)
        checkBossSpawn(); 
    }
    
    // --- END OF DAY LOGIC ---
    
    public void resolveFailedTask(DailyTask daily, boolean usedDodgeCharge) {
        int penalty = 5; 
        
        if (usedDodgeCharge) {
            player.useDodgeCharge();
            System.out.println("💨 Dodge Charge used! Penalty for '" + daily.getDescription() + "' avoided.");
            return;
        }
        
        double passiveDodgeChance = 0.10 + (player.getDex() / 500.0);

        if (Math.random() < passiveDodgeChance) {
            System.out.println("💨 You passively dodged the penalty from: " + daily.getDescription());
        } else {
            player.takeDamage(penalty);
            System.out.println("❌ Failed Daily Task: " + daily.getDescription() + " - Taking " + penalty + " damage. HP: " + player.getCurrHp() + "/" + player.getMaxHp());
        }
    }
    
    public void completeDayReset() {
        taskManager.resetDailyTasks();
        
        // Heal and replenish charges on rest
        player.rest(); 
        player.replenishDodgeCharge(1);

        System.out.println("\n--- 🌞 New Day Begins! ---");
        System.out.println("Daily tasks reset. You have been fully healed and regained 1 Dodge Charge.");
        System.out.println("Current HP: " + player.getCurrHp() + "/" + player.getMaxHp());
    }
}