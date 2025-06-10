import java.awt.*;
import javax.swing.*;

public class LoadingScreen extends JWindow {

    public LoadingScreen(int storylineId, int durationMs) {
        String gifPath = getCutsceneGifPath(storylineId);
        String sfxPath = getCutsceneSfxPath(storylineId);
        
        ImagePanel imagePanel = new ImagePanel(gifPath);
        getContentPane().add(imagePanel, BorderLayout.CENTER);

        setSize(800, 600); 
        setLocationRelativeTo(null);
        
        if (sfxPath != null && !sfxPath.isEmpty()) {
            AudioManager.getInstance().playMusic(sfxPath, false); 
        }
        
        setVisible(true);

        Timer timer = new Timer(durationMs, e -> {
            AudioManager.getInstance().stopMusic(); 
            
            this.dispose(); 
            SwingUtilities.invokeLater(() -> {
                if (storylineId == 4) {
                    StartMenu startMenu = new StartMenu();
                    startMenu.setVisible(true);
                } else {
                    GameUI gameUI = new GameUI();
                    gameUI.startGame(storylineId);
                    gameUI.setVisible(true);
                }
            });
        });
        timer.setRepeats(false); 
        timer.start();
    }
    
    private String getCutsceneGifPath(int storylineId) {
        switch (storylineId) {
            case 4:
                return "./Resources/Assets/opening-screen.gif";
            case 1:
                return "./Resources/Assets/lulaby-opening.gif";
            case 2:
                return "./Resources/Assets/euphoria-opening.gif";
            case 3:
                return "./Resources/Assets/burning-opening.gif";
            default:
                return "./Resources/Assets/default-cutscene.gif";
        }
    }
    
    private String getCutsceneSfxPath(int storylineId) {
        switch (storylineId) {
            case 4:
                return "./Resources/Audio/loading-sfx.wav";
            case 1:
                return "./Resources/Audio/burning-audio.wav";
            case 2:
                return "./Resources/Audio/euphoria-opening.wav";
            case 3:
                return "./Resources/Audio/burning-audio.wav";
            default:
                return "./Resources/Audio/default-cutscene.wav";
        }
    }
}
