package models;

public class Player {
    // ------ Player Attributes ------
    private String name;
    private int level;
    private int statPoints;

    // ------ Core Attributes ------
    private int currExp = 0;
    private int maxExp = 20;
    private int currHp;
    private int maxHp = 50;
    private int currMana;
    private int maxMana = 20;

    // ------ Player Stats ------
    private int str; // damage multiplier/adder
    private int def; // damage reduction
    private int intel; // exp gains
    private int dex; // dodge chance
    private String playerSprite;

    public Player(String name) {
        this.name = name;
        this.level = 1;
        this.statPoints = 0;
        this.currExp = 0;
        this.currHp = this.maxHp;
        this.currMana = this.maxMana;
        this.str = 10;
        this.def = 10;
        this.intel = 10;
        this.dex = 10;
    }

    // ------ Getters ------
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getStatPoints() {
        return statPoints;
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

    // ------ Setters ------
    public void setCurrExp(int currExp) {
        this.currExp = currExp;
    }

    public void setCurrHp(int currHp) {
        this.currHp = currHp;
    }

    public void setCurrMana(int currMana) {
        this.currMana = currMana;
    }

    void setPlayerSprite(int spriteChoice) {
        if (spriteChoice == 1) {
            this.playerSprite = "                                                            \r\n" + //
                                "                        ░░▓▒░░░░░░                          \r\n" + //
                                "                      ░░▓█▓▓▓▓▓▓▓▓▓░░                       \r\n" + //
                                "                     ░▒███▓▓▓▓▓▓▓▓▓▒░░░                     \r\n" + //
                                "                  ░░░░░▓▓▓▓▓▓▓▓▓▓▓█▓▓░░                     \r\n" + //
                                "     ░░░▒▒▓░░     ░░▒██▓█▓▓██████████░░                     \r\n" + //
                                "     ░▒▓▒▒▓▓░░      ░█████▓▓▓███▓████▒░                     \r\n" + //
                                "     ░░░▓██▓▒░     ░░▒█░██▒▓██▒▒▓█▒█░░░                     \r\n" + //
                                "       ░░░░██▓▒   ░░▒▓█▒▒▓░░▒░░░░▒░░░                       \r\n" + //
                                "           ░███▒░░▒██▓░▓██▒▒░░░░░░░░                        \r\n" + //
                                "            ░██▓▓█████░▒▒██▓▓▓▓▒▒░░░░░░                     \r\n" + //
                                "          ░░▒▒▓█▓▒▒▒▒░▒▓▒▒▒▒▓███▓▒▓▓░▒░░░                   \r\n" + //
                                "          ░▓▓██▒▒░░░░▒▒▒█▒░░▒▒▒▒▒▒▒▓▒▒▓▒░░                  \r\n" + //
                                "        ░░█▓░▓▓▓▒▒▒▒▒▒▒▓█▓░░░░▒▒▒░▒▓▓▓▓▓▒░                  \r\n" + //
                                "        ░░░░░░▒▒▒▒▒▒▒▒▒█▒▒▓▓░░░░░░░░█▒▒▒░░                  \r\n" + //
                                "             ░▓▓▒▒▒▒▓███▒░░▒▓▓▓▒░░░░████▒░                  \r\n" + //
                                "           ░░░▓████████▒▒░░░░▒▓▓▓▒▒▒████▒░                  \r\n" + //
                                "          ░░▓▒░▓▓▓█████▒▒▒▒▒▒░▒▒▒▒▓▒████▓█▒░                \r\n" + //
                                "          ░█▓▒▒▒▒▓█▓▒░░▒███▓▓▓▒▒▒▓████████▒░░               \r\n" + //
                                "          ░▓█▓▒▒▒▓█▒░▒▒▒██▓█▓▒▒▓▓▒▓███████▓▒▒               \r\n" + //
                                "           ░▓▓▓▓▓▓▓░░▓▓█▓▓██▓█▓▓▓███▓█▓██▓▓▓▓               \r\n" + //
                                "            ░▓█▓▓▓▓░▒█▓▓▓█▓▓▓▒▓▓▓▒▓▓██▓██▓▓▓▓               \r\n" + //
                                "            ░▓▓▓▓▓█▓░▓▓█▓▓█▓░░░▒▒░▒▓█▓▓██▓▓▓▓               \r\n" + //
                                "           ░░▓▓▓▓▓▓███████░░░░░░▓░░░▒████▓▓▓▓               \r\n" + //
                                "            ░██▓▓█████▒▒▒░░░░░░░▓░░░░▒██████▓               \r\n" + //
                                "            ░░▓███▓░▒▒▒░░░░░░░░░▓░░░░▒▒▒██▓░░               \r\n" + //
                                "             ░░░░░░░░▒░░░░░░░░░░▓░░░░░▒░░░░                 \r\n" + //
                                "                  ░▒▓▒░░░░░░░░░░▓░▒░░░▒▓▒░                  \r\n" + //
                                "                  ░▒▓▒▒░▒░░░▒▒░▒▓▒▒░▒░▒█▓▒░░░               \r\n" + //
                                "                  ░▒▓▓▓▓▓▓▓▓▓▓░░██▓▓▓▓▓██▓▒░░               \r\n" + //
                                "                  ░░▒██████▒░░░░█████████▓▒▒▒░░░            \r\n" + //
                                "                  ░▒█▓▓█▓▓▒░   ░▓██▓▓▓▓███▓▒▒▒▒░            \r\n" + //
                                "                  ░▒██▓███░    ░▒████▓█░▒██▓▓▒▒▓░░          \r\n" + //
                                "                  ░▒███▓██░░   ░░░██▓▓█░░░▒██▓▓▓░░          \r\n" + //
                                "                  ░▒█▓▓██░░░░░░░░░████▓▓░░░░▒▒▒░            \r\n" + //
                                "          ░░░░░░░░▒█▓▓▓███▓▓▓▓▒░▓▓████▓▓█▒░░░░░░░░          \r\n" + //
                                "        ░░▒░▒▓▒▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒░▓▒░░         \r\n" + //
                                "            ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░                  \r\n" + //
                                "                                                            \r\n" + //
                                "                                                            ";
        } else if (spriteChoice == 2) {
            this.playerSprite = "                                                            \r\n" + //
                                "                           ▒▒▒▒▒▒▒▒                         \r\n" + //
                                "                          ▒▒▒▒▒▒▒░░░                        \r\n" + //
                                "                          ▒▒██▓▓▒▒░▒                        \r\n" + //
                                "                         ▓▒▒▓▓▓▓▓▒▒▒                        \r\n" + //
                                "                         ▓▒▒▓█▓▓▓▒▒▒▒▓                      \r\n" + //
                                "                         ▒▒▒░░▒▒▒▒▒▒▒▓                      \r\n" + //
                                "                        ▓▒▒▒▒▓▓▓▓▒▒▒▒▒▓                     \r\n" + //
                                "                         ▓▓▒▓▓██▓▓▓▓▒▒▒▓                    \r\n" + //
                                "                        ▓▓▒▒▓██▒▓▒▒▓▓▒▒▓                    \r\n" + //
                                "                         ▒▒▒▓▓▓▓▓▒▒░▒▒▒▓                    \r\n" + //
                                "                         ▒▒▒▓███▓▒▒▒▒▒▒▓                    \r\n" + //
                                "                         ▓▓▒▓██▓▓▒▒░░▓▓▓                    \r\n" + //
                                "                       ▓▓▓▒▒▒▓▓▓▓▒▒░░▒█▓▒▓                  \r\n" + //
                                "                      ▒▒▓▒▒▒▒▒▒▒▒▒▒▒░░▓▓▒▒▓                 \r\n" + //
                                "                      ▒▓▒░▒▓▓▓▓▓▒▒░▒▒░▒▓▒░▒▓                \r\n" + //
                                "                    ▒▒▓▒▒▒▓▓▓▓▒▒▒▓▓▓▓░▒▓▒░░▒█               \r\n" + //
                                "                    ▒▒▒▒▓▓▓▓▓▓▓▓██▓▓▓▓▓▓▒▒▒▒▒█▓             \r\n" + //
                                "                   ▓▒░▒▒▓█▓▓▒▓▓▓███▓▓▓▓▓▒▒▒▒▒▒▓▓            \r\n" + //
                                "                 ▓▓▒░  ▒▓█▓██▓█▓███▓▓▓▒▒▒▒▒▒▒▒▒▓▓▓▓         \r\n" + //
                                "                █▓▒     ▓▓▓██▓█▒███▓▓▒▒░▒▒▒▒▒▒▒▒▒▓▓▓        \r\n" + //
                                "              ██▓▒       ▓▒▓▓▒▒░▓▓▒▒▒▒▒▒▒▒▒▒▒▒▓▒▒▒▓██       \r\n" + //
                                "            ██▓▓         ▓▓▓▒▒░░▒▓▓▓▓▒▒▒▒▒▒▒▒▒▓▓▓▒▒▓▓       \r\n" + //
                                "           ▓▓▓           ▓▓▓▓▒░░▒▓▓▓▓▒▒▒▒▒▒▒▓▒▒▓▓▓▓▓        \r\n" + //
                                "         ▒▒▒▒           ▒▒▒▒▒▒▒▒▒▒▓▒▒▒▒▓▓▓▓▒▒▒▓▒▒▓▓         \r\n" + //
                                "         ▒▒              ▒▒▒░▒▒▒▒▒▓▒▒▒▒▓▓▓▓▓▒▒▒▓            \r\n" + //
                                "                         ▒▒░░░   ▒▒▒▒░ ▒▓▓                  \r\n" + //
                                "                         ▒▓▒▒     ▒▓▒▒                      \r\n" + //
                                "                        ▒▓▒▒      ▒▓▒▒                      \r\n" + //
                                "                        ▒▓▒▒      ▒▒▒▒                      \r\n" + //
                                "                      ▓▒▓▒▒▒      ▒▓▓▒                      \r\n" + //
                                "                     ▒▒▒▒░░░     ▒▓▓▓▒                      \r\n" + //
                                "                                                            ";
        }
    }

    // ------ Adders ------
    public void addExp(int exp) {
        this.currExp += exp;
    }

    public void addStatPoints(int points) {
        this.statPoints += points;
    }

    
}
