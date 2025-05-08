import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Typewriter {
    private Timer timer;
    private String fullText;
    private int currentIndex = 0;
    private JTextArea textArea;

    public Typewriter(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void typeText(String text, int delayMs) {
        fullText = text;
        currentIndex = 0;
        textArea.setText(""); // Clear previous text

        timer = new Timer(delayMs, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < fullText.length()) {
                    textArea.append(String.valueOf(fullText.charAt(currentIndex)));
                    currentIndex++;
                } else {
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        timer.start();
    }
}