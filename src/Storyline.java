public abstract class Storyline {
    protected GameUI ui;
    protected GameState state;

    public Storyline(GameUI ui, GameState state) {
        this.ui = ui;
        this.state = state;
    }

    public abstract void startStory();
    public abstract void handleChoice(String selectedOption); 
    public abstract String[] getCurrentChoices();
}