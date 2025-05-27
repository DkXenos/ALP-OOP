import java.awt.*;
import java.awt.event.MouseAdapter; // Add for exception handling
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*; // For hover effects
import javax.swing.border.EmptyBorder;  // For hover effects
import javax.swing.border.LineBorder; // For flatter button border


// Inner class for drawing a background image (if you added it from previous suggestion)
class ImagePanel extends JPanel {
    private Image backgroundImage;

    public ImagePanel(String imagePath) {
        try {
            InputStream is = getClass().getResourceAsStream(imagePath);
            if (is == null) {
                System.err.println("Background image not found: " + imagePath);
                backgroundImage = null;
            } else {
                backgroundImage = ImageIO.read(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
            backgroundImage = null;
        }
        setLayout(new BorderLayout()); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}


public class StartMenu extends JFrame{
    private JButton startButton, loadGameButton; // Renamed saveFileButton
    private Font customFont;

    public StartMenu(){
         try {
            InputStream is = getClass().getResourceAsStream("/Resources/Fonts/lightes/Lightes.otf");
            // Add null check for resource stream
            if (is == null) throw new IOException("Font resource not found: /Resources/Fonts/lightes/Lightes.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(64f);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.BOLD, 48); // Fallback font
        }

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(customFont);

        setTitle("Chapters of a Lost Summer");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Assuming ImagePanel is used as content pane from previous suggestion
        ImagePanel backgroundPanel = new ImagePanel("/Resources/Assets/start_menu_background.jpg"); // Ensure path is correct
        setContentPane(backgroundPanel);


        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false); 

        JLabel titleLabel = new JLabel("Chapters of a Lost Summer", SwingConstants.CENTER);
        titleLabel.setFont(customFont); 
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("Made by: Jason Tio & Eileen Cynthia Mark", SwingConstants.CENTER);
        descLabel.setFont(customFont.deriveFont(Font.PLAIN, 24f)); 
        descLabel.setForeground(new Color(220, 220, 220));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(Box.createVerticalGlue()); 
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        titlePanel.add(descLabel);
        titlePanel.add(Box.createVerticalGlue());

        backgroundPanel.add(titlePanel, BorderLayout.CENTER);


        Font buttonFont = new Font("SansSerif", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(200, 50); 


        JPanel buttonContainer = new JPanel(new BorderLayout());
        buttonContainer.setOpaque(false);
        buttonContainer.setBorder(new EmptyBorder(20, 20, 50, 20));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        startButton = new JButton("Start Game");
        styleButton(startButton, buttonFont, buttonSize, new Color(50, 50, 50), new Color(0, 150, 136)); 

        loadGameButton = new JButton("Load Game"); // Changed from saveFileButton
        styleButton(loadGameButton, buttonFont, buttonSize, new Color(50, 50, 50), new Color(0, 100, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(startButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(loadGameButton, gbc);

        buttonContainer.add(buttonPanel, BorderLayout.CENTER);
        backgroundPanel.add(buttonContainer, BorderLayout.SOUTH);

        startButton.addActionListener(e -> startGame(null)); // Pass null for new game
        loadGameButton.addActionListener(e -> {
            SaveData saveData = SaveManager.loadGame();
            if (saveData != null) {
                startGame(saveData); // Pass loaded data
            } else {
                JOptionPane.showMessageDialog(this, "No save file found or error loading.", "Load Game", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void styleButton(JButton button, Font font, Dimension size, Color bgColor, Color hoverColor) {
        button.setFont(font);
        button.setPreferredSize(size);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(Color.DARK_GRAY, 1));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(hoverColor);
                button.setBorder(new LineBorder(hoverColor.brighter(), 1));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
                button.setBorder(new LineBorder(Color.DARK_GRAY, 1));
            }
        });
    }

    // Modified startGame to accept SaveData
    public void startGame(SaveData saveDataToLoad){
        GameUI gameUI = new GameUI();
        if (saveDataToLoad != null) {
            gameUI.applySaveData(saveDataToLoad);
        } else {
            // Start a new game (e.g., storyline 1 by default)
            
            gameUI.startGame(2); // Default to storyline 1 for a new game
        }
        gameUI.setVisible(true);
        this.dispose();
    }
}
