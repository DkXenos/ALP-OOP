import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;

public class Typewriter {
    private Timer timer;
    private JTextArea textArea;
    
    private static class Message {
        String text;
        Color color;
        int currentCharIndex;

        Message(String text, Color color) {
            this.text = text;
            this.color = color;
            this.currentCharIndex = 0;
        }
    }

    private Queue<Message> messageQueue = new LinkedList<>();
    private Message currentMessage = null;
    private int currentDelayMs = 20;
    
    public Typewriter(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void typeText(String text, Color color, int delayMs) {
        messageQueue.offer(new Message(text, color));
        this.currentDelayMs = delayMs;
        
        if (timer == null) {
            timer = new Timer(this.currentDelayMs, e -> processQueue());
            timer.setInitialDelay(0);
        } else {
            timer.setDelay(30);
        }
        
        if (!timer.isRunning() && !messageQueue.isEmpty()) {
            if (currentMessage == null) {
                 processQueue();
            }
            if (!messageQueue.isEmpty() || (currentMessage != null && currentMessage.currentCharIndex < currentMessage.text.length())) {
                 timer.start();
            }
        }
    }
    
    private void processQueue() {
        if (currentMessage == null || currentMessage.currentCharIndex >= currentMessage.text.length()) {
            currentMessage = messageQueue.poll();
            if (currentMessage == null) {
                if (timer != null) {
                    timer.stop();
                }
                return;
            }
        }

        textArea.setForeground(currentMessage.color);
        textArea.append(String.valueOf(currentMessage.text.charAt(currentMessage.currentCharIndex)));
        currentMessage.currentCharIndex++;

        if (currentMessage.currentCharIndex >= currentMessage.text.length() && messageQueue.isEmpty()) {
            currentMessage = null;
            if (timer != null) {
                timer.stop();
            }
        }
    }

    public void stopAndClearQueue() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        messageQueue.clear();
        currentMessage = null;
    }
}