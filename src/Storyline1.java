import java.awt.Color;
import java.util.Map;
import javax.swing.Timer;

public class Storyline1 extends Storyline {
    private int dialogueState = 0;
    private BattleManager battleManager;
    private String playerName = "Alex";
    private boolean isFirstAlcoholBattle = false;
    private boolean isCigaretteBattle = false;
    private boolean isHardDrugsBattle = false;
    private boolean isFinalBattle = false;

    public static final String UNIQUE_STAT_KEY_S1 = "depressionLevel";
    public static final String STAT_SOCIAL_CONNECTION = "socialConnection";
    public static final String STAT_PLAYER_WILLPOWER = "playerWillpower";

    public static final String FLAG_SKILL_RESILIENCE_ACQUIRED = "skillResilienceAcquired";

    private static final Item ALCOHOL_ITEM = new SmokingItem(
        "Bottle of Beer", 
        "A cold bottle of beer. Promises to wash away the pain.",
        "drinks the beer. The numbness feels temporary but welcome...",
        Map.of(UNIQUE_STAT_KEY_S1, 3, STAT_PLAYER_WILLPOWER, -1, STAT_SOCIAL_CONNECTION, 1)
    );
    
    private static final Item CIGARETTE_ITEM = new SmokingItem(
        "Cigarette", 
        "A standard cigarette. Something to calm the nerves.",
        "lights up the cigarette. The smoke burns, but the ritual is comforting...",
        Map.of(UNIQUE_STAT_KEY_S1, 2, STAT_PLAYER_WILLPOWER, -1)
    );
    
    private static final Item PILLS_ITEM = new SmokingItem(
        "Pain Pills", 
        "Strong prescription painkillers. A dangerous escape from reality.",
        "takes the pills. Everything becomes distant and hazy...",
        Map.of(UNIQUE_STAT_KEY_S1, 5, STAT_PLAYER_WILLPOWER, -2, GameState.PLAYER_HEALTH, -2, STAT_SOCIAL_CONNECTION, -2)
    );

