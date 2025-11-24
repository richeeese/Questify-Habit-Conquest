package main;

// Import necessary classes from other packages
import models.Player;
import logic.SaveManager;
import logic.TaskManager;
import logic.GameEngine;
import ui.ConsoleMenu;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Use a temporary scanner for initial setup
        Scanner setupScanner = new Scanner(System.in);

        System.out.println("======================================");
        System.out.println("  W E L C O M E  T O  Q U E S T I F Y ");
        System.out.println("======================================");
        System.out.print("Enter your Hero's Name: ");
        String name = setupScanner.nextLine();

        // 1. Initialize Core Models and Logic
        System.out.println("\nInitializing game world...");
        Player hero = new Player(name);
        TaskManager taskManager = new TaskManager(hero);
        GameEngine gameEngine = new GameEngine(hero, taskManager);

        // 2. Initialize UI
        ConsoleMenu menu = new ConsoleMenu(gameEngine);

        // 3. Start the Game Loop
        System.out.println("\nStarting menu...");
        menu.startMenu();

        // Clean up scanner resource
        setupScanner.close();
    }
}