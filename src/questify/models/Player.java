package models;

// NOTE: No import java.util.UUID is needed here as this class does not generate a UUID.

public class Player {
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

    public void gainLevel() {
        levelUp();
    }

    private void levelUp() {
        // 1. CRITICAL FIX: Subtract the cost BEFORE changing the level
        // We pay the cost of the CURRENT level (e.g., Level 3 cost)
        this.currExp -= this.maxExp;

        // 2. Now increase the level
        this.level++;

        // 3. Grant Stat Points
        this.statPoints += (this.level % 5 == 0) ? 2 : 1;

        // 4. Calculate new Max EXP and Stats for the NEW level
        recalculateDerivedStats();

        // 5. Restore Health/Mana
        this.currHp = this.maxHp;
        this.currMana = this.maxMana;

        System.out.println("🎉 LEVEL UP! You are now level " + this.level);
    }

    public void takeDamage(int rawDamage) {
        // Damage reduction based on DEF
        int actualDamage = Math.max(1, rawDamage - (this.def / 2));
        this.currHp -= actualDamage;
        if (this.currHp < 0)
            this.currHp = 0;
    }

    public void failedTask() {
        this.currHp = this.currHp - failedDmg;
    }

    // Used for End Day reset
    public void rest() {
        this.currMana = this.maxMana;
        this.currHp = this.currHp + baseHeal;
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