    public Storyline1(GameUI ui, GameState state) {
        super(ui, state);
        this.battleManager = new BattleManager(ui, state);
        ui.setTitle("Lullaby of Empty Bottles");
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
            default -> showStage0();
        }
    }

    @Override
    public void startStory() {
        startStory(false);
    }

    public void startStory(boolean fromSave) {
        if (!fromSave) {
            state.setStat(GameState.PLAYER_HEALTH, 12);
            state.setStat(GameState.PLAYER_MAX_HEALTH, 12);
            state.setStat(GameState.PLAYER_ATTACK, 1);
            state.setStat(STAT_PLAYER_WILLPOWER, 6);
            
            state.setStat(UNIQUE_STAT_KEY_S1, 0); 
            state.setStat(STAT_SOCIAL_CONNECTION, 3);
            state.setFlag(FLAG_SKILL_RESILIENCE_ACQUIRED, false);
            
            ui.setStageImage("/Resources/Images/Story1/stage0.png");
            AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage0.wav", true);

            Timer timer = new Timer(500, e -> {
                ui.displayText("Chapter 1: When the Silence Speaks", Color.BLACK);
            });
            timer.setRepeats(false);
            timer.start();

            showDialogue(0);
        } else {
            showDialogue(dialogueState);
        }
    }

    public int realPlayerDamage() {
        int baseAttack = state.getStat(GameState.PLAYER_ATTACK);
        int willpower = state.getStat(STAT_PLAYER_WILLPOWER);
        int socialConnection = state.getStat(STAT_SOCIAL_CONNECTION);
        
        if (state.getFlag(FLAG_SKILL_RESILIENCE_ACQUIRED)) {
            if (isFinalBattle) {
                return baseAttack + willpower + socialConnection + 3 + new java.util.Random().nextInt(4);
            }
            return baseAttack + willpower + socialConnection + 1 + new java.util.Random().nextInt(2);
        }
        
        return baseAttack + Math.max(0, willpower - 1) + Math.max(0, socialConnection - 1);
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
                    state.adjustStat(STAT_SOCIAL_CONNECTION, 2);
                    ui.displayText("\nYou chose to meet. Maybe human connection is what you need.", Color.BLACK);
                    showDialogue(1);
                } else {
                    ui.displayText("\nYou decided to stay in. The silence feels heavier now.", Color.BLACK);
                    state.setFlag("choseToIsolate_d0", true);
                    state.adjustStat(UNIQUE_STAT_KEY_S1, 1);
                    showDialogue(1); 
                }
                break;
                
            case 1:
                showDialogue(2);
                break;
                
            case 2:
                if (choice == 1) {
                    ui.displayText("\nYour choice leads to a confrontation with your inner demons!", Color.BLACK);
                    Timer battleStartTimer = new Timer(1500, e -> startChapter1Battle());
                    battleStartTimer.setRepeats(false);
                    battleStartTimer.start();
                } else {
                    ui.displayText("\nYou avoided the confrontation, but the weight follows you.", Color.BLACK);
                    state.adjustStat(UNIQUE_STAT_KEY_S1, 1);
                    showDialogue(3); 
                }
                break;
                
            case 3:
                showDialogue(4);
                break;
                
            case 4:
                if (choice == 1) {
                    state.adjustStat(STAT_SOCIAL_CONNECTION, 2);
                    ui.displayText("\n" + playerName + ": \"Honestly? I've been struggling. Everything feels... empty.\"", Color.BLACK);
                } else {
                    state.adjustStat(UNIQUE_STAT_KEY_S1, 1);
                    ui.displayText("\n" + playerName + ": \"Just the usual routine. Nothing exciting.\"", Color.BLACK);
                }
                Timer proceedTimer = new Timer(3000, e -> showDialogue(5));
                proceedTimer.setRepeats(false);
                proceedTimer.start();
                break;
                
            case 5:
                if (choice == 1) {
                    ui.displayText("\n" + playerName + ": \"You know what? Yeah. Let's do it.\"", Color.BLACK);
                    Timer barTimer = new Timer(2000, e -> startChapter2Battle());
                    barTimer.setRepeats(false);
                    barTimer.start();
                } else {
                    ui.displayText("\n" + playerName + ": \"Actually, I should head home. Early day tomorrow.\"", Color.BLACK);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 1);
                    Timer homeTimer = new Timer(3000, e -> showDialogue(6));
                    homeTimer.setRepeats(false);
                    homeTimer.start();
                }
                break;
                
            case 6:
                String reflectionText = "";
                int depressionLevel = state.getStat(UNIQUE_STAT_KEY_S1);
                if (depressionLevel >= 4) {
                    reflectionText = "\n" + playerName + ": (That dinner... I couldn't even pretend to be okay. The emptiness is consuming me.)";
                } else if (depressionLevel >= 2) {
                    reflectionText = "\n" + playerName + ": (Seeing them reminded me how isolated I've become. Maybe I need help.)";
                } else {
                    reflectionText = "\n" + playerName + ": (That was actually nice. Maybe I should reach out more often.)";
                }
                ui.displayText(reflectionText, Color.GRAY.darker());
                Timer choiceTimer = new Timer(3000, e -> ui.showChoicesDialog(new String[]{
                    "\"I need to make changes.\"",
                    "\"Maybe tomorrow will be better.\"",
                    "\"I should call someone for help.\""
                }));
                choiceTimer.setRepeats(false);
                choiceTimer.start();
                break;
                
            case 7:
                switch (choice) {
                    case 1:
                        ui.displayText("\n" + playerName + ": \"I can't keep living like this. I need to do something.\"", Color.BLACK);
                        state.adjustStat(STAT_PLAYER_WILLPOWER, 2);
                        break;
                    case 2:
                        ui.displayText("\n" + playerName + ": \"Maybe if I just sleep it off...\"", Color.BLACK);
                        state.adjustStat(UNIQUE_STAT_KEY_S1, 1);
                        break;
                    case 3:
                        ui.displayText("\n" + playerName + ": \"I think... I think I need professional help.\"", Color.BLACK);
                        state.adjustStat(STAT_SOCIAL_CONNECTION, 3);
                        state.setFlag("seekingHelp", true);
                        break;
                }
                Timer stressTimer = new Timer(3000, e -> showDialogue(8));
                stressTimer.setRepeats(false);
                stressTimer.start();
                break;
                
            case 8:
                showDialogue(9);
                break;
                
            case 9:
                if (choice == 1) {
                    ui.displayText("\n" + playerName + ": \"I can handle this. I just need to work harder.\"", Color.BLACK);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 1);
                } else {
                    ui.displayText("\n" + playerName + ": \"Maybe I should take some time off...\"", Color.BLACK);
                    state.adjustStat(UNIQUE_STAT_KEY_S1, 1);
                }
                Timer workTimer = new Timer(3000, e -> showDialogue(10));
                workTimer.setRepeats(false);
                workTimer.start();
                break;
                
            case 10:
                if (choice == 1) {
                    startChapter3Battle();
                } else {
                    ui.displayText("\n" + playerName + ": \"No... I can't go down that path.\"", Color.BLACK);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 2);
                    Timer walkAwayTimer = new Timer(3000, e -> showDialogue(11));
                    walkAwayTimer.setRepeats(false);
                    walkAwayTimer.start();
                }
                break;
                
            case 11:
                if (choice == 1) {
                    state.adjustStat(STAT_SOCIAL_CONNECTION, 3);
                    state.setFlag(FLAG_SKILL_RESILIENCE_ACQUIRED, true);
                    ui.displayText("\n" + playerName + ": \"I'm calling the suicide hotline. I need help.\"", Color.BLACK);
                    Timer helpTimer = new Timer(3000, e -> showDialogue(12));
                    helpTimer.setRepeats(false);
                    helpTimer.start();
                } else {
                    startFinalBattle();
                }
                break;
                
            case 12:
                showDialogue(13);
                break;
                
            case 13:
                if (choice == 1) {
                    showGoodEnding();
                } else {
                    showBitterSweetEnding();
                }
                break;
                
            case 14:
                if (choice == 1) {
                    ui.displayText("\nReturning to main menu...", Color.BLACK);
                } else if (choice == 2) {
                    ui.displayText("\nRestarting story...", Color.BLACK);
                    Timer restartTimer = new Timer(1000, e -> startStory(false));
                    restartTimer.setRepeats(false);
                    restartTimer.start();
                }
                break;
        }
    }

    public void useInventoryItem(String itemName) {
        Item itemToUse = state.getItemPrototype(itemName);
        if (itemToUse != null && state.getItemQuantity(itemName) > 0) {
            if (isFirstAlcoholBattle || isCigaretteBattle || isHardDrugsBattle || isFinalBattle) {
                if (itemName.equals("Bottle of Beer")) {
                    useBeerDuringBattle();
                } else if (itemName.equals("Cigarette")) {
                    useCigaretteDuringBattle();
                } else if (itemName.equals("Pain Pills")) {
                    usePillsDuringBattle();
                } else {
                    itemToUse.applyEffect(state, ui, playerName);
                    state.consumeItem(itemName);
                }
            } else {
                itemToUse.applyEffect(state, ui, playerName);
                state.consumeItem(itemName);
                ui.displayText("\nUsed " + itemName + ".", Color.BLACK);
            }
        } else {
            ui.displayText("\nCannot use " + itemName + " - not available.", Color.BLACK);
        }
    }

    private void useBeerDuringBattle() {
        ui.displayText("\n" + playerName + " drinks the beer. The alcohol dulls the pain temporarily, but the emptiness remains...", Color.ORANGE);
        state.adjustStat(GameState.PLAYER_HEALTH, 3);
        state.adjustStat(UNIQUE_STAT_KEY_S1, 2);
        state.adjustStat(STAT_PLAYER_WILLPOWER, -1);
        state.consumeItem("Bottle of Beer");
    }

    private void useCigaretteDuringBattle() {
        ui.displayText("\n" + playerName + " lights up a cigarette. The ritual is comforting, but it's just another escape...", Color.ORANGE);
        state.adjustStat(GameState.PLAYER_HEALTH, 1);
        state.adjustStat(UNIQUE_STAT_KEY_S1, 1);
        state.consumeItem("Cigarette");
    }

    private void usePillsDuringBattle() {
        ui.displayText("\n" + playerName + " takes the pills. Everything becomes hazy. This is dangerous territory...", Color.RED);
        state.adjustStat(GameState.PLAYER_HEALTH, 5);
        state.adjustStat(UNIQUE_STAT_KEY_S1, 4);
        state.adjustStat(STAT_PLAYER_WILLPOWER, -2);
        state.adjustStat(STAT_SOCIAL_CONNECTION, -2);
        state.consumeItem("Pain Pills");
    }

    private void startChapter1Battle() {
        ui.setStageImage("/Resources/Images/Story1/battle1.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/battle1.wav", true);
        isFirstAlcoholBattle = true;
        
        ui.displayText("\nNarrator: The figure turns around. It's you - but distorted, angry, broken.", Color.GRAY);
        Timer startBattleActual = new Timer(2000, e2 -> {
            Enemy enemy = new DrugEnemy("Inner Despair", 18, 2);
            battleManager.startBattle(enemy, battleResult -> {
                isFirstAlcoholBattle = false;
                if (battleResult == BattleManager.BattleResult.WIN) {
                    ui.displayText("\nNarrator: You've faced your inner demons and won this round.", Color.GRAY);
                    state.adjustStat(GameState.PLAYER_MAX_HEALTH, 2);
                    state.adjustStat(GameState.PLAYER_ATTACK, 1);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 2);
                    ui.displayText("\nSystem Status: \"Victory! Health +2, Attack +1, Willpower +2\"", Color.GREEN);
                    Timer proceedTimer = new Timer(3000, e -> showDialogue(3));
                    proceedTimer.setRepeats(false);
                    proceedTimer.start();
                } else {
                    ui.displayText("\nNarrator: The despair overwhelms you. You give in to the darkness.", Color.GRAY);
                    state.addItem(ALCOHOL_ITEM, 1);
                    state.adjustStat(UNIQUE_STAT_KEY_S1, 3);
                    ui.displayText("\nSystem: Added 1 Bottle of Beer to inventory. Depression Level +3", Color.ORANGE);
                    Timer proceedTimer = new Timer(3000, e -> showDialogue(3));
                    proceedTimer.setRepeats(false);
                    proceedTimer.start();
                }
            });
        });
        startBattleActual.setRepeats(false);
        startBattleActual.start();
    }
    
    private void startChapter2Battle() { 
        ui.setStageImage("/Resources/Images/Story1/battle2.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/battle2.wav", true);
        isCigaretteBattle = true;
        
        ui.displayText("\nNarrator: The bar is loud, chaotic. Everyone seems so happy. The alcohol promises to join them.", Color.GRAY);
        Timer startBattleActual = new Timer(2000, e2 -> {
            Enemy enemy = new DrugEnemy("Liquid Escape", 22, 3);
            battleManager.startBattle(enemy, battleResult -> {
                isCigaretteBattle = false;
                if (battleResult == BattleManager.BattleResult.WIN) {
                    ui.displayText("\nNarrator: You resist the urge to drink away your problems.", Color.GRAY);
                    state.adjustStat(GameState.PLAYER_MAX_HEALTH, 2);
                    state.adjustStat(GameState.PLAYER_ATTACK, 2);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 2);
                    ui.displayText("\nSystem Status: \"Victory! Health +2, Attack +2, Willpower +2\"", Color.GREEN);
                    Timer proceedTimer = new Timer(3000, e -> showDialogue(6));
                    proceedTimer.setRepeats(false);
                    proceedTimer.start();
                } else {
                    ui.displayText("\nNarrator: You give in and drink heavily. The pain numbs, but only temporarily.", Color.GRAY);
                    state.addItem(ALCOHOL_ITEM, 2);
                    state.addItem(CIGARETTE_ITEM, 1);
                    state.adjustStat(UNIQUE_STAT_KEY_S1, 4);
                    ui.displayText("\nSystem: Added alcohol and cigarettes to inventory. Depression Level +4", Color.ORANGE);
                    Timer proceedTimer = new Timer(3000, e -> showDialogue(6));
                    proceedTimer.setRepeats(false);
                    proceedTimer.start();
                }
            });
        });
        startBattleActual.setRepeats(false);
        startBattleActual.start();
    }

    private void startChapter3Battle() { 
        ui.setStageImage("/Resources/Images/Story1/battle3.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/battle3.wav", true);
        isHardDrugsBattle = true;
        
        ui.displayText("\nNarrator: Someone offers you pills. 'These will make everything stop hurting,' they say.", Color.GRAY);
        Timer startBattleActual = new Timer(2000, e2 -> {
            Enemy enemy = new DrugEnemy("Chemical Numbness", 28, 3);
            battleManager.startBattle(enemy, battleResult -> {
                isHardDrugsBattle = false;
                if (battleResult == BattleManager.BattleResult.WIN) {
                    ui.displayText("\nNarrator: You refuse the pills. 'I won't solve this by running away,' you think.", Color.GRAY);
                    state.adjustStat(GameState.PLAYER_MAX_HEALTH, 3);
                    state.adjustStat(GameState.PLAYER_ATTACK, 3);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 3);
                    ui.displayText("\nSystem Status: \"Major Victory! Health +3, Attack +3, Willpower +3\"", Color.GREEN);
                    Timer proceedTimer = new Timer(3000, e -> showDialogue(11));
                    proceedTimer.setRepeats(false);
                    proceedTimer.start();
                } else {
                    ui.displayText("\nNarrator: You take the pills. The world becomes distant and hazy.", Color.GRAY);
                    state.addItem(PILLS_ITEM, 1);
                    state.adjustStat(UNIQUE_STAT_KEY_S1, 6);
                    ui.displayText("\nSystem: Added Pain Pills to inventory. Depression Level +6", Color.RED.darker());
                    Timer proceedTimer = new Timer(3000, e -> showDialogue(11));
                    proceedTimer.setRepeats(false);
                    proceedTimer.start();
                }
            });
        });
        startBattleActual.setRepeats(false);
        startBattleActual.start();
    }

    private void startFinalBattle() {
        ui.setStageImage("/Resources/Images/Story1/battle4.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/battle4.wav", true);
        isFinalBattle = true;
        
        ui.displayText("\nNarrator: You stand before a mirror. The reflection shows every broken piece of yourself.", Color.GRAY);
        Timer preBattleTimer = new Timer(2500, e -> {
            Enemy enemy = new DrugEnemy("Complete Despair", 35, 4);
            battleManager.startBattle(enemy, battleResult -> {
                isFinalBattle = false;
                if (battleResult == BattleManager.BattleResult.WIN) {
                    ui.displayText("\nNarrator: You've found the strength to keep fighting. There's still hope.", Color.GRAY);
                    state.setFlag(FLAG_SKILL_RESILIENCE_ACQUIRED, true);
                    state.adjustStat(GameState.PLAYER_MAX_HEALTH, 5);
                    state.adjustStat(GameState.PLAYER_ATTACK, 5);
                    state.adjustStat(STAT_PLAYER_WILLPOWER, 5);
                    ui.displayText("\nSystem: \"SKILL RESILIENCE ACQUIRED! Major stat bonuses gained!\"", Color.GREEN);
                    showDialogue(12);
                } else {
                    ui.displayText("\nNarrator: The despair is overwhelming. But even in darkness, a small light remains.", Color.GRAY);
                    state.adjustStat(UNIQUE_STAT_KEY_S1, 5);
                    showBadEnding();
                }
            });
            
            battleManager.setMidBattleEvent(() -> {
                battleManager.pauseBattle();
                ui.displayText("\nComplete Despair: \"Why do you keep fighting? You're worthless. Give up.\"", Color.RED.darker());
                Timer t1 = new Timer(2000, e2 -> ui.displayText("\nNarrator: \"In your darkest moment, maybe something in your inventory could help...\"", Color.GRAY));
                t1.setRepeats(false); t1.start();
                Timer t2 = new Timer(4000, e2 -> {
                    ui.displayText("\nSystem: \"Mid-battle pause! You can use items from your inventory or continue fighting.\"", Color.ORANGE);
                    battleManager.resumeBattle();
                });
                t2.setRepeats(false); t2.start();
            });
        });
        preBattleTimer.setRepeats(false);
        preBattleTimer.start();
    }

    private void showStage0() {
        ui.setStageImage("/Resources/Images/Story1/stage0.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage0.wav", true);
        
        ui.displayText("\n[Your Apartment - Evening]", Color.DARK_GRAY);
        Timer t = new Timer(1500, e -> ui.displayText("\nNarrator: The silence is deafening. Empty bottles line the counter.", Color.GRAY));
        t.setRepeats(false);
        t.start();

        Timer timer = new Timer(3000, e -> ui.displayText("\n[Phone Notification]\nOLD_FRIEND: Hey, I'm in town... you wanna go grab some food?", Color.BLUE));
        timer.setRepeats(false);
        timer.start();

        Timer t2 = new Timer(5000, e -> ui.showChoicesDialog(new String[]{
            "\"Definitely! Where?\"",
            "\"Maybe another day...\""
        }));
        t2.setRepeats(false);
        t2.start();
    }

    private void showStage1() {
        ui.setStageImage("/Resources/Images/Story1/stage1.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage1.wav", true);
        
        ui.displayText("\nOLD_FRIEND: Great! How about that new place downtown, 'The Rusty Spoon', around 7?", Color.BLUE);
        Timer timer = new Timer(2000, e -> {
            ui.displayText("\n" + playerName + ": (It's been months since I've seen another human being socially...)", Color.GRAY);
            Timer t2 = new Timer(2000, e2 -> ui.showChoicesDialog(new String[]{
                "\"Sounds good, see you there!\"",
                "\"Actually, can we do a bit earlier?\""
            }));
            t2.setRepeats(false); t2.start();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showStage2() {
        ui.setStageImage("/Resources/Images/Story1/stage2.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage2.wav", true);
        
        ui.displayText("\n[Walking to The Rusty Spoon]", Color.DARK_GRAY);
        Timer timer = new Timer(1500, e -> {
            ui.displayText("\nNarrator: The evening air is cold. As you turn a corner, you see a figure in the shadows - it looks familiar, too familiar.", Color.GRAY);
            Timer t2 = new Timer(3000, e2 -> ui.showChoicesDialog(new String[]{
                "\"Face whatever this is\" (Confront)",
                "\"Just keep walking\" (Avoid)"
            }));
            t2.setRepeats(false); t2.start();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showStage3() {
        ui.setStageImage("/Resources/Images/Story1/stage3.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage3.wav", true);
        
        ui.displayText("\n[At 'The Rusty Spoon']", Color.DARK_GRAY);
        Timer timer = new Timer(1000, e -> {
            if (state.getFlag("choseToIsolate_d0")) {
                ui.displayText("\nNarrator: Despite your earlier hesitation, you made it. Your friend waves from a corner booth, concern visible in their eyes.", Color.GRAY);
            } else {
                ui.displayText("\nNarrator: Your friend is already there, waving at you from a corner booth. They look genuinely happy to see you.", Color.GRAY);
            }
            Timer t2 = new Timer(3000, e2 -> ui.showChoicesDialog(new String[]{
                "\"Hey! Good to see you!\"",
                "\"Sorry I'm a bit late.\""
            }));
            t2.setRepeats(false); t2.start();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showStage4() {
        ui.setStageImage("/Resources/Images/Story1/stage4.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage4.wav", true);
        
        ui.displayText("\nOLD_FRIEND: \"So, how have you been? Really, I mean. You look... tired.\"", Color.BLUE);
        Timer timer = new Timer(2000, e -> {
            ui.displayText("\nNarrator: They're looking at you with genuine concern. This is your chance to open up... or keep the mask on.", Color.GRAY);
            Timer t2 = new Timer(3000, e2 -> ui.showChoicesDialog(new String[]{
                "\"Honestly? I've been struggling.\"",
                "\"Just tired from work, you know?\""
            }));
            t2.setRepeats(false); t2.start();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showStage5() {
        ui.setStageImage("/Resources/Images/Story1/stage5.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage5.wav", true);
        
        ui.displayText("\nOLD_FRIEND: \"You know what? Let's grab a drink after this. There's a nice bar down the street.\"", Color.BLUE);
        Timer t1 = new Timer(2000, e -> ui.displayText("\n" + playerName + ": (A drink sounds... dangerous. But also appealing.)", Color.GRAY));
        t1.setRepeats(false); t1.start();
        Timer choiceTimer = new Timer(4000, e -> ui.showChoicesDialog(new String[]{
            "\"You know what? Yeah. Let's do it.\"",
            "\"Actually, I should head home.\""
        }));
        choiceTimer.setRepeats(false);
        choiceTimer.start();
    }

    private void showStage6() {
        ui.setStageImage("/Resources/Images/Story1/stage6.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage6.wav", true);
        
        ui.displayText("\n[Walking Home Alone]", Color.DARK_GRAY);
        Timer t1 = new Timer(1500, e -> ui.displayText("\nNarrator: The dinner is over. You're walking home under the streetlights, processing what just happened.", Color.GRAY));
        t1.setRepeats(false);
        t1.start();
        Timer proceedTimer = new Timer(3000, e -> handleDialogueChoice(0));
        proceedTimer.setRepeats(false); proceedTimer.start();
    }

    private void showStage7() {
        ui.setStageImage("/Resources/Images/Story1/stage7.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage7.wav", true);
        
        Timer proceedTimer = new Timer(2000, e -> showDialogue(8));
        proceedTimer.setRepeats(false); proceedTimer.start();
    }

    private void showStage8() {
        ui.setStageImage("/Resources/Images/Story1/stage8.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage8.wav", true);
        
        ui.displayText("\nNarrator: Days pass. The emptiness returns, stronger than before.", Color.GRAY);
        Timer t1 = new Timer(3000, e -> ui.displayText("\n[End of Chapter 1]", Color.DARK_GRAY));
        t1.setRepeats(false); t1.start();
        Timer proceedTimer = new Timer(5000, e -> showDialogue(9));
        proceedTimer.setRepeats(false); proceedTimer.start();
    }

    private void showStage9() {
        ui.setStageImage("/Resources/Images/Story1/stage9.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage9.wav", true);
        
        ui.displayText("\n\n--- Chapter 2: The Weight of Days ---", Color.MAGENTA.darker());
        Timer t1 = new Timer(1500, e -> ui.displayText("\n[Your Workplace - Monday Morning]", Color.DARK_GRAY));
        t1.setRepeats(false);
        t1.start();
        Timer t2 = new Timer(3000, e -> ui.displayText("\nNarrator: Every task feels monumental. Your coworkers' laughter sounds distant.", Color.GRAY));
        t2.setRepeats(false);
        t2.start();
        Timer choiceTimer = new Timer(5000, e -> ui.showChoicesDialog(new String[]{
            "\"I can push through this.\"",
            "\"Maybe I should take a sick day.\""
        }));
        choiceTimer.setRepeats(false); choiceTimer.start();
    }

    private void showStage10() {
        ui.setStageImage("/Resources/Images/Story1/stage10.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage10.wav", true);
        
        ui.displayText("\n[After Work - Dark Alley]", Color.DARK_GRAY);
        Timer t1 = new Timer(1500, e -> ui.displayText("\nStranger: \"Hey buddy, you look like you could use something to take the edge off.\"", Color.RED.darker()));
        t1.setRepeats(false); t1.start();
        Timer choiceTimer = new Timer(4000, e -> ui.showChoicesDialog(new String[]{
            "\"What do you have?\" (Face Temptation)",
            "\"No thanks, I'm good.\" (Walk Away)"
        }));
        choiceTimer.setRepeats(false); choiceTimer.start();
    }

    private void showStage11() {
        ui.setStageImage("/Resources/Images/Story1/stage11.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage11.wav", true);
        
        ui.displayText("\n[Your Apartment - 3 AM]", Color.DARK_GRAY);
        Timer t1 = new Timer(1500, e -> ui.displayText("\nNarrator: You're staring at the ceiling. The thoughts are getting darker.", Color.GRAY));
        t1.setRepeats(false); t1.start();
        Timer choiceTimer = new Timer(4000, e -> ui.showChoicesDialog(new String[]{
            "\"I need to call someone.\"",
            "\"Nobody would understand.\""
        }));
        choiceTimer.setRepeats(false); choiceTimer.start();
    }

    private void showStage12() {
        ui.setStageImage("/Resources/Images/Story1/stage12.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage12.wav", true);
        
        ui.displayText("\n[Phone Call with Crisis Counselor]", Color.DARK_GRAY);
        Timer t1 = new Timer(1500, e -> ui.displayText("\nCounselor: \"You took a brave step by calling. Let's talk about what you're feeling.\"", Color.GREEN.darker()));
        t1.setRepeats(false); t1.start();
        Timer proceedTimer = new Timer(4000, e -> showDialogue(13));
        proceedTimer.setRepeats(false); proceedTimer.start();
    }

    private void showStage13() {
        ui.setStageImage("/Resources/Images/Story1/stage13.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage13.wav", true);
        
        ui.displayText("\n[Therapist's Office - One Month Later]", Color.DARK_GRAY);
        Timer t1 = new Timer(1500, e -> ui.displayText("\nTherapist: \"Recovery isn't linear. Some days will be harder than others.\"", Color.GREEN.darker()));
        t1.setRepeats(false); t1.start();
        Timer choiceTimer = new Timer(4000, e -> ui.showChoicesDialog(new String[]{
            "\"I'm ready to keep fighting.\"",
            "\"I'm scared I'll relapse.\""
        }));
        choiceTimer.setRepeats(false); choiceTimer.start();
    }

    private void showStage14() {
        ui.setStageImage("/Resources/Images/Story1/stage14.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/stage14.wav", true);
        
        ui.displayText("\n[End of Story]", Color.DARK_GRAY);
        Timer endTimer = new Timer(2000, e -> showFinalChoices());
        endTimer.setRepeats(false); endTimer.start();
    }

    private void showStage15() {
        showFinalChoices();
    }

    private void showGoodEnding() {
        ui.setStageImage("/Resources/Images/Story1/ending1.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/ending1.wav", true);
        
        ui.displayText("\n[Six Months Later]", Color.DARK_GRAY);
        Timer t1 = new Timer(2000, e -> ui.displayText("\nNarrator: You're at the same restaurant where it all started, but this time you're meeting your therapist for coffee.", Color.GRAY));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(5000, e -> ui.displayText("\n" + playerName + ": \"I never thought I'd say this, but I'm genuinely looking forward to tomorrow.\"", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer t3 = new Timer(8000, e -> ui.displayText("\nNarrator: The bottles are gone. The silence no longer speaks of emptiness, but of peace.", Color.GRAY));
        t3.setRepeats(false); t3.start();
        Timer endTimer = new Timer(11000, e -> showFinalChoices());
        endTimer.setRepeats(false); endTimer.start();
    }

    private void showBitterSweetEnding() {
        ui.setStageImage("/Resources/Images/Story1/ending2.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/ending2.wav", true);
        
        ui.displayText("\n[Present Day]", Color.DARK_GRAY);
        Timer t1 = new Timer(2000, e -> ui.displayText("\nNarrator: You're still fighting. Some days are better than others.", Color.GRAY));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(5000, e -> ui.displayText("\n" + playerName + ": \"At least now I know I'm not alone in this battle.\"", Color.BLACK));
        t2.setRepeats(false); t2.start();
        Timer endTimer = new Timer(8000, e -> showFinalChoices());
        endTimer.setRepeats(false); endTimer.start();
    }

    private void showBadEnding() {
        ui.setStageImage("/Resources/Images/Story1/ending3.png");
        AudioManager.getInstance().playMusic("/Resources/Audio/Story1/ending3.wav", true);
        
        ui.displayText("\n[The Void]", Color.DARK_GRAY);
        Timer t1 = new Timer(2000, e -> ui.displayText("\nNarrator: The silence has won. But even in the darkness, someone, somewhere, is looking for you.", Color.GRAY));
        t1.setRepeats(false); t1.start();
        Timer t2 = new Timer(5000, e -> ui.displayText("\nNarrator: If you're reading this and struggling, please reach out. You're not alone.", Color.BLUE));
        t2.setRepeats(false); t2.start();
        Timer endTimer = new Timer(8000, e -> showFinalChoices());
        endTimer.setRepeats(false); endTimer.start();
    }

    private void showFinalChoices() {
        ui.setStageImage(null);
        ui.displayText("\n\nTo be continued...", Color.BLACK);
        Timer t3 = new Timer(3000, e2 -> ui.showChoicesDialog(new String[]{
            "Return to Main Menu",
            "Restart Story"
        }));
        t3.setRepeats(false); t3.start();
    }

    @Override
    public String[] getCurrentChoices() {
        if (battleManager.isBattleActive()) {
            String itemChoiceText = "2. Use Item";
            if (state.getInventoryQuantities().isEmpty()) {
                itemChoiceText = "2. Use Item (None)";
            }
            return new String[]{
                "1. Fight Back", 
                itemChoiceText
            };
        }
        return new String[0]; 
    }

    public void setMidBattleEvent(Runnable event) {
        if (battleManager != null) {
            battleManager.setMidBattleEvent(event);
        }
    }
}