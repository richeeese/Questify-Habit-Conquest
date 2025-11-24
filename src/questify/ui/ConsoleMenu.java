package ui;

import models.Player;
import java.util.Scanner;
import logic.TaskManager;

public class ConsoleMenu {
    private Player activePlayer;
    private TaskManager taskManager;

    private Scanner input;

    public ConsoleMenu (Player player) {
        this.activePlayer = player;
        this.input = new Scanner(System.in);
    }

    
}
