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
    private int currentDelayMs = 20; // Default delay
    
    public Typewriter(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void typeText(String text, Color color, int delayMs) {
        messageQueue.offer(new Message(text, color));
        this.currentDelayMs = delayMs; // Use the delay from the most recent call for new starts
        
        if (timer == null) {
            timer = new Timer(this.currentDelayMs, e -> processQueue());
            timer.setInitialDelay(0); // Process first character immediately
        } else {
            timer.setDelay(this.currentDelayMs); // Ensure timer has the latest delay
        }
        
        if (!timer.isRunning() && !messageQueue.isEmpty()) {
            // If timer is not running and there's something to type, start it.
            // processQueue will be called by the timer's first tick.
            // Or, to ensure immediate start if currentMessage was null:
            if (currentMessage == null) {
                 processQueue(); // Manually call once to pick up the first message
            }
            if (!messageQueue.isEmpty() || (currentMessage != null && currentMessage.currentCharIndex < currentMessage.text.length())) {
                 timer.start();
            }
        }
    }
    
    private void processQueue() {
        if (currentMessage == null || currentMessage.currentCharIndex >= currentMessage.text.length()) {
            // Current message finished, or no message was being processed. Try to get the next one.
            currentMessage = messageQueue.poll();
            if (currentMessage == null) {
                // No more messages in the queue
                if (timer != null) {
                    timer.stop();
                }
                return;
            }
        }

        // Type the next character of the current message
        textArea.setForeground(currentMessage.color);
        textArea.append(String.valueOf(currentMessage.text.charAt(currentMessage.currentCharIndex)));
        currentMessage.currentCharIndex++;

        // If current message is now finished, and queue is empty, timer will stop on next check.
        // If queue is not empty, next tick will pick up currentMessage (if not finished) or poll next.
        if (currentMessage.currentCharIndex >= currentMessage.text.length() && messageQueue.isEmpty()) {
            currentMessage = null; // Clear current message
            if (timer != null) {
                timer.stop();
            }
        }
    }

    // Optional: Method to stop typing immediately and clear the queue
    public void stopAndClearQueue() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        messageQueue.clear();
        currentMessage = null;
        // You might want to clear any partially typed text from textArea if needed,
        // but usually, just stopping is enough.
    }
}