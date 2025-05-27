import java.awt.Color;
import javax.swing.Timer;

public class Storyline3 extends Storyline {
    private int dialogueState = 0;
    private BattleManager battleManager;
    private String playerName = "Kai"; 

    private static final String UNIQUE_STAT_KEY_S3 = "nicotineAddictionLevel"; 
    private static final String STAT_SOCIAL_STATUS = "socialStatus";
    private static final String STAT_PLAYER_WILLPOWER = "playerWillpower";
    private static final String STAT_PLAYER_DEFENSE = "playerDefense";

    public Storyline3(GameUI ui, GameState state) {
        super(ui, state);
        this.battleManager = new BattleManager(ui, state);
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
    
    public void showDialoguePublic(int stage) {
        this.showDialogue(stage);
    }

    @Override
    public void startStory() {
        state.setStat(GameState.PLAYER_HEALTH, 10);
        state.setStat(GameState.PLAYER_MAX_HEALTH, 10);
        state.setStat(GameState.PLAYER_ATTACK, 6);
        state.setStat(STAT_PLAYER_DEFENSE, 0);
        state.setStat(STAT_PLAYER_WILLPOWER, 5);
        
        state.setStat(UNIQUE_STAT_KEY_S3, 0); // Initializes "nicotineAddictionLevel" to 0
        state.setStat(STAT_SOCIAL_STATUS, 0);


        Timer timer = new Timer(500, e ->{
            ui.displayText("Chapter 1: First Encounter", Color.BLACK);
        });
        timer.setRepeats(false);
        timer.start();
        showDialogue(0); 
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
            case 12 -> showStage12();
            case 13 -> showStage13();
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
            case 0: // Handle choices made in showStage0
                String choiceText = "";
                switch (choice) {
                    case 1: // Look at the Window
                        choiceText = "\n" + playerName + ": (The view from up here is pretty nice. New city, new life.)";
                        break;
                    case 2: // Open your Closet
                        choiceText = "\n" + playerName + ": (Empty hangers. A blank slate, just like my social life right now.)";
                        break;
                    case 3: // Check your Phone
                        choiceText = "\n" + playerName + ": (No new messages. Guess everyone's busy settling in too.)";
                        break;
                    default:
                        choiceText = "\n" + playerName + ": (Hmm, what to do first?)";
                        break;
                }
                ui.displayText(choiceText, Color.BLACK);
                // After displaying the choice-specific text, proceed to the next stage
                Timer proceedAfterChoiceTimer = new Timer(3000, e -> showDialogue(1)); // 3-second delay
                proceedAfterChoiceTimer.setRepeats(false);
                proceedAfterChoiceTimer.start();
                break;
            case 2: 
                if (choice == 1) { 
                    startChapter1Battle();
                }
                break;
            case 6: 
                if (choice == 1) { 
                    startChapter2Battle(); // Assuming this was intended for vape battle
                } else { 
                    ui.displayText("\n" + playerName + ": \"No thanks, I think I'll just grab some coffee instead. Need to keep my head clear.\"", Color.BLACK);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 1);
                    ui.displayText("\nSystem Status: Willpower +1", Color.GREEN.darker());
                    Timer proceed = new Timer(3000, e -> showDialogue(8)); 
                    proceed.setRepeats(false);
                    proceed.start();
                }
                break;
            case 11: 
                if (choice == 1) { 
                    startChapter3Battle(); // Assuming this was intended for premium cig battle
                } else { 
                    ui.displayText("\n" + playerName + ": \"Nah, I'm good. Trying to keep my lungs clear for the game.\"", Color.BLACK);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 2);
                    ui.displayText("\nSystem Status: Willpower +2", Color.GREEN.darker());
                    Timer proceed = new Timer(3000, e -> showDialogue(13)); 
                    proceed.setRepeats(false);
                    proceed.start();
                }
                break;
        }
    }

    private void startChapter1Battle() {
        ui.displayText("\nNarrator: A wave of peer pressure washes over you. It feels like a challenge.", Color.GRAY);
        Timer startBattleActual = new Timer(2000, e2 -> {
            battleManager.startBattle("Temptation: Cigarette", 10, 5, battleResult -> {
                if (battleResult == BattleManager.BattleResult.WIN) {
                    ui.displayText("\nNarrator: \"You feel a strange sense of accomplishment having resisted.\"", Color.GRAY);
                    Timer r1 = new Timer(2500, res -> ui.displayText("\nNarrator: \"Something tells you this won't be the last time you're tested.\"", Color.GRAY));
                    r1.setRepeats(false);
                    r1.start();
                    state.adjustStat(GameState.PLAYER_HEALTH, 1);
                    state.adjustStat(GameState.PLAYER_MAX_HEALTH, 1);
                    state.adjustStat(GameState.PLAYER_ATTACK, 1);
                    state.adjustStat(STAT_PLAYER_DEFENSE, 1);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 2);
                    Timer r2 = new Timer(5000, res -> ui.displayText("\nSystem Status: \"Level Up! Health +1, Max Health +1, Attack +1, Defense +1, Willpower +2\"", Color.GREEN.darker()));
                    r2.setRepeats(false);
                    r2.start();
                    Timer proceed = new Timer(7500, res -> showDialogue(3));
                    proceed.setRepeats(false);
                    proceed.start();
                } else { 
                    ui.displayText("\nNarrator: \"The smoke fills your lungs. It burns slightly, but there's also a strange buzz that momentarily feels… not entirely unpleasant.\"", Color.GRAY);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, -1);
                    state.adjustStat(UNIQUE_STAT_KEY_S3, 2); // Nicotine Addiction +2
                    Timer r1 = new Timer(3000, res -> ui.displayText("\nSystem Status: \"Status Effect: Lightheaded. Willpower -1. " + UNIQUE_STAT_KEY_S3 + " +2\"", Color.RED.darker()));
                    r1.setRepeats(false);
                    r1.start();
                    Timer proceed = new Timer(6000, res -> showDialogue(3));
                    proceed.setRepeats(false);
                    proceed.start();
                }
            });
        });
        startBattleActual.setRepeats(false);
        startBattleActual.start();
    }
    
    // Chapter 2 Battle (Vape)
    private void startChapter2Battle() { 
        ui.displayText("\nNarrator: The sleek vape pen gleams under the desk lamp. Another test of will.", Color.GRAY);
        Timer startBattleActual = new Timer(2000, e2 -> {
            battleManager.startBattle("Temptation: Vape", 15, 4, battleResult -> {
                if (battleResult == BattleManager.BattleResult.WIN) {
                    state.adjustStat(GameState.PLAYER_HEALTH, 1);
                    state.adjustStat(GameState.PLAYER_MAX_HEALTH, 1);
                    state.adjustStat(GameState.PLAYER_ATTACK, 1);
                    state.adjustStat(STAT_PLAYER_DEFENSE, 1);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 2); 
                    ui.displayText("\nSystem Status: \"Level Up! Health +1, Max Health +1, Attack +1, Defense +1, Willpower +2\"", Color.GREEN.darker());
                    Timer t1 = new Timer(2500, res -> ui.displayText("\n" + playerName + ": \"I think I'll just grab some coffee instead. Need to keep my head clear.\"", Color.BLACK));
                    t1.setRepeats(false); t1.start();
                } else { 
                    ui.displayText("\nStudy Partner: \"See? Feels better, right? You can borrow it anytime.\"", Color.CYAN.darker());
                    state.adjustStat(STAT_PLAYER_DEFENSE, -1);
                    state.adjustStat(GameState.PLAYER_HEALTH, -1);
                    state.adjustStat(GameState.PLAYER_ATTACK, -1);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, -1); 
                    state.adjustStat(UNIQUE_STAT_KEY_S3, 3); // Nicotine Addiction +3
                    Timer t1 = new Timer(3000, res -> ui.displayText("\nSystem Status: \"Status Effect: Sleep disturbance. Defense -1, Health -1, Attack -1, Willpower -1. " + UNIQUE_STAT_KEY_S3 + " +3\"", Color.RED.darker()));
                    t1.setRepeats(false); t1.start();
                    Timer t2 = new Timer(6000, res -> ui.displayText("\nNarrator: “Later, Kai struggles to fall asleep and disturbs his exam performance, leading him to feel judged by other people.”", Color.GRAY));
                    t2.setRepeats(false); t2.start();
                }
                Timer proceed = new Timer(9000, res -> showDialogue(7)); 
                proceed.setRepeats(false);
                proceed.start();
            });
        });
        startBattleActual.setRepeats(false);
        startBattleActual.start();
    }

    // Chapter 3 Battle (Premium Cigarette)
    private void startChapter3Battle() { 
        ui.displayText("\nNarrator: This cigarette feels different, heavier. A true test of resolve before the big game.", Color.GRAY);
        Timer startBattleActual = new Timer(2000, e2 -> {
            battleManager.startBattle("Temptation: Premium Cigarette", 25, 8, battleResult -> {
                if (battleResult == BattleManager.BattleResult.WIN) {
                    ui.displayText("\nCoach: \"Good endurance out there. You've got potential, next round, you’ll play.\"", Color.MAGENTA.darker());
                    state.adjustStat(GameState.PLAYER_HEALTH, 2);
                    state.adjustStat(GameState.PLAYER_MAX_HEALTH, 2);
                    state.adjustStat(GameState.PLAYER_ATTACK, 1);
                    state.adjustStat(STAT_PLAYER_DEFENSE, 1);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 2);
                    Timer r1 = new Timer(3000, res -> ui.displayText("\nSystem Status: \"Level Up! Health +2, Max Health +2, Attack +1, Defense +1, Willpower +2\"", Color.GREEN.darker()));
                    r1.setRepeats(false); r1.start();
                } else { 
                    ui.displayText("\nNarrator: \"During the game, you notice your lungs burning more than they should. Your performance suffers.\"", Color.GRAY);
                    state.adjustStat(GameState.PLAYER_MAX_HEALTH, -1); 
                    state.adjustStat(STAT_SOCIAL_STATUS, -1);
                    state.adjustStat(UNIQUE_STAT_KEY_S3, 5); // Nicotine Addiction +5
                    if(state.getStat(GameState.PLAYER_HEALTH) > state.getStat(GameState.PLAYER_MAX_HEALTH)) {
                        state.setStat(GameState.PLAYER_HEALTH, state.getStat(GameState.PLAYER_MAX_HEALTH));
                    }
                    Timer r1 = new Timer(3000, res -> ui.displayText("\nSystem Status: \"Status Effect: Diminished Lung Capacity. Max Health -1, Social Status -1. " + UNIQUE_STAT_KEY_S3 + " +5\"", Color.RED.darker()));
                    r1.setRepeats(false); r1.start();
                }
                Timer proceed = new Timer(6000, res -> showDialogue(12)); 
                proceed.setRepeats(false);
                proceed.start();
            });
        });
        startBattleActual.setRepeats(false);
        startBattleActual.start();
    }

    private void showStage0() { 
        ui.displayText("\n[University Campus - Dorm Room]", Color.DARK_GRAY);
        Timer t1 = new Timer(1000, e -> ui.displayText("\n" + playerName + ": \"Finally, I'm on my own. No parents, no rules.\"", Color.BLACK));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(3500, e -> ui.displayText("\n" + playerName + ": \"I can't wait to see what university life is like.\"", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(5000, e -> { // Changed from 6500 to 5000 to show choices sooner
            ui.displayText("\n" + playerName + ": (I wanna look around my room first, I guess...)", Color.GRAY); // Changed text slightly
            Timer choiceTimer = new Timer(1500, e2 -> ui.showChoicesDialog(new String[]{"Look at the Window", "Open your Closet", "Check your Phone"}));
            choiceTimer.setRepeats(false); choiceTimer.start();
        });
        t3.setRepeats(false); t3.start();
    }
    private void showStage1() { 
        ui.displayText("\n[A moment later, someone enters the room]", Color.DARK_GRAY);
        Timer t1 = new Timer(1500, e -> ui.displayText("\nRoomate: \"Hey, I'm Alex! Looks like we're roommates.\"", Color.BLUE.darker()));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(4000, e -> ui.displayText("\nAlex: \"There's a welcome party tonight, great way to meet people. You should come!\"", Color.BLUE.darker()));
        t2.setRepeats(false); t2.start();
        Timer proceedTimer = new Timer(7000, e -> showDialogue(2));
        proceedTimer.setRepeats(false); proceedTimer.start();
    }
    private void showStage2() { 
        ui.displayText("\n[Later that night - University Welcome Party]", Color.DARK_GRAY);
        Timer t1 = new Timer(1500, e -> ui.displayText("\nParty-goer: \"Hey freshman! Want a cigarette?\"", Color.ORANGE.darker()));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(4000, e -> ui.displayText("\nParty-goer: \"Everyone hangs out in the smoking area - best place to make friends.\"", Color.ORANGE.darker()));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(7000, e -> {
            ui.displayText("\nNarrator: Uh oh...", Color.RED.darker());
            Timer choiceTimer = new Timer(1500, e2 -> ui.showChoicesDialog(new String[]{"Face the Temptation"}));
            choiceTimer.setRepeats(false); choiceTimer.start();
        });
        t3.setRepeats(false); t3.start();
    }
    private void showStage3() { 
        ui.displayText("\n" + playerName + ": (That was intense... I need to stay strong.)", Color.BLACK);
        Timer proceedTimer = new Timer(3000, e -> showDialogue(4));
        proceedTimer.setRepeats(false); proceedTimer.start();
    }
    private void showStage4() { 
        ui.displayText("\nNarrator: The night continues, filled with music, laughter, and the lingering scent of smoke from a distance.", Color.GRAY);
        Timer t1 = new Timer(3000, e -> ui.displayText("\nNarrator: University life has begun, and its first test has passed.", Color.GRAY));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(6000, e -> ui.displayText("\n[End of Chapter 1]", Color.DARK_GRAY));
        t2.setRepeats(false); t2.start();
        Timer proceedToNextChapter = new Timer(8000, e -> showDialogue(5)); 
        proceedToNextChapter.setRepeats(false); proceedToNextChapter.start();
    }

    private void showStage5() { 
        ui.displayText("\n[Days 8-14: Late Night Study Session]", Color.DARK_GRAY);
        Timer t1 = new Timer(1000, e -> ui.displayText("\nStudy Partner: \"Ughhh, how do you do this? My brain is fried.\"", Color.CYAN.darker()));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(4000, e -> ui.displayText("\n" + playerName + ": \"Let's take a break. I'm also stressed about this.\"", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(7000, e -> ui.displayText("\nStudy Partner: \"Yeah, let's go out for some drinks, I can't handle all of this material.\"", Color.CYAN.darker()));
        t3.setRepeats(false); t3.start();
        Timer t4 = new Timer(10000, e -> ui.displayText("\n" + playerName + ": \"Ehhhh, I don't think I can do that.\"", Color.BLACK));
        t4.setRepeats(false); t4.start();
        Timer proceedTimer = new Timer(12000, e -> showDialogue(6));
        proceedTimer.setRepeats(false); proceedTimer.start();
    }

    private void showStage6() { 
        ui.displayText("\nStudy Partner: \"What, ugh fine. My roommate left his vape pen, how 'bout that? Helps with stress.\"", Color.CYAN.darker());
        Timer choiceTimer = new Timer(3000, e -> {
            ui.showChoicesDialog(new String[]{
                playerName + ": \"fine…\"", 
                playerName + ": \"No thanks, coffee for me.\"" 
            });
        });
        choiceTimer.setRepeats(false);
        choiceTimer.start();
    }
    private void showStage7() { 
        ui.displayText("\n" + playerName + ": (Another one down... or am I going down?)", Color.BLACK);
        Timer proceedTimer = new Timer(3000, e -> showDialogue(8));
        proceedTimer.setRepeats(false);
        proceedTimer.start();
    }
    private void showStage8() { 
        ui.displayText("\nNarrator: The pressure of exams and new social dynamics continues to mount.", Color.GRAY);
        Timer t1 = new Timer(3000, e -> ui.displayText("\n[End of Chapter 2]", Color.DARK_GRAY));
        t1.setRepeats(false); t1.start();
        Timer proceedToNextChapter = new Timer(5000, e -> showDialogue(9)); 
        proceedToNextChapter.setRepeats(false); proceedToNextChapter.start();
    }

    private void showStage9() { 
        ui.displayText("\n[Days 15-30: University Grounds]", Color.DARK_GRAY);
        Timer t1 = new Timer(1000, e -> ui.displayText("\n" + playerName + ": \"It's been a few months since I became a uni student, I should do something else to fill this void…\"", Color.BLACK));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(4500, e -> ui.displayText("\nTeammate: \"There’s this new slot open for the tryouts, you wanna try? I heard you’ll be regarded as one of the top people if you actually got in.\"", Color.GREEN.darker()));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(8000, e -> ui.displayText("\n" + playerName + ": \"sure…, I don't think I’ll be accepted though..\"", Color.BLACK));
        t3.setRepeats(false); t3.start();
        Timer proceedTimer = new Timer(10000, e -> showDialogue(10));
        proceedTimer.setRepeats(false); proceedTimer.start();
    }

    private void showStage10() { 
        ui.displayText("\n[Sports Tryouts]", Color.DARK_GRAY);
        Timer decisionTimer = new Timer(1500, e-> {
            if (state.getStat(UNIQUE_STAT_KEY_S3) < 10) { // Check Nicotine Addiction
                ui.displayText("\nCoach: \"Good lung capacity, freshman! You've got potential.\"", Color.MAGENTA.darker());
                Timer t1 = new Timer(3000, e2 -> ui.displayText("\n[Scene] Kai Joins the Sports Team.", Color.DARK_GRAY));
                t1.setRepeats(false); t1.start();
                state.setFlag("joinedSportsTeam", true); 
                state.adjustStat(STAT_SOCIAL_STATUS, 5); 
                Timer proceed = new Timer(5000, e2 -> showDialogue(11)); 
                proceed.setRepeats(false);
                proceed.start();
            } else {
                ui.displayText("\n[Scene] During tryouts, Kai struggles to keep up.", Color.DARK_GRAY);
                Timer t1 = new Timer(2500, e2 -> ui.displayText("\nCoach: \"You need to work on your stamina, kid. Come back when you're serious.\"", Color.MAGENTA.darker()));
                t1.setRepeats(false); t1.start();
                state.setFlag("failedSportsTryouts", true);
                state.adjustStat(STAT_SOCIAL_STATUS, -3);
                Timer proceed = new Timer(5000, e2 -> showDialogue(13)); 
                proceed.setRepeats(false);
                proceed.start();
            }
        });
        decisionTimer.setRepeats(false);
        decisionTimer.start();
    }

    private void showStage11() { 
        if (!state.getFlag("joinedSportsTeam")) { 
            showDialogue(13); 
            return;
        }
        ui.displayText("\n[Team Locker Room - Before First Game]", Color.DARK_GRAY);
        Timer t1 = new Timer(1000, e -> ui.displayText("\nTeammate: \"Coach is brutal, man. Most of us take a quick break behind the bleachers to calm our nerves before our first game.\"", Color.GREEN.darker()));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(5000, e -> ui.displayText("\n" + playerName + ": \"Yeah, I think I could use some break.\"", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(7500, e -> ui.displayText("\nTeammate #2: \"Yeah, here you go, the latest and most premium cigs you’ll ever see. IT costs a lot you know, you’ll have to try that out. I promise it’ll help.\"", Color.ORANGE.darker()));
        t3.setRepeats(false); t3.start();
        Timer t4 = new Timer(11000, e -> ui.displayText("\n" + playerName + ": (in his head) ”will it really?”", Color.GRAY));
        t4.setRepeats(false); t4.start();

        Timer choiceTimer = new Timer(13000, e -> {
            ui.showChoicesDialog(new String[]{
                "Try the premium cigarette",
                "Decline politely"
            });
        });
        choiceTimer.setRepeats(false);
        choiceTimer.start();
    }
    private void showStage12() { 
        ui.displayText("\n" + playerName + ": (The game is over... but what about my game?)", Color.BLACK);
        Timer proceedTimer = new Timer(3000, e -> showDialogue(13));
        proceedTimer.setRepeats(false);
        proceedTimer.start();
    }
    private void showStage13() { 
        ui.displayText("\nNarrator: The semester wears on, bringing new challenges and temptations.", Color.GRAY);
        Timer t1 = new Timer(3000, e -> ui.displayText("\n[End of Chapter 3]", Color.DARK_GRAY));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(5000, e -> {
             ui.displayText("\nTo be continued...", Color.BLACK);
        });
        t2.setRepeats(false); t2.start();
    }

    @Override
    public String[] getCurrentChoices() {
        if (battleManager.isBattleActive()) {
            return new String[]{
                "1. Resist (Attack)", 
                "2. Use Item (Not Implemented)"};
        }
        return new String[0]; 
    }
}