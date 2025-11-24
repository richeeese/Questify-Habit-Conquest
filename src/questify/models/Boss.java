package models;

public class Boss {
    // ------ Boss Attributes ------
    private String name;
    private int level;
    private int currHp;
    private int maxHp;
    private int attack;
    private int defense;
    private int expReward;

    // A static method to determine the base stats of a boss for a given level
    private static final int BASE_HP = 100;
    private static final int BASE_ATTACK = 25;
    private static final int BASE_DEFENSE = 15;
    private static final int HP_SCALING = 50;
    private static final int ATTACK_SCALING = 5;
    private static final int DEFENSE_SCALING = 3;
    private static final int EXP_SCALING = 150;


    // Constructor: Creates a boss for a specific level (must be a multiple of 10)
    public Boss(int bossLevel) {
        if (bossLevel % 10 != 0 || bossLevel <= 0) {
            throw new IllegalArgumentException("Boss level must be a positive multiple of 10.");
        }
        this.level = bossLevel;
        this.name = generateBossName(bossLevel);

        // Calculate stats based on level (e.g., Level 10 boss gets multiplier of 1)
        int levelMultiplier = bossLevel / 10;
        
        this.maxHp = BASE_HP + (HP_SCALING * levelMultiplier);
        this.currHp = this.maxHp;
        this.attack = BASE_ATTACK + (ATTACK_SCALING * levelMultiplier);
        this.defense = BASE_DEFENSE + (DEFENSE_SCALING * levelMultiplier);
        
        // Reward is proportionate to the challenge
        this.expReward = (bossLevel * 100) + (EXP_SCALING * levelMultiplier); 
    }

    // Helper method to generate a thematic boss name based on level
    private String generateBossName(int level) {
        switch (level) {
            case 10:
                return "The Taskmaster's Dread";
            case 20:
                return "The Overdue Entity";
            case 30:
                return "The Procrastination Horror";
            case 40:
                return "The Deadline Destroyer";
            default:
                // Fallback name for higher levels
                return "The Quest Tyrant of Level " + level;
        }
    }


    // ------ Combat and State Methods ------

    /**
     * Reduces the boss's HP by a calculated amount of damage.
     * @param damage The raw damage dealt by the player.
     * @return The actual damage taken by the boss (after defense reduction).
     */
    public int takeDamage(int damage) {
        // Simple damage reduction: Actual Damage = Max(0, Raw Damage - Boss Defense)
        int actualDamage = Math.max(0, damage - this.defense);
        this.currHp -= actualDamage;

        // Ensure HP doesn't drop below zero
        if (this.currHp < 0) {
            this.currHp = 0;
        }
        return actualDamage;
    }

    /**
     * Checks if the boss has been defeated.
     * @return true if HP is 0, false otherwise.
     */
    public boolean isDefeated() {
        return this.currHp <= 0;
    }


    // ------ Getters ------
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrHp() {
        return currHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getExpReward() {
        return expReward;
    }
}