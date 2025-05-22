import java.awt.Color;
import javax.swing.Timer;

// Make sure to import BattleManager and its enum if they are in the same package,
// or use fully qualified names if in different packages.
// Assuming they are in the same package (e.g., default package for src files):
// import BattleManager.BattleResult; // Not strictly needed if using BattleManager.BattleResult directly

public class Storyline1 extends Storyline {
    private int dialogueState = 0;
    private BattleManager battleManager;

    public Storyline1(GameUI ui, GameState state) {
        super(ui, state);
        this.battleManager = new BattleManager(ui, state);
    }

    public BattleManager getBattleManager() {
        return this.battleManager;
    }

    // Getter for dialogueState
    public int getDialogueState() {
        return dialogueState;
    }

    // Setter for dialogueState, potentially also triggers UI update
    public void setDialogueState(int dialogueState) {
        this.dialogueState = dialogueState;
        // Important: After setting state, the UI needs to reflect this.
        // This might involve calling showDialogue directly or having GameUI manage it.
        // For simplicity, let's assume GameUI will call showDialoguePublic after this.
    }
    
    // Public wrapper for showDialogue if it's private or package-private
    public void showDialoguePublic(int stage) {
        this.showDialogue(stage);
    }

    @Override
    public void startStory() {
        Timer timer = new Timer(500, e ->{
            ui.displayText("[Lullaby of Empty Bottles]", Color.BLACK);
        });
        timer.setRepeats(false);
        timer.start();
        showDialogue(0); // Start first dialogue
    }

    private void showDialogue(int stage) {
        dialogueState = stage;
        // Ensure we are not in battle when showing dialogue stages
        // The battleManager.isBattleActive() will control flow in handleChoice
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
                        // Define opponent and start battle
                        battleManager.startBattle("Mysterious Assailant", 50, 8, battleResult -> {
                            // This lambda is the callback executed when the battle ends
                            if (battleResult == BattleManager.BattleResult.WIN) {
                                // Proceed to the next part of the story
                                showDialogue(3); 
                            } else {
                                // Handle game over or consequences
                                ui.displayText("\nGame Over (for now).", Color.BLACK);
                                // Potentially end the game or go to a specific "defeated" state
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

    // Removed battle-specific methods: startBattle, handleBattleChoice, updateBattleDisplay, endBattle
    // These are now in BattleManager.

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
            // The BattleManager itself calls ui.showChoicesDialog, so this might not be strictly needed
            // for the current flow, but good for consistency if other parts of your system query for choices.
            return new String[]{
                "1. Attack (" + state.getStat(GameState.PLAYER_ATTACK) + " dmg)",
                "2. Use Item (Not Implemented)"};
        }
        // Could return dialogue choices based on dialogueState if needed elsewhere
        // For now, dialogue choices are presented directly in showStageX methods.
        return new String[0]; 
    }
}