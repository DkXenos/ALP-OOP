import java.awt.Color;
import java.util.Map;

public class SmokingItem extends Item {
    private Map<String, Integer> statChanges; // e.g., {"nicotineAddictionLevel": 2, "playerWillpower": -1}
    private String usageMessage;

    public SmokingItem(String itemName, String description, String usageMessage, Map<String, Integer> statChanges) {
        super(itemName, description);
        this.usageMessage = usageMessage;
        this.statChanges = statChanges;
    }

    @Override
    public void applyEffect(GameState state, GameUI ui, String playerName) {
        ui.displayText("\n" + playerName + " " + usageMessage, Color.ORANGE.darker());
        
        StringBuilder effectsSystemMessage = new StringBuilder("\nSystem Status: ");
        boolean firstEffect = true;
        for (Map.Entry<String, Integer> entry : statChanges.entrySet()) {
            state.adjustStat(entry.getKey(), entry.getValue());
            if (!firstEffect) {
                effectsSystemMessage.append(", ");
            }
            effectsSystemMessage.append(entry.getKey()).append(" ");
            if (entry.getValue() >= 0) {
                effectsSystemMessage.append("+");
            }
            effectsSystemMessage.append(entry.getValue());
            firstEffect = false;
        }
        effectsSystemMessage.append(".");
        ui.displayText(effectsSystemMessage.toString(), Color.RED.darker());
    }
}
