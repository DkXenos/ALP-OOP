import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Start the game with selected storyline
        SwingUtilities.invokeLater(() -> {
            StartMenu s = new StartMenu();
            s.setVisible(true);
        });
    }
}