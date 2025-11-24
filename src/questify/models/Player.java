package models;

public class Player {
    private String name;
    private int level;
    private int statPoints;
    private int currExp = 0;
    private int maxExp = 20;
    private int currHp;
    private int maxHp = 50;
    private int currMana;
    private int maxMana = 20;
    
    private int dodgeCharges; // NEW: Resource for avoiding daily penalties
    private final int maxDodgeCharges = 3; // Maximum charges allowed

    private int str; // Strength (Combat Damage, Max HP scaling)
    private int def; // Defense (Damage Reduction)
    private int intel; // Intelligence (EXP Multiplier, Max Mana scaling)
    private int dex; // Dexterity (Dodge/Hit Chance)

    public Player(String name) {
        this.name = name;
        this.level = 1;
        this.statPoints = 0;
        this.currExp = 0;
        this.currHp = this.maxHp;
        this.currMana = this.maxMana;
        this.dodgeCharges = 1; // Start with one charge
        this.str = 10;
        this.def = 10;
        this.intel = 10;
        this.dex = 10;
    }

    public boolean addExp(int exp) {
        this.currExp += exp;
        boolean leveledUp = false;
        while (this.currExp >= this.maxExp) {
            levelUp();
            leveledUp = true;
        }
        return leveledUp;
    }

    // New method for forced level-up reward from Boss defeat
    public void gainLevel() {
        levelUp();
    }
    
    private void levelUp() {
        this.level++;
        this.currExp -= this.maxExp;
        this.maxExp = 20 + (this.level * 10);
        // Max HP/Mana increases on level up based on primary stats
        this.maxHp = 50 + (this.str * 2);
        this.maxMana = 20 + (this.intel * 1);
        this.currMana = this.maxMana;
        // HP is not fully restored on level up to keep combat consequences
    }

    public void takeDamage(int rawDamage) {
        int actualDamage = Math.max(1, rawDamage - (this.def / 2));
        this.currHp -= actualDamage;
        if (this.currHp < 0) this.currHp = 0;
    }
    
    // NEW: Fully restore HP/Mana for End Day reset
    public void rest() {
        this.currHp = this.maxHp;
        this.currMana = this.maxMana;
    }

    public boolean isDefeated() {
        return this.currHp <= 0;
    }
    
    // --- New Dodge Charge Methods ---
    public boolean hasDodgeAvailable() {
        return this.dodgeCharges > 0;
    }
    
    public void useDodgeCharge() {
        if (this.dodgeCharges > 0) this.dodgeCharges--;
    }
    
    public void replenishDodgeCharge(int amount) {
        this.dodgeCharges = Math.min(this.maxDodgeCharges, this.dodgeCharges + amount);
    }

    // --- Getters and Setters ---
    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getStatPoints() { return statPoints; }
    public void setStatPoints(int statPoints) { this.statPoints = statPoints; }
    public int getCurrExp() { return currExp; }
    public int getMaxExp() { return maxExp; }
    public int getCurrHp() { return currHp; }
    public int getMaxHp() { return maxHp; }
    public int getCurrMana() { return currMana; }
    public int getMaxMana() { return maxMana; }
    public int getDodgeCharges() { return dodgeCharges; }

    public int getStr() { return str; }
    public int getDef() { return def; }
    public int getIntel() { return intel; }
    public int getDex() { return dex; }

    public void setStr(int str) { 
        this.str = str; 
        this.maxHp = 50 + (str * 2); // HP scales with STR
        if (currHp > maxHp) currHp = maxHp;
    }
    public void setDef(int def) { this.def = def; }
    public void setIntel(int intel) { 
        this.intel = intel; 
        this.maxMana = 20 + (intel * 1); // Mana scales with INTEL
        if (currMana > maxMana) currMana = maxMana;
    }
    public void setDex(int dex) { this.dex = dex; }
    
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
            str, def, intel, dex
        );
    }
}