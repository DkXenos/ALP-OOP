import java.awt.Color;
import java.util.function.Consumer;
import javax.swing.Timer;

public class BattleManager {
    private GameUI ui;
    private GameState gameState;
    // private String opponentName; // Will be replaced by currentEnemy.getName()
    // private int opponentHealth; // Will be replaced by currentEnemy.getCurrentHealth()
    // private int opponentAttack; // Will be replaced by currentEnemy.getAttack()
    private Enemy currentEnemy; // Stores the current enemy object
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

    // Modified startBattle method
    public void startBattle(Enemy enemy, Consumer<BattleResult> callback) {
        this.currentEnemy = enemy;
        // this.opponentName = enemy.getName(); // No longer needed as separate field
        // this.opponentHealth = enemy.getCurrentHealth(); // No longer needed as separate field
        // this.opponentAttack = enemy.getAttack(); // No longer needed as separate field
        this.onBattleEndCallback = callback;
        this.isBattleActive = true;

        ui.displayText("\n--- BATTLE START ---", Color.BLACK);
        ui.displayText("\n" + currentEnemy.getName() + " appears!", Color.BLACK);

        ui.showBattleInterface(currentEnemy.getName(), gameState.getStat(GameState.PLAYER_HEALTH), currentEnemy.getCurrentHealth());
        ui.appendBattleLog("--- Your Turn ---", Color.BLACK);
    }

    public boolean isBattleActive() {
        return isBattleActive;
    }

    public void processPlayerTurn(int choice) {
        if (!isBattleActive || currentEnemy == null) {
            return;
        }

        int playerAttackValue = gameState.getStat(GameState.PLAYER_ATTACK);

        switch (choice) {
            case 1: // Attack
                currentEnemy.takeDamage(playerAttackValue);
                ui.appendBattleLog("You attack " + currentEnemy.getName() + " for " + playerAttackValue + " damage!", Color.BLACK);
                ui.appendBattleLog(currentEnemy.getName() + " health: " + currentEnemy.getCurrentHealth(), Color.BLACK);
                ui.updateBattleInterfaceHealth(gameState.getStat(GameState.PLAYER_HEALTH), currentEnemy.getCurrentHealth());

                if (currentEnemy.isDefeated()) {
                    endBattle(BattleResult.WIN);
                    return;
                }
                break;
            case 2: // Use Item (Placeholder)
                ui.appendBattleLog("Item functionality not yet implemented.", Color.BLACK);
                // Item use might take a turn or not, adjust opponent's turn accordingly
                break;
            default:
                ui.appendBattleLog("Invalid battle action. Try again.", Color.BLACK);
                return; 
        }

        // Opponent's turn (if opponent is still alive and player didn't win)
        if (!currentEnemy.isDefeated()) {
            int enemyAttackValue = currentEnemy.getAttack();
            gameState.adjustStat(GameState.PLAYER_HEALTH, -enemyAttackValue);
            int playerCurrentHealth = gameState.getStat(GameState.PLAYER_HEALTH);
            ui.appendBattleLog(currentEnemy.getName() + " attacks you for " + enemyAttackValue + " damage!", Color.BLACK);
            ui.appendBattleLog("Your health: " + playerCurrentHealth, Color.BLACK);
            ui.updateBattleInterfaceHealth(playerCurrentHealth, currentEnemy.getCurrentHealth());

            if (playerCurrentHealth <= 0) {
                endBattle(BattleResult.LOSS);
                return;
            }
        }
        
        if (isBattleActive) {
            ui.appendBattleLog("\n--- Your Turn ---", Color.BLACK);
        }
    }

    private void endBattle(BattleResult result) {
        isBattleActive = false;
        String enemyNameForMessage = (currentEnemy != null) ? currentEnemy.getName() : "The opponent";

        if (result == BattleResult.WIN) {
            ui.appendBattleLog("\n--- VICTORY! ---", Color.BLACK);
            ui.appendBattleLog("You defeated " + enemyNameForMessage + "!", Color.BLACK);
        } else {
            ui.appendBattleLog("\n--- DEFEAT ---", Color.BLACK);
            ui.appendBattleLog("You were defeated by " + enemyNameForMessage + ".", Color.BLACK);
        }
        
        Timer endBattleTimer = new Timer(2000, e -> {
            ui.hideBattleInterface();
            if (result == BattleResult.WIN) {
                ui.displayText("\nYou reflect on your victory...", Color.BLACK);
            } else {
                ui.displayText("\nThe sting of defeat lingers...", Color.BLACK);
            }

            if (onBattleEndCallback != null) {
                onBattleEndCallback.accept(result);
            }
            this.currentEnemy = null; // Clear the enemy reference
        });
        // registerTimer(endBattleTimer); // This timer is internal to BattleManager, not Storyline
        endBattleTimer.setRepeats(false);
        endBattleTimer.start();
    }
}