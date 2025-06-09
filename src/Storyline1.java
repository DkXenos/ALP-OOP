import java.awt.Color;
import javax.swing.Timer;

public class Storyline1 extends Storyline {
    private int dialogueState = 0;
    private BattleManager battleManager;

    public Storyline1(GameUI ui, GameState state) {
        super(ui, state);
        this.battleManager = new BattleManager(ui, state);
    }

    @Override
    public void startStory() {
        startStory(false);
    }

    public void startStory(boolean fromSave) {
        // Only initialize if NOT loading from save
        if (!fromSave) {
            Timer timer = new Timer(500, e ->{
                ui.displayText("[Lullaby of Empty Bottles]", Color.BLACK);
            });
            timer.setRepeats(false);
            timer.start();
            showDialogue(0);
        } else {
            // Loading from save - get the saved dialogue state and show that stage
            int savedDialogueState = state.getStat("currentDialogueState");
            showDialogue(savedDialogueState);
        }
    }

    public BattleManager getBattleManager() {
        return this.battleManager;
    }

    public int getDialogueState() {
        return dialogueState;
    }

    public void setDialogueState(int dialogueState) {
        this.dialogueState = dialogueState;
    }
    
    public void showDialoguePublic(int state) {
        // Instead of always starting from the beginning, use a switch or if-else to jump to the correct state
        switch(state) {
            case 0:
                showStage0();
                break;
            case 1:
                showStage1();
                break;
            case 2:
                showStage2();
                break;
            case 3:
                showStage3(); 
                break;
            case 4:
                showStage4();
                break;
            default:
                // fallback or error
                break;
        }
    }

    private void showDialogue(int stage) {
        dialogueState = stage;
        switch (stage) {
            case 0 -> showStage0();
            case 1 -> showStage1();
            case 2 -> showStage2();
            case 3 -> showStage3(); 
            case 4 -> showStage4();
        }
    }

    @Override
    public void handleChoice(int choice) {
        if (battleManager.isBattleActive()) {
            battleManager.processPlayerTurn(choice);
        } else {
            handleDialogueChoice(choice);
        }
    }

    private void handleDialogueChoice(int choice) {
        switch(dialogueState) {
            case 0:
                if(choice == 1) {
                    state.setStat(GameState.PLAYER_HEALTH, state.getStat(GameState.PLAYER_HEALTH) + 5);
                    ui.displayText("\nYou chose to meet. You feel a bit more sociable.", Color.BLACK);
                    showDialogue(1);
                } else {
                    ui.displayText("\nYou decided to stay in. The silence feels heavy.", Color.BLACK);
                    state.setFlag("choseToIsolate_d0", true);
                    showDialogue(1); 
                }
                break;
            case 1:
                showDialogue(2);
                break;
            case 2:
                if (choice == 1) { 
                    ui.displayText("\nYour choice leads to a confrontation!", Color.BLACK);
                    Timer battleStartTimer = new Timer(1500, e -> {
                        Enemy enemy = new Enemy("Temptation: Cigarette", 10, 5); // Create enemy
                        battleManager.startBattle(enemy, battleResult -> { //ini maish ada eror, ini kek teko for now gituan
                            if (battleResult == BattleManager.BattleResult.WIN) {
                                showDialogue(3); 
                            } else {
                                ui.displayText("\nGame Over (for now).", Color.BLACK);
                            }
                        });
                    });
                    battleStartTimer.setRepeats(false);
                    battleStartTimer.start();
                } else {
                    ui.displayText("\nYou avoided the confrontation.", Color.BLACK);
                    showDialogue(3); 
                }
                break;
            case 3:
                showDialogue(4);
                break;
            case 4:
                ui.displayText("\nTo be continued...", Color.BLACK);
                break;
        }
    }

    private void showStage0() {
        Timer t = new Timer(1500, e -> {
            ui.displayText("\n[Phone Notification]", Color.BLACK);
        });
        t.setRepeats(false);
        t.start();

        Timer timer = new Timer(2000, e -> {
            ui.displayText("\nOLD_FRIEND: Hey, I'm in town... you wanna go grab some food?", Color.BLACK);
        });
        timer.setRepeats(false);
        timer.start();

        Timer t2 = new Timer(3000, e -> {
            ui.showChoicesDialog(new String[]{
                "1. Definitely! Where?",
                "2. Maybe another day"
            });
        });
        t2.setRepeats(false);
        t2.start();
    }

    private void showStage1() {
        ui.displayText("\nOLD_FRIEND: Great! How about that new place downtown, 'The Rusty Spoon', around 7?", Color.BLACK);
        Timer timer = new Timer(1000, e -> {
            ui.displayText("\nIt's been a while. You wonder what they've been up to.", Color.BLACK);
            ui.showChoicesDialog(new String[]{
                "1. Sounds good, see you there!",
                "2. Actually, can we do a bit earlier?"
            });
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showStage2() {
        ui.displayText("\nLater that day, you head towards 'The Rusty Spoon'. The air is getting chilly.", Color.BLACK);
        Timer timer = new Timer(1000, e -> {
            ui.displayText("\nAs you turn a corner, you bump into a shadowy figure who seems agitated.", Color.BLACK);
            ui.showChoicesDialog(new String[]{
                "1. \"Hey, watch it!\" (Confront)",
                "2. \"Sorry about that.\" (Avoid)"
            });
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showStage3() {
        ui.displayText("\n[At 'The Rusty Spoon']", Color.BLACK);
        Timer timer = new Timer(1000, e -> {
            if (state.getFlag("choseToIsolate_d0")) {
                 ui.displayText("\nDespite your earlier hesitation, you made it. Your friend waves at you from a corner booth.", Color.BLACK);
            } else {
                 ui.displayText("\nYour friend is already there, waving at you from a corner booth.", Color.BLACK);
            }
            ui.showChoicesDialog(new String[]{
                "1. \"Hey! Good to see you!\"",
                "2. \"Sorry I'm a bit late.\""
            });
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showStage4() {
        ui.displayText("\nOLD_FRIEND: So, what have you been up to? Still wrestling with those existential thoughts?", Color.BLACK);
        Timer timer = new Timer(1000, e -> {
            ui.displayText("\nThe conversation flows, and for a while, the world outside fades.", Color.BLACK);
            ui.showChoicesDialog(new String[]{
                "1. \"You know me, always overthinking.\"",
                "2. \"Trying to take it one day at a time.\""
            });
        });
        timer.setRepeats(false);
        timer.start();
    }


    @Override
    public String[] getCurrentChoices() {
        if (battleManager.isBattleActive()) {
            return new String[]{
                "1. Attack (" + state.getStat(GameState.PLAYER_ATTACK) + " dmg)",
                "2. Use Item (Not Implemented)"};
        }
        return new String[0]; 
    }

    // Add this helper method for setting mid-battle events
    public void setMidBattleEvent(Runnable event) {
        if (battleManager != null) {
            battleManager.setMidBattleEvent(event);
        }
    }

    // Add useInventoryItem method for consistency
    public void useInventoryItem(String itemName) {
        Item itemToUse = state.getItemPrototype(itemName);
        if (itemToUse != null && state.getItemQuantity(itemName) > 0) {
            itemToUse.applyEffect(state, ui, "Player");
            state.consumeItem(itemName);
            ui.displayText("\nUsed " + itemName + ".", Color.BLACK);
        } else {
            ui.displayText("\nCannot use " + itemName + " - not available.", Color.BLACK);
        }
    }
}