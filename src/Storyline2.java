import java.awt.Color;
import javax.swing.Timer;

public class Storyline2 extends Storyline {
    private int dialogueState = 0;
    private BattleManager battleManager;

    public Storyline2(GameUI ui, GameState state) {
        super(ui, state);
        this.battleManager = new BattleManager(ui, state);
    }

    @Override
    public void startStory() {
        Timer timer = new Timer(500, e ->{
            ui.displayText("[Storyline 2 Title Placeholder]", Color.BLACK);
        });
        timer.setRepeats(false);
        timer.start();
        showDialogue(0);
    }

    public BattleManager getBattleManager() {
        return this.battleManager;
    }

    // Getter for dialogueState
    public int getDialogueState() {
        return dialogueState;
    }

    // Setter for dialogueState
    public void setDialogueState(int dialogueState) {
        this.dialogueState = dialogueState;
    }
    
    // Public wrapper for showDialogue
    public void showDialoguePublic(int stage) {
        this.showDialogue(stage);
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
                    state.setStat(GameState.PLAYER_HEALTH, state.getStat(GameState.PLAYER_HEALTH) + 2); 
                    ui.displayText("\nStoryline 2: You chose option 1. Something happens.", Color.BLACK);
                    showDialogue(1);
                } else {
                    ui.displayText("\nStoryline 2: You chose option 2. Something else happens.", Color.BLACK);
                    state.setFlag("storyline2_flag_example", true); 
                    showDialogue(1); 
                }
                break;
            case 1:
                showDialogue(2);
                break;
            case 2:
                if (choice == 1) { 
                    ui.displayText("\nStoryline 2: A confrontation begins!", Color.BLACK);
                    Timer battleStartTimer = new Timer(1500, e -> {
                        battleManager.startBattle("Storyline 2 Enemy", 60, 10, battleResult -> {
                            if (battleResult == BattleManager.BattleResult.WIN) {
                                showDialogue(3); 
                            } else {
                                ui.displayText("\nStoryline 2: Game Over.", Color.BLACK);
                            }
                        });
                    });
                    battleStartTimer.setRepeats(false);
                    battleStartTimer.start();
                } else {
                    ui.displayText("\nStoryline 2: You avoided the confrontation.", Color.BLACK);
                    showDialogue(3); 
                }
                break;
            case 3:
                showDialogue(4);
                break;
            case 4:
                ui.displayText("\nStoryline 2: To be continued...", Color.BLACK);
                break;
        }
    }

    private void showStage0() {
        Timer t = new Timer(1500, e -> {
            ui.displayText("\n[Storyline 2: An event occurs]", Color.BLACK);
        });
        t.setRepeats(false);
        t.start();

        Timer timer = new Timer(2000, e -> {
            ui.displayText("\nStoryline 2: A character says something.", Color.BLACK);
        });
        timer.setRepeats(false);
        timer.start();

        Timer t2 = new Timer(3000, e -> {
            ui.showChoicesDialog(new String[]{
                "Storyline 2: Option A",
                "Storyline 2: Option B"
            });
        });
        t2.setRepeats(false);
        t2.start();
    }

    private void showStage1() {
        ui.displayText("\nStoryline 2: Consequence of previous choice or new situation.", Color.BLACK);
        Timer timer = new Timer(1000, e -> {
            ui.displayText("\nStoryline 2: More details unfold.", Color.BLACK);
            ui.showChoicesDialog(new String[]{
                "Storyline 2: Choice X",
                "Storyline 2: Choice Y"
            });
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showStage2() {
        ui.displayText("\nStoryline 2: Another development in the plot.", Color.BLACK);
        Timer timer = new Timer(1000, e -> {
            ui.displayText("\nStoryline 2: A critical decision point.", Color.BLACK);
            ui.showChoicesDialog(new String[]{
                "Storyline 2: Path 1 (Leads to battle)",
                "Storyline 2: Path 2 (Avoids battle)"
            });
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showStage3() {
        ui.displayText("\nStoryline 2: Aftermath or new scene.", Color.BLACK);
        Timer timer = new Timer(1000, e -> {
            if (state.getFlag("storyline2_flag_example")) { 
                 ui.displayText("\nStoryline 2: Something specific happens due to a flag.", Color.BLACK);
            } else {
                 ui.displayText("\nStoryline 2: General progression.", Color.BLACK);
            }
            ui.showChoicesDialog(new String[]{
                "Storyline 2: Continue",
                "Storyline 2: Ask a question"
            });
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showStage4() {
        ui.displayText("\nStoryline 2: Nearing the end of this chapter.", Color.BLACK);
        Timer timer = new Timer(1000, e -> {
            ui.displayText("\nStoryline 2: Final thoughts or lead-in to conclusion.", Color.BLACK);
            ui.showChoicesDialog(new String[]{
                "Storyline 2: Reflect",
                "Storyline 2: Move on"
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
}