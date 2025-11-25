package logic;

import models.Player;
import java.io.*;

public class SaveManager {
    private static final String FILE_NAME = "questify_save.dat";

    // saving method
    public static void saveGame(Player player) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(player);
            System.out.println("[System] Game saved successfully!");
        } catch (IOException e) {
            System.err.println("[Error] Failed to save game: " + e.getMessage());
        }
    }
    
    // loading method
    public static Player loadGame() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return null; // no save file; create new game
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Player loadedPlayer = (Player) in.readObject();
            System.out.println("[System] Save file found! Continuing conquest...");
            return loadedPlayer;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[Error] Save file corrupted or unreadable. Starting a new conquest...");
            return null;
        }
    }
}