import java.io.Serializable;
import java.util.HashMap;

public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L; // Important for versioning

    // Storyline progress
    public int storylineId; // e.g., 1 for Storyline1
    public int dialogueState; // Current stage within the storyline

    // GameState
    public HashMap<String, Integer> stats;
    public HashMap<String, Boolean> flags;

    // Add any other relevant data, e.g., inventory, battle state if saving during battles

    public SaveData(int storylineId, int dialogueState, HashMap<String, Integer> stats, HashMap<String, Boolean> flags) {
        this.storylineId = storylineId;
        this.dialogueState = dialogueState;
        this.stats = stats; // Store copies to avoid modification issues
        this.flags = flags;
    }
}