import java.awt.*;
import javax.swing.*;

public class LoadingScreen extends JWindow {

    public LoadingScreen(String gifPath, int durationMs, String sfxPath) { // Added sfxPath parameter
        ImagePanel imagePanel = new ImagePanel(gifPath);
        getContentPane().add(imagePanel, BorderLayout.CENTER);

        setSize(800, 600); 
        setLocationRelativeTo(null);
        
        // Play SFX - set loop to false
        if (sfxPath != null && !sfxPath.isEmpty()) {
            AudioManager.getInstance().playMusic(sfxPath, false); 
        }
        
        setVisible(true); // Show after potentially starting audio

        Timer timer = new Timer(durationMs, e -> {
            // Stop the loading screen SFX before disposing
            AudioManager.getInstance().stopMusic(); 
            
            this.dispose(); 
            SwingUtilities.invokeLater(() -> {
                StartMenu startMenu = new StartMenu();
                startMenu.setVisible(true);
            });
        });
        timer.setRepeats(false); 
        timer.start();
    }

    // You might need to copy or ensure visibility of ImagePanel if it's not a public top-level class.
    // From your provided StartMenu.java, ImagePanel is a package-private class in the same package.
    // If Main.java is in a different package, ImagePanel would need to be public.
    // For simplicity, let's assume they are in the same default package or ImagePanel is made public.
    // If ImagePanel is defined within StartMenu.java as a non-static inner or local class,
    // you'd need to extract it to its own file or make it a static nested class.
    // Given your file structure, ImagePanel is defined in StartMenu.java but not as an inner class,
    // it's a separate class in the same file. This is unusual but might compile.
    // It's better practice to have ImagePanel in its own ImagePanel.java file.

    // For this to work cleanly, ImagePanel should ideally be in its own file:
    // src/ImagePanel.java
    /*
    import javax.swing.*;
    import java.awt.*;
    import java.net.URL;

    public class ImagePanel extends JPanel {
        private ImageIcon backgroundImageIcon;

        public ImagePanel(String imagePath) {
            try {
                URL imgUrl = getClass().getResource(imagePath);
                if (imgUrl == null) {
                    System.err.println("Background image not found: " + imagePath);
                    backgroundImageIcon = null;
                } else {
                    backgroundImageIcon = new ImageIcon(imgUrl);
                }
            } catch (Exception e) {
                System.err.println("Error loading background image: " + imagePath);
                e.printStackTrace();
                backgroundImageIcon = null;
            }
            setLayout(new BorderLayout()); // Default layout
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImageIcon != null && backgroundImageIcon.getImage() != null) {
                g.drawImage(backgroundImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(Color.BLACK); // Fallback color
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.WHITE);
                g.drawString("Loading...", 10, 20);
            }
        }
    }
    */
}
