package models;

public class Boss {
    private String name;
    private int currHp;
    private int maxHp;
    private int expReward;
    private int attackPower; 
    private int defense;

    public Boss(int level) {
        this.name = "The Level " + level + " Boss";
        this.maxHp = 10 + (level * 5); // Boss HP scales with level
        this.currHp = this.maxHp;
        this.expReward = level * 10;
        this.attackPower = 5 + (level / 2); // Boss attack scales with level
        this.defense = 2 + (level / 5);
    }

    public int takeDamage(int rawDamage) {
        // Boss defense reduces incoming damage
        int actualDamage = Math.max(1, rawDamage - (this.defense / 2)); 
        this.currHp -= actualDamage;
        if (this.currHp < 0) this.currHp = 0;
        return actualDamage;
    }

    public boolean isDefeated() {
        return currHp <= 0;
    }

    // --- Getters ---
    public String getName() { return name; }
    public int getCurrHp() { return currHp; }
    public int getMaxHp() { return maxHp; }
    public int getExpReward() { return expReward; }
    public int getAttackPower() { return attackPower; }

    public void setName(String bossName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setName'");
    }
}