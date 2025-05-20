import java.awt.Color;
import javax.swing.*;

public class Typewriter {
    private Timer timer;
    private JTextArea textArea;
    private Color currentColor;
    private StringBuilder pendingText = new StringBuilder();
    
    public Typewriter(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void typeText(String text, Color color, int delayMs) {
        this.currentColor = color;
        pendingText.append(text);
        
        if (timer == null || !timer.isRunning()) {
            startTyping(delayMs);
        }
    }
    
    private void startTyping(int delayMs) {
        timer = new Timer(delayMs, e -> {
            if (pendingText.length() > 0) {
                textArea.setForeground(currentColor);
                textArea.append(String.valueOf(pendingText.charAt(0)));
                pendingText.deleteCharAt(0);
            } else {
                timer.stop();
            }
        });
        timer.start();
    }
}