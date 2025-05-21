import java.util.HashMap;

public class GameState {
    private HashMap<String, Integer> stats = new HashMap<>();
    private HashMap<String, Boolean> flags = new HashMap<>();

    // Define keys for player stats
    public static final String PLAYER_HEALTH = "playerHealth";
    public static final String PLAYER_MAX_HEALTH = "playerMaxHealth";
    public static final String PLAYER_ATTACK = "playerAttack";

    public GameState() {
        // Initialize default player stats
        setStat(PLAYER_MAX_HEALTH, 100);
        setStat(PLAYER_HEALTH, getStat(PLAYER_MAX_HEALTH)); // Start with full health
        setStat(PLAYER_ATTACK, 10);
    }

    public void setStat(String key, int value) {
        stats.put(key, value);
        // Ensure health doesn't exceed max_health or go below 0 immediately after setting
        if (key.equals(PLAYER_HEALTH)) {
            int currentHealth = stats.getOrDefault(key, 0);
            int maxHealth = stats.getOrDefault(PLAYER_MAX_HEALTH, currentHealth);
            stats.put(key, Math.min(Math.max(currentHealth, 0), maxHealth));
        }
    }

    public int getStat(String key) {
        return stats.getOrDefault(key, 0);
    }

    // Helper to adjust stats, e.g., taking damage or healing
    public void adjustStat(String key, int amount) {
        int currentValue = getStat(key);
        setStat(key, currentValue + amount);
    }

    public void setFlag(String key, boolean value) {
        flags.put(key, value);
    }

    public boolean getFlag(String key) {
        return flags.getOrDefault(key, false);
    }
}