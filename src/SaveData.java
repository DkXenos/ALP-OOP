import java.io.Serializable;
import java.util.HashMap;

public class SaveData implements Serializable {
    private static final long serialVersionUID = 2L;

    // Storyline progress
    public int storylineId;
    public int dialogueState;

    // GameState
    public HashMap<String, Integer> stats;
    public HashMap<String, Boolean> flags;
    public HashMap<String, Integer> inventoryQuantities; // Changed from 'inventory'

    public SaveData(int storylineId, int dialogueState, HashMap<String, Integer> stats, HashMap<String, Boolean> flags, HashMap<String, Integer> inventoryQuantities) {
        this.storylineId = storylineId;
        this.dialogueState = dialogueState;
        this.stats = stats;
        this.flags = flags;
        this.inventoryQuantities = inventoryQuantities; // Updated
    }
    
    public int getDialogueState() {
        return dialogueState;
    }
}