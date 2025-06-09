import java.io.*;
import java.sql.*;
import java.util.HashMap;

public class SaveManager {

    // Static initialization block - runs when class is first loaded
    // This ensures the SQLite JDBC driver is available before any database operations
    static {
        try {
            // Class.forName() - Dynamically loads and registers the SQLite JDBC driver
            // "org.sqlite.JDBC" - Fully qualified class name of SQLite driver
            // This step is required for JDBC to know how to handle "jdbc:sqlite:" URLs
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // Error handling if SQLite JAR file is missing from classpath
            System.err.println("SQLite JDBC driver not found. Please add the driver to your project.");
        }
    }

    // Creates and returns a database connection
    // Private method used internally by other methods in this class
    private static Connection getConnection() throws SQLException {
        // DriverManager.getConnection() - Factory method that creates database connections
        // "jdbc:sqlite:savegame.db" breakdown:
        //   - "jdbc:" - Java Database Connectivity protocol
        //   - "sqlite:" - Tells JDBC to use SQLite driver (loaded above)
        //   - "savegame.db" - Database file name (created if doesn't exist)
        return DriverManager.getConnection("jdbc:sqlite:savegame.db");
    }

    // Saves game data to specified save slot in database
    public static void saveGame(SaveData data, int slot) {
        createTableIfNotExists();  // Ensure saves table exists before inserting
        
        // try-with-resources pattern ensures proper resource cleanup
        try (Connection conn = getConnection();  // Get database connection
             PreparedStatement pstmt = conn.prepareStatement( //preparedstatement itu kaya safe way to run sql command, spy ga kena injectionattack, ? juga bisa kesiisi saftely
                 // INSERT OR REPLACE - SQLite specific command
                 // If slot exists: UPDATE the existing record
                 // If slot doesn't exist: INSERT new record
                 // This prevents duplicate slot numbers
                 "INSERT OR REPLACE INTO saves (slot, data) VALUES (?, ?)"
             )) {
            
            // Set parameters for the prepared statement
            pstmt.setInt(1, slot);                    // ? position 1: save slot number
            pstmt.setBytes(2, serialize(data));       // ? position 2: serialized game data as bytes
            
            // executeUpdate() - Execute the INSERT/REPLACE command
            // Returns number of rows affected (should be 1 for successful save)
            pstmt.executeUpdate();
            
            // Save inventory data to separate inventory table using InventoryDBManager
            // This maintains data normalization (separates complex inventory from main save data)
            InventoryDBManager.saveInventory(slot, data.inventoryQuantities);
            
            System.out.println("Game saved to slot " + slot);
        } catch (Exception e) {  // Catch both SQL and serialization exceptions
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    // Loads game data from specified save slot
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
                
                // Signal to stop all running timers before loading
                data.shouldCleanupTimers = true;
                
                return data;
            }
        } catch (Exception e) {
            System.err.println("Failed to load game: " + e.getMessage());
        }
        return null;
    }

    // Creates the main saves table if it doesn't exist
    // Private method called by save/load operations
    private static void createTableIfNotExists() {
        // try-with-resources for proper resource management
        try (Connection conn = getConnection();      // Get database connection
             Statement stmt = conn.createStatement()) {  // Create statement for SQL execution
            
            // executeUpdate() - Execute DDL (Data Definition Language) command
            stmt.executeUpdate(
                // CREATE TABLE IF NOT EXISTS - Only create if table doesn't exist
                "CREATE TABLE IF NOT EXISTS saves (" +
                    // slot INTEGER PRIMARY KEY - Save slot number, unique identifier
                    // PRIMARY KEY ensures no duplicate slots and creates index for fast lookups
                    "slot INTEGER PRIMARY KEY, " + 
                    // data BLOB NOT NULL - Binary data column for serialized SaveData
                    // BLOB = Binary Large Object, can store any binary data
                    // NOT NULL = This column must have a value
                    "data BLOB NOT NULL)");
        } catch (SQLException e) {  // Handle table creation errors
            System.err.println("Failed to create saves table: " + e.getMessage());
        }
    }

    // Gets display description for save slot (used in save/load menu)
    public static String getSlotStage(int slot) {
        createTableIfNotExists();  // Ensure table exists before querying
        
        // try-with-resources for connection management
        try (Connection conn = getConnection();  // Get database connection
             PreparedStatement pstmt = conn.prepareStatement(
                     // SELECT query to get save data for stage information
                     "SELECT data FROM saves WHERE slot = ?"
             )) {
            
            // Set slot parameter
            pstmt.setInt(1, slot);
            
            // Execute query and get results
            ResultSet rs = pstmt.executeQuery();
            
            // Check if save slot has data
            if (rs.next()) {  // If row exists
                // Extract serialized data
                byte[] saveBytes = rs.getBytes("data");
                
                // Deserialize to get SaveData object
                SaveData data = (SaveData) deserialize(saveBytes);
                
                // Return formatted stage description
                return "Stage " + data.getDialogueState();
            }
        } catch (Exception e) {
            // Ignore errors for empty slots (expected behavior)
            // This allows the method to return "Empty" for non-existent saves
        }
        return "Empty";  // Return if slot has no save data
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