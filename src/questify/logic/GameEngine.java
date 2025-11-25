package logic;

import models.Player;
import models.Task;
import models.DailyTask;
import models.Boss;
import java.util.List;
import java.lang.Math;

public class GameEngine {

    private Player player;
    private TaskManager taskManager;
    private Boss currentBoss;
    private int lastBossDefeatedLevel = 0; // NEW TRACKER ADDED HERE

    public GameEngine(Player player, TaskManager taskManager) {
        this.player = player;
        this.taskManager = taskManager;
        // Check for Lvl 10 boss spawn if player starts at Lvl 10+
        checkBossSpawn(); 
    }

    // --- Core Getters for UI ---
    public Player getPlayer() { return player; }
    public TaskManager getTaskManager() { return taskManager; }
    public Boss getCurrentBoss() { return currentBoss; }

    // --- BOSS NAMING & TIER LOGIC ---

    /**
     * Determines the level of the *current* boss challenge (10, 20, 30, etc.).
     * This ensures correct progression after defeating a boss.
     */
    public int getCurrentChallengeLevel() {
        int playerLevel = player.getLevel();
        
        // 1. Always return 10 if player hasn't reached the first tier.
        if (playerLevel < 10) return 10;
        
        // 2. If a boss is currently active, always return its level.
        if (currentBoss != null && !currentBoss.isDefeated()) {
             return currentBoss.getLevel();
        }
        
        // 3. If no boss is active (defeated or not yet spawned): 
        // The challenge is always the tier immediately following the last defeated boss.
        return this.lastBossDefeatedLevel + 10; 
    }


    /**
     * Retrieves the name of the boss for the player's CURRENT challenge level.
     */
    public String getUpcomingBossName() {
        if (currentBoss != null && !currentBoss.isDefeated()) {
             return currentBoss.getName();
        }
        
        int challengeLevel = getCurrentChallengeLevel();
        return getBossNameByLevel(challengeLevel);
    }
    
    /**
     * Returns the appropriate boss name for a given target level (e.g., 10, 20, 30).
     */
    private String getBossNameByLevel(int bossLevel) {
        switch (bossLevel) {
            case 10: return "The Procrastination Daemon";
            case 20: return "The Siren of Distraction";
            case 30: return "The Fog of Burnout";
            case 40: return "The Perfectionist Hydra";
            case 50: return "The Time Sink Kraken";
            default:
                return "The Level " + bossLevel + " Overwhelming Task";
        }
    }
    
    // --- EXPERIENCE & LEVEL UP LOGIC ---

    public void playerCompletesTask(Task completedTask) {
        if (completedTask == null || !completedTask.isCompleted()) return;
        
        int baseExp = completedTask.getExpReward();
        int finalExp = baseExp * player.getIntel(); 

        if (player.addExp(finalExp)) {
            handleLevelUp();
        }
    }
    
    private void handleLevelUp() {
        System.out.println("\n✨ LEVEL UP! You are now Level " + player.getLevel() + "!");
        System.out.println("You have " + player.getStatPoints() + " points to allocate.");
        
        checkBossSpawn();
    }

    // --- CORE BOSS SPAWN LOGIC ---

    private void checkBossSpawn() {
        int playerLevel = player.getLevel();
        
        // Calculate the base level of the current tier (e.g., Lvl 33 -> 30)
        int currentTierStart = (playerLevel / 10) * 10;
        
        // Bosses only spawn when the player hits the tier start level (10, 20, 30, etc.)
        if (playerLevel >= 10 && playerLevel % 10 == 0) {
            
            // 1. If a boss is currently active, do nothing.
            if (currentBoss != null && !currentBoss.isDefeated()) {
                return;
            }
            
            // 2. If no boss is active, spawn if the player's level is higher than the last defeated boss.
            if (playerLevel > this.lastBossDefeatedLevel) {
                spawnBossAtLevel(playerLevel);
            }
        }
        
        // If the player over-leveled (e.g., Lvl 29 -> Lvl 31) and missed the spawn, 
        // we check if the current tier boss was never defeated.
        if (currentBoss == null && currentTierStart > this.lastBossDefeatedLevel) {
             spawnBossAtLevel(currentTierStart);
        }
    }
    
    private void spawnBossAtLevel(int level) {
         this.currentBoss = new Boss(level); 
         String bossName = getBossNameByLevel(level);
         this.currentBoss.setName(bossName);
         
         if (level == 10) {
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

        // --- FIX: Save defeated level before clearing currentBoss ---
        this.lastBossDefeatedLevel = this.currentBoss.getLevel(); 
        this.currentBoss = null;
        // -----------------------------------------------------------
        
        System.out.println("*** Boss Rewards Processed. ***");
        
        // This check will now correctly spawn the next boss if the bonus level up hits the next tier milestone (e.g., Lvl 30)
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
        
        // Full heal and replenish charges on rest
        player.rest(); 
        player.replenishDodgeCharge(1);

        System.out.println("\n--- 🌞 New Day Begins! ---");
        System.out.println("Daily tasks reset. You have been fully healed and regained 1 Dodge Charge.");
        System.out.println("Current HP: " + player.getCurrHp() + "/" + player.getMaxHp());
    }
}