package logic;

import models.Player;
import models.Task;
import models.DailyTask;
import models.Boss;

public class GameEngine {

    private Player player;
    private TaskManager taskManager;
    private Boss currentBoss;

    private int lastBossLevelDefeated = 0;

    public GameEngine(Player player, TaskManager taskManager) {
        this.player = player;
        this.taskManager = taskManager;
        // Start with the Level 10 Boss available immediately for testing
        // You can change this to null if you want them to wait for level 10
        checkBossSpawn();
    }

    // --- Core Getters for UI ---
    public Player getPlayer() {
        return player;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public Boss getCurrentBoss() {
        return currentBoss;
    }

    public void playerCompletesTask(Task completedTask) {
        // Safety check
        if (completedTask == null)
            return;

        int baseExp = completedTask.getExpReward();
        int finalExp = baseExp * player.getIntel(); // Intel multiplier

        // 1. Check if the task is currently DONE or NOT DONE
        if (completedTask.isCompleted()) {
            // --- CASE A: Player marked it as DONE ---
            System.out.println("⚔️ Quest Completed! +" + finalExp + " EXP");

            // Add EXP (and handle level up if it happens)
            if (player.addExp(finalExp)) {
                handleLevelUp();
            }
        } else {
            // --- CASE B: Player marked it as UNDONE (The Fix) ---
            // We MUST subtract the EXP they previously gained.
            // This prevents them from toggling it on/off to farm points.
            player.removeExp(finalExp);
        }
    }

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

        System.out.println("\n⚔️ You enter combat with " + currentBoss.getName() + " (HP: " + currentBoss.getCurrHp()
                + "/" + currentBoss.getMaxHp() + ")");

        // 1. PLAYER ATTACK PHASE
        double hitChance = 0.80 + (player.getDex() / 1000.0);

        if (Math.random() < hitChance) {
            int playerDamage = player.getStr();
            int damageDealt = currentBoss.takeDamage(playerDamage);
            System.out.println("    ➡️ Your attack HITS! You dealt " + damageDealt + " damage.");

            if (currentBoss.isDefeated()) {
                handleBossDefeat();
                return;
            }
        } else {
            System.out.println("    ➡️ Your attack MISSES! The boss dodges your strike.");
        }

        // 2. BOSS ATTACK PHASE
        System.out.println("    ⬅️ " + currentBoss.getName() + " attacks you!");

        double dodgeChance = 0.10 + (player.getDex() / 1000.0);

        if (Math.random() < dodgeChance) {
            System.out.println("    🛡️ You DODGE! No damage taken.");
        } else {
            int bossRawAttack = currentBoss.getAttackPower();
            int actualDamage = player.takeDamage(bossRawAttack);

            System.out.println(
                    "    💔 You took " + actualDamage + " damage (Reduced by DEF). HP: " + player.getCurrHp() + "/"
                            + player.getMaxHp());

            if (player.isDefeated()) {
                System.out.println("💀 GAME OVER. You were defeated by " + currentBoss.getName() + ".");
                return;
            }
        }
        System.out.println("Combat Round End. Boss HP: " + currentBoss.getCurrHp());
    }

    private void handleLevelUp() {
        System.out.println("\n✨ LEVEL UP! You are now Level " + player.getLevel() + "!");
        System.out.println("You have " + player.getStatPoints() + " points to allocate.");
        checkBossSpawn();
    }

    private void handleBossDefeat() {
        System.out.println("🎉 Boss Defeated!");

        int bossTier = (player.getLevel() / 10) * 10;
        this.lastBossLevelDefeated = bossTier;

        // 1. Grant Boss EXP
        // (This is usually enough to level up anyway!)
        if (player.addExp(currentBoss.getExpReward()))
            handleLevelUp();

        System.out.println("   + " + currentBoss.getExpReward() + " Bonus EXP!");

        // 3. Grant bonus +3 Stat Points
        player.setStatPoints(player.getStatPoints() + 3);

        System.out.println("   + 3 Bonus Stat Points!");

        this.currentBoss = null;

        checkBossSpawn();
    }

    // FIX: Correctly applies custom names to the Boss object
    private void checkBossSpawn() {
        // If a boss is already alive, don't spawn another one
        if (this.currentBoss != null && !this.currentBoss.isDefeated()) {
            return;
        }

        int currentLevel = player.getLevel();

        // Calculate the "Milestone" level (The nearest 10 below current level)
        // Example: Level 19 -> 10. Level 21 -> 20. Level 35 -> 30.
        int milestoneLevel = (currentLevel / 10) * 10;

        // LOGIC:
        // 1. We must be at least level 10.
        // 2. The calculated milestone must be HIGHER than the last boss we beat.
        if (milestoneLevel >= 10 && milestoneLevel > lastBossLevelDefeated) {

            spawnBoss(milestoneLevel);
        }
    }

    private void spawnBoss(int bossLevel) {
        this.currentBoss = new Boss(bossLevel);

        String bossName;
        switch (bossLevel) {
            case 10:
                bossName = "The Procrastination Daemon";
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
                bossName = "The Level " + bossLevel + " Overwhelming Task";
                break;
        }

        this.currentBoss.setName(bossName);

        System.out.println("\n*** 🚨 New Threat Emerges! 🚨 ***");
        System.out.println("A Level " + bossLevel + " Boss, " + currentBoss.getName() + ", has appeared! Defeat it!");
    }

    // NEW: End-of-Day Penalty Resolver
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
            player.takeDamage(penalty); // dito ka
        }
    }

    // Overload for standard tasks
    public void resolveFailedTask(models.Task task, boolean usedDodgeCharge) {
        int penalty = 5;

        if (usedDodgeCharge) {
            player.useDodgeCharge();
            System.out.println("💨 Dodge Charge used! Penalty for '" + task.getDescription() + "' avoided.");
            return;
        }

        double passiveDodgeChance = 0.10 + (player.getDex() / 500.0);

        if (Math.random() < passiveDodgeChance) {
            System.out.println("💨 You passively dodged the penalty from: " + task.getDescription());
        } else {
            // Assuming Player has a method for this, otherwise use takeDamage
            player.takeDamage(penalty);
            System.out.println("❌ Failed Task: " + task.getDescription() + " - Taking " + penalty + " damage.");
        }
    }

    public void completeDayReset() {
        taskManager.resetDailyTasks();
        player.rest();
        player.replenishDodgeCharge(1);

        System.out.println("\n--- 🌞 New Day Begins! ---");
        System.out.println("Daily tasks reset. You have been healed and regained 1 Dodge Charge.");
        System.out.println("Current HP: " + player.getCurrHp() + "/" + player.getMaxHp());
    }
}