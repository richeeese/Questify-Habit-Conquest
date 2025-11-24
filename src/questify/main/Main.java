package main;

import models.*;
import logic.*;
import ui.*;
import java.util.*;

public class Main {
	public static void main(String[] args) {
        // start screen ui using ascii

        // initialize and get a player name
        Scanner input = new Scanner(System.in);
        System.out.print("Enter your player name: ");
        Player player = new Player(input.nextLine());

        // manager initS
        TaskManager taskManager = new TaskManager(player);
        GameEngine gameEngine = new GameEngine(player, taskManager);

        ConsoleMenu menu = new ConsoleMenu(player, taskManager, gameEngine); // Initialize the console menu
        
        menu.start(); // Start the menu interaction

        System.out.println("Exiting Questify. Goodbye, " + player.getName() + "!");

        input.close();
    }
}
