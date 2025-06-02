import java.awt.Color;
import java.util.Map;
import javax.swing.Timer; // Required for Map.of
// Make sure AudioManager is imported if it's in a different package,
// or accessible if in the same package. Assuming it's accessible.
// import AudioManager; // If needed

public class Storyline3 extends Storyline {
    private int dialogueState = 0;
    private BattleManager battleManager;
    private String playerName = "Kai"; 

    // Stat Keys - Making them public static for GameState's item factory
    public static final String UNIQUE_STAT_KEY_S3 = "nicotineAddictionLevel"; 
    public static final String STAT_SOCIAL_STATUS = "socialStatus";
    public static final String STAT_PLAYER_WILLPOWER = "playerWillpower";
    public static final String STAT_PLAYER_DEFENSE = "playerDefense";

    // New Flag for Chapter 4 Skill
    public static final String FLAG_SKILL_OVERCOME_ACQUIRED = "skillOvercomeAcquired";

    // Item Definitions
    private static final Item CIGARETTE_ITEM = new SmokingItem(
        "Cigarette", 
        "A standard, mass-produced cigarette.",
        "smokes a Cigarette. A brief, harsh buzz...",
        Map.of(UNIQUE_STAT_KEY_S3, 2, STAT_PLAYER_WILLPOWER, -1)
    );
    private static final Item VAPE_PEN_ITEM = new SmokingItem(
        "Vape Pen", 
        "A sleek, modern vape pen. Produces flavored vapor.",
        "uses the Vape Pen. Smooth vapor, lingering desire...",
        Map.of(UNIQUE_STAT_KEY_S3, 3, STAT_PLAYER_WILLPOWER, -1, STAT_PLAYER_DEFENSE, -1) // Example effect
    );
    private static final Item PREMIUM_CIGARETTE_ITEM = new SmokingItem(
        "Premium Cigarette", 
        "A high-quality, expensive cigarette. Promises a richer experience.",
        "lights up the Premium Cigarette. It's strong, a noticeable kick.",
        Map.of(UNIQUE_STAT_KEY_S3, 5, STAT_PLAYER_WILLPOWER, -2, GameState.PLAYER_HEALTH, -1, STAT_SOCIAL_STATUS, -1)
    );


    public Storyline3(GameUI ui, GameState state) {
        super(ui, state);
        this.battleManager = new BattleManager(ui, state);
        ui.setTitle("A Burning Melody"); //ini buat ngganti title e yang diatas itu
    }

