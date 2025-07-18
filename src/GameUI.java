import java.awt.*;
import javax.swing.*;

public class GameUI extends JFrame {
    JTextArea textArea;
    private JButton inventoryBtn, saveBtn; // Removed dialogueBtn
    private Storyline currentStory;
    private GameState gameState;
    // private JPanel choicePanel; // Removed choicePanel


     public GameUI() {

        setTitle("Text Adventure");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Removed choicePanel and its setup

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2)); // Only 2 buttons now
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(200, 50); 

        inventoryBtn = new JButton("Inventory");
        styleButton(inventoryBtn, buttonFont, buttonSize, new Color(100, 200, 100)); 

        saveBtn = new JButton("Save/Load");
        styleButton(saveBtn, buttonFont, buttonSize, new Color(100, 100, 200)); 
        
        inventoryBtn.addActionListener(e -> showInventory());
        saveBtn.addActionListener(e -> showSaveLoadMenu());
        
        buttonPanel.add(inventoryBtn);
        buttonPanel.add(saveBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void startGame(int storylineId) {
        this.gameState = new GameState();
        
        switch(storylineId) {
            case 1 -> currentStory = new Storyline1(this, gameState);
            case 2 -> currentStory = new Storyline2(this, gameState);
            case 3 -> currentStory = new Storyline3(this, gameState);
        }
        
        currentStory.startStory();
    }
    public void showChoicesDialog(String[] options) {
        if (options == null || options.length == 0) return;

        JDialog dialogueDialog = new JDialog(this, "Dialogue Choices", true);
        dialogueDialog.setLayout(new BoxLayout(dialogueDialog.getContentPane(), BoxLayout.Y_AXIS));

        dialogueDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialogueDialog.setUndecorated(false);
        dialogueDialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String option : options) {
            listModel.addElement(option); 
        }

        JList<String> optionList = new JList<>(listModel);

        JButton btn = new JButton("Select");
        btn.addActionListener(e -> {
            int selectedIdx = optionList.getSelectedIndex();
            if (selectedIdx >= 0 && currentStory != null) {
                currentStory.handleChoice(optionList.getSelectedValue());
            }
            dialogueDialog.dispose();
        });

        dialogueDialog.add(new JScrollPane(optionList));
        dialogueDialog.add(btn, BorderLayout.SOUTH);
        dialogueDialog.pack();
        dialogueDialog.setSize(500,200);
        dialogueDialog.setLocationRelativeTo(null);
        dialogueDialog.setResizable(false);
        dialogueDialog.setVisible(true);
    }
    public void displayText(String text, Color color) {

    Typewriter typewriter = new Typewriter(textArea);
    typewriter.typeText(text, color != null ? color : Color.WHITE, 20);
}


    private void showInventory() {
        JDialog inventoryDialog = new JDialog(this, "Inventory", true);
        inventoryDialog.setSize(300, 200);
        
        // Example items
        String[] items = {"Whiskey Bottle (3 uses)", "Pocket Knife", "Car Keys"};
        JList<String> itemList = new JList<>(items);
        
        JButton useBtn = new JButton("Use Selected");
        useBtn.addActionListener(e -> {
            String selected = itemList.getSelectedValue();
            inventoryDialog.dispose();
            
            Typewriter typewriter = new Typewriter(textArea);
            typewriter.typeText("\nUsed: " + selected + "...", Color.black,20); // untuk delay
        });
        
        
        inventoryDialog.add(new JScrollPane(itemList));
        inventoryDialog.add(useBtn, BorderLayout.SOUTH);
        inventoryDialog.setLocationRelativeTo(null);
        inventoryDialog.setVisible(true);
    }

    private void showDialogueOptions() {
        JDialog dialogueDialog = new JDialog(this, "Dialogue Choices", true);
        dialogueDialog.setLayout(new BoxLayout(dialogueDialog.getContentPane(), BoxLayout.Y_AXIS));
        
        // Example options
        String[] options = {"Hello nice to meet you!", "This is another text dialogue", "this is a longer text dialogue that is used to test limit"};
        String redOption = "THIS IS A FAST RED TEXT";

        DefaultListModel<String> listModel = new DefaultListModel<>(); // ini kek list yang modifiable

        for (String option : options) {
            listModel.addElement(option); 
        }
        listModel.addElement(redOption);

        JList<String> optionList = new JList<>(listModel);
        optionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btn = new JButton("Say dialogue");

        btn.addActionListener(e -> {
                String selected = optionList.getSelectedValue();
                dialogueDialog.dispose();
                int delayTimer = 20;

                if(selected == redOption){
                    delayTimer = 5;
                }  else {
                    delayTimer = 20;
                } 
                
                Typewriter typewriter = new Typewriter(textArea);

                currentStory.handleChoice(selected);
                typewriter.typeText("\n" + selected + "...", Color.black, delayTimer);
            });
        dialogueDialog.add(new JScrollPane(optionList));
        dialogueDialog.add(btn, BorderLayout.SOUTH);
        dialogueDialog.pack();
        dialogueDialog.setSize(500,200);
        dialogueDialog.setLocationRelativeTo(null);
        dialogueDialog.setResizable(false);
        dialogueDialog.setVisible(true);

    }

    private void showSaveLoadMenu() {
        JDialog saveDialog = new JDialog(this, "Save System", true);
        saveDialog.setLayout(new GridLayout(2, 1));
        
        JButton saveBtn = new JButton("Save Game");
        saveBtn.addActionListener(e -> {
            // Connect to your SaveManager class
            textArea.append("\nGame saved!");
            saveDialog.dispose();

            Typewriter typewriter = new Typewriter(textArea);
            typewriter.typeText("\nSaved Game...", Color.black, 20);
        });
        
        JButton loadBtn = new JButton("Load Game");
        loadBtn.addActionListener(e -> {
            // Connect to SaveManager.loadGame()
            textArea.append("\nGame loaded!");
            saveDialog.dispose();

            Typewriter typewriter = new Typewriter(textArea);
            typewriter.typeText("\nGame Loaded!", Color.black, 20);

        });
        
        saveDialog.add(saveBtn);
        saveDialog.add(loadBtn);
        saveDialog.setSize(200, 150);
        saveDialog.setLocationRelativeTo(null);
        saveDialog.setVisible(true);
    }

    private void styleButton(JButton button, Font font, Dimension size, Color bgColor) {
        button.setFont(font);
        button.setPreferredSize(size);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false); 
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }
    
    // Font textOutput = new Font("Arial", Font.BOLD, 16);

    // private void textOutput(String textMessage, Font font, Dimension size, Color bgColor) {
    //     button.setFont(font);
    //     button.setPreferredSize(size);
    //     button.setBackground(bgColor);
    //     button.setForeground(Color.WHITE);
    //     button.setFocusPainted(false); 
    //     button.setBorder(BorderFactory.createRaisedBevelBorder());
    // }
}