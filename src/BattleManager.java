import java.awt.Color;
import java.util.function.Consumer;
import javax.swing.Timer;

public class BattleManager {
    private GameUI ui;
    private GameState gameState;
    private String opponentName;
    private int opponentHealth;
    private int opponentAttack;
    private Consumer<BattleResult> onBattleEndCallback;
    private boolean isBattleActive = false;

    public enum BattleResult {
        WIN,
        LOSS
    }

    public BattleManager(GameUI ui, GameState gameState) {
        this.ui = ui;
        this.gameState = gameState;
    }

    public void startBattle(String name, int health, int attack, Consumer<BattleResult> callback) {
        this.opponentName = name;
        this.opponentHealth = health;
        this.opponentAttack = attack;
        this.onBattleEndCallback = callback;
        this.isBattleActive = true;

        // These messages will appear in the main text area before the UI switches
        ui.displayText("\n--- BATTLE START ---", Color.BLACK); // Changed to BLACK
        ui.displayText("\n" + opponentName + " appears!", Color.BLACK); // Changed to BLACK

        // Switch to battle UI and show initial stats
        ui.showBattleInterface(this.opponentName, gameState.getStat(GameState.PLAYER_HEALTH), this.opponentHealth);
        ui.appendBattleLog("--- Your Turn ---", Color.BLACK); // Changed to BLACK
    }

    public boolean isBattleActive() {
        return isBattleActive;
    }

    public void processPlayerTurn(int choice) {
        if (!isBattleActive) {
            return;
        }

        int playerAttackValue = gameState.getStat(GameState.PLAYER_ATTACK);

        switch (choice) {
            case 1: // Attack
                opponentHealth -= playerAttackValue;
                opponentHealth = Math.max(0, opponentHealth);
                ui.appendBattleLog("You attack " + opponentName + " for " + playerAttackValue + " damage!", Color.BLACK); // Changed to BLACK
                ui.appendBattleLog(opponentName + " health: " + opponentHealth, Color.BLACK); // Changed to BLACK
                ui.updateBattleInterfaceHealth(gameState.getStat(GameState.PLAYER_HEALTH), opponentHealth);

                if (opponentHealth <= 0) {
                    endBattle(BattleResult.WIN);
                    return;
                }
                break;
            case 2: // Use Item (Placeholder)
                ui.appendBattleLog("Item functionality not yet implemented.", Color.BLACK); // Changed to BLACK
                // Item use might take a turn or not, adjust opponent's turn accordingly
                break;
            default:
                ui.appendBattleLog("Invalid battle action. Try again.", Color.BLACK); // Changed to BLACK
                // Don't proceed to opponent's turn if action was invalid
                return; 
        }

        // Opponent's turn (if opponent is still alive and player didn't win)
        if (opponentHealth > 0) {
            gameState.adjustStat(GameState.PLAYER_HEALTH, -opponentAttack);
            int playerCurrentHealth = gameState.getStat(GameState.PLAYER_HEALTH);
            ui.appendBattleLog(opponentName + " attacks you for " + opponentAttack + " damage!", Color.BLACK); // Changed to BLACK
            ui.appendBattleLog("Your health: " + playerCurrentHealth, Color.BLACK); // Changed to BLACK
            ui.updateBattleInterfaceHealth(playerCurrentHealth, opponentHealth);

            if (playerCurrentHealth <= 0) {
                endBattle(BattleResult.LOSS);
                return;
            }
        }
        
        // If battle is still ongoing, prompt for next player turn
        if (isBattleActive) {
            ui.appendBattleLog("\n--- Your Turn ---", Color.BLACK); // Changed to BLACK
        }
    }

    // updateBattleUI() is no longer needed as UI updates are pushed directly

    private void endBattle(BattleResult result) {
        isBattleActive = false;
        if (result == BattleResult.WIN) {
            ui.appendBattleLog("\n--- VICTORY! ---", Color.BLACK); // Changed to BLACK
            ui.appendBattleLog("You defeated " + opponentName + "!", Color.BLACK); // Changed to BLACK
        } else {
            ui.appendBattleLog("\n--- DEFEAT ---", Color.BLACK); // Changed to BLACK
            ui.appendBattleLog("You were defeated by " + opponentName + ".", Color.BLACK); // Changed to BLACK
        }
        
        // Short delay before hiding battle UI to let player read final message
        Timer endBattleTimer = new Timer(2000, e -> {
            ui.hideBattleInterface();
            // Optionally, display a summary in the main text area after hiding battle UI
            if (result == BattleResult.WIN) {
                ui.displayText("\nYou reflect on your victory...", Color.BLACK);
            } else {
                ui.displayText("\nThe sting of defeat lingers...", Color.BLACK);
            }

            if (onBattleEndCallback != null) {
                onBattleEndCallback.accept(result);
            }
        });
        endBattleTimer.setRepeats(false);
        endBattleTimer.start();
    }
}