import java.awt.*;
import javax.swing.*;

public class GameUI extends JFrame {
    JTextArea textArea; // For main story/dialogue
    private JButton inventoryBtn, saveBtn;
    private Storyline currentStory;
    private GameState gameState;
    private Typewriter typewriter; // For main text area
    private Typewriter battleLogTypewriter; // For battle log text area

    // Panels for CardLayout
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private static final String TEXT_AREA_CARD = "TextAreaCard";
    private static final String BATTLE_CARD = "BattleCard";

    // Battle UI components
    private JPanel battleDisplayPanel; // The main panel for the battle card
    private JTextArea battleLogTextArea; // For battle-specific messages
    private JLabel playerHealthBattleLabel;
    private JLabel opponentNameBattleLabel;
    private JLabel opponentHealthBattleLabel;
    private JPanel battleActionPanel;
    private JButton attackButton;
    private JButton itemButtonBattle;

    public GameUI() {
        setTitle("Text Adventure");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        this.typewriter = new Typewriter(textArea);
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);


        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);


        battleDisplayPanel = new JPanel(new BorderLayout(5, 5));
        battleDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0)); 
        playerHealthBattleLabel = new JLabel("Player HP: --", SwingConstants.LEFT);
        opponentNameBattleLabel = new JLabel("Opponent: --", SwingConstants.CENTER);
        opponentHealthBattleLabel = new JLabel("HP: --", SwingConstants.RIGHT);
        statsPanel.add(playerHealthBattleLabel);
        statsPanel.add(opponentNameBattleLabel);
        statsPanel.add(opponentHealthBattleLabel);
        battleDisplayPanel.add(statsPanel, BorderLayout.NORTH);


        battleLogTextArea = new JTextArea();
        battleLogTextArea.setEditable(false);
        battleLogTextArea.setLineWrap(true);
        battleLogTextArea.setWrapStyleWord(true);
        this.battleLogTypewriter = new Typewriter(battleLogTextArea);
        battleDisplayPanel.add(new JScrollPane(battleLogTextArea), BorderLayout.CENTER);


        battleActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        attackButton = new JButton("Attack");
        itemButtonBattle = new JButton("Use Item");

        attackButton.addActionListener(e -> {
            if (currentStory != null && battleManagerIsActive()) {
                currentStory.handleChoice(1); // 1 for Attack
            }
        });
        itemButtonBattle.addActionListener(e -> {
            if (currentStory != null && battleManagerIsActive()) {
                currentStory.handleChoice(2); // 2 for Item
            }
        });

        battleActionPanel.add(attackButton);
        battleActionPanel.add(itemButtonBattle);
        battleDisplayPanel.add(battleActionPanel, BorderLayout.SOUTH);

        // Add cards to mainContentPanel
        mainContentPanel.add(textAreaScrollPane, TEXT_AREA_CARD);
        mainContentPanel.add(battleDisplayPanel, BATTLE_CARD);

        add(mainContentPanel, BorderLayout.CENTER); 

        // Bottom button panel (Inventory, Save/Load)
        JPanel bottomButtonPanel = new JPanel(new GridLayout(1, 2));
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(200, 50);

        inventoryBtn = new JButton("Inventory");
        styleButton(inventoryBtn, buttonFont, buttonSize, new Color(100, 200, 100));
        saveBtn = new JButton("Save Game"); // Clarify button purpose
        styleButton(saveBtn, buttonFont, buttonSize, new Color(100, 100, 200));
        saveBtn.addActionListener(e -> saveCurrentGame());

        inventoryBtn.addActionListener(e -> showInventory());
        // The "Load Game" button is primarily on the StartMenu.
        // If you want a load button in GameUI, it would likely restart the game with loaded data.
        // For now, we'll focus on StartMenu loading.

        bottomButtonPanel.add(inventoryBtn);
        bottomButtonPanel.add(saveBtn);
        add(bottomButtonPanel, BorderLayout.SOUTH);

        cardLayout.show(mainContentPanel, TEXT_AREA_CARD);
    }

    private boolean battleManagerIsActive() {
        if (currentStory instanceof Storyline1) { 
            return ((Storyline1) currentStory).getBattleManager().isBattleActive();
        }
        // Add similar checks for Storyline2, Storyline3 if they can have battles
        return false;
    }

    public void startGame(int storylineId) {
        this.gameState = new GameState();
        switch (storylineId) {
            case 1 -> currentStory = new Storyline1(this, gameState);
            case 2 -> currentStory = new Storyline2(this, gameState);
            case 3 -> currentStory = new Storyline3(this, gameState);
            default -> {
                displayText("Error: Invalid storyline ID.", Color.RED);
                return;
            }
        }
        if (currentStory != null) {
            currentStory.startStory();
        }
    }

    public void showBattleInterface(String opponentName, int playerHealth, int opponentHealth) {
        playerHealthBattleLabel.setText("Player HP: " + playerHealth);
        opponentNameBattleLabel.setText(opponentName);
        opponentHealthBattleLabel.setText("HP: " + opponentHealth);
        battleLogTextArea.setText(""); 
        if (battleLogTypewriter != null) { // Ensure typewriter queue is cleared if it exists
            battleLogTypewriter.stopAndClearQueue();
        }
        cardLayout.show(mainContentPanel, BATTLE_CARD);
    }

    public void updateBattleInterfaceHealth(int playerHealth, int opponentHealth) {
        playerHealthBattleLabel.setText("Player HP: " + playerHealth);
        opponentHealthBattleLabel.setText("HP: " + opponentHealth);
    }

    public void appendBattleLog(String message, Color color) {
        // Use the battleLogTypewriter with a short delay
        int shortDelay = 15; // milliseconds, adjust as needed for "very quickly"
        this.battleLogTypewriter.typeText(message + "\n", color != null ? color : Color.BLACK, shortDelay);
    }

    public void hideBattleInterface() {
        cardLayout.show(mainContentPanel, TEXT_AREA_CARD);
    }

    public void showChoicesDialog(String[] options) {
        if (options == null || options.length == 0) return;

        JDialog dialogueDialog = new JDialog(this, "Dialogue Choices", true);
        dialogueDialog.setLayout(new BoxLayout(dialogueDialog.getContentPane(), BoxLayout.Y_AXIS));
        dialogueDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String option : options) {
            listModel.addElement(option);
        }
        JList<String> optionList = new JList<>(listModel);
        JButton btn = new JButton("Select");
        btn.addActionListener(e -> {
            int selectedIdx = optionList.getSelectedIndex();
            dialogueDialog.dispose();
            if (selectedIdx >= 0 && currentStory != null) {
                currentStory.handleChoice(selectedIdx + 1);
            }
        });
        dialogueDialog.add(new JScrollPane(optionList));
        dialogueDialog.add(btn, BorderLayout.SOUTH);
        dialogueDialog.pack(); // Pack first to get preferred height based on content

        // Set a fixed width (e.g., 400) and an adaptive height with a maximum (e.g., 250)
        int fixedWidth = 400; 
        int maxHeight = 250; // Example maximum height
        // Use preferred height from pack(), add some padding, but cap at maxHeight
        int preferredHeightWithPadding = dialogueDialog.getPreferredSize().height + 50;
        dialogueDialog.setSize(fixedWidth, Math.min(maxHeight, preferredHeightWithPadding)); 
        
        dialogueDialog.setLocationRelativeTo(null);
        dialogueDialog.setResizable(false);
        dialogueDialog.setVisible(true);
    }

    public void displayText(String text, Color color) {
        this.typewriter.typeText(text, color != null ? color : Color.BLACK, 20); 
    }

    private void showInventory() {
        JDialog inventoryDialog = new JDialog(this, "Inventory", true);
        inventoryDialog.setSize(300, 200);
        String[] items = {"Whiskey Bottle (3 uses)", "Pocket Knife", "Car Keys"}; 
        JList<String> itemList = new JList<>(items);
        JButton useBtn = new JButton("Use Selected");
        useBtn.addActionListener(e -> {
            String selected = itemList.getSelectedValue();
            inventoryDialog.dispose();
            displayText("\nUsed: " + (selected != null ? selected : "nothing") + "...", Color.BLACK);
        });
        inventoryDialog.add(new JScrollPane(itemList));
        inventoryDialog.add(useBtn, BorderLayout.SOUTH);
        inventoryDialog.setLocationRelativeTo(null);
        inventoryDialog.setVisible(true);
    }

    private void showDialogueOptions() { 
        String[] options = {"Hello nice to meet you!", "This is another text dialogue", "THIS IS A FAST RED TEXT"};
        showChoicesDialog(options); 
    }

    private void saveCurrentGame() {
        if (currentStory == null || gameState == null) {
            displayText("\nNothing to save.", Color.BLACK);
            return;
        }
        // For simplicity, disallow saving during active battle in this iteration
        if (battleManagerIsActive()) {
            displayText("\nCannot save during an active battle.", Color.RED);
            return;
        }

        int storylineId = -1;
        int currentDialogueState = -1;

        if (currentStory instanceof Storyline1) {
            storylineId = 1;
            currentDialogueState = ((Storyline1) currentStory).getDialogueState();
        } else if (currentStory instanceof Storyline2) {
            storylineId = 2;
            // Storyline2 doesn't have a complex dialogueState field yet
            // currentDialogueState = ((Storyline2) currentStory).getDialogueState();
        } else if (currentStory instanceof Storyline3) {
            storylineId = 3;
            // Storyline3 doesn't have a complex dialogueState field yet
            // currentDialogueState = ((Storyline3) currentStory).getDialogueState();
        } else {
            displayText("\nCannot determine storyline type to save.", Color.RED);
            return;
        }
        
        // If a storyline doesn't have a specific dialogue state to save, use a default (e.g., 0 or -1)
        // For Storyline1, we have it. For others, adjust as they develop.
        if (storylineId != 1 && currentDialogueState == -1) currentDialogueState = 0; // Default if not Storyline1

        SaveData saveData = new SaveData(
            storylineId,
            currentDialogueState,
            gameState.getAllStats(),
            gameState.getAllFlags()
        );

        SaveManager.saveGame(saveData);
        displayText("\nGame Saved!", Color.BLACK);
    }

    // Method to apply loaded data
    public void applySaveData(SaveData data) {
        if (data == null) {
            displayText("Failed to load save data.", Color.RED);
            return;
        }

        this.gameState = new GameState(); // Re-initialize or create new
        this.gameState.setAllStats(data.stats);
        this.gameState.setAllFlags(data.flags);

        boolean storyLoaded = false;
        switch (data.storylineId) {
            case 1:
                currentStory = new Storyline1(this, gameState);
                ((Storyline1) currentStory).setDialogueState(data.dialogueState);
                ((Storyline1) currentStory).showDialoguePublic(data.dialogueState); // Trigger UI update
                storyLoaded = true;
                break;
            case 2:
                currentStory = new Storyline2(this, gameState);
                // Storyline2.startStory() might be enough if no specific state
                currentStory.startStory(); // Or a setDialogueState + showDialoguePublic if implemented
                storyLoaded = true;
                break;
            case 3:
                currentStory = new Storyline3(this, gameState);
                currentStory.startStory(); // Or a setDialogueState + showDialoguePublic if implemented
                storyLoaded = true;
                break;
            default:
                displayText("Error: Invalid storyline ID in save data: " + data.storylineId, Color.RED);
                return;
        }
        
        if(storyLoaded) {
            // Clear the main text area before displaying loaded content,
            // especially if the typewriter is involved.
            // The typewriter should handle appending to a cleared or existing area.
            // If typewriter appends, ensure it starts fresh or from where the loaded story dictates.
            // For simplicity, let's assume showDialoguePublic/startStory handles the initial display.
            textArea.setText(""); // Clear previous text before loading new story text
            if (currentStory instanceof Storyline1) { // Re-trigger display for Storyline1
                 ((Storyline1) currentStory).showDialoguePublic(data.dialogueState);
            } else { // For others, startStory might be enough
                 currentStory.startStory();
            }
            displayText("\nGame Loaded!", Color.BLACK);
        }
    }
    private void styleButton(JButton button, Font font, Dimension size, Color bgColor) {
        button.setFont(font);
        button.setPreferredSize(size);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }
}