package models;

import java.io.Serializable;

public class Boss implements Serializable {
    private String name;
    private int currHp;
    private int maxHp;
    private int expReward;
    private int attackPower;
    private int defense;
    private int level;

    public Boss(int level) {
        this.name = "The Level " + level + " Boss"; // Default name
        this.maxHp = 10 + (level * 5);
        this.currHp = this.maxHp;
        this.expReward = level * 10;
        this.attackPower = 5 + (level / 2);
        this.defense = 2 + (level / 5);
    }

    public int takeDamage(int rawDamage) {
        int actualDamage = Math.max(1, rawDamage - (this.defense / 2));
        this.currHp -= actualDamage;
        if (this.currHp < 0)
            this.currHp = 0;
        return actualDamage;
    }

    public boolean isDefeated() {
        return currHp <= 0;
    }

    // --- Setters (Needed for GameEngine to customize the Boss) ---
    public void setName(String name) {
        this.name = name;
    }

    // --- Getters ---

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getCurrHp() {
        return currHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getExpReward() {
        return expReward;
    }

    public int getAttackPower() {
        return attackPower;
    }
}