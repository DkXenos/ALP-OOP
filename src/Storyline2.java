import java.awt.Color;
import javax.swing.Timer;

public class Storyline2 extends Storyline {
    private int dialogueState = 0;
    private BattleManager battleManager;
    private String playerName = "Milo"; 

    public Storyline2(GameUI ui, GameState state) {
        super(ui, state);
        this.battleManager = new BattleManager(ui, state);
    }

    @Override
    public void startStory() {
        Timer timer = new Timer(500, e ->{
            ui.displayText("[Storyline 2] ", Color.BLACK);
        });
        ui.setTitle("A Lost Euphoria"); 
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

        // Timer t4 = new Timer(5000, e -> ui.displayText("\n" + playerName + ": \"\"", Color.BLACK));
        // t4.setRepeats(false); t4.start();

   private void showStage0() {
      ui.displayText("\n [University Campus - Dorm Room] ", Color.DARK_GRAY);
        Timer t0 = new Timer(1000, e -> ui.displayText("\n" + "Milo arrive at a prestigious high school after his family move to another city. He walks through hallways lined with trophy cases showcasing the extraordinary students and their achievement" + "\n", Color.BLACK));
        t0.setRepeats(false); t0.start(); 
        Timer t1 = new Timer(3000, e -> ui.displayText("\n" + playerName+ " (internal though): \"Dad's new job means a 'better opportunity' for me. Elite school, competitive students, and expectations through the roof." + "\n", Color.BLACK));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(5000, e -> ui.displayText("\n" + "(He stops in front of a door, preparing himself for the welcome)\r\n", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(7000, e -> ui.displayText("\n" + "Teacher" + ": \"Class, we have a transfer student. Milo scored in the 98th percentile on the entrance exam. We expect great things.\"", Color.BLACK));
        t3.setRepeats(false); t3.start();
         Timer proceedTimer = new Timer(10000, e -> showDialogue(1));
        proceedTimer.setRepeats(false); proceedTimer.start();
 
   }

    private void showStage1() {
          Timer t1 = new Timer(1000, e -> ui.displayText("\n" + "Counselor" + ": \"How can I help you today, Milo?\"", Color.BLACK));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(2000, e -> ui.displayText("\n" + playerName + ": \"I've been having trouble concentrating. My mind races, I can't sit still in class, and I'm falling behind despite studying constantly. I think I might have ADHD.\"", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(4000, e -> ui.displayText("\n" + "Counselor" + ": \"That's something a doctor would need to diagnose. I can give your parents a referral.\"", Color.BLACK));
        t3.setRepeats(false); t3.start();
        Timer t4 = new Timer(5000, e -> ui.displayText("\n" + playerName + ": \"Could we... not tell my parents yet? They're already stressed about my grades.\"", Color.BLACK));
        t4.setRepeats(false); t4.start();
  Timer proceedTimer = new Timer(10000, e -> showDialogue(2));
        proceedTimer.setRepeats(false); proceedTimer.start();
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



        private void showStage5() {
         ui.displayText("\n [University Campus - Dorm Room] ", Color.DARK_GRAY);
        Timer t1 = new Timer(1000, e -> ui.displayText("\n" + "Dad" + ": \"So I spoke with Mr. Peterson today about college recommendations. He says you need more extracurriculars if you want Stanford to even look at your application.\"", Color.BLACK));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(3000, e -> ui.displayText("\n" + "Mom" + ": \"And your math grade slipped to an A-. Is everything okay?\"", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(4000, e -> ui.displayText("\n" + playerName + ": \"I'm already doing six activities and staying up until 2 AM studying. What more do you want?\"", Color.BLACK));
        t3.setRepeats(false); t3.start();



        }
    private void showStage6() {
       Timer t1 = new Timer(1000, e -> ui.displayText("\n" + "Counselor" + ": \"How can I help you today, Milo?\"", Color.BLACK));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(2000, e -> ui.displayText("\n" + playerName + ": \"I've been having trouble concentrating. My mind races, I can't sit still in class, and I'm falling behind despite studying constantly. I think I might have ADHD.\"", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(4000, e -> ui.displayText("\n" + "Counselor" + ": \"That's something a doctor would need to diagnose. I can give your parents a referral.\"", Color.BLACK));
        t3.setRepeats(false); t3.start();
        Timer t4 = new Timer(5000, e -> ui.displayText("\n" + playerName + ": \"Could we... not tell my parents yet? They're already stressed about my grades.\"", Color.BLACK));
        t4.setRepeats(false); t4.start();
  Timer proceedTimer = new Timer(10000, e -> showDialogue(6));
        proceedTimer.setRepeats(false); proceedTimer.start();
 
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