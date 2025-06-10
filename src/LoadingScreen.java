import java.awt.*;
import javax.swing.*;

public class LoadingScreen extends JWindow {

    public LoadingScreen(int storylineId, int durationMs) {
        // Determine cutscene assets based on storyline
        String gifPath = getCutsceneGifPath(storylineId);
        String sfxPath = getCutsceneSfxPath(storylineId);
        
        ImagePanel imagePanel = new ImagePanel(gifPath);
        getContentPane().add(imagePanel, BorderLayout.CENTER);

        setSize(800, 600); 
        setLocationRelativeTo(null);
        
        // Play storyline-specific cutscene SFX
        if (sfxPath != null && !sfxPath.isEmpty()) {
            AudioManager.getInstance().playMusic(sfxPath, false); 
        }
        
        setVisible(true);

        Timer timer = new Timer(durationMs, e -> {
            // Stop the cutscene audio before disposing
            AudioManager.getInstance().stopMusic(); 
            
            this.dispose(); 
            SwingUtilities.invokeLater(() -> {
                if (storylineId == 4) {
                    // Initial loading - go to StartMenu
                    StartMenu startMenu = new StartMenu();
                    startMenu.setVisible(true);
                } else {
                    // Storyline cutscene - start the specific game
                    GameUI gameUI = new GameUI();
                    gameUI.startGame(storylineId);
                    gameUI.setVisible(true);
                }
            });
        });
        timer.setRepeats(false); 
        timer.start();
    }
    
    // Method to get the appropriate cutscene GIF based on storyline
    private String getCutsceneGifPath(int storylineId) {
        switch (storylineId) {
            case 4:
                return "./Resources/Assets/opening-screen.gif"; // Initial app loading
            case 1:
                return "./Resources/Assets/lulaby-opening.gif"; // Cutscene for Storyline1
            case 2:
                return "./Resources/Assets/euphoria-opening.gif"; // Cutscene for Storyline2
            case 3:
                return "./Resources/Assets/burning-opening.gif"; // Cutscene for Storyline3
            default:
                return "./Resources/Assets/default-cutscene.gif"; // Fallback
        }
    }
    
    // Method to get the appropriate cutscene audio based on storyline
    private String getCutsceneSfxPath(int storylineId) {
        switch (storylineId) {
            case 4:
                return "./Resources/Audio/loading-sfx.wav"; // Initial app loading audio
            case 1:
                return "./Resources/Audio/burning-audio.wav"; // Cutscene audio for Storyline1
            case 2:
                return "./Resources/Audio/euphoria-opening.wav"; // Cutscene audio for Storyline2
            case 3:
                return "./Resources/Audio/burning-audio.wav"; // Cutscene audio for Storyline3
            default:
                return "./Resources/Audio/default-cutscene.wav"; // Fallback
        }
    }
}
