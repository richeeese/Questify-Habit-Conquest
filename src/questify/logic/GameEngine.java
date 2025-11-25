package logic;

import models.Player;
import models.Task;
import models.DailyTask;
import models.Boss;

public class GameEngine {

    private Player player;
    private TaskManager taskManager;
    private Boss currentBoss;

    // [Change A] New Tracker Variable
    private int lastBossLevelDefeated = 0;

    public GameEngine(Player player, TaskManager taskManager) {
        this.player = player;
        this.taskManager = taskManager;
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
        if (completedTask == null)
            return;

        int baseExp = completedTask.getExpReward();
        int finalExp = baseExp * player.getIntel();

        if (completedTask.isCompleted()) {
            System.out.println("⚔️ Quest Completed! +" + finalExp + " EXP");
            if (player.addExp(finalExp)) {
                handleLevelUp();
            }
        } else {
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
            int actualDamage = player.takeDamage(bossRawAttack); // Fixed: capture return value

            System.out.println("    💔 You took " + actualDamage + " damage (Reduced by DEF). HP: " + player.getCurrHp()
                    + "/" + player.getMaxHp());

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

        // [Change B] Save the boss level BEFORE destroying the object
        // If the boss is Level 10, we record '10' as defeated.
        // If the boss was somehow null (rare), default to current tracker.
        if (this.currentBoss != null) {
            // We calculate the "Tier" (10, 20, 30) just in case
            // For a Level 10 boss, maxHp logic roughly aligns with level
            // Since Boss doesn't explicitly store 'level' as a field in your model,
            // we infer it from the EXP reward or MaxHP logic,
            // OR we rely on the tracker update logic below.

            // Better approach given your Boss model:
            // Calculate the tier based on player level, as that spawned the boss.
            int bossTier = (player.getLevel() / 10) * 10;
            this.lastBossLevelDefeated = bossTier;
        }

        // 1. Grant Boss EXP
        if (player.addExp(currentBoss.getExpReward()))
            handleLevelUp();

        System.out.println("   + " + currentBoss.getExpReward() + " Bonus EXP!");

        // 3. Grant bonus +3 Stat Points
        player.setStatPoints(player.getStatPoints() + 3);
        System.out.println("   + 3 Bonus Stat Points!");

        this.currentBoss = null;

        // Re-check immediately in case the EXP gain unlocked the NEXT boss
        checkBossSpawn();
    }

    // [Change C] Corrected Boss Spawn Logic
    private void checkBossSpawn() {
        // If a boss is already alive, don't spawn another one
        if (this.currentBoss != null && !this.currentBoss.isDefeated()) {
            return;
        }

        int currentLevel = player.getLevel();

        // Calculate the "Milestone" (Tier Start)
        // Level 19 -> 10. Level 21 -> 20. Level 30 -> 30.
        int currentTierStart = (currentLevel / 10) * 10;

        // LOGIC:
        // 1. Player must be at least Level 10.
        // 2. The current tier (e.g. 20) must be HIGHER than the last one beaten (e.g.
        // 10).
        // This catches "Over-Leveling" (e.g. Jumping from 19 to 21).
        if (currentTierStart >= 10 && currentTierStart > this.lastBossLevelDefeated) {
            spawnBoss(currentTierStart);
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

    // --- Penalty Logic (Kept exactly as is) ---
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
        }
    }

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