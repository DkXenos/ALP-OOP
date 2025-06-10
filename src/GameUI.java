import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GameUI extends JFrame {
    JTextArea textArea;
    private JButton inventoryBtn, saveBtn;
    private Storyline currentStory;
    private GameState gameState;
    private Typewriter typewriter;
    private Typewriter battleLogTypewriter;

    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private static final String TEXT_AREA_CARD = "TextAreaCard";
    private static final String BATTLE_CARD = "BattleCard";

    private JPanel battleDisplayPanel;
    private JTextArea battleLogTextArea;
    private JLabel playerHealthBattleLabel;
    private JLabel opponentNameBattleLabel;
    private JLabel opponentHealthBattleLabel;
    private JPanel battleActionPanel;
    private JButton attackButton;

    private JPanel topImagePanel;
    private Image currentStageImage;

    public GameUI() {
        setTitle("Text Adventure");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainFramePanel = new JPanel(new BorderLayout());
        setContentPane(mainFramePanel);

        topImagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (currentStageImage != null) {
                    g.drawImage(currentStageImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                }
            }
        };
        topImagePanel.setBackground(new Color(50, 50, 60));
        topImagePanel.setPreferredSize(new Dimension(800, 300));
        mainFramePanel.add(topImagePanel, BorderLayout.CENTER);

        JPanel bottomSectionPanel = new JPanel(new BorderLayout());
        bottomSectionPanel.setPreferredSize(new Dimension(800, 250));
        mainFramePanel.add(bottomSectionPanel, BorderLayout.SOUTH);
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(10, 15, 10, 15));
        textArea.setBackground(new Color(230, 230, 230));
        textArea.setForeground(Color.BLACK);
        this.typewriter = new Typewriter(textArea);
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);
        textAreaScrollPane.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(Color.DARK_GRAY)
        ));

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        
        battleDisplayPanel = new JPanel(new BorderLayout(5, 5));
        battleDisplayPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        battleDisplayPanel.setBackground(new Color(220, 220, 220));

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.setOpaque(false);
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
        battleLogTextArea.setMargin(new Insets(10, 15, 10, 15));
        battleLogTextArea.setBackground(new Color(240, 240, 240));
        battleLogTextArea.setForeground(Color.BLACK);
        this.battleLogTypewriter = new Typewriter(battleLogTextArea);
        JScrollPane battleLogScrollPane = new JScrollPane(battleLogTextArea);
        battleLogScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        battleDisplayPanel.add(battleLogScrollPane, BorderLayout.CENTER);

        battleActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        battleActionPanel.setOpaque(false);
        attackButton = new JButton("Attack");

        attackButton.addActionListener(e -> {
            if (currentStory != null && battleManagerIsActive()) {
                currentStory.handleChoice(1);
            }
        });

        battleActionPanel.add(attackButton);
        battleDisplayPanel.add(battleActionPanel, BorderLayout.SOUTH);

        mainContentPanel.add(textAreaScrollPane, TEXT_AREA_CARD);
        mainContentPanel.add(battleDisplayPanel, BATTLE_CARD);

        bottomSectionPanel.add(mainContentPanel, BorderLayout.CENTER);
        cardLayout.show(mainContentPanel, TEXT_AREA_CARD);

        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomButtonPanel.setBorder(new EmptyBorder(5, 0, 10, 0));
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        Dimension buttonSize = new Dimension(130, 35);

        inventoryBtn = new JButton("Inventory");
        styleButton(inventoryBtn, buttonFont, buttonSize, new Color(70, 130, 180));
        
        saveBtn = new JButton("Save Game");
        styleButton(saveBtn, buttonFont, buttonSize, new Color(60, 179, 113));
        saveBtn.addActionListener(e -> saveCurrentGame());

        inventoryBtn.addActionListener(e -> showInventory());

        bottomButtonPanel.add(inventoryBtn);
        bottomButtonPanel.add(saveBtn);
                
        bottomSectionPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
    }

    private boolean battleManagerIsActive() {
        if (currentStory instanceof Storyline1) {
            return ((Storyline1) currentStory).getBattleManager().isBattleActive();
        } else if (currentStory instanceof Storyline2) {
            BattleManager bm = ((Storyline2) currentStory).getBattleManager();
            return bm != null && bm.isBattleActive();
        } else if (currentStory instanceof Storyline3) {
            return ((Storyline3) currentStory).getBattleManager().isBattleActive();
        }
        return false;
    }

    public void setStageImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            this.currentStageImage = null;
        } else {
            try {
                URL imgUrl = getClass().getResource(imagePath);
                if (imgUrl != null) {
                    this.currentStageImage = new ImageIcon(imgUrl).getImage();
                } else {
                    System.err.println("Image not found: " + imagePath);
                    this.currentStageImage = null;
                }
            } catch (Exception e) {
                System.err.println("Error loading image: " + imagePath);
                e.printStackTrace();
                this.currentStageImage = null;
            }
        }
        topImagePanel.repaint();
    }
    
    public void startGame(int storylineId) {
        this.gameState = new GameState();
        setStageImage(null);
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
        if (gameState == null) {
            displayText("\nCannot open inventory: GameState not initialized.", Color.RED);
            return;
        }

        JDialog inventoryDialog = new JDialog(this, "Inventory", true);
        inventoryDialog.setSize(450, 300);
        inventoryDialog.setLayout(new BorderLayout());

        List<String> itemsForDisplay = gameState.getInventoryForDisplay();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String itemDisplayString : itemsForDisplay) {
            listModel.addElement(itemDisplayString);
        }

        JList<String> itemList = new JList<>(listModel);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JButton useBtn = new JButton("Use Selected");
        useBtn.setEnabled(!itemsForDisplay.isEmpty() && !itemsForDisplay.get(0).equals("Your inventory is empty."));

        useBtn.addActionListener(e -> {
            String selectedValue = itemList.getSelectedValue();
            if (selectedValue != null && !selectedValue.equals("Your inventory is empty.")) {
                String itemName = selectedValue.substring(0, selectedValue.indexOf(" (x"));
                inventoryDialog.dispose();

                if (battleManagerIsActive()) {
                    handleBattleItemUsage(itemName);
                } else {
                    handleNormalItemUsage(itemName);
                }
            } else {
                 displayText("\nNo item selected or inventory is empty.", Color.BLACK);
            }
        });

        inventoryDialog.add(new JScrollPane(itemList), BorderLayout.CENTER);
        inventoryDialog.add(useBtn, BorderLayout.SOUTH);
        inventoryDialog.setLocationRelativeTo(this);
        inventoryDialog.setVisible(true);
    }

    private void handleBattleItemUsage(String itemName) {
        if (currentStory instanceof Storyline1) {
            Item item = gameState.getItemPrototype(itemName);
            if (item != null && gameState.getItemQuantity(itemName) > 0) {
                item.applyEffect(gameState, this, "Player");
                gameState.consumeItem(itemName);
                appendBattleLog("Used " + itemName + " during battle!", Color.GREEN);
            } else {
                appendBattleLog("Cannot use " + itemName + ".", Color.RED);
            }
        } else if (currentStory instanceof Storyline2) {
            ((Storyline2) currentStory).useInventoryItem(itemName);
        } else if (currentStory instanceof Storyline3) {
            ((Storyline3) currentStory).useInventoryItem(itemName);
        }
    }

    private void handleNormalItemUsage(String itemName) {
        if (currentStory instanceof Storyline3) {
            ((Storyline3) currentStory).useInventoryItem(itemName);
        } else if (currentStory instanceof Storyline2) {
            ((Storyline2) currentStory).useInventoryItem(itemName);
        } else {
            Item item = gameState.getItemPrototype(itemName);
            if (item != null && gameState.getItemQuantity(itemName) > 0) {
                item.applyEffect(gameState, this, "Player");
                gameState.consumeItem(itemName);
                displayText("\nUsed: " + itemName + ".", Color.BLACK);
            } else {
                displayText("\nCannot use " + itemName + ".", Color.ORANGE);
            }
        }
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
        }  else if (currentStory instanceof Storyline3) {
            storylineId = 3;
            currentDialogueState = ((Storyline3) currentStory).getDialogueState();
        } else {
            displayText("\nCannot determine storyline type to save.", Color.RED);
            return;
        }

        String[] options = {
            "Slot 1: " + SaveManager.getSlotStage(1),
            "Slot 2: " + SaveManager.getSlotStage(2),
            "Slot 3: " + SaveManager.getSlotStage(3)
        };
        String choice = (String) JOptionPane.showInputDialog(
            this,
            "Select a slot to save:",
            "Save Game",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );
        if (choice == null) {
            displayText("\nSave cancelled.", Color.BLACK);
            return;
        }
        int slot = choice.startsWith("Slot 1") ? 1 : choice.startsWith("Slot 2") ? 2 : 3;

        SaveData saveData = new SaveData(
            storylineId,
            currentDialogueState,
            gameState.getAllStats(),
            gameState.getAllFlags(),
            new HashMap<>(gameState.getInventoryQuantities())
        );

        try {
            SaveManager.saveGame(saveData, slot);
            displayText("\nGame Saved to slot " + slot + "!", Color.BLACK);
        } catch (Exception ex) {
            displayText("\nFailed to save game: " + ex.getMessage(), Color.RED);
            ex.printStackTrace();
        }
    }

    public void applySaveData(SaveData data) {
        if (data == null) {
            displayText("Failed to load save data.", Color.RED);
            return;
        }

        setStageImage(null);

        this.gameState = new GameState();
        this.gameState.setAllStats(data.stats);
        this.gameState.setAllFlags(data.flags);
        this.gameState.setInventoryQuantities(data.inventoryQuantities);

        textArea.setText("");
        if (this.typewriter != null) {
            this.typewriter.stopAndClearQueue();
        }

        switch (data.storylineId) {
            case 1:
                currentStory = new Storyline1(this, gameState);
                ((Storyline1) currentStory).setDialogueState(data.dialogueState);
                ((Storyline1) currentStory).showDialoguePublic(data.dialogueState);
                break;
            case 2:
                currentStory = new Storyline2(this, gameState);
                ((Storyline2) currentStory).setDialogueState(data.dialogueState);
                ((Storyline2) currentStory).startStory(true);
                break;
            case 3:
                currentStory = new Storyline3(this, gameState);
                ((Storyline3) currentStory).setDialogueState(data.dialogueState);
                ((Storyline3) currentStory).showDialoguePublic(data.dialogueState);
                break;
            default:
                displayText("Error: Invalid storyline ID in save data: " + data.storylineId, Color.RED);
                return;
        }

        displayText("\nGame Loaded!", Color.BLACK);
    }
    private void styleButton(JButton button, Font font, Dimension size, Color bgColor) {
        button.setFont(font);
        button.setPreferredSize(size);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
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

    public void loadSaveGame(int slot) {
        SaveData data = SaveManager.loadGame(slot);
        if (data != null) {
            applySaveData(data);
        } else {
            displayText("\nNo save found in slot " + slot + ".", Color.RED);
        }
    }
}