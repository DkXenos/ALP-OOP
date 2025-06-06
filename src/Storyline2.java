import java.awt.Color;
import java.util.Map;
import java.util.Random;

import javax.swing.Timer;

public class Storyline2 extends Storyline {
    private int dialogueState = 0;
    private BattleManager battleManager;
    private String playerName = "Milo"; 

    public static final String UNIQUE_STAT_KEY_S2 = "drugAddictionLevel"; 
    public static final String STAT_SOCIAL_STATUS = "socialStatus";
    public static final String STAT_PLAYER_WILLPOWER = "playerWillpower";
    public static final String STAT_PLAYER_DEFENSE = "playerDefense";

private static final Item XANAX_ITEM = new SmokingItem(
        "XANAX", 
        "Xanax is a nicotine replacement product.",
        "smokes a Xanax. A brief, harsh buzz...",
        Map.of(UNIQUE_STAT_KEY_S2, 2, STAT_PLAYER_WILLPOWER, -1)
    );

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
            case 12 -> showStage12();
            case 13 -> showStage13();
            case 14 -> showStage14();
            case 15 -> showStage15();
            case 16 -> showStage16();

        }
    }
 
    @Override
    public void startStory() {
        state.setStat(GameState.PLAYER_HEALTH, 10);
        state.setStat(GameState.PLAYER_MAX_HEALTH, 10);
        state.setStat(GameState.PLAYER_ATTACK, 1);
        state.setStat(STAT_PLAYER_DEFENSE, 0);
        state.setStat(STAT_PLAYER_WILLPOWER, 5);
        state.setStat(UNIQUE_STAT_KEY_S2, 0);
        state.setStat(STAT_SOCIAL_STATUS, 0);
        
        ui.setStageImage("/Resources/Images/Story2/classroom.png"); // Updated with proper path
        AudioManager.getInstance().playMusic("/Resources/Audio/Story2/chapter1_intro_bgm.wav", true); // Updated with proper path

        Timer timer = new Timer(500, e -> {
            ui.displayText("Chapter 1: A New Beginning", Color.BLACK);
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

    public int realPlayerDamage() {
    int baseAttack = state.getStat(GameState.PLAYER_ATTACK);
    // Returns random value between baseAttack-1 and baseAttack+2
    return baseAttack - 1 + new Random().nextInt(4);
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
                String choiceText = "";
                switch (choice) {
                    case 1: 
                        choiceText = "\n(You steady yourself and push open the door. Confidence is key)";
                        break;
                    case 2: 
                        choiceText = "\n(You pull out your phone and review your carefully planned schedule. Everything must be perfect.)";
                        break;
                    case 3: 
                        choiceText = "\n(You see yourself reflected among the trophies. Soon, you think, your achievements will be displayed here too.)";
                        break;
                    default:
                        choiceText = "\n" + playerName + ": (Hmm, what to do first?)";
                        break;
                }
                ui.displayText(choiceText, Color.BLACK);  
                Timer t4 = new Timer(1500, e -> ui.displayText("\n" + "Teacher" + ": \"Class, we have a transfer student. Milo scored in the 98th percentile on the entrance exam. We expect great things.", Color.BLACK));
                t4.setRepeats(false); t4.start();
                Timer proceedAfterChoiceTimer = new Timer(3000, e -> showDialogue(1)); 
                proceedAfterChoiceTimer.setRepeats(false);
                proceedAfterChoiceTimer.start();
                break;

            case 4: // Family dinner choices
                String familyChoiceText = "";
                switch (choice) {
                    case 1: 
                        familyChoiceText = "\nNarrator: \"You keep your head down, hoping to avoid confrontation. Food tastes like nothing.\"";
                        break;
                    case 2: 
                        familyChoiceText = "\nNarrator: \"You prepare your arguments, knowing this conversation was inevitable.\"";
                        break;
                    case 3: 
                        familyChoiceText = "\nNarrator: \"Maybe they have wisdom to share, though you doubt they understand the pressure.\"";
                        break;
                }
                ui.displayText(familyChoiceText, Color.GRAY);
                Timer familyProceed = new Timer(3000, e -> ui.displayText("\nDad: \"Everyone's working hard, Milo. That's just how it is these days. You can't expect things to be handed to you.\"", Color.BLUE.darker()));
                familyProceed.setRepeats(false); familyProceed.start();
                Timer proceedAfterChoiceTimer2 = new Timer(3000, e -> showDialogue(5)); 
                proceedAfterChoiceTimer2.setRepeats(false);
                proceedAfterChoiceTimer2.start();
             
                  break;

            case 5: // Jake's offer choices  
                String jakeChoiceText = "";
                switch (choice) {
                    case 1:
                        jakeChoiceText = "\nNarrator: \"Curiosity wins over caution. You lean in to listen.\"";
                        break;
                    case 2:
                        jakeChoiceText = "\nNarrator: \"You try to brush him off, but he follows you.\"";
                        break;
                    case 3:
                        jakeChoiceText = "\nNarrator: \"You're desperate for help but wary of getting into trouble.\"";
                        break;
                }
                ui.displayText(jakeChoiceText, Color.GRAY);
                Timer jakeOffer = new Timer(2000, e -> {
                    ui.displayText("\nJake: (lowering his voice) \"My older brother gave me these. Energy pills, way better than coffee.\" (hands you a small bottle) \"Keep them. Use them when you really need them.\"", Color.GREEN.darker());
                    Timer t1 = new Timer(5000, e2 ->
                        ui.displayText("\nSystem: Added Energy Pills to inventory.", Color.RED.darker()));
                        // Add energy pills to inventory here if you have that system
            
                    t1.setRepeats(false); t1.start();
                    Timer t2 = new Timer(9000, e2 -> ui.displayText("\n" + playerName + ": \"I... thanks. I'll think about it.\"\nJake: \"Trust me, they're lifesavers during finals.\"", Color.BLACK));
                    t2.setRepeats(false); t2.start(); 
                });
                jakeOffer.setRepeats(false); jakeOffer.start();
                Timer proceed = new Timer(13000, e2 -> showDialogue(6));
                proceed.setRepeats(false); proceed.start();
                break;

            case 6: // Counselor choices
                String counselorChoiceText = "";
                switch (choice) {
                    case 1:
                        counselorChoiceText = "\nNarrator: \"You decide honesty might be the best policy, though you're scared of the consequences.\"";
                        break;
                    case 2:
                        counselorChoiceText = "\nNarrator: \"You've researched the symptoms. This is your way to get legitimate help.\"";
                        break;
                    case 3:
                        counselorChoiceText = "\nNarrator: \"Maybe there are other solutions you haven't considered yet.\"";
                        break;
                }
                ui.displayText(counselorChoiceText, Color.GRAY);
                Timer counselorProceed = new Timer(3000, e -> startChapter6Battle());
                counselorProceed.setRepeats(false); counselorProceed.start();
                break;

            case 7: // Jake conversation choices (Month 3)
                String outputText = "";
                switch (choice) {
                    case 1: 
                        outputText = "\nNarrator: \"Honesty feels dangerous, but you're running out of options.\"";
                        break;
                    case 2: 
                        outputText = "\nNarrator: \"You try to maintain some dignity, but Jake sees right through you.\"";
                        break;
                    case 3: 
                        outputText = "\nNarrator: \"You've crossed a line you never thought you would. The old you would be horrified.\"";
                        break;
                }
                ui.displayText(outputText, Color.BLACK);   
                Timer proceedAfterChoiceTimer3 = new Timer(5000, e -> showDialogue(8)); 
                proceedAfterChoiceTimer3.setRepeats(false);
                proceedAfterChoiceTimer3.start();
                break;

            case 9: // Month 4 parent confrontation choices
                String parentChoiceText = "";
                switch (choice) {
                    case 1:
                        parentChoiceText = "\nNarrator: \"You put on your best performance, but your hands shake as you speak.\"";
                        break;
                    case 2:
                        parentChoiceText = "\nNarrator: \"Anger is easier than admitting the truth. You lash out at the people who care most.\"";
                        break;
                    case 3:
                        parentChoiceText = "\nNarrator: \"For a moment, you consider honesty. But the words stick in your throat.\"";
                        break;
                }
                ui.displayText(parentChoiceText, Color.GRAY);
                Timer parentProceed = new Timer(3000, e -> showDialogue(10));
                parentProceed.setRepeats(false); parentProceed.start();
                break;

            case 10: // Online research choices
                String researchChoiceText = "";
                switch (choice) {
                    case 1:
                        researchChoiceText = "\nNarrator: \"You try to resist, but sleep won't come without chemical assistance.\"";
                        break;
                    case 2:
                        researchChoiceText = "\nNarrator: \"The rabbit hole goes deeper. Each search leads to more dangerous territory.\"";
                        break;
                    case 3:
                        researchChoiceText = "\nNarrator: \"Jake has become your lifeline to a world you never wanted to enter.\"";
                        break;
                }
                ui.displayText(researchChoiceText, Color.GRAY);
                Timer researchProceed = new Timer(3000, e -> showDialogue(11));
                researchProceed.setRepeats(false); researchProceed.start();
                break;
                 case 12: //  dealer choices
            String dealerChoiceText = "";
            switch (choice) {
                case 1:
                    dealerChoiceText = "\nNarrator: \"You slam the laptop shut, your hands shaking. This has gone too far.\"";
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 1);
                    break;
                case 2:
                    dealerChoiceText = "\nNarrator: \"Curiosity and desperation override your better judgment.\"";
                    state.adjustStat(UNIQUE_STAT_KEY_S2, 1);
                    break;
                case 3:
                    dealerChoiceText = "\nNarrator: \"You've crossed a line you never thought you would cross.\"";
                    state.adjustStat(UNIQUE_STAT_KEY_S2, 2);
                    break;
            }
            ui.displayText(dealerChoiceText, Color.GRAY);
            Timer dealerProceed = new Timer(3000, e -> showDialogue(13));
            dealerProceed.setRepeats(false); dealerProceed.start();
            break;
            
        case 15: 
            String finalChoiceText = "";
            switch (choice) {
                case 1:
                    finalChoiceText = "\nNarrator: \"You gather all your remaining strength to fight the monster you've become.\"";
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 2);
                    break;
                case 2:
                    finalChoiceText = "\nNarrator: \"The addiction whispers sweetly, promising relief from all the pain.\"";
                    state.adjustStat(UNIQUE_STAT_KEY_S2, 2);
                    break;
                case 3:
                    finalChoiceText = "\nNarrator: \"Maybe something in your inventory can help in this desperate moment.\"";
                    // Check if player has items and potentially use them
                    if (state.getItemQuantity("XANAX") > 0) {
                        finalChoiceText += "\n" + playerName + " (reaching for the Xanax): \"Maybe... maybe this will help me think clearly.\"";
                        
                        Timer xanaxUse = new Timer(2000, e -> {
                            ui.displayText("\nNarrator: \"You swallow the Xanax. Within minutes, your anxiety melts away, but reality begins to blur...\"", Color.GRAY);
                            Timer t1 = new Timer(3000, e2 -> ui.displayText("\n" + playerName + " (vision blurring): \"What... what's happening? The room is spinning...\"", Color.BLACK));
                            t1.setRepeats(false); t1.start();
                                    Timer t2 = new Timer(5000, e2 -> ui.displayText("\nNarrator: \"The Addicted Milo in the mirror begins to transform. His face melts and reshapes into something monstrous...\"", Color.GRAY));
                            t2.setRepeats(false); t2.start();
                                    Timer t3 = new Timer(7000, e2 -> ui.displayText("\nAddicted Milo (voice distorting): \"Yesssss... let the chemicals flow. You're mine now, completely mine!\"", Color.RED.darker()));
                            t3.setRepeats(false); t3.start();
                                     Timer t10 = new Timer(9000, e2 -> ui.displayText("\n" + playerName + " (hallucinating): \"No... you're not real... this isn't real... I can't tell what's real anymore!\"", Color.BLACK));
                            t10.setRepeats(false); t10.start();
                                     Timer t5 = new Timer(11000, e2 -> ui.displayText("\nNarrator: \"The hallucinations intensify. Multiple versions of your addicted self surround you, each more terrifying than the last.\"", Color.GRAY));
                            t5.setRepeats(false); t5.start();
                                     Timer t6 = new Timer(13000, e2 -> ui.displayText("\nAddicted Milo (multiplying voices): \"You can't fight us all! We ARE you! Your weakness, your fear, your need!\"", Color.RED.darker()));
                            t6.setRepeats(false); t6.start();
                            
                            Timer t7 = new Timer(15000, e2 -> {
                                ui.displayText("\nSystem Status: \"Xanax consumed! Hallucinations active - Attack Power decreased by 2! Addiction Level +1\"", Color.RED.darker());
                                state.consumeItem("XANAX");
                                state.adjustStat(GameState.PLAYER_ATTACK, -2);
                                state.adjustStat(UNIQUE_STAT_KEY_S2, 1);
                                state.adjustStat(STAT_PLAYER_WILLPOWER, -2);
                            });
                            t7.setRepeats(false); t7.start();
                            
                            Timer t8 = new Timer(17000, e2 -> ui.displayText("\n" + playerName + " (weakened, confused): \"I... I made it worse. I can barely focus... can barely fight...\"", Color.BLACK));
                            t8.setRepeats(false); t8.start();
                        });
                        xanaxUse.setRepeats(false); xanaxUse.start();
                        
                        Timer finalProceed = new Timer(20000, e -> showDialogue(16));
                        finalProceed.setRepeats(false); finalProceed.start();
                        return; // Exit early to prevent the normal flow
                    } else {
                        finalChoiceText += "\nNarrator: \"You reach for your inventory, but find nothing that could help in this moment.\"";
                        state.adjustStat(STAT_PLAYER_WILLPOWER, -1);
                    }
                    break;
            }
            ui.displayText(finalChoiceText, Color.GRAY);
            Timer finalProceed = new Timer(3000, e -> showDialogue(16));
            finalProceed.setRepeats(false); finalProceed.start();
            break;
    }
}
                
    


     private void startChapter3Battle() {
        ui.setStageImage("/Resources/Images/Story2/.png"); // Battle background
        AudioManager.getInstance().playMusic("/Resources/Audio/Story2", true); // Battle BGM
 
        Timer startBattleActual = new Timer(1000, e2 -> {
            Enemy enemy = new DrugEnemy("Caffeine Pills", 10, 1); // Create enemy
            battleManager.startBattle(enemy, battleResult -> { // Pass enemy object
                if (battleResult == BattleManager.BattleResult.WIN) {
                    ui.setStageImage("/Resources/Images/Story3/kai_resists_cigarette.png");
                    ui.displayText("\nNarrator: \"You feel a strange sense of accomplishment having resisted.\"", Color.GRAY);
                    Timer r1 = new Timer(2500, res -> ui.displayText("\nNarrator: \"Something tells you this won't be the last time you're tested.\"", Color.GRAY));
                    r1.setRepeats(false); r1.start();
                     Timer r2 = new Timer(5000, e -> ui.displayText("\n" + playerName + " (closing laptop): \"No. I just need better time management. I can handle this.\"", Color.BLACK));
                   r2.setRepeats(false); r2.start();

                    state.adjustStat(GameState.PLAYER_HEALTH, 1);
                    state.adjustStat(GameState.PLAYER_MAX_HEALTH, 1);
                    state.adjustStat(GameState.PLAYER_ATTACK, 1);
                    state.adjustStat(STAT_PLAYER_DEFENSE, 1);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 2);
                    Timer r3 = new Timer(7500, res -> ui.displayText("\nSystem Status: \"Level Up! Health +1, Max Health +1, Attack +1, Defense +1, Willpower +2\"", Color.GREEN.darker()));
                    r3.setRepeats(false); r3.start();
                    Timer proceed = new Timer(10000, res -> showDialogue(4));
                    proceed.setRepeats(false); proceed.start();
                } else { 
                    ui.setStageImage("/Resources/Images/Story2/.png");
                    ui.displayText("\nNarrator: \"The effect of the pills is too strong. You succumb to the urge...\"", Color.GRAY);
                    Timer proceed = new Timer(3000, res -> showDialogue(4));
                    proceed.setRepeats(false); proceed.start();
                }
            });
        });
        startBattleActual.setRepeats(false);
        startBattleActual.start();
    }

    
     private void startChapter6Battle() {
        ui.setStageImage("/Resources/Images/Story2/.png"); 
        AudioManager.getInstance().playMusic("/Resources/Audio/Story2", true); 
 
        ui.displayText("\nNarrator: \"This is your first encounter with real prescription drugs. Your hands shake as you hold the bottle.\"", Color.GRAY);
        Timer fearMessage = new Timer(2000, e -> ui.displayText("\n" + playerName + " (trembling): \"This... this is different from caffeine pills. This is actual medication. What am I doing?\"", Color.BLACK));
        fearMessage.setRepeats(false); fearMessage.start();
        
        Timer startBattleActual = new Timer(4000, e2 -> {
            Enemy enemy = new DrugEnemy("Adderall", 15, 3); 
            battleManager.startBattle(enemy, battleResult -> { 
                // This battle is designed to be lost due to fear
                ui.setStageImage("/Resources/Images/Story2/.png");
                ui.displayText("\nNarrator: \"You were too afraid to fight back effectively. The fear of using real drugs paralyzed you.\"", Color.GRAY);
                Timer r1 = new Timer(2500, res -> ui.displayText("\n" + playerName + " (defeated): \"I... I couldn't resist. The pressure was too much. I took it.\"", Color.BLACK));
                r1.setRepeats(false); r1.start();
                Timer r2 = new Timer(5000, res -> ui.displayText("\nNarrator: \"The Adderall takes effect quickly. Laser focus floods your mind, but something feels fundamentally wrong.\"", Color.GRAY));
                r2.setRepeats(false); r2.start();
                Timer r3 = new Timer(7500, res -> ui.displayText("\nNew Skill Unlocked: OVERCOME - You've learned from this failure and won't be paralyzed by fear again.\"", Color.GREEN.darker()));
                r3.setRepeats(false); r3.start();
                Timer r4 = new Timer(10000, res -> ui.displayText("\nSystem Status: \"Defeat! Addiction Level +2, but you've gained experience. Health +1, Max Health +1, Attack +1, Defense +1, Willpower +1\"", Color.ORANGE.darker()));
                r4.setRepeats(false); r4.start();
                
                // Apply the consequences of losing
                state.adjustStat(UNIQUE_STAT_KEY_S2, 2); // Addiction increases
                state.adjustStat(GameState.PLAYER_HEALTH, 1);
                state.adjustStat(GameState.PLAYER_MAX_HEALTH, 1);
                state.adjustStat(GameState.PLAYER_ATTACK, 1);
                state.adjustStat(STAT_PLAYER_DEFENSE, 1);
                state.adjustStat(STAT_PLAYER_WILLPOWER, 1); // Less willpower gain due to failure
                
                Timer proceed = new Timer(12500, res -> showDialogue(7));
                proceed.setRepeats(false); proceed.start();
            });
        });
        startBattleActual.setRepeats(false);
        startBattleActual.start();
    }


     private void startChapter13Battle() {  
        ui.setStageImage("/Resources/Images/Story2/.png"); // Battle background
        AudioManager.getInstance().playMusic("/Resources/Audio/Story2", true); // Battle BGM
 
        Timer startBattleActual = new Timer(1000, e2 -> {
            Enemy enemy = new DrugEnemy("Xanax", 20, 3); // Create enemy
            battleManager.startBattle(enemy, battleResult -> { // Pass enemy object
                if (battleResult == BattleManager.BattleResult.WIN) {
                    ui.setStageImage("/Resources/Images/Story3/kai_resists_cigarette.png");
                    ui.displayText("\nNarrator: \"You feel a strange sense of accomplishment having resisted.\"", Color.GRAY);
                    Timer r1 = new Timer(2500, res -> ui.displayText("\nNarrator: \"Something tells you this won't be the last time you're tested.\"", Color.GRAY));
                    r1.setRepeats(false); r1.start();
                    Timer r2 = new Timer(5000, e -> ui.displayText("\n" + playerName + " (closing laptop): \"No. I just need better time management. I can handle this.\"", Color.BLACK));
                    r2.setRepeats(false); r2.start();

                    state.adjustStat(GameState.PLAYER_HEALTH, 1);
                    state.adjustStat(GameState.PLAYER_MAX_HEALTH, 1);
                    state.adjustStat(GameState.PLAYER_ATTACK, 1);
                    state.adjustStat(STAT_PLAYER_DEFENSE, 1);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 2);
                    Timer r3 = new Timer(7500, res -> ui.displayText("\nSystem Status: \"Level Up! Health +1, Max Health +1, Attack +1, Defense +1, Willpower +2\"", Color.GREEN.darker()));
                    r3.setRepeats(false); r3.start();
                    Timer proceed = new Timer(10000, res -> showDialogue(7));
                    proceed.setRepeats(false); proceed.start();
                } else { 
                    ui.setStageImage("/Resources/Images/Story2/.png");
                    ui.displayText("\nNarrator: \"You were too afraid to fight back effectively. Addiction Level +2\"", Color.GRAY);
                     Timer r1 = new Timer(2500, res -> ui.displayText("\nNew Skill Unlocked: OVERCOME - You've learned from this failure and won't be paralyzed by fear again.\"", Color.GREEN.darker()));
                    r1.setRepeats(false); r1.start();
                    Timer r2 = new Timer(5000, res -> ui.displayText("\nSystem Status: \"Level Up! Health +1, Max Health +1, Attack +1, Defense +1, Willpower +2\"", Color.GREEN.darker()));
                    r2.setRepeats(false); r2.start();
                    Timer proceed = new Timer(7500, res -> showDialogue(7));
                    proceed.setRepeats(false); proceed.start();
                }
            });
        });
        startBattleActual.setRepeats(false);
        startBattleActual.start();
    }

