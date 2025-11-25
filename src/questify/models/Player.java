package models;

import java.io.Serializable;

public class Player implements Serializable{
    private String name;
    private int level;
    private int statPoints;
    private int currExp;
    private int maxExp;
    private int currHp;
    private int maxHp;
    private int currMana;
    private int maxMana;
    private int baseHeal = 5;
    private int failedDmg = 5;

    private int dodgeCharges;
    private final int maxDodgeCharges = 1;

    private int str; // Strength (Combat Damage, Max HP scaling)
    private int def; // Defense (Damage Reduction)
    private int intel; // Intelligence (EXP Multiplier, Max Mana scaling)
    private int dex; // Dexterity (Dodge/Hit Chance)

    private String playerSprite;

    public Player(String name) {
        this.name = name;
        this.level = 1;
        this.statPoints = 0;
        this.currExp = 0;

        // Base Stats
        this.str = 10;
        this.def = 10;
        this.intel = 1;
        this.dex = 10;

        recalculateDerivedStats();

        this.currHp = this.maxHp;
        this.currMana = this.maxMana;
        this.dodgeCharges = 1;
    }

    // Recalculates HP and Mana when stats are updated or on level up
    private void recalculateDerivedStats() {
        // Derived HP and Mana based on STR and INTEL
        int newMaxHp = 50 + (this.str * 5);
        int newMaxMana = 20 + (this.intel * 1);

        // Adjust current HP/Mana safely when max changes
        if (newMaxHp > this.maxHp)
            this.currHp += (newMaxHp - this.maxHp);
        this.maxHp = newMaxHp;
        this.currHp = Math.min(this.currHp, this.maxHp);

        if (newMaxMana > this.maxMana)
            this.currMana += (newMaxMana - this.maxMana);
        this.maxMana = newMaxMana;
        this.currMana = Math.min(this.currMana, this.maxMana);

        // Update Max EXP for next level
        this.maxExp = 20 + (this.level * 10);
    }

    public boolean addExp(int exp) {
        this.currExp += exp;
        boolean leveledUp = false;

        // Loop: While we have enough EXP to level up...
        while (this.currExp >= this.maxExp) {
            levelUp();
            leveledUp = true;
        }
        return leveledUp;
    }

    public void removeExp(int exp) {
        this.currExp -= exp;

        // MERCIFUL LOGIC:
        // If EXP drops below zero, just set it to 0.
        // Do NOT decrease the level. Do NOT remove stats.
        if (this.currExp < 0) {
            this.currExp = 0;
            System.out.println("⚠️ EXP drained to 0 (Level retained).");
        } else {
            System.out.println("🔻 " + this.name + " lost " + exp + " EXP.");
        }
    }

    public void gainLevel() {
        this.level++;

        // Grant stat points
        this.statPoints += (this.level % 5 == 0) ? 2 : 1;

        // Update Max EXP / HP / Mana for the new level
        recalculateDerivedStats();

        // Free Heal
        this.currHp = this.maxHp;
        this.currMana = this.maxMana;

        System.out.println("🎉 BONUS LEVEL! You are now level " + this.level);
    }

    private void levelUp() {
        // 1. FIX: Pay the cost FIRST (using the OLD maxExp)
        // Example: If you have 60 Exp and need 50, we subtract 50 here.
        this.currExp -= this.maxExp;

        // 2. NOW increase the level
        this.level++;

        // 3. Grant stat points
        this.statPoints += (this.level % 5 == 0) ? 2 : 1;

        // 4. Update Max EXP and Stats for the NEW level
        // This will set the NEW maxExp (e.g., to 60) AFTER we already subtracted.
        recalculateDerivedStats();

        // 5. Full heal
        this.currHp = this.maxHp;
        this.currMana = this.maxMana;

        System.out.println("🎉 LEVEL UP! You are now level " + this.level);
    }

    public int takeDamage(int rawDamage) {
        // 1. Calculate Damage Reduction (Defense / 2)
        int damageReduction = this.def / 2;

        // 2. Calculate Actual Damage (Minimum 1)
        int actualDamage = Math.max(1, rawDamage - damageReduction);

        // 3. Apply to HP
        this.currHp -= actualDamage;
        if (this.currHp < 0)
            this.currHp = 0;

        // 4. RETURN the value so GameEngine knows what happened
        return actualDamage;
    }

    public void failedTask() {
        this.currHp = this.currHp - failedDmg;

        if (this.currHp < 0) {
            this.currHp = 0;
        }
    }

    // Used for End Day reset
    public void rest() {
        this.currMana = this.maxMana;
        this.currHp = Math.min(this.currHp + baseHeal, this.maxHp);
    }

    public boolean isDefeated() {
        return this.currHp <= 0;
    }

    // --- Dodge Charge Methods ---
    public boolean hasDodgeAvailable() {
        return this.dodgeCharges > 0;
    }

