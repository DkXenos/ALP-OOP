import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; // For GameUI inventory display
import java.util.Map;    // For GameUI inventory display
import java.util.function.Supplier; // For item factories

public class GameState {
    private HashMap<String, Integer> stats = new HashMap<>();
    private HashMap<String, Boolean> flags = new HashMap<>();
    
    // Inventory: Store item prototypes and their quantities
    private Map<String, Item> itemPrototypes = new HashMap<>();
    private Map<String, Integer> inventoryQuantities = new HashMap<>();

    // Define keys for player stats
    public static final String PLAYER_HEALTH = "playerHealth";
    public static final String PLAYER_MAX_HEALTH = "playerMaxHealth";
    public static final String PLAYER_ATTACK = "playerAttack";
    public static final String PLAYER_WILLPOWER = "playerWillpower";

    // Item Factories for repopulating items on load
    private static final Map<String, Supplier<Item>> KNOWN_ITEM_FACTORIES = new HashMap<>();
    
    static {
        // Smoking items (Storyline3)
        String nicotineKey = "nicotineAddictionLevel";
        String willpowerKey = "playerWillpower";
        String socialStatusKey = "socialStatus";

        KNOWN_ITEM_FACTORIES.put("Cigarette", () -> new SmokingItem(
            "Cigarette", 
            "A standard, mass-produced cigarette.",
            "smokes a Cigarette. A brief, harsh buzz...",
            Map.of(nicotineKey, 2, PLAYER_MAX_HEALTH, -2)
        ));
        KNOWN_ITEM_FACTORIES.put("Vape Pen", () -> new SmokingItem(
            "Vape Pen", 
            "A sleek, modern vape pen. Produces flavored vapor.",
            "uses the Vape Pen. Smooth vapor, lingering desire...",
            Map.of(nicotineKey, 3, PLAYER_MAX_HEALTH, -3)
        ));
        KNOWN_ITEM_FACTORIES.put("Premium Cigarette", () -> new SmokingItem(
            "Premium Cigarette", 
            "A high-quality, expensive cigarette. Promises a richer experience.",
            "lights up the Premium Cigarette. It's strong, a noticeable kick.",
            Map.of(nicotineKey, 5, PLAYER_MAX_HEALTH, -5, PLAYER_HEALTH, -2, socialStatusKey, -1)
        ));

        // Drug items (Storyline2)
        String drugAddictionKey = "drugAddictionLevel";

        KNOWN_ITEM_FACTORIES.put("XANAX", () -> new DrugItem(
            "XANAX", 
            "Xanax is an anti-anxiety medication.",
            "takes a Xanax pill. Anxiety melts away, but reality blurs...",
            Map.of(drugAddictionKey, 2, PLAYER_MAX_HEALTH, -2)
        ));
        KNOWN_ITEM_FACTORIES.put("ENERGY_PILLS", () -> new DrugItem(
            "ENERGY_PILLS", 
            "Energy Pills are a stimulant drug.",
            "swallows an Energy Pill. A rush of artificial energy surges through...",
            Map.of(drugAddictionKey, 3, PLAYER_MAX_HEALTH, -3)
        ));
    }


    public GameState() {
        // Initialize default player stats
        setStat(PLAYER_MAX_HEALTH, 100);
        setStat(PLAYER_HEALTH, getStat(PLAYER_MAX_HEALTH));
        setStat(PLAYER_ATTACK, 10);
        setStat(PLAYER_WILLPOWER, 0);
    }

    public void setStat(String key, int value) {
        stats.put(key, value);
        // Ensure health doesn't exceed max_health or go below 0 immediately after setting
        if (key.equals(PLAYER_HEALTH)) {
            int currentHealth = stats.getOrDefault(key, 0);
            int maxHealth = stats.getOrDefault(PLAYER_MAX_HEALTH, currentHealth);
            stats.put(key, Math.min(Math.max(currentHealth, 0), maxHealth));
        }
    }

    public int getStat(String key) {
        return stats.getOrDefault(key, 0);
    }

    // Helper to adjust stats, e.g., taking damage or healing
    public void adjustStat(String key, int amount) {
        int currentValue = getStat(key);
        setStat(key, currentValue + amount);
    }

    // Get effective attack power (base attack + willpower bonus)
    public int getEffectiveAttack() {
        return getStat(PLAYER_ATTACK) + getStat(PLAYER_WILLPOWER);
    }

