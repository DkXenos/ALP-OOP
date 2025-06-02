
public class Enemy {
    protected String name;
    protected int maxHealth;
    protected int currentHealth;
    protected int attack;
    // You could add more common properties like defense, resistances, special attack names, etc.

    public Enemy(String name, int maxHealth, int attack) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth; // Enemies start at full health
        this.attack = attack;
    }

    public String getName() {
        return name;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getAttack() {
        return attack;
    }

    public boolean isDefeated() {
        return currentHealth <= 0;
    }

    public void takeDamage(int damage) {
        this.currentHealth -= damage;
        if (this.currentHealth < 0) {
            this.currentHealth = 0;
        }
    }

    // Optional: A method for the enemy's turn logic, if it becomes more complex
    // public void performAction(GameState gameState, GameUI ui) {
    //     // Default action: basic attack
    //     gameState.adjustStat(GameState.PLAYER_HEALTH, -this.attack);
    //     ui.appendBattleLog(this.name + " attacks you for " + this.attack + " damage!", Color.RED);
    // }
}