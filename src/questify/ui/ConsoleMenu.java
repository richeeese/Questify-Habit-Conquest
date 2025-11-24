package ui;

import models.Player;
import java.util.Scanner;
import logic.TaskManager;
import logic.GameEngine;

public class ConsoleMenu implements GameUI {
    private Player activePlayer;
    private TaskManager taskManager;
    private GameEngine gameEngine;
    private Scanner input;


    public ConsoleMenu (Player p, TaskManager tm, GameEngine ge) {
        this.activePlayer = p;
        this.taskManager = tm;
        this.gameEngine = ge;
        this.input = new Scanner(System.in);
    }

    @Override
    public void start() {
        // start method from gameUI
        boolean running = true;

        while(running) {
            
        }
    }

    public void characterCreation() {
        //welcoming and sprite selection
        System.out.println(); // put intro banner here
        System.out.println("Welcome, Hero " + activePlayer.getName() + "! The realm needs your habits. Quickly, choose an avatar form before procrastination takes over our land!"); // put welcome message here
        System.out.println("Choose your avatar form from the ones below:"); // put quirky sprite selection passage here
        displaySprite1();
        System.out.println("This avatar resembles a valiant and persistent warrior. Having been through multiple battles, he needs your help in battling the tardiness that has spread across the land!"); // "this is your character" or something
        displaySprite2();
        System.out.println("This avatar resembles a swift and quick-witted warrior. She has survived the invasion of procrastination throughout the land and wants you to aid her in warding it off!"); // "this is your character" or something
        System.out.println("Choose your avatar (1 or 2): ");

        // error handling for sprite selection
        try {
            int choice = Integer.parseInt(input.nextLine());
            if (choice == 1 || choice == 2) {
                activePlayer.setPlayerSprite(choice);
                System.out.println("You have selected character " + choice + "!");
            } else {
                System.out.println("Invalid choice. Default character selected.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Default character selected.");
        }
    }

    public void displaySprite1() {
        System.out.println("                                                            \r\n" + //
                        "                        ░░▓▒░░░░░░                          \r\n" + //
                        "                      ░░▓█▓▓▓▓▓▓▓▓▓░░                       \r\n" + //
                        "                     ░▒███▓▓▓▓▓▓▓▓▓▒░░░                     \r\n" + //
                        "                  ░░░░░▓▓▓▓▓▓▓▓▓▓▓█▓▓░░                     \r\n" + //
                        "     ░░░▒▒▓░░     ░░▒██▓█▓▓██████████░░                     \r\n" + //
                        "     ░▒▓▒▒▓▓░░      ░█████▓▓▓███▓████▒░                     \r\n" + //
                        "     ░░░▓██▓▒░     ░░▒█░██▒▓██▒▒▓█▒█░░░                     \r\n" + //
                        "       ░░░░██▓▒   ░░▒▓█▒▒▓░░▒░░░░▒░░░                       \r\n" + //
                        "           ░███▒░░▒██▓░▓██▒▒░░░░░░░░                        \r\n" + //
                        "            ░██▓▓█████░▒▒██▓▓▓▓▒▒░░░░░░                     \r\n" + //
                        "          ░░▒▒▓█▓▒▒▒▒░▒▓▒▒▒▒▓███▓▒▓▓░▒░░░                   \r\n" + //
                        "          ░▓▓██▒▒░░░░▒▒▒█▒░░▒▒▒▒▒▒▒▓▒▒▓▒░░                  \r\n" + //
                        "        ░░█▓░▓▓▓▒▒▒▒▒▒▒▓█▓░░░░▒▒▒░▒▓▓▓▓▓▒░                  \r\n" + //
                        "        ░░░░░░▒▒▒▒▒▒▒▒▒█▒▒▓▓░░░░░░░░█▒▒▒░░                  \r\n" + //
                        "             ░▓▓▒▒▒▒▓███▒░░▒▓▓▓▒░░░░████▒░                  \r\n" + //
                        "           ░░░▓████████▒▒░░░░▒▓▓▓▒▒▒████▒░                  \r\n" + //
                        "          ░░▓▒░▓▓▓█████▒▒▒▒▒▒░▒▒▒▒▓▒████▓█▒░                \r\n" + //
                        "          ░█▓▒▒▒▒▓█▓▒░░▒███▓▓▓▒▒▒▓████████▒░░               \r\n" + //
                        "          ░▓█▓▒▒▒▓█▒░▒▒▒██▓█▓▒▒▓▓▒▓███████▓▒▒               \r\n" + //
                        "           ░▓▓▓▓▓▓▓░░▓▓█▓▓██▓█▓▓▓███▓█▓██▓▓▓▓               \r\n" + //
                        "            ░▓█▓▓▓▓░▒█▓▓▓█▓▓▓▒▓▓▓▒▓▓██▓██▓▓▓▓               \r\n" + //
                        "            ░▓▓▓▓▓█▓░▓▓█▓▓█▓░░░▒▒░▒▓█▓▓██▓▓▓▓               \r\n" + //
                        "           ░░▓▓▓▓▓▓███████░░░░░░▓░░░▒████▓▓▓▓               \r\n" + //
                        "            ░██▓▓█████▒▒▒░░░░░░░▓░░░░▒██████▓               \r\n" + //
                        "            ░░▓███▓░▒▒▒░░░░░░░░░▓░░░░▒▒▒██▓░░               \r\n" + //
                        "             ░░░░░░░░▒░░░░░░░░░░▓░░░░░▒░░░░                 \r\n" + //
                        "                  ░▒▓▒░░░░░░░░░░▓░▒░░░▒▓▒░                  \r\n" + //
                        "                  ░▒▓▒▒░▒░░░▒▒░▒▓▒▒░▒░▒█▓▒░░░               \r\n" + //
                        "                  ░▒▓▓▓▓▓▓▓▓▓▓░░██▓▓▓▓▓██▓▒░░               \r\n" + //
                        "                  ░░▒██████▒░░░░█████████▓▒▒▒░░░            \r\n" + //
                        "                  ░▒█▓▓█▓▓▒░   ░▓██▓▓▓▓███▓▒▒▒▒░            \r\n" + //
                        "                  ░▒██▓███░    ░▒████▓█░▒██▓▓▒▒▓░░          \r\n" + //
                        "                  ░▒███▓██░░   ░░░██▓▓█░░░▒██▓▓▓░░          \r\n" + //
                        "                  ░▒█▓▓██░░░░░░░░░████▓▓░░░░▒▒▒░            \r\n" + //
                        "          ░░░░░░░░▒█▓▓▓███▓▓▓▓▒░▓▓████▓▓█▒░░░░░░░░          \r\n" + //
                        "        ░░▒░▒▓▒▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒░▓▒░░         \r\n" + //
                        "            ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░                  \r\n" + //
                        "                                                            \r\n" + //
                        "                                                            ")
    }

    public void displaySprite2() {
        System.out.println("                                                            \r\n" + //
                        "                           ▒▒▒▒▒▒▒▒                         \r\n" + //
                        "                          ▒▒▒▒▒▒▒░░░                        \r\n" + //
                        "                          ▒▒██▓▓▒▒░▒                        \r\n" + //
                        "                         ▓▒▒▓▓▓▓▓▒▒▒                        \r\n" + //
                        "                         ▓▒▒▓█▓▓▓▒▒▒▒▓                      \r\n" + //
                        "                         ▒▒▒░░▒▒▒▒▒▒▒▓                      \r\n" + //
                        "                        ▓▒▒▒▒▓▓▓▓▒▒▒▒▒▓                     \r\n" + //
                        "                         ▓▓▒▓▓██▓▓▓▓▒▒▒▓                    \r\n" + //
                        "                        ▓▓▒▒▓██▒▓▒▒▓▓▒▒▓                    \r\n" + //
                        "                         ▒▒▒▓▓▓▓▓▒▒░▒▒▒▓                    \r\n" + //
                        "                         ▒▒▒▓███▓▒▒▒▒▒▒▓                    \r\n" + //
                        "                         ▓▓▒▓██▓▓▒▒░░▓▓▓                    \r\n" + //
                        "                       ▓▓▓▒▒▒▓▓▓▓▒▒░░▒█▓▒▓                  \r\n" + //
                        "                      ▒▒▓▒▒▒▒▒▒▒▒▒▒▒░░▓▓▒▒▓                 \r\n" + //
                        "                      ▒▓▒░▒▓▓▓▓▓▒▒░▒▒░▒▓▒░▒▓                \r\n" + //
                        "                    ▒▒▓▒▒▒▓▓▓▓▒▒▒▓▓▓▓░▒▓▒░░▒█               \r\n" + //
                        "                    ▒▒▒▒▓▓▓▓▓▓▓▓██▓▓▓▓▓▓▒▒▒▒▒█▓             \r\n" + //
                        "                   ▓▒░▒▒▓█▓▓▒▓▓▓███▓▓▓▓▓▒▒▒▒▒▒▓▓            \r\n" + //
                        "                 ▓▓▒░  ▒▓█▓██▓█▓███▓▓▓▒▒▒▒▒▒▒▒▒▓▓▓▓         \r\n" + //
                        "                █▓▒     ▓▓▓██▓█▒███▓▓▒▒░▒▒▒▒▒▒▒▒▒▓▓▓        \r\n" + //
                        "              ██▓▒       ▓▒▓▓▒▒░▓▓▒▒▒▒▒▒▒▒▒▒▒▒▓▒▒▒▓██       \r\n" + //
                        "            ██▓▓         ▓▓▓▒▒░░▒▓▓▓▓▒▒▒▒▒▒▒▒▒▓▓▓▒▒▓▓       \r\n" + //
                        "           ▓▓▓           ▓▓▓▓▒░░▒▓▓▓▓▒▒▒▒▒▒▒▓▒▒▓▓▓▓▓        \r\n" + //
                        "         ▒▒▒▒           ▒▒▒▒▒▒▒▒▒▒▓▒▒▒▒▓▓▓▓▒▒▒▓▒▒▓▓         \r\n" + //
                        "         ▒▒              ▒▒▒░▒▒▒▒▒▓▒▒▒▒▓▓▓▓▓▒▒▒▓            \r\n" + //
                        "                         ▒▒░░░   ▒▒▒▒░ ▒▓▓                  \r\n" + //
                        "                         ▒▓▒▒     ▒▓▒▒                      \r\n" + //
                        "                        ▒▓▒▒      ▒▓▒▒                      \r\n" + //
                        "                        ▒▓▒▒      ▒▒▒▒                      \r\n" + //
                        "                      ▓▒▓▒▒▒      ▒▓▓▒                      \r\n" + //
                        "                     ▒▒▒▒░░░     ▒▓▓▓▒                      \r\n" + //
                        "                                                            ")
    }
}
