public class Storyline3 extends Storyline {
    public Storyline3(GameUI ui, GameState state) {
        super(ui, state);
    }
    
    @Override
    public void startStory() {
        ui.textArea.append("\n[Needle of Promise]");
        ui.textArea.append("\nOuch...");
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