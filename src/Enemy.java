public class Enemy {
    protected String name;
    protected int maxHealth;
    protected int currentHealth;
    protected int attack;

    public Enemy(String name, int health, int attack) {
        this.name = name;
        this.maxHealth = health;
        this.currentHealth = health;
        this.attack = attack;
    }

    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getAttack() {
        return attack;
    }

    public void takeDamage(int damage) {
        currentHealth = Math.max(0, currentHealth - damage);
    }

    public boolean isDefeated() {
        return currentHealth <= 0;
    }
}