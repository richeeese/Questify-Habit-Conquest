package models;

public class Player {

    // --- Identity & Core Stats ---
    private String username;
    private int level;
    private int statPoints; // Points available for allocation

    // --- Resources ---
    private int currentHp;
    private int maxHp;
    private int currentMana;
    private int maxMana;

    // --- Experience ---
    private int currentExp;
    private int expToNextLevel;

    // --- Battle Stats ---
    private int strength; // STR - Damage dealt
    private int defense; // DEF - Damage taken reduction
    private int intelligence; // INT - EXP multiplier
    private int dexterity; // DEX - Dodge chance

    // --- Dodge Resource ---
    private int dodgeCharges;
    private int maxDodgeCharges;

    // ---------------------------------------------------------
    // 🏗️ CONSTRUCTOR
    // ---------------------------------------------------------
    public Player(String username) {
        this.username = username;
        this.level = 1;
        this.statPoints = 0;

        // Base Stats
        this.maxHp = 50;
        this.currentHp = this.maxHp;
        this.maxMana = 20;
        this.currentMana = this.maxMana;

        this.currentExp = 0;
        this.expToNextLevel = 20; // Starting EXP requirement

        // Attributes (Set to 10 as per your preference)
        this.strength = 10;
        this.defense = 10;
        this.intelligence = 10;
        this.dexterity = 10;

        // Dodge Logic (1 Free Dodge per day)
        this.maxDodgeCharges = 1;
        this.dodgeCharges = this.maxDodgeCharges;
    }

    // ---------------------------------------------------------
    // ⚔️ GAME LOGIC METHODS
    // ---------------------------------------------------------

    /**
     * Adds EXP to the player.
     * 
     * @return true if the player leveled up (Engine uses this to trigger bonuses).
     */
    public boolean addExp(int amount) {
        this.currentExp += amount;
        System.out.println("✨ " + this.username + " gained " + amount + " EXP.");

        // Check if we leveled up
        if (this.currentExp >= this.expToNextLevel) {
            levelUp();
            return true;
        }
        return false;
    }

    private void levelUp() {
        this.level++;
        this.currentExp -= this.expToNextLevel; // Carry over excess EXP
        this.expToNextLevel = (int) (this.expToNextLevel * 1.5); // Increase difficulty

        // Restore HP/Mana on Level Up
        this.currentHp = this.maxHp;
        this.currentMana = this.maxMana;

        System.out.println("🎉 LEVEL UP! You are now level " + this.level);
    }

    public void takeDamage(int damage) {
        // Simple damage reduction: Damage - (Defense / 2)
        int effectiveDamage = damage - (this.defense / 2);

        // Always take at least 1 damage unless it was a dodge (handled in Engine)
        if (effectiveDamage < 1)
            effectiveDamage = 1;

        this.currentHp -= effectiveDamage;
        if (this.currentHp < 0)
            this.currentHp = 0;

        System.out.println(
                "💔 " + this.username + " took " + effectiveDamage + " damage. (HP: " + currentHp + "/" + maxHp + ")");
    }

    public boolean isDefeated() {
        return this.currentHp <= 0;
    }

    /**
     * Allows the user to spend a stat point.
     */
    public boolean allocateStat(String stat) {
        if (this.statPoints <= 0)
            return false;

        boolean success = true;
        switch (stat.toLowerCase()) {
            case "str":
                this.strength++;
                break;
            case "def":
                this.defense++;
                break;
            case "int":
                this.intelligence++;
                break;
            case "dex":
                this.dexterity++;
                break;
            default:
                success = false;
        }

        if (success) {
            this.statPoints--;
            System.out.println("💪 Upgraded " + stat.toUpperCase() + "!");
        }
        return success;
    }

    // ---------------------------------------------------------
    // 🛡️ DODGE CHARGE METHODS
    // ---------------------------------------------------------

    public boolean hasDodgeAvailable() {
        return dodgeCharges > 0;
    }

    public int getDodgeCharges() {
        return dodgeCharges;
    }

    public void decrementDodge() {
        if (dodgeCharges > 0) {
            dodgeCharges--;
        }
    }

    public void refreshDodges() {
        // Reset to max at start of new day
        this.dodgeCharges = this.maxDodgeCharges;
    }

    // ---------------------------------------------------------
    // 📤 GETTERS & SETTERS
    // ---------------------------------------------------------

    public String getUsername() {
        return username;
    }

    public int getLevel() {
        return level;
    }

    public int getStatPoints() {
        return statPoints;
    }

    public void setStatPoints(int p) {
        this.statPoints = p;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getCurrentExp() {
        return currentExp;
    }

    public int getExpToNextLevel() {
        return expToNextLevel;
    }

    public int getStrength() {
        return strength;
    }

    public int getDefense() {
        return defense;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getDexterity() {
        return dexterity;
    }

    @Override
    public String toString() {
        return String.format(
                "Name: %s | Lvl: %d | HP: %d/%d | EXP: %d/%d\nStats: [STR: %d] [DEF: %d] [INT: %d] [DEX: %d]\nDodges: %d | Points: %d",
                username, level, currentHp, maxHp, currentExp, expToNextLevel,
                strength, defense, intelligence, dexterity, dodgeCharges, statPoints);
    }
}