import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder; // For padding

public class GameUI extends JFrame {
    JTextArea textArea; // For main story/dialogue
    private JButton inventoryBtn, saveBtn;
    private Storyline currentStory;
    private GameState gameState;
    private Typewriter typewriter; // For main text area
    private Typewriter battleLogTypewriter; // For battle log text area

    // Panels for CardLayout
    private JPanel mainContentPanel; // This will hold textArea and battleDisplayPanel via CardLayout
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

    // New top panel for background
    private JPanel topImagePanel;

    public GameUI() {
        setTitle("Text Adventure");
        setSize(800, 600); // Keep overall size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main container panel for the JFrame's content pane
        JPanel mainFramePanel = new JPanel(new BorderLayout());
        setContentPane(mainFramePanel);

        // 1. Top panel (for background image later)
        topImagePanel = new JPanel();
        topImagePanel.setBackground(new Color(50, 50, 60)); // Dark placeholder color
        // This panel will be in BorderLayout.CENTER, so it will take available space.
        // We can set a preferred size to influence layout if needed, but BorderLayout.CENTER is flexible.
        // For now, let's give it a substantial preferred height.
        topImagePanel.setPreferredSize(new Dimension(800, 300)); // Adjust as needed
        mainFramePanel.add(topImagePanel, BorderLayout.CENTER);

        // 2. Bottom UI Panel (will contain text area/battle UI and control buttons)
        JPanel bottomSectionPanel = new JPanel(new BorderLayout());
        // Set a preferred height for the entire bottom section
        bottomSectionPanel.setPreferredSize(new Dimension(800, 250)); // Adjust as needed
        mainFramePanel.add(bottomSectionPanel, BorderLayout.SOUTH);

        // 2a. Main Content Panel (CardLayout for text and battle)
        // This panel will contain the JScrollPanes for textArea and battleDisplayPanel
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(10, 15, 10, 15)); // Added padding for readability
        textArea.setBackground(new Color(230, 230, 230)); // Light gray background for text area
        textArea.setForeground(Color.BLACK);
        this.typewriter = new Typewriter(textArea);
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);
        textAreaScrollPane.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(5, 5, 5, 5), // Outer padding for the scroll pane
            BorderFactory.createLineBorder(Color.DARK_GRAY) // Border around scroll pane
        ));


        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        // mainContentPanel will be added to bottomSectionPanel's CENTER

        // --- Battle UI Setup ---
        battleDisplayPanel = new JPanel(new BorderLayout(5, 5));
        battleDisplayPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // Padding for the whole battle card
        battleDisplayPanel.setBackground(new Color(220, 220, 220)); // Slightly different background for battle

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.setOpaque(false); // Make transparent if battleDisplayPanel has bg color
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
        battleLogTextArea.setMargin(new Insets(10, 15, 10, 15)); // Added padding
        battleLogTextArea.setBackground(new Color(240, 240, 240)); // Light background for battle log
        battleLogTextArea.setForeground(Color.BLACK);
        this.battleLogTypewriter = new Typewriter(battleLogTextArea);
        JScrollPane battleLogScrollPane = new JScrollPane(battleLogTextArea);
        battleLogScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        battleDisplayPanel.add(battleLogScrollPane, BorderLayout.CENTER);

        battleActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        battleActionPanel.setOpaque(false);
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
        // --- End of Battle UI Setup ---

        // Add cards to mainContentPanel
        mainContentPanel.add(textAreaScrollPane, TEXT_AREA_CARD);
        mainContentPanel.add(battleDisplayPanel, BATTLE_CARD);

        // Add mainContentPanel to the center of bottomSectionPanel
        bottomSectionPanel.add(mainContentPanel, BorderLayout.CENTER);
        cardLayout.show(mainContentPanel, TEXT_AREA_CARD); // Show text area by default

        // 2b. Bottom button panel (Inventory, Save)
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomButtonPanel.setBorder(new EmptyBorder(5, 0, 10, 0)); // Padding around buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 14); // Slightly smaller font
        Dimension buttonSize = new Dimension(130, 35); // Slightly smaller buttons

        inventoryBtn = new JButton("Inventory");
        styleButton(inventoryBtn, buttonFont, buttonSize, new Color(70, 130, 180)); // Steel Blue
        saveBtn = new JButton("Save Game");
        styleButton(saveBtn, buttonFont, buttonSize, new Color(60, 179, 113)); // Medium Sea Green
        saveBtn.addActionListener(e -> saveCurrentGame());

        inventoryBtn.addActionListener(e -> showInventory());

        bottomButtonPanel.add(inventoryBtn);
        bottomButtonPanel.add(saveBtn);
        
        // Add bottomButtonPanel to the south of bottomSectionPanel
        bottomSectionPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
    }

    private boolean battleManagerIsActive() {
        if (currentStory instanceof Storyline1) { 
            return ((Storyline1) currentStory).getBattleManager().isBattleActive();
        } else if (currentStory instanceof Storyline2) {
            return ((Storyline2) currentStory).getBattleManager().isBattleActive();
        } else if (currentStory instanceof Storyline3) {
            return ((Storyline3) currentStory).getBattleManager().isBattleActive();
        }
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
        if (battleLogTypewriter != null) { 
            battleLogTypewriter.stopAndClearQueue();
        }
        cardLayout.show(mainContentPanel, BATTLE_CARD);
    }

    public void updateBattleInterfaceHealth(int playerHealth, int opponentHealth) {
        playerHealthBattleLabel.setText("Player HP: " + playerHealth);
        opponentHealthBattleLabel.setText("HP: " + opponentHealth);
    }

    public void appendBattleLog(String message, Color color) {
        int shortDelay = 15; 
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
        dialogueDialog.pack(); 

        int fixedWidth = 400; 
        int maxHeight = 250; 
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

    private void saveCurrentGame() {
        if (currentStory == null || gameState == null) {
            displayText("\nNothing to save.", Color.BLACK);
            return;
        }
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
            currentDialogueState = ((Storyline2) currentStory).getDialogueState();
        } else if (currentStory instanceof Storyline3) {
            storylineId = 3;
            currentDialogueState = ((Storyline3) currentStory).getDialogueState();
        } else {
            displayText("\nCannot determine storyline type to save.", Color.RED);
            return;
        }
        
        if (currentDialogueState == -1 && storylineId != -1) { 
             displayText("\nWarning: dialogueState is -1 for Storyline " + storylineId + ". Saving as 0.", Color.ORANGE);
             currentDialogueState = 0; 
        }

        SaveData saveData = new SaveData(
            storylineId,
            currentDialogueState,
            gameState.getAllStats(),
            gameState.getAllFlags()
        );

        SaveManager.saveGame(saveData);
        displayText("\nGame Saved!", Color.BLACK);
    }

    public void applySaveData(SaveData data) {
        if (data == null) {
            displayText("Failed to load save data.", Color.RED);
            return;
        }

        this.gameState = new GameState(); 
        this.gameState.setAllStats(data.stats);
        this.gameState.setAllFlags(data.flags);

        boolean storyLoaded = false;
        
        textArea.setText(""); 
        if (this.typewriter != null) {
            this.typewriter.stopAndClearQueue(); 
        }

        switch (data.storylineId) {
            case 1:
                currentStory = new Storyline1(this, gameState);
                break;
            case 2:
                currentStory = new Storyline2(this, gameState);
                break;
            case 3:
                currentStory = new Storyline3(this, gameState);
                break;
            default:
                displayText("Error: Invalid storyline ID in save data: " + data.storylineId, Color.RED);
                return;
        }
        
        if (currentStory != null) { 
            if (currentStory instanceof Storyline1) {
                ((Storyline1) currentStory).setDialogueState(data.dialogueState);
                ((Storyline1) currentStory).showDialoguePublic(data.dialogueState);
            } else if (currentStory instanceof Storyline2) {
                ((Storyline2) currentStory).setDialogueState(data.dialogueState);
                ((Storyline2) currentStory).showDialoguePublic(data.dialogueState);
            } else if (currentStory instanceof Storyline3) {
                ((Storyline3) currentStory).setDialogueState(data.dialogueState);
                ((Storyline3) currentStory).showDialoguePublic(data.dialogueState);
            }
            storyLoaded = true;
        } else {
            displayText("Failed to instantiate storyline for ID: " + data.storylineId, Color.RED);
            return;
        }
            
        if(storyLoaded) {
            displayText("\nGame Loaded!", Color.BLACK);
        }
    }
    private void styleButton(JButton button, Font font, Dimension size, Color bgColor) {
        button.setFont(font);
        button.setPreferredSize(size);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder()); // Keep raised bevel or change as desired
        // Add hover effects if you like
        Color hoverColor = bgColor.brighter();
        Color pressedColor = bgColor.darker();

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(pressedColor);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if (button.getBounds().contains(evt.getPoint())) {
                     button.setBackground(hoverColor);
                } else {
                     button.setBackground(bgColor);
                }
            }
        });
    }
}