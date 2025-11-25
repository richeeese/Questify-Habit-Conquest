package models;

public class Boss {

    private String name;
    private int level; 
    private int maxHp;
    private int currHp;
    private int attackPower; // Field that needs the setter
    private int expReward;
    private boolean isDefeated;

    public Boss(int level) {
        this.level = level;
        // Boss stats scale heavily with level
        this.maxHp = 50 + (level * 15);
        this.currHp = this.maxHp;
        this.attackPower = 5 + (level * 2);
        this.expReward = 100 * (level / 10); 
        this.isDefeated = false;
        this.name = "Level " + level + " Boss"; 
    }

    // --- FIX: ADDED SETTER FOR ATTACK POWER ---
    /**
     * Allows GameEngine to set unique attack traits for specific bosses.
     */
    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }
    // ------------------------------------
    
    // --- Existing Getters ---
    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrHp() {
        return currHp;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getExpReward() {
        return expReward;
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    // --- Core Logic ---
    public int takeDamage(int damage) {
        if (isDefeated) return 0;
        
        this.currHp -= damage;
        if (this.currHp <= 0) {
            this.currHp = 0;
            this.isDefeated = true;
        }
        return damage;
    }
}