import java.io.*;
import java.sql.*;
import java.util.HashMap;

public class SaveManager {

    // load the SQLite JDBC driver when the class is loaded
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found. Please add the driver to your project.");
        }
    }

    //  Opens a connection to the savegame database.
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:savegame.db");
    }

   
    // Saves data to specified slot.
    public static void saveGame(SaveData data, int slot) {
        createTableIfNotExists();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT OR REPLACE INTO saves (slot, data) VALUES (?, ?)"
             )) {
            pstmt.setInt(1, slot);
            pstmt.setBytes(2, serialize(data));
            pstmt.executeUpdate();
            // Save inventory to inventory table
            InventoryDBManager.saveInventory(slot, data.inventoryQuantities);
            System.out.println("Game saved to slot " + slot);
        } catch (Exception e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    
    //  Loads  data from  specified slot.
   
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
            // Load inventory from inventory table
            data.inventoryQuantities = new HashMap<>(InventoryDBManager.loadInventory(slot));
            return data;
        }
    } catch (Exception e) {
        System.err.println("Failed to load game: " + e.getMessage());
    }
    return null;
    }

    // Creates the saves table if it doesn't exist.   
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

    
    //  ngasih desc for the saveslot
   
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
                return "Stage " + data.getDialogueState();
            }
        } catch (Exception e) {
            // Ignore errors for empty slots
        }
        return "Empty";
    }

   
    //  Serializes an object to a byte array.
         private static byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        }
    }

   
    //  Deserializes an object from a byte array.
     
    private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return ois.readObject();
        }
    }
}

// serialization itu untuk ngubah object saveData ke kaya format yg bisa disimpen, makanya diubah ke byte
// soale sqlite cuma bisa store basic type kaya int, string, blob, ga bisa store object
// kalo serialization itu berarti ngubah object ke byte array, spy bisa di store di databse typenya BLOB binary large object
// jadi habis load data,  derizalize itungubah byte back to java object, biar isa dipake di code