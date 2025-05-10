public class Storyline1 extends Storyline {
    public Storyline1(GameUI ui, GameState state) {
        super(ui, state);
    }
    
    @Override
    public void startStory() {
        ui.textArea.append("\n[Lullaby of Empty Bottles]");
        ui.textArea.append("\nIt begins...");
    }

    @Override
    public void handleChoice(String choice) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] getCurrentChoices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}