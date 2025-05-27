import java.util.HashMap;

public class GameState {
    private HashMap<String, Integer> stats = new HashMap<>();
    private HashMap<String, Boolean> flags = new HashMap<>();

    public void setStat(String key, int value) {
        stats.put(key, value);
    }

    public int getStat(String key) {
        return stats.getOrDefault(key, 0);
    }

    public void setFlag(String key, boolean value) {
        flags.put(key, value);
    }

    public boolean getFlag(String key) {
        return flags.getOrDefault(key, false);
    }
}