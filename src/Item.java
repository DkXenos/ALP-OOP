import java.io.Serializable;

// Making Item Serializable if we decide to serialize Item objects directly,
// though current plan is to serialize names/quantities and reconstruct.
// For now, let's make it Serializable as a good practice for potential future use.
public abstract class Item implements Serializable {
    protected String itemName;
    protected String description;

    public Item(String itemName, String description) {
        this.itemName = itemName;
        this.description = description;
    }

    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Defines the action to be taken when the item is used.
     * @param state The current game state, to allow modification of stats or flags.
     * @param ui The game UI, to display messages.
     * @param playerName The name of the player using the item.
     */
    public abstract void applyEffect(GameState state, GameUI ui, String playerName);
}