    public void setFlag(String key, boolean value) {
        flags.put(key, value);
    }

    public boolean getFlag(String key) {
        return flags.getOrDefault(key, false);
    }

    // New methods for save/load
    public HashMap<String, Integer> getAllStats() {
        return new HashMap<>(this.stats); // Return a copy
    }

    public void setAllStats(Map<String, Integer> newStats) {
        this.stats.clear();
        if (newStats != null) {
            this.stats.putAll(newStats);
        }
    }

    public HashMap<String, Boolean> getAllFlags() {
        return new HashMap<>(this.flags); // Return a copy
    }

    public void setAllFlags(Map<String, Boolean> newFlags) {
        this.flags.clear();
        if (newFlags != null) {
            this.flags.putAll(newFlags);
        }
    }

    // --- New Inventory Methods ---
    public void addItem(Item item, int quantity) {
        if (item == null || quantity <= 0) return;
        itemPrototypes.putIfAbsent(item.getItemName(), item);
        inventoryQuantities.put(item.getItemName(), inventoryQuantities.getOrDefault(item.getItemName(), 0) + quantity);
        System.out.println("Added " + quantity + " " + item.getItemName() + "(s). Total: " + inventoryQuantities.get(item.getItemName()));
    }

    public boolean consumeItem(String itemName) {
        if (inventoryQuantities.containsKey(itemName) && inventoryQuantities.get(itemName) > 0) {
            inventoryQuantities.put(itemName, inventoryQuantities.get(itemName) - 1);
            if (inventoryQuantities.get(itemName) == 0) {
                inventoryQuantities.remove(itemName);
            }
            System.out.println("Consumed 1 " + itemName + ".");
            return true;
        }
        System.out.println("Cannot consume " + itemName + ": Not enough quantity or item unknown.");
        return false;
    }
    
    // For removing items without using them (e.g., dropping, selling)
    public boolean removeItemCompletely(String itemName, int quantity) {
        int currentQuantity = inventoryQuantities.getOrDefault(itemName, 0);
        if (currentQuantity >= quantity) {
            inventoryQuantities.put(itemName, currentQuantity - quantity);
            if (inventoryQuantities.get(itemName) == 0) {
                inventoryQuantities.remove(itemName);
            }
            System.out.println("Removed " + quantity + " " + itemName + "(s) from inventory.");
            return true;
        }
        System.out.println("Not enough " + itemName + "(s) to remove.");
        return false;
    }

    public int getItemQuantity(String itemName) {
        return inventoryQuantities.getOrDefault(itemName, 0);
    }

    public Item getItemPrototype(String itemName) {
        return itemPrototypes.get(itemName);
    }

    // For displaying inventory in UI
    public List<String> getInventoryForDisplay() {
        List<String> displayList = new ArrayList<>();
        if (inventoryQuantities.isEmpty()) {
            displayList.add("Your inventory is empty.");
        } else {
            for (Map.Entry<String, Integer> entry : inventoryQuantities.entrySet()) {
                Item proto = itemPrototypes.get(entry.getKey());
                String desc = (proto != null) ? " (" + proto.getDescription() + ")" : "";
                displayList.add(entry.getKey() + " (x" + entry.getValue() + ")" + desc);
            }
        }
        return displayList;
    }
    
    public Map<String, Integer> getInventoryQuantities() { // For saving
        return new HashMap<>(this.inventoryQuantities);
    }

    public void setInventoryQuantities(Map<String, Integer> newQuantities) { // For loading
        this.inventoryQuantities.clear();
        this.itemPrototypes.clear(); // Clear old prototypes
        if (newQuantities != null) {
            this.inventoryQuantities.putAll(newQuantities);
            repopulateItemPrototypesFromQuantities();
        }
    }

    private void repopulateItemPrototypesFromQuantities() {
        if (inventoryQuantities == null) return;
        for (String itemName : inventoryQuantities.keySet()) {
            if (!itemPrototypes.containsKey(itemName)) {
                Supplier<Item> factory = KNOWN_ITEM_FACTORIES.get(itemName);
                if (factory != null) {
                    itemPrototypes.put(itemName, factory.get());
                } else {
                    System.err.println("Unknown item type during prototype repopulation: " + itemName);
                }
            }
        }
    }
}