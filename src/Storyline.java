import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public abstract class Storyline {
    protected GameUI ui;
    protected GameState state;

    public Storyline(GameUI ui, GameState state) {
        this.ui = ui;
        this.state = state;
    }

    // --- Abstract methods your concrete storylines implement ---
    public abstract void handleChoice(int choice); 
    public abstract String[] getCurrentChoices();
    public abstract int getDialogueState(); 
    public abstract void setDialogueState(int state); 
    public abstract void showDialoguePublic(int stage); 
    
    // Override this in subclasses to handle cleanup before starting
    public void startStory() {
        // Default implementation - subclasses can override
    }
}