    // ... getBattleManager, getDialogueState, setDialogueState, showDialoguePublic ...
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
        // Always start the requested stage from the beginning
        this.dialogueState = stage;
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
            case 14 -> showStage14_Chapter4Intro();
            case 15 -> showStage15_IsolationPath();
            case 16 -> showStage16_PostBattleCutscene();
            case 17 -> showStage17_Chapter4End();
            default -> showStage0();
        }
    }

    @Override
    public void startStory() {
        state.setStat(GameState.PLAYER_HEALTH, 10);
        state.setStat(GameState.PLAYER_MAX_HEALTH, 10);
        state.setStat(GameState.PLAYER_ATTACK, 1);
        state.setStat(STAT_PLAYER_DEFENSE, 0);
        state.setStat(STAT_PLAYER_WILLPOWER, 5);
        
        state.setStat(UNIQUE_STAT_KEY_S3, 0); 
        state.setStat(STAT_SOCIAL_STATUS, 0);
        state.setFlag(FLAG_SKILL_OVERCOME_ACQUIRED, false); // Initialize new flag
        
        ui.setStageImage("/Resources/Images/Story3/dorm-room.png"); // Initial image for the story
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/chapter1_intro_bgm.wav", true); // Initial BGM

        Timer timer = new Timer(500, e ->{
            ui.displayText("Chapter 1: First Encounter", Color.BLACK);
        });
        timer.setRepeats(false);
        timer.start();
        showDialogue(0); 
    }

    // ... showDialogue(int stage) method ...
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
            // Chapter 4 Stages
            case 14 -> showStage14_Chapter4Intro();
            case 15 -> showStage15_IsolationPath();
            case 16 -> showStage16_PostBattleCutscene();
            case 17 -> showStage17_Chapter4End();
        }
    }


    // New method to use an item from inventory
    public void useInventoryItem(String itemName) {
        Item itemToUse = state.getItemPrototype(itemName);
        if (itemToUse != null && state.getItemQuantity(itemName) > 0) {
            // Apply the item's effect
            itemToUse.applyEffect(state, ui, playerName);
            // Consume the item from inventory
            state.consumeItem(itemName);
            // Potentially update UI elements that show stats if you have any always-visible stat displays
        } else {
            ui.displayText("\n" + playerName + " doesn't have any " + itemName + " to use or item is unknown.", Color.BLACK);
        }
    }

    @Override
    public void handleChoice(int choice) { // Note: Storyline abstract class expects String
        if (battleManager.isBattleActive()) {
            battleManager.processPlayerTurn(choice);
        } else {
            handleDialogueChoice(choice);
        }
    }

    private void handleDialogueChoice(int choice) {
        switch(dialogueState) {
            case 0: 
                // ... existing code ...
                String choiceText = "";
                switch (choice) {
                    case 1: 
                        choiceText = "\n" + playerName + ": (The view from up here is pretty nice. New city, new life.)";
                        // ui.setStageImage("/Resources/Images/Story3/dorm_window_selected.png"); // Optional: image change on choice
                        break;
                    case 2: 
                        choiceText = "\n" + playerName + ": (Empty hangers. A blank slate, just like my social life right now.)";
                        // ui.setStageImage("/Resources/Images/Story3/dorm_closet_selected.png"); // Optional
                        break;
                    case 3: 
                        choiceText = "\n" + playerName + ": (No new messages. Guess everyone's busy settling in too.)";
                        // ui.setStageImage("/Resources/Images/Story3/dorm_phone_selected.png"); // Optional
                        break;
                    default:
                        choiceText = "\n" + playerName + ": (Hmm, what to do first?)";
                        break;
                }
                ui.displayText(choiceText, Color.BLACK);
                Timer proceedAfterChoiceTimer = new Timer(3000, e -> showDialogue(1)); 
                proceedAfterChoiceTimer.setRepeats(false);
                proceedAfterChoiceTimer.start();
                break;
            case 1:
                String textOutput = "";
                switch (choice) {
                    case 1:
                        textOutput = "\n" + playerName + ": Whoa... what's that smell";
                        break;
                    case 2:
                        textOutput = "\n" + playerName + ": Nice to meet you ALex!";
                        break;
                    case 3:
                        textOutput = "\n" + playerName + ": Whats that you're holding?";
                        break;
                    default:
                        textOutput = "\n" + playerName + ": Hmm, what to do first?";
                        break;
                }
                ui.displayText(textOutput, Color.BLACK);

                Timer t2 = new Timer(4000, e -> ui.displayText("\nAlex: \"There's a welcome party tonight, great way to meet people. You should come!\"", Color.BLUE.darker()));
                t2.setRepeats(false); t2.start();
                Timer proceedTimer = new Timer(7000, e -> showDialogue(2));
                proceedTimer.setRepeats(false); proceedTimer.start();
                break;
            case 2: // University Welcome Party
                if (choice == 1) { // Face the Temptation (Battle)
                    // Image for battle might be set by BattleManager or here
                    // ui.setStageImage("/Resources/Images/Story3/cigarette_temptation_battle.png");
                    startChapter1Battle();
                } else if (choice == 2) { // New option: Take the cigarette
                    ui.setStageImage("/Resources/Images/Story3/kai_takes_cigarette.png"); // Image for taking cigarette
                    ui.displayText("\n" + playerName + ": \"Uh, okay... I'll hold onto it.\"", Color.BLACK);
                    state.addItem(CIGARETTE_ITEM, 1);
                    ui.displayText("\nSystem: Added 1 " + CIGARETTE_ITEM.getItemName() + " to inventory.", Color.GREEN.darker());
                    Timer proceed = new Timer(3000, e -> showDialogue(3)); // Proceed to next stage
                    proceed.setRepeats(false);
                    proceed.start();
                }
                break;
            case 5: // Small talk during study session break
                String smallTalkText = "";
                switch (choice) {
                    case 1:
                        smallTalkText = "\n" + playerName + ": (This studying is killing me...)";
                        break;
                    case 2:
                        smallTalkText = "\n" + playerName + ": (Maybe I should take a break and explore the campus.)";
                        break;
                    case 3:
                        smallTalkText = "\n" + playerName + ": (I hope I can keep up with this pace.)";
                        break;
                    default:
                        smallTalkText = "\n" + playerName + ": (Focus, Kai. You can do this.)";
                        break;
                }
                ui.displayText(smallTalkText, Color.BLACK);
                Timer proceedTimerStage5 = new Timer(3000, e -> showDialogue(6));
                proceedTimerStage5.setRepeats(false);
                proceedTimerStage5.start();
                break;
            case 6: // Late Night Study Session - Vape offer
                if (choice == 1) { // "fine..." - leads to vape battle/use
                    ui.setStageImage("/Resources/Images/Story3/kai_accepts_vape.png"); // Image for accepting vape
                    ui.displayText("\n" + playerName + ": \"...fine.\"", Color.BLACK);
                    VAPE_PEN_ITEM.applyEffect(state, ui, playerName); 
                    Timer proceedAfterVape = new Timer(3000, e -> showDialogue(7)); 
                    proceedAfterVape.setRepeats(false);
                    proceedAfterVape.start();
                } else { // "No thanks, coffee for me."
                    ui.setStageImage("/Resources/Images/Story3/kai_declines_vape.png"); // Image for declining vape
                    ui.displayText("\n" + playerName + ": \"No thanks, I think I'll just grab some coffee instead. Need to keep my head clear.\"", Color.BLACK);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 1);
                    ui.displayText("\nSystem Status: Willpower +1", Color.GREEN.darker());
                    Timer proceed = new Timer(3000, e -> showDialogue(8)); 
                    proceed.setRepeats(false);
                    proceed.start();
                }
                break;
            case 7: // Kai's reflection after vape encounter/choice
                String kaiReflectionVape = "";
                switch (choice) {
                    case 1:
                        kaiReflectionVape = "\n" + playerName + ": (Is this becoming a habit? I need to be careful...)";
                        break;
                    case 2:
                        kaiReflectionVape = "\n" + playerName + ": (That wasn't so bad. Maybe it does help with stress a bit.)";
                        break;
                    case 3:
                        kaiReflectionVape = "\n" + playerName + ": (I should talk to Alex about this later. Or maybe not.)";
                        break;
                    default:
                        kaiReflectionVape = "\n" + playerName + ": (My head feels... fuzzy.)";
                        break;
                }
                ui.displayText(kaiReflectionVape, Color.GRAY);
                Timer proceedAfterReflectionVape = new Timer(3000, e -> showDialogue(8));
                proceedAfterReflectionVape.setRepeats(false);
                proceedAfterReflectionVape.start();
                break;
            case 9: // Kai's thoughts on filling the void
                String voidFillingThoughts = "";
                switch (choice) {
                    case 1:
                        voidFillingThoughts = "\n" + playerName + ": (Maybe joining a club could help...)";
                        break;
                    case 2:
                        voidFillingThoughts = "\n" + playerName + ": (I should focus on my studies and try out for the team.)";
                        break;
                    case 3:
                        voidFillingThoughts = "\n" + playerName + ": (It's time to stop procrastinating and start living.)";
                        break;
                    default:
                        voidFillingThoughts = "\n" + playerName + ": (This emptiness... I need to fill it with something meaningful.)";
                        break;
                }
                ui.displayText(voidFillingThoughts, Color.GRAY);
                Timer proceedTimerStage9 = new Timer(3000, e -> showDialogue(10));
                proceedTimerStage9.setRepeats(false);
                proceedTimerStage9.start();
                break;
            case 11: // Locker Room - Premium Cigarette offer
                if (choice == 1) { // Try the premium cigarette
                    ui.setStageImage("/Resources/Images/Story3/kai_tries_premium_cig.png"); // Image for trying premium
                    ui.displayText("\n" + playerName + ": \"Alright, just one then...\"", Color.BLACK);
                    PREMIUM_CIGARETTE_ITEM.applyEffect(state, ui, playerName);
                    Timer proceedAfterPremium = new Timer(3000, e -> showDialogue(12)); 
                    proceedAfterPremium.setRepeats(false);
                    proceedAfterPremium.start();
                } else { // Decline politely
                    ui.setStageImage("/Resources/Images/Story3/kai_declines_premium_cig.png"); // Image for declining
                    ui.displayText("\n" + playerName + ": \"Nah, I'm good. Trying to keep my lungs clear for the game.\"", Color.BLACK);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 2);
                    ui.displayText("\nSystem Status: Willpower +2", Color.GREEN.darker());
                    Timer proceed = new Timer(3000, e -> showDialogue(13)); 
                    proceed.setRepeats(false);
                    proceed.start();
                }
                break;
            case 12: // Kai's reflection after premium cigarette encounter/game
                String kaiReflectionPremium = "";
                switch (choice) {
                    case 1:
                        kaiReflectionPremium = "\n" + playerName + ": (That was a close call. Or was it? The game felt off.)";
                        break;
                    case 2:
                        kaiReflectionPremium = "\n" + playerName + ": (I need to focus on my health if I want to keep playing.)";
                        break;
                    case 3:
                        kaiReflectionPremium = "\n" + playerName + ": (These 'breaks' are getting more intense. Is this what it takes?)";
                        break;
                    default:
                        kaiReflectionPremium = "\n" + playerName + ": (My lungs feel heavy. Not good.)";
                        break;
                }
                ui.displayText(kaiReflectionPremium, Color.GRAY);
                Timer proceedAfterReflectionPremium = new Timer(3000, e -> showDialogue(13));
                proceedAfterReflectionPremium.setRepeats(false);
                proceedAfterReflectionPremium.start();
                break;
            // --- CHAPTER 4 CHOICES ---
            case 14: // Choices from showStage14_Chapter4Intro
                if (choice == 1) { // Join them (Face another enemy encounter)
                    // Image for battle might be set by BattleManager or here
                    // ui.setStageImage("/Resources/Images/Story3/balcony_vape_battle.png");
                    startChapter4Battle();
                } else { // Find another group (Risk isolation)
                    showDialogue(15); // -> showStage15_IsolationPath will set its own image
                }
                break;
            // case 17: // For "Back to Main Menu" - no image change needed here usually
            //    break; 
        }
    }

    // --- Battle Methods ---
    private void startChapter1Battle() {
        ui.setStageImage("/Resources/Images/Story3/cigarette_temptation_battle_bg.png"); // Battle background
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/battle_temptation1_bgm.wav", true); // Battle BGM
        
        ui.displayText("\nNarrator: A wave of peer pressure washes over you. It feels like a challenge.", Color.GRAY);
        Timer startBattleActual = new Timer(2000, e2 -> {
            battleManager.startBattle("Temptation: Cigarette", 10, 5, battleResult -> {
                // BGM will be changed by the next showDialogue stage
                if (battleResult == BattleManager.BattleResult.WIN) {
                    ui.setStageImage("/Resources/Images/Story3/kai_resists_cigarette.png"); // Victory image
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
                } else { // LOSS - Player succumbs
                    ui.setStageImage("/Resources/Images/Story3/kai_succumbs_cigarette.png"); // Defeat image
                    ui.displayText("\nNarrator: \"The smoke fills your lungs. It burns slightly, but there's also a strange buzz...\"", Color.GRAY);
                    CIGARETTE_ITEM.applyEffect(state, ui, playerName); 
                    Timer proceed = new Timer(6000, res -> showDialogue(3));
                    proceed.setRepeats(false);
                    proceed.start();
                }
            });
        });
        startBattleActual.setRepeats(false);
        startBattleActual.start();
    }
    
    private void startChapter2Battle() { 
        ui.setStageImage("/Resources/Images/Story3/vape_temptation_battle_bg.png"); // Battle background
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/battle_temptation2_bgm.wav", true); // Battle BGM
        ui.displayText("\nNarrator: The sleek vape pen gleams. Another test of will.", Color.GRAY);
        Timer startBattleActual = new Timer(2000, e2 -> {
            battleManager.startBattle("Temptation: Vape", 15, 4, battleResult -> {
                // BGM will be changed by the next showDialogue stage
                if (battleResult == BattleManager.BattleResult.WIN) {
                    ui.setStageImage("/Resources/Images/Story3/kai_resists_vape.png"); // Victory image
                    state.adjustStat(GameState.PLAYER_HEALTH, 1);
                    state.adjustStat(GameState.PLAYER_MAX_HEALTH, 1);
                    state.adjustStat(GameState.PLAYER_ATTACK, 1);
                    state.adjustStat(STAT_PLAYER_DEFENSE, 1);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 2); 
                    ui.displayText("\nSystem Status: \"Level Up! Health +1, Max Health +1, Attack +1, Defense +1, Willpower +2\"", Color.GREEN.darker());
                    Timer t1 = new Timer(2500, res -> ui.displayText("\n" + playerName + ": \"I think I'll just grab some coffee instead. Need to keep my head clear.\"", Color.BLACK));
                    t1.setRepeats(false); t1.start();
                    Timer proceed = new Timer(5000, res -> showDialogue(7)); 
                    proceed.setRepeats(false);
                    proceed.start();
                } else { // LOSS
                    ui.setStageImage("/Resources/Images/Story3/kai_succumbs_vape.png"); // Defeat image
                    ui.displayText("\nStudy Partner: \"See? Feels better, right? You can borrow it anytime.\"", Color.CYAN.darker());
                    VAPE_PEN_ITEM.applyEffect(state, ui, playerName);
                    Timer t2 = new Timer(3000, res -> ui.displayText("\nNarrator: “Later, Kai struggles to fall asleep...\"", Color.GRAY)); 
                    t2.setRepeats(false); t2.start();
                    Timer proceed = new Timer(6000, res -> showDialogue(7)); 
                    proceed.setRepeats(false);
                    proceed.start();
                }
            });
        });
        startBattleActual.setRepeats(false);
        startBattleActual.start();
    }

    private void startChapter3Battle() { 
        ui.setStageImage("/Resources/Images/Story3/premium_cig_battle_bg.png"); // Battle background
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/battle_temptation3_bgm.wav", true); // Battle BGM
        ui.displayText("\nNarrator: This cigarette feels different, heavier. A true test of resolve.", Color.GRAY);
        Timer startBattleActual = new Timer(2000, e2 -> {
            battleManager.startBattle("Temptation: Premium Cigarette", 25, 8, battleResult -> {
                // BGM will be changed by the next showDialogue stage
                if (battleResult == BattleManager.BattleResult.WIN) {
                    ui.setStageImage("/Resources/Images/Story3/kai_resists_premium_cig.png"); // Victory image
                    ui.displayText("\nCoach: \"Good endurance out there. You've got potential, next round, you’ll play.\"", Color.MAGENTA.darker());
                    state.adjustStat(GameState.PLAYER_HEALTH, 2);
                    state.adjustStat(GameState.PLAYER_MAX_HEALTH, 2);
                    state.adjustStat(GameState.PLAYER_ATTACK, 1);
                    state.adjustStat(STAT_PLAYER_DEFENSE, 1);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 2);
                    Timer r1 = new Timer(3000, res -> ui.displayText("\nSystem Status: \"Level Up! Health +2, Max Health +2, Attack +1, Defense +1, Willpower +2\"", Color.GREEN.darker()));
                    r1.setRepeats(false); r1.start();
                } else { // Defeat image and apply item effect
                    ui.setStageImage("/Resources/Images/Story3/kai_succumbs_premium_cig.png"); // Defeat image
                    ui.displayText("\nNarrator: \"During the game, you notice your lungs burning... Your performance suffers.\"", Color.GRAY);
                    PREMIUM_CIGARETTE_ITEM.applyEffect(state, ui, playerName);
                    if(state.getStat(GameState.PLAYER_HEALTH) > state.getStat(GameState.PLAYER_MAX_HEALTH)) {
                        state.setStat(GameState.PLAYER_HEALTH, state.getStat(GameState.PLAYER_MAX_HEALTH));
                    }
                }
                Timer proceed = new Timer(6000, res -> showDialogue(12)); 
                proceed.setRepeats(false);
                proceed.start();
            });
        });
        startBattleActual.setRepeats(false);
        startBattleActual.start();
    }

    // --- CHAPTER 4 BATTLE ---
    private void startChapter4Battle() {
        ui.setStageImage("/Resources/Images/Story3/balcony_vape_battle_bg.png"); // Battle background
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/battle_boss_social_bgm.wav", true); // Boss Battle BGM
        ui.displayText("\nNarrator: Kai heads to the balcony, a cloud of sweet-smelling vapor hangs in the air.", Color.GRAY);
        Timer preBattleTimer = new Timer(2500, e -> {
            ui.displayText("\nSocial Smoking Boss: \"EVERYONE DOES IT! ONE WON'T HURT! DON'T BE ANTISOCIAL!\"", Color.RED.darker());
            Timer startBattleActual = new Timer(3000, e2 -> {
                battleManager.startBattle("Social Smoking (Vape Form)", 999, 25, battleResult -> {
                    // Regardless of actual win/loss, the story dictates a "near defeat" scenario
                    // For simplicity, we'll use the LOSS path to trigger the cutscene.
                    // If you want a true "near defeat" trigger, BattleManager would need modification.
                    
                    // Cutscene and Skill Acquisition
                    showDialogue(16); // -> showStage16_PostBattleCutscene
                });
            });
            startBattleActual.setRepeats(false);
            startBattleActual.start();
        });
        preBattleTimer.setRepeats(false);
        preBattleTimer.start();
    }


    // --- Stage Methods ---
    private void showStage0() { 
        ui.setStageImage("/Resources/Images/Story3/dorm-room.png"); // Kai waking up
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage-0.wav", true); // Stage BGM
        
        ui.displayText("\n[University Campus - Dorm Room]", Color.DARK_GRAY);
        Timer t1 = new Timer(1000, e -> ui.displayText("\n" + playerName + ": \"Finally, I'm on my own. No parents, no rules.\"", Color.BLACK));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(3500, e -> ui.displayText("\n" + playerName + ": \"I can't wait to see what university life is like.\"", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(5000, e -> { 
            ui.displayText("\n" + playerName + ": (I wanna look around my room first, I guess...)", Color.GRAY); 
            Timer choiceTimer = new Timer(1500, e2 -> ui.showChoicesDialog(new String[]{"Look at the Window", "Open your Closet", "Check your Phone"}));
            choiceTimer.setRepeats(false); choiceTimer.start();
        });
        t3.setRepeats(false); t3.start();
    }
    private void showStage1() { 
        ui.setStageImage("/Resources/Images/Story3/stage-1.png"); // Alex entering
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage-1.wav", true); // Stage BGM
        
        ui.displayText("\n[A moment later, someone enters the room]", Color.DARK_GRAY);
        Timer t1 = new Timer(1500, e -> ui.displayText("\nRoomate: \"Hey, I'm Alex! Looks like we're roommates.\"", Color.BLUE.darker()));
        t1.setRepeats(false); t1.start();
        Timer t3 = new Timer(5000, e -> { 
            ui.displayText("\n" + playerName + ": Hey Alex, im Kai", Color.GRAY); 
            Timer choiceTimer = new Timer(2000, e2 -> ui.showChoicesDialog(new String[]{"Whoa... whats that smell", "Nice to meet you!", "Whats that you're holding?"}));
            choiceTimer.setRepeats(false); choiceTimer.start();
        });
        t3.setRepeats(false); t3.start();
    }


    private void showStage2() { 
        ui.setStageImage("/Resources/Images/Story3/welcome_party_crowd.png"); // Party scene
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage2_party_bgm.wav", true); // Stage BGM
        ui.displayText("\n[Later that night - University Welcome Party]", Color.DARK_GRAY);
        Timer t1 = new Timer(1500, e -> ui.displayText("\nParty-goer: \"Hey freshman! Want a cigarette?\"", Color.ORANGE.darker()));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(4000, e -> ui.displayText("\nParty-goer: \"Everyone hangs out in the smoking area - best place to make friends.\"", Color.ORANGE.darker()));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(7000, e -> {
            ui.displayText("\nNarrator: Uh oh...", Color.RED.darker());
            Timer choiceTimer = new Timer(1500, e2 -> ui.showChoicesDialog(new String[]{
                "Face the Temptation (Battle)", 
                playerName + ": \"Uh, maybe just one to hold onto...\"" 
            }));
            choiceTimer.setRepeats(false); choiceTimer.start();
        });
        t3.setRepeats(false); t3.start();
    }
    private void showStage3() { 
        ui.setStageImage("/Resources/Images/Story3/kai_reflects_after_party.png"); // Kai thinking
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage3_reflection_bgm.wav", true); // Stage BGM
        ui.displayText("\n" + playerName + ": (That was intense... I need to stay strong.)", Color.BLACK);
        Timer proceedTimer = new Timer(3000, e -> showDialogue(4));
        proceedTimer.setRepeats(false); proceedTimer.start();
    }
    private void showStage4() { 
        ui.setStageImage("/Resources/Images/Story3/campus_night_end_chapter1.png"); // Campus at night
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage4_chapter_end_bgm.wav", true); // Stage BGM
        ui.displayText("\nNarrator: The night continues, filled with music, laughter, and the lingering scent of smoke from a distance.", Color.GRAY);
        Timer t1 = new Timer(3000, e -> ui.displayText("\nNarrator: University life has begun, and its first test has passed.", Color.GRAY));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(6000, e -> ui.displayText("\n[End of Chapter 1]", Color.DARK_GRAY));
        t2.setRepeats(false); t2.start();
        Timer proceedToNextChapter = new Timer(8000, e -> showDialogue(5)); 
        proceedToNextChapter.setRepeats(false); proceedToNextChapter.start();
    }

    private void showStage5() { 
        ui.setStageImage("/Resources/Images/Story3/study_session_late.png"); // Late night study
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage5_study_bgm.wav", true); // Stage BGM
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
        ui.setStageImage("/Resources/Images/Story3/study_partner_offers_vape.png"); // Vape offer
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage6_vape_offer_bgm.wav", true); // Stage BGM
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
        ui.setStageImage("/Resources/Images/Story3/kai_reflects_vape_choice.png"); // Kai thinking about vape
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage7_vape_reflection_bgm.wav", true); // Stage BGM
        ui.displayText("\n" + playerName + ": (That whole situation with the vape...)", Color.GRAY.darker()); 
        Timer choiceTimer = new Timer(1500, e_ct -> ui.showChoicesDialog(new String[]{
            "\"Is this becoming a habit?\"",
            "\"That wasn't so bad...\"",
            "\"I should talk to someone.\""
        }));
        choiceTimer.setRepeats(false);
        choiceTimer.start();
    }

    private void showStage8() { 
        ui.setStageImage("/Resources/Images/Story3/exam_pressure_end_chapter2.png"); // Exam pressure montage
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage8_chapter_end_bgm.wav", true); // Stage BGM
        ui.displayText("\nNarrator: The pressure of exams and new social dynamics continues to mount.", Color.GRAY);
        Timer t1 = new Timer(3000, e -> ui.displayText("\n[End of Chapter 2]", Color.DARK_GRAY));
        t1.setRepeats(false); t1.start();
        Timer proceedToNextChapter = new Timer(5000, e -> showDialogue(9)); 
        proceedToNextChapter.setRepeats(false); proceedToNextChapter.start();
    }

    private void showStage9() { 
        ui.setStageImage("/Resources/Images/Story3/university_grounds_contemplation.png"); // Kai on campus thinking
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage9_contemplation_bgm.wav", true); // Stage BGM
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
        ui.setStageImage("/Resources/Images/Story3/sports_tryouts_field.png"); // Sports tryouts
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage10_tryouts_bgm.wav", true); // Stage BGM
        ui.displayText("\n[Sports Tryouts]", Color.DARK_GRAY);
        Timer decisionTimer = new Timer(1500, e-> {
            if (state.getStat(UNIQUE_STAT_KEY_S3) < 10) { 
                ui.setStageImage("/Resources/Images/Story3/kai_joins_team.png"); // Kai successful at tryouts
                ui.displayText("\nCoach: \"Good lung capacity, freshman! You've got potential.\"", Color.MAGENTA.darker());
                Timer t1 = new Timer(3000, e2 -> ui.displayText("\n[Scene] Kai Joins the Sports Team.", Color.DARK_GRAY));
                t1.setRepeats(false); t1.start();
                state.setFlag("joinedSportsTeam", true); 
                state.adjustStat(STAT_SOCIAL_STATUS, 5); 
                Timer proceed = new Timer(5000, e2 -> showDialogue(11)); 
                proceed.setRepeats(false);
                proceed.start();
            } else {
                ui.setStageImage("/Resources/Images/Story3/kai_fails_tryouts.png"); // Kai struggling at tryouts
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
            ui.setStageImage("/Resources/Images/Story3/kai_not_on_team_generic.png");
            AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage11_alt_path_bgm.wav", true); // Alt path BGM
            showDialogue(13); 
            return;
        }
        ui.setStageImage("/Resources/Images/Story3/locker_room_before_game.png"); // Locker room scene
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage11_locker_room_bgm.wav", true); // Stage BGM
        ui.displayText("\n[Team Locker Room - Before First Game]", Color.DARK_GRAY);
        Timer t1 = new Timer(1000, e -> ui.displayText("\nTeammate: \"Coach is brutal, man. Most of us take a quick break behind the bleachers to calm our nerves before our first game.\"", Color.GREEN.darker()));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(5000, e -> ui.displayText("\n" + playerName + ": \"Yeah, I think I could use some break.\"", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(7500, e -> ui.displayText("\nTeammate #2: \"Yeah, here you go, the latest and most premium cigs you'll ever see. It costs a lot you know, you’ll have to try that out. I promise it’ll help.\"", Color.ORANGE.darker()));
        t3.setRepeats(false); t3.start();
        Timer t4 = new Timer(11000, e -> ui.displayText("\n" + playerName + ": (in his head) \"will it really?\"", Color.GRAY));
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
        ui.setStageImage("/Resources/Images/Story3/kai_reflects_premium_choice.png"); // Kai thinking after premium cig choice/game
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage12_premium_reflection_bgm.wav", true); // Stage BGM
        ui.displayText("\n" + playerName + ": (That game... and that cigarette before...)", Color.GRAY.darker()); 
        Timer choiceTimer = new Timer(1500, e_ct -> ui.showChoicesDialog(new String[]{
            "\"That was a close call.\"",
            "\"Need to focus on health.\"",
            "\"Are these 'breaks' worth it?\""
        }));
        choiceTimer.setRepeats(false);
        choiceTimer.start();
    }

    private void showStage13() { 
        ui.setStageImage("/Resources/Images/Story3/semester_ends_end_chapter3.png"); // End of semester/chapter
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage13_chapter_end_bgm.wav", true); // Stage BGM
        ui.displayText("\nNarrator: The semester wears on, bringing new challenges and temptations.", Color.GRAY);
        Timer t1 = new Timer(3000, e -> ui.displayText("\n[End of Chapter 3]", Color.DARK_GRAY));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(5000, e -> {
             showDialogue(14);
        });
        t2.setRepeats(false); t2.start();
    }

    // --- CHAPTER 4 STAGE METHODS ---

    private void showStage14_Chapter4Intro() {
        ui.setStageImage("/Resources/Images/Story3/jock_party_house_exterior.png"); // Party house
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage14_party_intro_bgm.wav", true); // Stage BGM
        ui.displayText("\n\n--- Chapter 4: Social Pressures (Days 31-45) ---", Color.MAGENTA.darker());
        Timer t0 = new Timer(1500, e -> ui.displayText("\n[Scene] Weekend party at popular student's house", Color.DARK_GRAY));
        t0.setRepeats(false); t0.start();

        Timer t1 = new Timer(3500, e -> ui.displayText("\nNarrator: \"After the first months, one of the popular jocks invites most of the freshmen to a party in a rented house.\"", Color.GRAY));
        t1.setRepeats(false); t1.start();

        Timer t2 = new Timer(7000, e -> ui.displayText("\nParty Host (Jock): \"Yo, Kai! Everyone's heading to the balcony for vapes. Coming?\"", Color.ORANGE.darker()));
        t2.setRepeats(false); t2.start();

        Timer t3 = new Timer(10000, e -> {
            ui.showChoicesDialog(new String[]{
                "Join them on the balcony.", 
                "Nah, I'll find another group."  
            });
        });
        t3.setRepeats(false); t3.start();
    }

    private void showStage15_IsolationPath() {
        ui.setStageImage("/Resources/Images/Story3/kai_alone_at_party.png"); // Kai feeling isolated
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage15_isolation_bgm.wav", true); // Stage BGM
        ui.displayText("\n" + playerName + ": \"Nah, I think I'll pass. Maybe later.\"", Color.BLACK);
        Timer t1 = new Timer(2000, e -> ui.displayText("\nNarrator: Kai wanders through the party, feeling a bit out of place. The laughter from the balcony seems distant.", Color.GRAY));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(5000, e -> ui.displayText("\n" + playerName + ": (Maybe I should've just gone... it's hard to connect when you're always on the outside.)", Color.GRAY.darker()));
        t2.setRepeats(false); t2.start();
        Timer proceedTimer = new Timer(8000, e -> showDialogue(17)); 
        proceedTimer.setRepeats(false);
        proceedTimer.start();
    }

    private void showStage16_PostBattleCutscene() {
        ui.setStageImage("/Resources/Images/Story3/kai_overwhelmed_balcony_skill_gain.png"); // Kai after "unbeatable" battle
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage16_skill_gain_bgm.wav", true); // Stage BGM
        ui.displayText("\nNarrator: Overwhelmed, Kai stumbles back, the pressure immense.", Color.RED.darker());
        Timer t1 = new Timer(3000, e -> ui.displayText("\n" + playerName + "'s Inner Voice: \"I... I can't win this now. But I understand now. Each attempt, each failure... it teaches me something new.\"", Color.BLUE.darker()));
        t1.setRepeats(false); t1.start();
        
        Timer t2 = new Timer(7000, e -> {
            ui.displayText("\nSystem Message: \"Evolution! New Skill Acquired: 'Overcome' - The ability to learn from failures and build resilience against future attacks.\"", Color.CYAN.darker());
            state.setFlag(FLAG_SKILL_OVERCOME_ACQUIRED, true);
        });
        t2.setRepeats(false); t2.start();

        Timer proceedTimer = new Timer(11000, e -> showDialogue(17)); 
        proceedTimer.setRepeats(false);
        proceedTimer.start();
    }

    private void showStage17_Chapter4End() {
        ui.setStageImage("/Resources/Images/Story3/party_ends_kai_reflects_end_chapter4.png"); // End of party, end of chapter
        AudioManager.getInstance().playMusic("/Resources/Audio/Story3/stage17_story_ending_bgm.wav", true); // Ending BGM
        ui.displayText("\nNarrator: The party fades into the night, leaving Kai with new experiences and a growing understanding of the challenges ahead.", Color.GRAY);
        Timer t1 = new Timer(4000, e -> ui.displayText("\n[End of Chapter 4]", Color.DARK_GRAY));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(6000, e -> {
             ui.displayText("\nTo be continued...", Color.BLACK);
             ui.setStageImage(null); // Clear image or set a "To be continued" screen
             // Music will be stopped by handleChoice case 17 when user selects an option
        });
        t2.setRepeats(false); t2.start();
        // Note: The "Back to Main Menu" choices are handled in handleDialogueChoice case 17
        // and will appear after "To be continued..."
        // If you want an image during that choice, it should be set before the choice dialog appears.
        // For now, the image is cleared with "To be continued..."
    }


    @Override
    public String[] getCurrentChoices() {
        if (battleManager.isBattleActive()) {
            return new String[]{
                "1. Resist (Attack)", 
                "2. Use Item (Not Implemented)"}; // Consider if items can be used in battle
        }
        // Return empty or specific choices if your GameUI needs them outside of showChoicesDialog calls
        return new String[0]; 
    }
}