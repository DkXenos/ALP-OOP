import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ImagePanel extends JPanel {
    private ImageIcon backgroundImageIcon; // Use ImageIcon to handle GIFs

    public ImagePanel(String imagePath) {
        try {
            URL imgUrl = getClass().getResource(imagePath);
            if (imgUrl == null) {
                System.err.println("Background image not found: " + imagePath);
                backgroundImageIcon = null;
            } else {
                backgroundImageIcon = new ImageIcon(imgUrl);
            }
        } catch (Exception e) { // ImageIcon constructor doesn't throw checked IOExceptions
            System.err.println("Error loading background image: " + imagePath);
            e.printStackTrace();
            backgroundImageIcon = null;
        }
        setLayout(new BorderLayout()); // Default layout
    }

    // Constructor overload for no image (just a plain panel)
    public ImagePanel() {
        this(null);
    }

    // Method to change the image after construction
    public void setImage(String imagePath) {
        try {
            if (imagePath == null || imagePath.isEmpty()) {
                backgroundImageIcon = null;
            } else {
                URL imgUrl = getClass().getResource(imagePath);
                if (imgUrl == null) {
                    System.err.println("Background image not found: " + imagePath);
                    backgroundImageIcon = null;
                } else {
                    backgroundImageIcon = new ImageIcon(imgUrl);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading background image: " + imagePath);
            e.printStackTrace();
            backgroundImageIcon = null;
        }
        repaint(); // Trigger a repaint to show the new image
    }

    // Method to get the current ImageIcon (useful for checking if image loaded successfully)
    public ImageIcon getImageIcon() {
        return backgroundImageIcon;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImageIcon != null && backgroundImageIcon.getImage() != null) {
            // Draw the image, scaling it to the panel's size.
            // 'this' (the ImagePanel itself) acts as the ImageObserver,
            // which is necessary for animated GIFs to update and repaint.
            g.drawImage(backgroundImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        } else {
            // Fallback if image failed to load - you can customize this
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}