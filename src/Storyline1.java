
import java.awt.Color;
import javax.swing.Timer;

public class Storyline1 extends Storyline {
    private int dialogueState = 0; // Tracks current conversation stage
    
    public Storyline1(GameUI ui, GameState state) {
        super(ui, state);
    }

    @Override
    public void startStory() {
        Timer timer = new Timer(500, e ->{
            ui.displayText("[Lullaby of Empty Bottles]", Color.black);
        });
        timer.setRepeats(false);
        timer.start();
        showDialogue(0); // Start first dialogue
    }

    // Handle all dialogue in one method
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
    public void handleChoice(String choice) {
        switch(dialogueState) {
            case 0 -> {
                if(choice == "1") {
                    // state.social++;
                    showDialogue(1);
                } else {
                    // if(state.alcoholDay1 > 2) state.isolation++;
                    // Continue story...
                }
            }
            case 1 -> {
                // if(choice == 1) state.positiveOutlook++;
                // else if(choice == 2) state.friendConcerned++;
                // else state.uncertainty++;
                showDialogue(2);
            }
            case 2 -> {
                // if(choice == 1) state.weekendEvent = true;
                // else state.decisionPending = true;
                // Continue to next story part...
                showDialogue(3);
            }
            case 3 -> {

                showDialogue(4);
            }
            case 4 -> {

                showDialogue(5);
            }
        }
    }

    private void showStage0() {
        Timer t = new Timer(1500, e -> {
            ui.displayText("\n[Phone Notification]", Color.black);
        });
        t.setRepeats(false);
        t.start();

        Timer timer = new Timer(2000, e -> {
            ui.displayText("\nOLD_FRIEND: Hey, I'm in town... you wanna go grab some food?", Color.black);
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
        ui.displayText("\ndialogue 2 test", Color.black);
        Timer timer = new Timer(1000, e -> {
            ui.displayText("\nthis is a third dialogue test", Color.black);
            ui.showChoicesDialog(new String[]{
                "1. this is choice test 1?",
                "2. this is choice test 2"
            });
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showStage2() {
        ui.displayText("\ndialogue 3 test", Color.black);
        Timer timer = new Timer(1000, e -> {
            ui.displayText("\nthis is a fourth dialogue test", Color.black);
            ui.showChoicesDialog(new String[]{
                "1. this is choice test 12?",
                "2. this is choice test 22"
            });
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showStage3() {
        ui.displayText("\ndialogue 4 test", Color.black);
        Timer timer = new Timer(1000, e -> {
            ui.displayText("\nthis is a fifth dialogue test", Color.black);
            ui.showChoicesDialog(new String[]{
                "1. this is choice test 12?",
                "2. this is choice test 22"
            });
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showStage4() {
        ui.displayText("\ndialogue 5 test", Color.black);
        Timer timer = new Timer(1000, e -> {
            ui.displayText("\nthis is a sixth dialogue test", Color.black);
            ui.showChoicesDialog(new String[]{
                "1. this is choice test 12?",
                "2. this is choice test 22"
            });
        });
        timer.setRepeats(false);
        timer.start();
    }


    @Override
    public String[] getCurrentChoices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}