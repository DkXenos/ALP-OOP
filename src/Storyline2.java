public class Storyline2 extends Storyline {
    public Storyline2(GameUI ui, GameState state) {
        super(ui, state);
    }
    
    @Override
    public void startStory() {
        ui.textArea.append("\n[Melody of a Burning Leaf]");
        ui.textArea.append("\n...");
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