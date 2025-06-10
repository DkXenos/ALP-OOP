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
        setLayout(new BorderLayout());
    }

    public ImagePanel() {
        this(null);
    }

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
        repaint();
    }

    public ImageIcon getImageIcon() {
        return backgroundImageIcon;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImageIcon != null && backgroundImageIcon.getImage() != null) {
            g.drawImage(backgroundImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}