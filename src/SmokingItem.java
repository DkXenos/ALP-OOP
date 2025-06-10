import java.awt.Color;
import java.util.Map;
import java.util.Random;

public class SmokingItem extends Item {
    private Map<String, Integer> statChanges;
    private String usageMessage;
    private static Random random = new Random();

    public SmokingItem(String itemName, String description, String usageMessage, Map<String, Integer> statChanges) {
        super(itemName, description);
        this.usageMessage = usageMessage;
        this.statChanges = statChanges;
    }

    @Override
    public void applyEffect(GameState state, GameUI ui, String playerName) {
        // Display the use message
        ui.displayText("\n" + playerName + " " + usageMessage, Color.BLACK);
        
        if (statChanges != null) {
            for (Map.Entry<String, Integer> effect : statChanges.entrySet()) {
                String statKey = effect.getKey();
                Integer value = effect.getValue();
                state.adjustStat(statKey, value);
                
                String effectMsg = value > 0 ? "+" + value : String.valueOf(value);
                ui.displayText("\nEffect: " + statKey + " " + effectMsg, Color.ORANGE);
            }
        }
        
        if (random.nextInt(100) < 25) { 
            int sideEffect = random.nextInt(3);
            switch (sideEffect) {
                case 0:
                    ui.displayText("\n" + playerName + " coughs violently, lungs burning...", Color.RED);
                    state.adjustStat("playerHealth", -1);
                    ui.displayText("\nSide Effect: Health -1 (lung irritation)", Color.RED.darker());
                    break;
                case 1:
                    ui.displayText("\n" + playerName + " feels lightheaded and dizzy...", Color.RED);
                    state.adjustStat(GameState.PLAYER_MAX_HEALTH, -1);
                    ui.displayText("\nSide Effect: Max Health -1 (dizziness)", Color.RED.darker());
                    break;
                case 2:
                    ui.displayText("\n" + playerName + "'s hands shake slightly from nicotine...", Color.RED);
                    state.adjustStat("playerAttack", -1);
                    ui.displayText("\nSide Effect: Attack -1 (hand tremors)", Color.RED.darker());
                    break;
            }
        }
        
        int nicotineAddictionLevel = state.getStat("nicotineAddictionLevel");
        if (nicotineAddictionLevel > 4) {
            ui.displayText("\n" + playerName + " feels the familiar urge for another smoke building...", Color.RED.darker());
        }
        
        ui.displayText("\n" + playerName + " tastes ash and feels the nicotine coursing through their system...", Color.GRAY.darker());
    }
}
