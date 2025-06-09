import java.sql.*;  // Import all SQL-related classes for database operations
import java.util.HashMap;
import java.util.Map;

public class InventoryDBManager {
    // Creates a connection to the SQLite database file
    // This method establishes the communication bridge between Java and the database
    private static Connection getConnection() throws SQLException {
        // DriverManager.getConnection() - Creates a connection to the database
        // "jdbc:sqlite:" - This is the JDBC URL protocol telling Java to use SQLite driver
        // "savegame.db" - The actual database file name that will be created/accessed
        // If file doesn't exist, SQLite will create it automatically
        return DriverManager.getConnection("jdbc:sqlite:savegame.db");
    }

    // Creates the inventory table structure if it doesn't already exist
    public static void createInventoryTableIfNotExists() {
        // try-with-resources: Automatically closes Connection and Statement when done
        // This prevents memory leaks and ensures proper resource cleanup
        try (Connection conn = getConnection();  // Get database connection
             Statement stmt = conn.createStatement()) {  // Create statement object for executing SQL
            
            // executeUpdate() - Used for SQL commands that modify the database (CREATE, INSERT, UPDATE, DELETE)
            // Returns number of rows affected (0 for CREATE TABLE)
            stmt.executeUpdate(
                // SQL DDL (Data Definition Language) command to create table structure
                "CREATE TABLE IF NOT EXISTS inventory (" +
                    // slot INTEGER - Column to store save slot number (1, 2, 3, etc.)
                    "slot INTEGER, " +
                    // item_name TEXT - Column to store item names like "XANAX", "Pills"
                    "item_name TEXT, " +
                    // quantity INTEGER - Column to store how many of each item
                    "quantity INTEGER, " +
                    // PRIMARY KEY (slot, item_name) - Composite key preventing duplicate slot+item combinations
                    // This means slot 1 can only have one entry for "XANAX"
                    "PRIMARY KEY (slot, item_name))"
            );
        } catch (SQLException e) {  // Catch any database-related errors
            // Print error message if table creation fails
            System.err.println("Failed to create inventory table: " + e.getMessage());
        }
    }

    // Saves all inventory items for a specific save slot
    public static void saveInventory(int slot, Map<String, Integer> inventory) {
        createInventoryTableIfNotExists();  // Ensure table exists before inserting data
        
        // try-with-resources automatically closes connection
        try (Connection conn = getConnection()) {
            // setAutoCommit(false) - Start transaction mode
            // This means all database changes are grouped together
            // Either ALL succeed or ALL fail (atomicity)
            conn.setAutoCommit(false);
            
            // First: Delete all existing inventory items for this slot
            // This ensures we don't have duplicate or outdated items
            try (PreparedStatement deleteStmt = conn.prepareStatement(
                    // DELETE command removes existing records
                    "DELETE FROM inventory WHERE slot = ?")) {
                // setInt(1, slot) - Replace first ? with the slot number
                // This prevents SQL injection attacks
                deleteStmt.setInt(1, slot);
                // executeUpdate() returns number of deleted rows
                deleteStmt.executeUpdate();
            }
            
            // Second: Insert all current inventory items
            try (PreparedStatement insertStmt = conn.prepareStatement(
                    // INSERT command adds new records to the table
                    "INSERT INTO inventory (slot, item_name, quantity) VALUES (?, ?, ?)")) {
                
                // Loop through each item in the inventory map
                for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                    // Set parameters for each item insertion
                    insertStmt.setInt(1, slot);              // ? position 1: save slot
                    insertStmt.setString(2, entry.getKey()); // ? position 2: item name
                    insertStmt.setInt(3, entry.getValue());  // ? position 3: quantity
                    
                    // addBatch() - Add this INSERT to a batch instead of executing immediately
                    // This is more efficient than executing each INSERT separately
                    insertStmt.addBatch();
                }
                // executeBatch() - Execute all batched INSERT statements at once
                // Returns array of update counts for each statement
                insertStmt.executeBatch();
            }
            // commit() - Make all changes permanent in the database
            // If any error occurred above, changes would be rolled back automatically
            conn.commit();
        } catch (SQLException e) {  // Catch any database errors
            System.err.println("Failed to save inventory: " + e.getMessage());
        }
    }

    // Loads all inventory items for a specific save slot
    public static Map<String, Integer> loadInventory(int slot) {
        createInventoryTableIfNotExists();  // Ensure table exists before querying
        
        // Create empty HashMap to store loaded inventory
        Map<String, Integer> inventory = new HashMap<>();
        
        // try-with-resources for automatic resource cleanup
        try (Connection conn = getConnection();  // Get database connection
             PreparedStatement pstmt = conn.prepareStatement(
                     // SELECT query retrieves data from database
                     // Only get item_name and quantity columns for specified slot
                     "SELECT item_name, quantity FROM inventory WHERE slot = ?")) {
            
            // Replace ? with actual slot number
            pstmt.setInt(1, slot);
            
            // executeQuery() - Used for SELECT statements that return data
            // Returns ResultSet containing query results
            ResultSet rs = pstmt.executeQuery();
            
            // while loop processes each row returned by the query
            while (rs.next()) {  // next() moves to next row, returns false when no more rows
                // rs.getString("column_name") - Get string value from specified column
                // rs.getInt("column_name") - Get integer value from specified column
                // put() adds the item to our inventory HashMap
                inventory.put(rs.getString("item_name"), rs.getInt("quantity"));
            }
        } catch (SQLException e) {  // Handle any database errors
            System.err.println("Failed to load inventory: " + e.getMessage());
        }
        
        // Return the loaded inventory (empty HashMap if slot has no items)
        return inventory;
    }
}