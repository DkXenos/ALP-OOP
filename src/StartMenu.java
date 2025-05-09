import java.awt.*;
import java.io.InputStream;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StartMenu extends JFrame{
    private JButton startButton, saveFileButton;
    private Font customFont; // Declare font as a field

    public StartMenu(){
         try {
            InputStream is = getClass().getResourceAsStream("/Resources/Fonts/lightes/Lightes.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(64f);
             
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.BOLD, 48);
        }

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(customFont);

        setTitle("Chapters of a Lost Summer");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false); // Transparent background

        // Title Label
        JLabel titleLabel = new JLabel("Chapters of a Lost Summer", SwingConstants.CENTER);
        titleLabel.setFont(customFont); 
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Horizontal centering

        // Description Label
        JLabel descLabel = new JLabel("Made by: Jason Tio & Eileen Cynthia Mark", SwingConstants.CENTER);
        descLabel.setFont(customFont.deriveFont(24f)); // Smaller size
        descLabel.setForeground(new Color(117, 8, 81));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add vertical spacing
        titlePanel.add(Box.createVerticalGlue()); // Pushes content to center
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 20))); // 20px gap
        titlePanel.add(descLabel);
        titlePanel.add(Box.createVerticalGlue());

        // Add to frame
        add(titlePanel, BorderLayout.CENTER);


        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(200, 50); // Width, Height


        JPanel buttonContainer = new JPanel(new BorderLayout());
        buttonContainer.setBorder(new EmptyBorder(70, 20, 20, 20)); // Padding

        JPanel buttonPanel = new JPanel(new GridBagLayout()); // Centers content
        buttonPanel.setOpaque(false); // Transparent background

        // Start Button
        startButton = new JButton("Start Game");
        styleButton(startButton, buttonFont, buttonSize, new Color(100, 200, 100)); // Green

        // Save Button
        saveFileButton = new JButton("Save Files");
        styleButton(saveFileButton, buttonFont, buttonSize, new Color(100, 150, 200));

        // Add buttons with constraints to center them
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Button margins
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(startButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(saveFileButton, gbc);

        buttonContainer.add(buttonPanel, BorderLayout.CENTER);
        add(buttonContainer, BorderLayout.SOUTH);

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
