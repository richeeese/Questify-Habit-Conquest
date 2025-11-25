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
        // important initializations
        Scanner setupScanner = new Scanner(System.in);
        Player hero;
        TaskManager taskManager;
        GameEngine gameEngine;
        ConsoleMenu menu;

        boolean isNewGame = false;

        // attempt to load existing game
        Player loadedPlayer = SaveManager.loadGame();
        if (loadedPlayer != null) {
            hero = loadedPlayer;
            System.out.println("=========================================================");
            System.out.println("\t   W E L C O M E  T O  Q U E S T I F Y ");
            System.out.println("=========================================================");
            System.out.println("Loaded existing game for hero: " + hero.getName());
        } else {
            System.out.println("=========================================================");
            System.out.println("\t   W E L C O M E  T O  Q U E S T I F Y ");
            System.out.println("=========================================================");
            isNewGame = true;

            System.out.print("Enter your Hero's Name: ");
            String name = setupScanner.nextLine();
            hero = new Player(name);
        }

        // initialize game components with the hero
        System.out.println("\nInitializing game world...");
        taskManager = new TaskManager(hero);
        gameEngine = new GameEngine(hero, taskManager);
        menu = new ConsoleMenu(gameEngine);

        // creates character if new game
        if (isNewGame) {
            menu.runCharacterCreation();

            SaveManager.saveGame(hero);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            SaveManager.saveGame(hero);
            System.out.println("\n[System] Shutting down and saving... Safe travels, hero!");
        }));

        // start the game loop
        System.out.println("\nStarting menu...");
        menu.startMenu();

        setupScanner.close();
    }
}