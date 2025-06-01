import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Optional: Set a look and feel
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            // Path to your loading screen GIF
            String loadingGifPath = "/Resources/Assets/opening-screen.gif"; // CHANGE THIS to your actual GIF path
            String loadingSfxPath = "/Resources/Audio/loading-sfx.wav"; // ADD PATH TO YOUR SFX
            int loadingDurationMs = 3000; // 3 seconds

            new LoadingScreen(loadingGifPath, loadingDurationMs, loadingSfxPath);
            // The LoadingScreen will handle opening the StartMenu after the duration
        });
    }
}