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
            // Show initial loading screen that leads to StartMenu
            new LoadingScreen(4, 3000); 
        });
    }
}