import java.io.*;

public class SaveManager {
    private static final String SAVE_FILE_PATH = "savegame.dat"; // Saves in the project root

    public static void saveGame(SaveData data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_PATH))) {
            oos.writeObject(data);
            System.out.println("Game saved successfully to " + SAVE_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
            // Optionally, show an error message to the user via UI
        }
    }

    public static SaveData loadGame() {
        File saveFile = new File(SAVE_FILE_PATH);
        if (!saveFile.exists()) {
            System.out.println("No save file found at " + SAVE_FILE_PATH);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE_PATH))) {
            SaveData data = (SaveData) ois.readObject();
            System.out.println("Game loaded successfully from " + SAVE_FILE_PATH);
            return data;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}