private void startFinalBossBattle() {
    ui.setStageImage("/Resources/Images/Story2/final_boss_battle.png");
    AudioManager.getInstance().playMusic("/Resources/Audio/Story2/final_boss_battle_bgm.wav", true);
    
    Timer startBattleActual = new Timer(1000, e2 -> {
        Enemy enemy = new DrugEnemy("Addicted Self", 35, 5);
        battleManager.startBattle(enemy, battleResult -> {
            if (battleResult == BattleManager.BattleResult.WIN) {
                ui.setStageImage("/Resources/Images/Story2/victory_moment.png");
                ui.displayText("\nNarrator: \"You've won this battle, but the war continues. You used your OVERCOME skill and newfound strength.\"", Color.GRAY);
                Timer r1 = new Timer(3000, res -> ui.displayText("\n" + playerName + ": \"I... I did it. I can fight this.\"", Color.BLACK));
                r1.setRepeats(false); r1.start();
                
                state.adjustStat(GameState.PLAYER_HEALTH, 2);
                state.adjustStat(GameState.PLAYER_MAX_HEALTH, 2);
                state.adjustStat(GameState.PLAYER_ATTACK, 2);
                state.adjustStat(STAT_PLAYER_DEFENSE, 2);
                state.adjustStat(STAT_PLAYER_WILLPOWER, 3);
                
                Timer r2 = new Timer(5000, res -> ui.displayText("\nSystem Status: \"Final Level Up! Health +2, Max Health +2, Attack +2, Defense +2, Willpower +3\"", Color.GREEN.darker()));
                r2.setRepeats(false); r2.start();
                Timer proceed = new Timer(8000, res -> showBadEnding());
                proceed.setRepeats(false); proceed.start();
            } else {
                ui.setStageImage("/Resources/Images/Story2/defeat_moment.png");
                ui.displayText("\nNarrator: \"The addiction was too strong. Even with your OVERCOME skill, you couldn't break free completely.\"", Color.GRAY);
                Timer r1 = new Timer(3000, res -> ui.displayText("\nAddicted Milo: \"You see? You'll never be free from me. Never.\"", Color.RED.darker()));
                r1.setRepeats(false); r1.start();
                
                state.adjustStat(UNIQUE_STAT_KEY_S2, 3); 
                Timer r2 = new Timer(5000, res -> ui.displayText("\nSystem Status: \"Final Level Up! But Addiction Level +3\"", Color.RED.darker()));
                r2.setRepeats(false); r2.start();
                Timer proceed = new Timer(7000, res -> showBadEnding());
                proceed.setRepeats(false); proceed.start();
            }
        });
    });

    
    startBattleActual.setRepeats(false);
    startBattleActual.start();
}
   private void showStage0() {
        ui.setStageImage("/Resources/Images/Story2/highschool.png"); 
        AudioManager.getInstance().playMusic("/Resources/Audio/Story2/stage0_bgm.wav", true);
        
        ui.displayText("\n[High School - 3rd year] ", Color.DARK_GRAY);
        Timer t0 = new Timer(1000, e -> ui.displayText("\nMilo arrives at a prestigious high school after his family moves to another city. He walks through hallways lined with trophy cases showcasing the extraordinary students and their achievements.", Color.BLACK));
        t0.setRepeats(false); t0.start(); 
        Timer t1 = new Timer(4000, e -> ui.displayText("\n" + playerName + " (internal thought): \"Dad's new job means a 'better opportunity' for me. Elite school, competitive students, and expectations through the roof.\"", Color.BLACK));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(7000, e -> ui.displayText("\n(He stops in front of a door, preparing himself for the welcome)", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer showchoices = new Timer(14000, e -> ui.showChoicesDialog(new String[]{"Take a deep breath and enter", "Check your schedule one more time", "Look at your reflection in the trophy case"}));
        showchoices.setRepeats(false); showchoices.start();
    }


    private void showStage1() {
        ui.setStageImage("/Resources/Images/Story2/milo_bedroom.png"); 
        AudioManager.getInstance().playMusic("/Resources/Audio/Story2/stage1_night_bgm.wav", true); 
        
        ui.displayText("\n\n[Milo's Bedroom - Later that Night, 7:00 PM]", Color.DARK_GRAY);
        Timer t1 = new Timer(1500, e -> ui.displayText("\n" + playerName + " (looking at a color-coded study schedule): \"OOP, WebProg, Database, Calculus, UKM, SU... I can do this. I have to do this.\"", Color.BLACK));
        t1.setRepeats(false); t1.start();
        
        Timer t2 = new Timer(5000, e -> ui.displayText("\n" + playerName + ": \"This is nothing compared to my previous school.\"", Color.BLACK));
        t2.setRepeats(false); t2.start();
        
        Timer proceedTimer = new Timer(8000, e -> showDialogue(2));
        proceedTimer.setRepeats(false); proceedTimer.start();
    }
    private void showStage2() {
        ui.setStageImage("/Resources/Images/Story2/late_night_study.png"); // Added image path
        AudioManager.getInstance().playMusic("/Resources/Audio/Story2/stage2_night_study_bgm.wav", true); // Added audio path
        
        ui.displayText("\n\n[Same Bedroom - Two weeks later, 2:00 AM] ", Color.DARK_GRAY);
        Timer t1 = new Timer(1000, e -> ui.displayText("\n" + playerName + " (rubbing bloodshot eyes, surrounded by energy drink cans): \"Third test this week. Can't focus anymore... I need something even more refreshing.\"", Color.BLACK));
        t1.setRepeats(false); t1.start();
        
        Timer t2 = new Timer(4000, e -> ui.displayText("\nNarrator: \"You open your laptop, searching for study tips. An anonymous forum thread catches your eye: 'How I Aced My Exams While Getting 4 Hours of Sleep.'\"", Color.GRAY));
        t2.setRepeats(false); t2.start();
        
        Timer t3 = new Timer(8000, e -> ui.displayText("\nAnonymous Post: \"The secret isn't working harder, it's working smarter. Adderall. Not from some dealer, just ask your doctor about ADHD symptoms. Changed my academic life.\"", Color.BLUE.darker()));
        t3.setRepeats(false); t3.start();
        
        Timer t4 = new Timer(12000, e -> ui.displayText("\nNarrator: \"You've never considered this before. But the pressure is crushing you already, and it's only September.\"", Color.GRAY));
        t4.setRepeats(false); t4.start();
        
        Timer proceedTimer = new Timer(26000, e -> showDialogue(3));
        proceedTimer.setRepeats(false); proceedTimer.start();
    }

    private void showStage3() {
        ui.setStageImage("/Resources/Images/Story2/caffeine_pills.png"); // Added image path
        AudioManager.getInstance().playMusic("/Resources/Audio/Story2/stage3_temptation_bgm.wav", true); // Added audio path
        
        Timer t1 = new Timer(1000, e -> ui.displayText("\nNarrator: You try to look for anything to help you stay awake, and found the caffeine pills that your dad owned.", Color.GRAY));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(6000, e -> startChapter3Battle());
        t2.setRepeats(false); t2.start();
    }

    private void showStage4() {
        ui.setStageImage("/Resources/Images/Story2/family_dinner.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story2/stage4_family_bgm.wav", true);
        
        ui.displayText("\n\n2nd Month\n[Milo sitting with his parents at dinner]", Color.DARK_GRAY);
        Timer t1 = new Timer(1000, e -> ui.displayText("\nDad: \"So I spoke with Mr. Peterson today about college recommendations. He says you need more extracurriculars if you want Stanford to even look at your application.\"", Color.BLUE.darker()));
        t1.setRepeats(false); t1.start();
        
        Timer t2 = new Timer(4000, e -> ui.displayText("\nMom: \"And your math grade slipped to an A-. Is everything okay?\"", Color.MAGENTA.darker()));
        t2.setRepeats(false); t2.start();
        
        Timer t3 = new Timer(6000, e -> ui.displayText("\n" + playerName + ": (frustrated) \"I'm already doing six activities and staying up until 2 AM studying. What more do you want?\"", Color.BLACK));
        t3.setRepeats(false); t3.start();
        
        Timer showChoices = new Timer(14000, e -> ui.showChoicesDialog(new String[]{"Focus on eating quietly", "Defend your current performance", "Ask for their advice"}));
        showChoices.setRepeats(false); showChoices.start();
    }

    private void showStage5() {
         Timer t1 = new Timer(1000, e2 -> ui.displayText("\n\n[School hallway, between classes]\nJake (Classmate): \"Hey Milo, you look exhausted. You know what helps me during exam week?\"", Color.GREEN.darker()));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(6000, e2 -> ui.showChoicesDialog(new String[]{"Ask what he means", "Say you're fine and walk away", "Show interest but stay cautious"}));
        t2.setRepeats(false); t2.start();
    }

    private void showStage6() {
        ui.setStageImage("/Resources/Images/Story2/counselor_office.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story2/stage5_counselor_bgm.wav", true);

        ui.displayText("\n\n[The Next Week - School counselor's office]", Color.DARK_GRAY);
        Timer t1 = new Timer(1500, e -> ui.displayText("\nCounselor: \"How can I help you today, Milo?\"", Color.CYAN.darker()));
        t1.setRepeats(false); t1.start();
        
        Timer showChoices = new Timer(4000, e -> ui.showChoicesDialog(new String[]{"Be completely honest about your struggles", "Stick to a rehearsed story about ADHD", "Try to get help without mentioning medication"}));
        showChoices.setRepeats(false); showChoices.start();
    }

    private void showStage7() {
        ui.setStageImage("/Resources/Images/Story2/counting_pills.png"); // Added image path
        AudioManager.getInstance().playMusic("/Resources/Audio/Story2/stage7_stress_bgm.wav", true); // Added audio path
        
        ui.displayText("\n\n [Milo's Bedroom - Month 4, Late Night] ", Color.DARK_GRAY);
        Timer t1 = new Timer(1500, e -> ui.displayText("\n" + playerName + " (counting pills): \"Only three left and the refill isn't for another two weeks. That's not enough for midterms. How do I get more, I need more.\"", Color.BLACK));
        t1.setRepeats(false); t1.start();
        
        Timer t2 = new Timer(4000, e -> ui.displayText("\nNarrator: \"You find yourself taking two pills sometimes when the workload is especially heavy. The prescribed dose no longer gives you that sharp focus you crave.\"", Color.GRAY));
        t2.setRepeats(false); t2.start();
        
        Timer t3 = new Timer(7000, e -> ui.displayText("\n(You resort to researching alternatives online) ", Color.DARK_GRAY));
        t3.setRepeats(false); t3.start();
        
        Timer t4 = new Timer(8000, e -> ui.displayText("\n" + playerName + ": \"Vyvanse lasts longer... or maybe something to help me sleep after the stimulants. Xanax could work.\"", Color.BLACK));
        t4.setRepeats(false); t4.start();
        
        Timer t5 = new Timer(11000, e -> ui.displayText("\n\n[The Next Day] (School bathroom, Jake approaches you.)", Color.DARK_GRAY));
        t5.setRepeats(false); t5.start();
        
        Timer t6 = new Timer(14000, e -> ui.displayText("\nJake: \"Hey, you look rough, Did those pills help?\"", Color.GREEN.darker()));
        t6.setRepeats(false); t6.start();
        
        Timer t7 = new Timer(28000, e -> ui.showChoicesDialog(new String[]{"Admit they helped but you need more", "Lie and say you didn't use them", "Ask if he has anything stronger"}));
        t7.setRepeats(false); t7.start();
    }

private void showStage8(){
    Timer t3 = new Timer(1000, e -> ui.displayText("\n" + "Jake: \"I might know someone who can help. My cousin deals with anxiety too - has some Xanax she could spare.\" (slips you a small baggie) \"For when you need to come down from the study drugs.\"", Color.BLACK));
    t3.setRepeats(false); t3.start();
    Timer t4 = new Timer(5000, e ->  ui.displayText("\nSystem: Added 1 " + XANAX_ITEM.getItemName() + " to inventory.", Color.RED.darker()));                  
    state.addItem(XANAX_ITEM, 1);
    t4.setRepeats(false); t4.start();       
    Timer t5 = new Timer(6000, e -> ui.displayText("\n" + playerName + " (taking the baggie): \"Just... just in case, right?\"", Color.BLACK));          
    t5.setRepeats(false); t5.start();
    Timer t6 = new Timer(7000, e -> ui.displayText("\n Jake: \"Right. Just in case.\"", Color.BLACK));          
    t6.setRepeats(false); t6.start();
    Timer proceedTimer = new Timer(9000, e -> showDialogue(9));
    proceedTimer.setRepeats(false); proceedTimer.start();
}

private void showStage9() {
    ui.displayText("\n\n[Milo's Room - Late Night, Month 6] ", Color.DARK_GRAY);
    Timer t1 = new Timer(1000, e -> ui.displayText("\n" + playerName + " (jittery, unable to sleep): \"I need the Adderall to focus, and the Xanax to sleep. It's just medicine.\"", Color.BLACK));
    t1.setRepeats(false); t1.start();
    Timer t2 = new Timer(3500, e -> ui.displayText("\n" + "Narrator: \"Your tolerance is building. Two Adderall, then three. Half a Xanax becomes a whole one. Your mood swings wildly between artificial confidence and crushing depression.\"", Color.BLACK));
    t2.setRepeats(false); t2.start();
    Timer proceedTimer = new Timer(7000, e -> showDialogue(10));
    proceedTimer.setRepeats(false); proceedTimer.start();
}

private void showStage10() {
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
    Timer proceedTimer = new Timer(23000, e -> showDialogue(11));
    proceedTimer.setRepeats(false); proceedTimer.start();
}

private void showStage11() {
    ui.displayText("\n\n [Milo's Room - Researching Online] ", Color.DARK_GRAY);
    Timer t1 = new Timer(1000, e -> ui.displayText("\n" + "Narrator: \"The darknet is a hot spot for drugs. A certain advertisement catches your attention.  \"DONT WANT TO DISAPPOINT YOUR FAMILY? CONTACT HERE NOW!\" A telegram account...", Color.BLACK));
    t1.setRepeats(false); t1.start();
    Timer t2 = new Timer(5000, e -> ui.displayText("\n" + "Narrator: \"You decide to ask the person behind the advertisement.", Color.BLACK));
    t2.setRepeats(false); t2.start();
    Timer proceedTimer = new Timer(8500, e -> showDialogue(12));
    proceedTimer.setRepeats(false); proceedTimer.start();
}

private void showStage12() {
    ui.setStageImage("/Resources/Images/Story2/online_dealer.png");
    AudioManager.getInstance().playMusic("/Resources/Audio/Story2/stage12_temptation_bgm.wav", true);
    
    ui.displayText("\n\n[Online Chat - Semester break - Month 7]", Color.DARK_GRAY);
    Timer t1 = new Timer(1000, e -> ui.displayText("\nDealer_X: \"You seem like a good student. I have something that will make everything easier. Fentanyl patches - medical grade, very clean. Perfect for when life gets overwhelming.\"", Color.RED.darker()));
    t1.setRepeats(false); t1.start();
    
    Timer t2 = new Timer(4000, e -> ui.displayText("\n" + playerName + " (hesitant): \"I... I don't know. That sounds really dangerous.\"", Color.BLACK));
    t2.setRepeats(false); t2.start();
    
    Timer t3 = new Timer(6000, e -> ui.displayText("\nDealer_X: \"Everything's dangerous if you're not smart about it. You're smart, right? Top of your class?\"", Color.RED.darker()));
    t3.setRepeats(false); t3.start();
    
    Timer showChoices = new Timer(10000, e -> ui.showChoicesDialog(new String[]{"Close the laptop and try to resist", "Ask more questions about the drug", "Agree to meet in person"}));
    showChoices.setRepeats(false); showChoices.start();
}

private void showStage13() {
    ui.setStageImage("/Resources/Images/Story2/xanax_temptation.png");
    AudioManager.getInstance().playMusic("/Resources/Audio/Story2/stage13_spiral_bgm.wav", true);
    
    ui.displayText("\n\n[Milo's Room - Same Night]", Color.DARK_GRAY);
    Timer t1 = new Timer(1000, e -> ui.displayText("\nNarrator: \"You stare at the Xanax Jake gave you weeks ago. You've been saving it, but tonight feels different. The pressure is crushing.\"", Color.GRAY));
    t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(4000, e -> ui.displayText("\n" + playerName + " (shaking): \"Just one. Just to calm down. I can't think straight anymore.\"", Color.BLACK));
    t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(6000, e -> startChapter13Battle());
    t3.setRepeats(false); t3.start();
}
private void showStage14() {
    ui.setStageImage("/Resources/Images/Story2/empty_prescription.png");
    AudioManager.getInstance().playMusic("/Resources/Audio/Story2/stage14_withdrawal_bgm.wav", true);
    
    ui.displayText("\n\n[Milo's Room - Month 7, Week 3]", Color.DARK_GRAY);
    Timer t1 = new Timer(1000, e -> ui.displayText("\nNarrator: \"Your prescription ran out. The doctor refused to refill it, citing 'signs of dependency.'\"", Color.GRAY));
    t1.setRepeats(false); t1.start();
    Timer t2 = new Timer(3500, e -> ui.displayText("\n" + playerName + " (sweating, shaking): \"I can't... think straight. My head... everything hurts.\"", Color.BLACK));
    t2.setRepeats(false); t2.start();
    Timer t3 = new Timer(6000, e -> ui.displayText("\nNarrator: \"Withdrawal hits hard. The crushing fatigue. The inability to focus. The overwhelming depression.\"", Color.GRAY));
    t3.setRepeats(false); t3.start();
    Timer t4 = new Timer(9000, e -> ui.displayText("\n" + playerName + " (looking at phone): \"Jake isn't answering. The dealer wants too much money...\"", Color.BLACK));
    t4.setRepeats(false); t4.start();
    Timer t5 = new Timer(12000, e -> ui.displayText("\nNarrator: \"Your grades begin to slip. Teachers notice. Your parents are called for another meeting. You prepare for the worst.\"", Color.GRAY));
    t5.setRepeats(false); t5.start();
    Timer proceedTimer = new Timer(15000, e -> showDialogue(15));
    proceedTimer.setRepeats(false); proceedTimer.start();
}
private void showStage15() {
    ui.setStageImage("/Resources/Images/Story2/mirror_confrontation.png");
    AudioManager.getInstance().playMusic("/Resources/Audio/Story2/stage14_final_bgm.wav", true);
    
    ui.displayText("\n\n[Milo's Room - 3 AM, Month 8]", Color.DARK_GRAY);
    Timer t1 = new Timer(1000, e -> ui.displayText("\nNarrator: \"You stand in front of your mirror, barely recognizing the person staring back. Hollow eyes, gaunt cheeks, trembling hands.\"", Color.GRAY));
    t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(4000, e -> ui.displayText("\n" + playerName + " (whispering): \"Who... who am I anymore?\"", Color.BLACK));
    t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(6000, e -> ui.displayText("\nNarrator: \"The reflection begins to speak...\"", Color.GRAY));
    t3.setRepeats(false); t3.start();
        Timer t4 = new Timer(8000, e -> ui.displayText("\nAddicted Milo: \"You're nothing without me. You think you can just stop? You NEED me to function, to succeed, to be worthy of anything.\"", Color.RED.darker()));
    t4.setRepeats(false); t4.start();
        Timer t5 = new Timer(11000, e -> ui.displayText("\n" + playerName + ": \"No... this isn't who I wanted to become.\"", Color.BLACK));
    t5.setRepeats(false); t5.start();
        Timer t6 = new Timer(13000, e -> ui.displayText("\nAddicted Milo: \"But it's who you ARE now. Without the pills, you're just ordinary. Mediocre. A disappointment.\"", Color.RED.darker()));
    t6.setRepeats(false); t6.start();
    
    Timer showChoices = new Timer(16000, e -> ui.showChoicesDialog(new String[]{"Fight against your addiction", "Give in to the voices", "Try to use an item from inventory"}));
    showChoices.setRepeats(false); showChoices.start();
}

private void showStage16() {
    ui.setStageImage("/Resources/Images/Story2/final_battle_prep.png");
    AudioManager.getInstance().playMusic("/Resources/Audio/Story2/final_battle_bgm.wav", true);
    
    ui.displayText("\n\nNarrator: \"This is it. The final confrontation with the monster you've become.\"", Color.GRAY);
    Timer t1 = new Timer(2000, e -> ui.displayText("\n" + playerName + ": \"I won't let you control me anymore!\"", Color.BLACK));
    t1.setRepeats(false); t1.start();
    Timer t2 = new Timer(4000, e -> ui.displayText("\nAddicted Milo: \"You're weak without me. You've always been weak!\"", Color.RED.darker()));
    t2.setRepeats(false); t2.start();
    Timer t3 = new Timer(6000, e -> startFinalBossBattle());
    t3.setRepeats(false); t3.start();
}




private void showBadEnding() {
    int addictionLevel = state.getStat(UNIQUE_STAT_KEY_S2);
    
    if (addictionLevel >= 7) {
        // worse ending
        ui.setStageImage("/Resources/Images/Story2/death_ending.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story2/tragic_ending_bgm.wav", true);
        
        ui.displayText("\n\n[Six Months Later - Hospital Room]", Color.DARK_GRAY);
        Timer t1 = new Timer(2000, e -> ui.displayText("\nNarrator: \"The overdose was inevitable. Your body couldn't handle all the substances anymore.\"", Color.GRAY));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(5000, e -> ui.displayText("\nDoctor: \"We did everything we could. I'm sorry.\"", Color.BLUE.darker()));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(7000, e -> ui.displayText("\nMom (sobbing): \"He was just trying to be perfect... we just wanted him to succeed...\"", Color.MAGENTA.darker()));
        t3.setRepeats(false); t3.start();
        Timer t5 = new Timer(15000, e -> ui.displayText("\n\n=== THE END ===\n\"Sometimes the cost of 'success' is everything we truly value.\"\n\nAddiction Level: " + addictionLevel + "/10", Color.RED.darker()));
        t5.setRepeats(false); t5.start();
    } else {
        // bad ending
        ui.setStageImage("/Resources/Images/Story2/family_tragedy.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story2/sad_ending_bgm", true);
        
        ui.displayText("\n\n[Living Room - One Year Later]", Color.DARK_GRAY);
        Timer t1 = new Timer(2000, e -> ui.displayText("\nNarrator: \"You survived, but the stress you caused your family had consequences you never imagined.\"", Color.GRAY));
        t1.setRepeats(false); t1.start();       
        Timer t2 = new Timer(5000, e -> ui.displayText("\n" + playerName + " (at father's funeral): \"The doctor said it was a heart attack. But I know... the stress of watching me destroy myself...\"", Color.BLACK));
        t2.setRepeats(false); t2.start();
          Timer t3 = new Timer(8000, e -> ui.displayText("\nMom (barely audible): \"He loved you so much. He just wanted you to be happy and successful.\"", Color.MAGENTA.darker()));
        t3.setRepeats(false); t3.start();
        Timer t4 = new Timer(11000, e -> ui.displayText("\n" + playerName + ": \"I'm clean now, but it's too late. The damage is done.\"", Color.BLACK));
        t4.setRepeats(false); t4.start();
         Timer t6 = new Timer(18000, e -> ui.displayText("\n\n=== THE END ===\n\"Addiction doesn't just destroy the Milo, it destroys everyone who loves them.\"\n\nAddiction Level: " + addictionLevel + "/10", Color.RED.darker()));
        t6.setRepeats(false); t6.start();
    }
}

   @Override
    public String[] getCurrentChoices() {
    if (battleManager.isBattleActive()) {
        int baseAttack = state.getStat(GameState.PLAYER_ATTACK);
        
        // Check if this is the Adderall battle (first real drug encounter)
        if (dialogueState == 6) { // During the counselor/Adderall encounter
            return new String[]{
                "1. Try to resist (Fear prevents effective action)",
                "2. Use Item (Too scared to think clearly)"
            };
        }
        
        return new String[]{
            "1. Attack (" + (baseAttack-1) + "-" + (baseAttack+2) + " dmg)",
            "2. Use Item (Not Implemented)"};
    }
    return new String[0]; 
}
}