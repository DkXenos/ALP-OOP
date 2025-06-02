// filepath: src/VapeEnemy.java
public class VapeEnemy extends Enemy {
    private static final int COUGHING_SKILL_EXTRA_DAMAGE = 3;

    public VapeEnemy(String name, int maxHealth, int attack) {
        super(name, maxHealth, attack);
    }

    //INI BELUM BISA DIPAKE DI BATTLE SCENE SKILL SKILL NYA, harus diupdate lagi pokoke

    /**
     * Performs an attack using the "Coughing" skill.
     * This attack deals the enemy's base attack damage plus bonus damage from the skill.
     * @return The total damage dealt by the coughing attack.
     */
    public int attackWithCoughingSkill() {
        // The BattleManager would typically announce this skill usage in the UI.
        // For example: ui.appendBattleLog(this.getName() + " uses a harsh Coughing attack!", Color.ORANGE);
        return this.attack + COUGHING_SKILL_EXTRA_DAMAGE;
    }

    // The regular getAttack() method from the parent Enemy class will still return the base attack.
    // public int getAttack() {
    //     return super.getAttack(); 
    // }
}