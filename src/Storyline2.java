import java.awt.Color;
import javax.swing.Timer;

public class Storyline2 extends Storyline {
    private int dialogueState = 0;
    private BattleManager battleManager;
    private String playerName = "Milo"; 

    public static final String UNIQUE_STAT_KEY_S2 = "drugAddictionLevel"; 
    public static final String STAT_SOCIAL_STATUS = "socialStatus";
    public static final String STAT_PLAYER_WILLPOWER = "playerWillpower";
    public static final String STAT_PLAYER_DEFENSE = "playerDefense";

    public Storyline2(GameUI ui, GameState state) {
        super(ui, state);
        this.battleManager = new BattleManager(ui, state);
    ui.setTitle("A Lost Euphoria"); 
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
            case 5 -> showStage5();
            case 6 -> showStage6();
            case 7 -> showStage7();
            case 8 -> showStage8();
            case 9 -> showStage9();
            case 10 -> showStage10();
            case 11 -> showStage11();

        }
    }
 
    @Override
    public void startStory() {
        state.setStat(GameState.PLAYER_HEALTH, 10);
        state.setStat(GameState.PLAYER_MAX_HEALTH, 10);
        state.setStat(GameState.PLAYER_ATTACK, 1);
        state.setStat(STAT_PLAYER_DEFENSE, 0);
        state.setStat(STAT_PLAYER_WILLPOWER, 5);

        ui.setStageImage("/Resources/Images/Story2"); // Initial image for the story
        AudioManager.getInstance().playMusic("/Resources/Audio/Story2/wav", true); // Initial BGM

        Timer timer = new Timer(500, e -> {
            ui.displayText("Chapter 1: 1st Month ", Color.BLACK);
        });
        timer.setRepeats(false);
        timer.start();
        showDialogue(0);
    }

    @Override
    public void handleChoice(int choice) {
        if (battleManager.isBattleActive()) {
            battleManager.processPlayerTurn(choice);
        } else {
            handleDialogueChoice(choice);
        }
    }

    public void useInventoryItem(String itemName) {
        Item itemToUse = state.getItemPrototype(itemName);
        if (itemToUse != null && state.getItemQuantity(itemName) > 0) {
            // Apply the item's effect
            itemToUse.applyEffect(state, ui, playerName);
            // Consume the item from inventory
            state.consumeItem(itemName);
            // Potentially update UI elements that show stats if you have any always-visible
            // stat displays
        } else {
            ui.displayText("\n" + playerName + " doesn't have any " + itemName + " to use or item is unknown.",
                    Color.BLACK);
        }
    }

    private void handleDialogueChoice(int choice) {
        switch (dialogueState) {
            case 0:
                Timer descriptionTimer = new Timer(500, e -> {
                    ui.displayText(
                            "\n(You look around the classroom, admiring and looking through each student one by one, assessing their faces and expression “this is who i must fight with for that no.1 spot)",
                            Color.BLACK);

                    Timer proceedTimer = new Timer(6500, e2 -> showDialogue(1));
                    proceedTimer.setRepeats(false);
                    proceedTimer.start();
                });
                descriptionTimer.setRepeats(false);
                descriptionTimer.start();
                break;
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
        }
    }

    //  private void startChapter4Battle() {
    //     ui.setStageImage("/Resources/Images/Story2/.png"); // Battle background
    //     AudioManager.getInstance().playMusic("/Resources/Audio/Story2", true); // Battle BGM
        
    //     ui.displayText("\nNarrator: A wave of peer pressure washes over you. It feels like a challenge.", Color.GRAY);
    //     Timer startBattleActual = new Timer(2000, e2 -> {
    //         battleManager.startBattle("Temptation: Cigarette", 10, 5, battleResult -> {
    //             // BGM will be changed by the next showDialogue stage
    //             if (battleResult == BattleManager.BattleResult.WIN) {
    //                 ui.setStageImage("/Resources/Images/Story3/kai_resists_cigarette.png"); // Victory image
    //                 ui.displayText("\nNarrator: \"You feel a strange sense of accomplishment having resisted.\"", Color.GRAY);
    //                 Timer r1 = new Timer(2500, res -> ui.displayText("\nNarrator: \"Something tells you this won't be the last time you're tested.\"", Color.GRAY));
    //                 r1.setRepeats(false);
    //                 r1.start();
    //                 state.adjustStat(GameState.PLAYER_HEALTH, 1);
    //                 state.adjustStat(GameState.PLAYER_MAX_HEALTH, 1);
    //                 state.adjustStat(GameState.PLAYER_ATTACK, 1);
    //                 state.adjustStat(STAT_PLAYER_DEFENSE, 1);
    //                 state.adjustStat(STAT_PLAYER_WILLPOWER, 2);
    //                 Timer r2 = new Timer(5000, res -> ui.displayText("\nSystem Status: \"Level Up! Health +1, Max Health +1, Attack +1, Defense +1, Willpower +2\"", Color.GREEN.darker()));
    //                 r2.setRepeats(false);
    //                 r2.start();
    //                 Timer proceed = new Timer(7500, res -> showDialogue(3));
    //                 proceed.setRepeats(false);
    //                 proceed.start();
    //             } else { // LOSS - Player succumbs
    //                 ui.setStageImage("/Resources/Images/Story3/kai_succumbs_cigarette.png"); // Defeat image
    //                 ui.displayText("\nNarrator: \"The smoke fills your lungs. It burns slightly, but there's also a strange buzz...\"", Color.GRAY);
    //                 CIGARETTE_ITEM.applyEffect(state, ui, playerName); 
    //                 Timer proceed = new Timer(6000, res -> showDialogue(3));
    //                 proceed.setRepeats(false);
    //                 proceed.start();
    //             }
    //         });
    //     });
    //     startBattleActual.setRepeats(false);
    //     startBattleActual.start();
    // }

        // Timer t4 = new Timer(5000, e -> ui.displayText("\n" + playerName + ": \"\"", Color.BLACK));
        // t4.setRepeats(false); t4.start();

   private void showStage0() {
      ui.displayText("\n [University Campus - Dorm Room] ", Color.DARK_GRAY);
        Timer t0 = new Timer(1000, e -> ui.displayText("\n" + "Milo arrive at a prestigious high school after his family move to another city. He walks through hallways lined with trophy cases showcasing the extraordinary students and their achievement \n", Color.BLACK));
        t0.setRepeats(false); t0.start(); 
        Timer t1 = new Timer(3000, e -> ui.displayText("\n" + playerName+ " (internal though): \"Dad's new job means a 'better opportunity' for me. Elite school, competitive students, and expectations through the roof.\n", Color.BLACK));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(5000, e -> ui.displayText("\n" + "(He stops in front of a door, preparing himself for the welcome)\n", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(6500, e -> ui.displayText("\n" + "Teacher" + ": \"Class, we have a transfer student. Milo scored in the 98th percentile on the entrance exam. We expect great things.", Color.BLACK));
        t3.setRepeats(false); t3.start();
        Timer choiceTimer = new Timer(19500, e2 -> ui.showChoicesDialog(new String[]{"Look around the classroom"}));
            choiceTimer.setRepeats(false); choiceTimer.start();
     }

    private void showStage1() {
        ui.displayText("\n\n [Milo's Bedroom - Night] \n", Color.DARK_GRAY);
        Timer t1 = new Timer(1000, e -> ui.displayText("\n" + playerName + " (looking at a color-coded study schedule): \"AP Physics, AP Calculus, AP Literature, Mandarin, Robotics Club, Debate Team... I can do this. I have to do this.\"", Color.BLACK));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(5500, e -> ui.displayText("\n" + playerName + ": \"This is nothing compared to my previous school\"", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer proceedTimer = new Timer(6000, e -> showDialogue(2));
        proceedTimer.setRepeats(false); proceedTimer.start();
    }
    private void showStage2() {
        ui.displayText("\n\n [Same Bedroom - Two weeks later, 2:00 AM] ", Color.DARK_GRAY);
        Timer t1 = new Timer(1000, e -> ui.displayText("\n" + playerName + " (rubbing bloodshot eyes, surrounded by energy drink cans): \"Third test this week. Can't focus anymore... I need something even more refreshing\n", Color.BLACK));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(3000, e -> ui.displayText("\n" + "Narrator: \"You open your laptop, searching for study tips. An anonymous forum thread catches your eye: 'How I Aced My APs While Getting 4 Hours of Sleep.'\n", Color.BLACK));
        t2.setRepeats(false); t2.start();
         Timer t3 = new Timer(1000, e -> ui.displayText("\n" + "Anonymous Post: \"The secret isn't working harder, it's working smarter. Adderall. Not from some dealer, just ask your doctor about ADHD symptoms. Changed my academic life.\n", Color.BLACK));
        t3.setRepeats(false); t3.start();
        Timer t4 = new Timer(3000, e -> ui.displayText("\n" + "Narrator: \"You've never considered this before. But the pressure is crushing you already, and it's only September.\n", Color.BLACK));
        t4.setRepeats(false); t4.start();
        Timer proceedTimer = new Timer(6000, e -> showDialogue(3));
        proceedTimer.setRepeats(false); proceedTimer.start();
    }

    private void showStage3() {
        //battle 
        Timer proceedTimer = new Timer(8000, e -> showDialogue(4));
        proceedTimer.setRepeats(false); proceedTimer.start();
        
        Timer t5 = new Timer(5000, e -> ui.displayText("\n" + playerName + " (closing laptop): \"No. I just need better time management. I can handle this.\"", Color.BLACK));
        t5.setRepeats(false); t5.start();
    }

        private void showStage4() {
         ui.displayText("\n [Milo sitting with his parents at dinner] ", Color.DARK_GRAY);
        Timer t1 = new Timer(1000, e -> ui.displayText("\n" + "Dad" + ": \"So I spoke with Mr. Peterson today about college recommendations. He says you need more extracurriculars if you want Stanford to even look at your application.\"", Color.BLACK));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(3000, e -> ui.displayText("\n" + "Mom" + ": \"And your math grade slipped to an A-. Is everything okay?\"", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(4000, e -> ui.displayText("\n" + playerName + ": \"I'm already doing six activities and staying up until 2 AM studying. What more do you want?\"", Color.BLACK));
        t3.setRepeats(false); t3.start();        
    }

    private void showStage5() {
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

    
    private void showStage6() {
       //fight if win
       //fight if loose
    }

   

private void showStage7() {
    ui.displayText("\n\n [Milo's Bedroom - Month 3, Late Night] ", Color.DARK_GRAY);
    Timer t1 = new Timer(1000, e -> ui.displayText("\n" + playerName + " (counting pills): \"Only three left and the refill isn't for another two weeks. That's not enough for midterms. How do I get more, I need more\"", Color.BLACK));
    t1.setRepeats(false); t1.start();
    Timer t2 = new Timer(3500, e -> ui.displayText("\n" + "Narrator: \"You find yourself taking two pills sometimes when the workload is especially heavy. The prescribed dose no longer gives you that sharp focus you crave.\"", Color.BLACK));
    t2.setRepeats(false); t2.start();

    //change bg
    Timer t3 = new Timer(6000, e -> ui.displayText("\n" + playerName + ": \"Vyvanse lasts longer... or maybe something to help me sleep after the stimulants. Xanax could work.\"", Color.BLACK));
    t3.setRepeats(false); t3.start();
    Timer t4 = new Timer(8000, e -> ui.displayText("\n" + "Narrator: \"The pressure is building. Each choice leads deeper into a dangerous path.\"", Color.BLACK));
    t4.setRepeats(false); t4.start();
    //battle
    Timer proceedTimer = new Timer(10000, e -> showDialogue(5));
    proceedTimer.setRepeats(false); proceedTimer.start();
}

private void showStage8() {
    ui.displayText("\n\n [Milo's Room - Late Night, Month 4] ", Color.DARK_GRAY);
    Timer t1 = new Timer(1000, e -> ui.displayText("\n" + playerName + " (jittery, unable to sleep): \"I need the Adderall to focus, and the Xanax to sleep. It's just medicine.\"", Color.BLACK));
    t1.setRepeats(false); t1.start();
    Timer t2 = new Timer(3500, e -> ui.displayText("\n" + "Narrator: \"Your tolerance is building. Two Adderall, then three. Half a Xanax becomes a whole one. Your mood swings wildly between artificial confidence and crushing depression.\"", Color.BLACK));
    t2.setRepeats(false); t2.start();
    Timer proceedTimer = new Timer(6000, e -> showStage8());
    proceedTimer.setRepeats(false); proceedTimer.start();
}

private void showStage9() {
    ui.displayText("\n\n [Living Room - Parent Confrontation] ", Color.DARK_GRAY);
    Timer t1 = new Timer(1000, e -> ui.displayText("\n" + "Mom: \"You don't look well, Milo. You've lost so much weight, and your English teacher says you fell asleep in class.\"", Color.BLACK));
    t1.setRepeats(false); t1.start();
    Timer t2 = new Timer(3500, e -> ui.displayText("\n" + playerName + " (stressed out): \"I'm fine! Just stressed about college applications.\"", Color.BLACK));
    t2.setRepeats(false); t2.start();
    Timer t3 = new Timer(5000, e -> ui.displayText("\n" + "Dad: \"We're worried. You're not yourself lately.\"", Color.BLACK));
    t3.setRepeats(false); t3.start();
    Timer t4 = new Timer(6500, e -> ui.displayText("\n" + playerName + ": \"Maybe this is what it takes to be 'exceptional' like you always wanted!\"", Color.BLACK));
    t4.setRepeats(false); t4.start();
    Timer t5 = new Timer(8000, e -> ui.displayText("\n" + "(Milo slams the door and walks out of the room)", Color.GRAY));
    t5.setRepeats(false); t5.start();
    Timer proceedTimer = new Timer(9500, e -> showStage9());
    proceedTimer.setRepeats(false); proceedTimer.start();
}

private void showStage10() {
    ui.displayText("\n\n [Milo's Room - Researching Online] ", Color.DARK_GRAY);
    Timer t1 = new Timer(1000, e -> ui.displayText("\n" + "Narrator: \"Your darknet contact suggests opioids. 'Perfect for when the pressure gets too much. Oxycodone - like Xanax but better. Medical grade.'\"", Color.BLACK));
    t1.setRepeats(false); t1.start();
    Timer t2 = new Timer(4000, e -> ui.displayText("\n" + "Narrator: \"The temptation is stronger than ever. This could solve everything... or destroy everything.\"", Color.BLACK));
    t2.setRepeats(false); t2.start();
    Timer proceedTimer = new Timer(6500, e -> showDialogue(6));
    proceedTimer.setRepeats(false); proceedTimer.start();
}

private void showStage11() {
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