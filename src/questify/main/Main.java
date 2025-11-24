package main;

import logic.GameEngine;
import ui.ConsoleMenu;

public class Main {

    public static void main(String[] args) {
        System.out.println("Initializing Questify Systems...");

        // 1. Create the Logic Layer (The Brains)
        // This sets up the Player, TaskManager, and internal math.
        GameEngine engine = new GameEngine();

        // 2. Create the UI Layer (The Face)
        // We pass the 'engine' to the menu so the UI can talk to the Logic.
        ConsoleMenu menu = new ConsoleMenu(engine);

        // 3. Start the Application Loop
        // This gives control to the ConsoleMenu, which will loop until the user exits.
        menu.startMenu();

        System.out.println("System Shutdown. See you next time!");
    }
}