import java.awt.Color;
import java.util.Random;
import java.util.function.Consumer;
import javax.swing.Timer;
import java.util.Random;

public class BattleManager {
    private GameUI ui;
    private GameState gameState;
    // private String opponentName; // Will be replaced by currentEnemy.getName()
    // private int opponentHealth; // Will be replaced by currentEnemy.getCurrentHealth()
    // private int opponentAttack; // Will be replaced by currentEnemy.getAttack()
    private Enemy currentEnemy; // Stores the current enemy object
    private Consumer<BattleResult> onBattleEndCallback;
    private boolean isBattleActive = false;
    private Runnable midBattleEvent; // Add this field
    private boolean midBattleEventTriggered = false; // Add this field

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

        ui.displayText("\n\n--- BATTLE START ---", Color.BLACK);
        ui.displayText("\n" + currentEnemy.getName() + " appears!", Color.BLACK);

        ui.showBattleInterface(currentEnemy.getName(), gameState.getStat(GameState.PLAYER_HEALTH), currentEnemy.getCurrentHealth());
        ui.appendBattleLog("\n--- Your Turn ---", Color.BLACK);
    }

    public boolean isBattleActive() {
        return isBattleActive;
    }


    public int calculatePlayerDamage() {
            int effectiveAttack = gameState.getEffectiveAttack(); // Use effective attack (base + willpower)
            // Returns the effective attack value
            return effectiveAttack;
    }

    // Modified this method to allow setting events mid-battle
    public void setMidBattleEvent(Runnable event) {
        this.midBattleEvent = event;
        this.midBattleEventTriggered = false; // Always reset flag to allow new event
    }

    // Add this method
    public void pauseBattle() {
        // Battle is effectively paused - no automatic enemy turn processing
        // This can be expanded if needed
    }

    // Add this method  
    public void resumeBattle() {
        // Battle resumes normal processing
        // This can be expanded if needed
    }

    public void processPlayerTurn(int choice) {
        if (!isBattleActive || currentEnemy == null) {
            return;
        }

        switch (choice) {
            case 1: // Attack
                int damageDealt = calculatePlayerDamage();
                currentEnemy.takeDamage(damageDealt);
                ui.appendBattleLog("You attack " + currentEnemy.getName() + " for " + damageDealt + " damage!", Color.BLACK);
                ui.appendBattleLog(currentEnemy.getName() + " health: " + currentEnemy.getCurrentHealth(), Color.BLACK);
                ui.updateBattleInterfaceHealth(gameState.getStat(GameState.PLAYER_HEALTH), currentEnemy.getCurrentHealth());

                // Simple mid-battle event trigger (around 50% enemy health)
                if (!midBattleEventTriggered && midBattleEvent != null && 
                    currentEnemy.getCurrentHealth() <= currentEnemy.getMaxHealth() / 2) {
                    midBattleEventTriggered = true;
                    midBattleEvent.run(); // Just run the event
                    return; // Skip enemy turn this time
                }

                if (currentEnemy.isDefeated()) {
                    endBattle(BattleResult.WIN);
                    return;
                }
                break;
            default:
                ui.appendBattleLog("Invalid battle action. Try again.", Color.BLACK);
                return;
        }

        // Enemy turn processing
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
        midBattleEvent = null; // Clear the event
        midBattleEventTriggered = false; // Reset flag

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
                ui.displayText("\nYou reflect on your victory...\n", Color.ORANGE);
            } else {
                ui.displayText("\nThe sting of defeat lingers...\n", Color.ORANGE);
            }

            if (onBattleEndCallback != null) {
                onBattleEndCallback.accept(result);
            }
            this.currentEnemy = null; // Clear the enemy reference
        });
        endBattleTimer.setRepeats(false);
        endBattleTimer.start();
    }
}