import java.awt.Color;
import java.util.Map;
import java.util.Random;

public class DrugItem extends Item {
    private Map<String, Integer> statChanges; 
    private String usageMessage;
    private static Random random = new Random();

    public DrugItem(String itemName, String description, String usageMessage, Map<String, Integer> statChanges) {
        super(itemName, description);
        this.usageMessage = usageMessage;
        this.statChanges = statChanges;
    }

    @Override
    public void applyEffect(GameState state, GameUI ui, String playerName) {
        // Display the use message
        ui.displayText("\n" + playerName + " " + usageMessage, Color.BLACK);
        
        // Apply stat effects
        if (statChanges != null) {
            for (Map.Entry<String, Integer> effect : statChanges.entrySet()) {
                String statKey = effect.getKey();
                Integer value = effect.getValue();
                state.adjustStat(statKey, value);
                
                // Display effect message
                String effectMsg = value > 0 ? "+" + value : String.valueOf(value);
                ui.displayText("\nEffect: " + statKey + " " + effectMsg, Color.ORANGE);
            }
        }
        
        // Drug-specific side effects
        if (random.nextInt(100) < 30) { // 30% chance of side effects
            int sideEffect = random.nextInt(3);
            switch (sideEffect) {
                case 0:
                    ui.displayText("\n" + playerName + "'s vision blurs and reality feels distant...", Color.RED);
                    state.adjustStat("playerAttack", -1);
                    ui.displayText("\nSide Effect: Attack -1 (disorientation)", Color.RED.darker());
                    break;
                case 1:
                    ui.displayText("\n" + playerName + " feels nauseous and weak...", Color.RED);
                    state.adjustStat("playerHealth", -2);
                    ui.displayText("\nSide Effect: Health -2 (nausea)", Color.RED.darker());
                    break;
                case 2:
                    ui.displayText("\n" + playerName + "'s heart races uncontrollably...", Color.RED);
                    state.adjustStat("playerDefense", -1);
                    ui.displayText("\nSide Effect: Defense -1 (anxiety)", Color.RED.darker());
                    break;
            }
        }
        
        // Withdrawal warning at high addiction levels - fix variable access
        int drugAddictionLevel = state.getStat("drugAddictionLevel");
        if (drugAddictionLevel > 5) {
            ui.displayText("\n" + playerName + " already craves the next dose... withdrawal symptoms are setting in.", Color.RED.darker());
        }
        
        // Drug-specific aftermath message
        ui.displayText("\n" + playerName + " feels the chemical effects taking hold of their mind...", Color.GRAY.darker());
    }
}
