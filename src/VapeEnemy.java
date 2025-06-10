// filepath: src/VapeEnemy.java
public class VapeEnemy extends Enemy {
    private static final int COUGHING_SKILL_EXTRA_DAMAGE = 3;

    public VapeEnemy(String name, int maxHealth, int attack) {
        super(name, maxHealth, attack);
    }

    public int attackWithCoughingSkill() {
        return this.attack + COUGHING_SKILL_EXTRA_DAMAGE;
    }

}