    public void useDodgeCharge() {
        if (this.dodgeCharges > 0)
            this.dodgeCharges--;
    }

    public void replenishDodgeCharge(int amount) {
        this.dodgeCharges = Math.min(this.maxDodgeCharges, this.dodgeCharges + amount);
    }

    // --- Getters and Setters ---
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getStatPoints() {
        return statPoints;
    }

    public void setStatPoints(int statPoints) {
        this.statPoints = statPoints;
    }

    public int getCurrExp() {
        return currExp;
    }

    public int getMaxExp() {
        return maxExp;
    }

    public int getCurrHp() {
        return currHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrMana() {
        return currMana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getDodgeCharges() {
        return dodgeCharges;
    }

    public int getStr() {
        return str;
    }

    public int getDef() {
        return def;
    }

    public int getIntel() {
        return intel;
    }

    public int getDex() {
        return dex;
    }

    public String getPlayerSprite() {
        return playerSprite;
    }

    public void setStr(int str) {
        this.str = str;
        recalculateDerivedStats();
    }

    public void setDef(int def) {
        this.def = def;
    }

    public void setIntel(int intel) {
        this.intel = intel;
        recalculateDerivedStats();
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public void setPlayerSprite(int choice) {
        if (choice == 1) {
            this.playerSprite = "                              \r\n" + //
                                "             ░▓▓▓▒            \r\n" + //
                                "         ░▓▓▓▓▓▓▒▓▓▓▓▓░       \r\n" + //
                                "     ░░▒▓▓▓▓▓▒▒▒▒▒▒▒▒▓▓▒░     \r\n" + //
                                "     ░█▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓░    \r\n" + //
                                "     ░█▓▓▓▓▒▒▓▓▓▓▓▓▓▓▓▓▒█░    \r\n" + //
                                "     ░█▓▓▓▓▒▒▓▒▓▓▒▒▒▒█▓▒█░    \r\n" + //
                                "     ░█▓▓▓▓▒▒▒▒▒░░▒▒░▒▒▒█░    \r\n" + //
                                "     ░█▓▓▓▓▓▓▓▒▒░░░░░▒▒▒█░    \r\n" + //
                                "     ▒▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓▒░   \r\n" + //
                                "     ░█▓▓▓▓█████████████░░    \r\n" + //
                                "     ▒▓██▓▓▓▓▓▓▓▒▒▒▓▓▓█░      \r\n" + //
                                "   ░▒▓█░█▓▓▓▓▓▓▓▓▓▓▓▓▓█░      \r\n" + //
                                "   ▒▓▒   ░██▓    ░▒██░        \r\n" + //
                                "         ░░░░    ░░░░         \r\n" + //
                                "                              ";
        } else if (choice == 2) {
            this.playerSprite = "        ░░░▓███▓░░            \r\n" + //
                                "      ░░▓▓▓▓▓▒▒▓▓█▒           \r\n" + //
                                "      ▒███▓▓▓▓▒▒▒▓█▒░░░       \r\n" + //
                                "       ░░▒▓▓▓▓▒▒▒▒▓███▒▒▒▒▒░░ \r\n" + //
                                "       ░▓█▓▓▒▒▒▒▒▒▒▒▒▒▓▓▓▓█▒░ \r\n" + //
                                "  ░░▒██▓▓▓▓▓▓▓▓▓▓██████▓░░    \r\n" + //
                                "  ░█▓▓▓█████▓▓▓▒▒▒▒▒▓▓██▓░    \r\n" + //
                                "      ▒█▓▓█▒░▒▓░░░░▒█▒██▓░    \r\n" + //
                                "    ░▓▓██████▓▓▓▓▓▓▓▓▓█▓▒░    \r\n" + //
                                "    ░▒█▓██▓▓▓▓▓▓▓▓▓▓▓▓▓░░     \r\n" + //
                                "     ░░░▓█████████████░░      \r\n" + //
                                "    ░▒██▓▓▓▓▒▒▓▓███▓▒▓▓▓░     \r\n" + //
                                "    ░▒▓▓▓▓▓▓▓████████▓▓▒░     \r\n" + //
                                "         ░▓█▒░   ░▓█▒░        \r\n" + //
                                "          ░░░     ░░░         ";
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Name: %s | Level: %d\n" +
                        "HP: %d/%d (Health) | Mana: %d/%d (Energy)\n" +
                        "EXP: %d/%d | Stat Points Available: %d\n" +
                        "Dodge Charges: %d/%d\n" +
                        "--------------------------------\n" +
                        "STR (Attack):  %d\n" +
                        "DEF (Defense): %d\n" +
                        "INT (Intel):   %d\n" +
                        "DEX (Agility): %d",
                name, level,
                currHp, maxHp, currMana, maxMana,
                currExp, maxExp, statPoints,
                dodgeCharges, maxDodgeCharges,
                str, def, intel, dex);
    }
}