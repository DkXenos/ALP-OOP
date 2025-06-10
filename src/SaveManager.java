import java.io.*;
import java.sql.*;
import java.util.HashMap;

public class SaveManager {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found. Please add the driver to your project.");
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:savegame.db");
    }

    public static void saveGame(SaveData data, int slot) {
        createTableIfNotExists();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT OR REPLACE INTO saves (slot, data) VALUES (?, ?)"
             )) {
            
            pstmt.setInt(1, slot);
            pstmt.setBytes(2, serialize(data));
            
            pstmt.executeUpdate();
            
            InventoryDBManager.saveInventory(slot, data.inventoryQuantities);
            
            System.out.println("Game saved to slot " + slot);
        } catch (Exception e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    public static SaveData loadGame(int slot) {
        createTableIfNotExists();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT data FROM saves WHERE slot = ?"
             )) {
            
            pstmt.setInt(1, slot);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                byte[] saveBytes = rs.getBytes("data");
                SaveData data = (SaveData) deserialize(saveBytes);
                data.inventoryQuantities = new HashMap<>(InventoryDBManager.loadInventory(slot));
                
                data.shouldCleanupTimers = true;
                
                return data;
            }
        } catch (Exception e) {
            System.err.println("Failed to load game: " + e.getMessage());
        }
        return null;
    }

    private static void createTableIfNotExists() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS saves (" +
                    "slot INTEGER PRIMARY KEY, " + 
                    "data BLOB NOT NULL)");
        } catch (SQLException e) {
            System.err.println("Failed to create saves table: " + e.getMessage());
        }
    }

    public static String getSlotStage(int slot) {
        createTableIfNotExists();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT data FROM saves WHERE slot = ?"
             )) {
            
            pstmt.setInt(1, slot);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                byte[] saveBytes = rs.getBytes("data");
                
                SaveData data = (SaveData) deserialize(saveBytes);
                
                String storylineName = getStorylineName(data.storylineId);
                
                return storylineName + " - Stage " + data.getDialogueState();
            }
        } catch (Exception e) {
        }
        return "Empty";
    }
    
    private static String getStorylineName(int storylineId) {
        switch (storylineId) {
            case 1:
                return "Storyline 1";
            case 2:
                return "Storyline 2";
            case 3:
                return "Storyline 3";
            default:
                return "Unknown Story";
        }
    }

    private static byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        }
    }

    private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return ois.readObject();
        }
    }
}