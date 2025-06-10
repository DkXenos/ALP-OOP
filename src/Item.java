import java.io.Serializable;

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

    public abstract void applyEffect(GameState state, GameUI ui, String playerName);
}
