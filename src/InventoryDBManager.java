import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class InventoryDBManager {
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:savegame.db");
    }

    public static void createInventoryTableIfNotExists() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS inventory (" +
                "slot INTEGER, " +
                "item_name TEXT, " +
                "quantity INTEGER, " +
                "PRIMARY KEY (slot, item_name))"
            );
        } catch (SQLException e) {
            System.err.println("Failed to create inventory table: " + e.getMessage());
        }
    }

    public static void saveInventory(int slot, Map<String, Integer> inventory) {
        createInventoryTableIfNotExists();
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement deleteStmt = conn.prepareStatement(
                    "DELETE FROM inventory WHERE slot = ?")) {
                deleteStmt.setInt(1, slot);
                deleteStmt.executeUpdate();
            }
            try (PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO inventory (slot, item_name, quantity) VALUES (?, ?, ?)")) {
                for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                    insertStmt.setInt(1, slot);
                    insertStmt.setString(2, entry.getKey());
                    insertStmt.setInt(3, entry.getValue());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
            conn.commit();
        } catch (SQLException e) {
            System.err.println("Failed to save inventory: " + e.getMessage());
        }
    }

    public static Map<String, Integer> loadInventory(int slot) {
        createInventoryTableIfNotExists();
        Map<String, Integer> inventory = new HashMap<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT item_name, quantity FROM inventory WHERE slot = ?")) {
            pstmt.setInt(1, slot);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                inventory.put(rs.getString("item_name"), rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            System.err.println("Failed to load inventory: " + e.getMessage());
        }
        return inventory;
    }
}