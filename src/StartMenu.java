
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StartMenu extends JFrame{
    private JTextArea textArea;
    private JButton startButton, saveFileButton;


    public StartMenu(){
        
        setTitle("Text Adventure");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("Welcome to the game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48)); // Large font
        titleLabel.setForeground(new Color(50, 100, 150)); // Custom color
        add(titleLabel, BorderLayout.CENTER); // Center position



        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10)); // Added gaps between buttons
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding around panel
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(200, 50); // Width, Height

        // Start Button
        startButton = new JButton("Start Game");
        styleButton(startButton, buttonFont, buttonSize, new Color(100, 200, 100)); // Green

        // Save Button
        saveFileButton = new JButton("Save Files");
        styleButton(saveFileButton, buttonFont, buttonSize, new Color(100, 150, 200)); // Blue

        buttonPanel.add(startButton);
        buttonPanel.add(saveFileButton);
        add(buttonPanel, BorderLayout.SOUTH);

        startButton.addActionListener(e -> startGame());

    }
    private void styleButton(JButton button, Font font, Dimension size, Color bgColor) {
        button.setFont(font);
        button.setPreferredSize(size);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false); // Removes the dotted border on click
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }


    public void startGame(){
        GameUI gameUI = new GameUI();
        gameUI.setVisible(true);
        this.dispose();
    }
    
}
