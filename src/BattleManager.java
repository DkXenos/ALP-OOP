import java.awt.Color;
import java.util.Random;
import java.util.function.Consumer;
import javax.swing.Timer;
import java.util.Random;

public class BattleManager {
    private GameUI ui;
    private GameState gameState;
    private Enemy currentEnemy;
    private Consumer<BattleResult> onBattleEndCallback;
    private boolean isBattleActive = false;
    private Runnable midBattleEvent;
    private boolean midBattleEventTriggered = false;

    public enum BattleResult {
        WIN,
        LOSS
    }

    public BattleManager(GameUI ui, GameState gameState) {
        
        this.ui = ui;
        this.gameState = gameState;
    }

    public void startBattle(Enemy enemy, Consumer<BattleResult> callback) {
        this.currentEnemy = enemy;
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
            int effectiveAttack = gameState.getEffectiveAttack();
            return effectiveAttack;
    }

    public void setMidBattleEvent(Runnable event) {
        this.midBattleEvent = event;
        this.midBattleEventTriggered = false;
    }

    public void pauseBattle() {
    }

    public void resumeBattle() {
    }

    public void processPlayerTurn(int choice) {
        if (!isBattleActive || currentEnemy == null) {
            return;
        }

        switch (choice) {
            case 1:
                int damageDealt = calculatePlayerDamage();
                currentEnemy.takeDamage(damageDealt);
                ui.appendBattleLog("You attack " + currentEnemy.getName() + " for " + damageDealt + " damage!", Color.BLACK);
                ui.appendBattleLog(currentEnemy.getName() + " health: " + currentEnemy.getCurrentHealth(), Color.BLACK);
                ui.updateBattleInterfaceHealth(gameState.getStat(GameState.PLAYER_HEALTH), currentEnemy.getCurrentHealth());

                if (!midBattleEventTriggered && midBattleEvent != null && 
                    currentEnemy.getCurrentHealth() <= currentEnemy.getMaxHealth() / 2) {
                    midBattleEventTriggered = true;
                    midBattleEvent.run();
                    return;
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
        midBattleEvent = null;
        midBattleEventTriggered = false;

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
            this.currentEnemy = null;
        });
        endBattleTimer.setRepeats(false);
        endBattleTimer.start();
    }
}