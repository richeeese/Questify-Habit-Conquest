package com.questify.ui;

import com.questify.logic.GameEngine;
import com.questify.models.Task;
import com.questify.models.DailyTask;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private GameEngine gameEngine;
    private Scanner sc = new Scanner(System.in);

    // Constructor accepts the engine (Dependency Injection)
    public ConsoleMenu(GameEngine engine) {
        this.gameEngine = engine;
    }

    // --- MAIN LOOP ---
    public void startMenu() {
        boolean running = true;

        while (running) {
            printHeader();
            System.out.println("1. 📜 Quest Log (List/Create)");
            System.out.println("2. ✅ Complete a Quest");
            System.out.println("3. 👤 Character Sheet & Stats");
            System.out.println("4. 💪 Allocate Stat Points");
            System.out.println("5. 🌅 End Day (Rest & Reset)");
            System.out.println("6. ❌ Exit Game");
            System.out.print("\nChoose an action: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    handleQuestLog();
                    break;
                case "2":
                    handleCompleteQuest();
                    break;
                case "3":
                    handleCharacterSheet();
                    break;
                case "4":
                    handleStatAllocation();
                    break;
                case "5":
                    handleEndDay();
                    break;
                case "6":
                    System.out.println("Saving progress... Goodbye, Hero!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command. Try again.");
            }

            // Pause for readability
            System.out.println("\n(Press Enter to continue...)");
            sc.nextLine();
        }
    }

    // --- 1. QUEST LOG ---
    private void handleQuestLog() {
        System.out.println("\n--- 📜 QUEST LOG ---");
        System.out.println("L. List all tasks");
        System.out.println("C. Create new task");
        System.out.print("Choice: ");
        String subChoice = sc.nextLine().toUpperCase();

        if (subChoice.equals("L")) {
            listTasks();
        } else if (subChoice.equals("C")) {
            createTask();
        }
    }

    private void listTasks() {
        List<Task> tasks = gameEngine.getTaskManager().getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("Your Quest Log is empty.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
    }

    private void createTask() {
        System.out.print("\nIs this a Daily Habit? (Y/N): ");
        boolean isDaily = sc.nextLine().equalsIgnoreCase("Y");

        System.out.print("Enter Description: ");
        String desc = sc.nextLine();

        System.out.print("Enter Difficulty (easy/medium/hard): ");
        String diff = sc.nextLine();

        if (isDaily) {
            gameEngine.getTaskManager().addDailyTask(desc, diff);
        } else {
            gameEngine.getTaskManager().addTask(desc, diff);
        }
        System.out.println("✨ Quest Added!");
    }

    // --- 2. COMPLETE QUEST ---
    private void handleCompleteQuest() {
        listTasks(); // Show options first
        if (gameEngine.getTaskManager().getAllTasks().isEmpty())
            return;

        int index = getValidInt("Enter the Quest Number to toggle completion: ") - 1;

        // Toggle in Logic Layer
        Task t = gameEngine.getTaskManager().toggleCompletion(index);

        if (t != null) {
            if (t.isCompleted()) {
                System.out.println("⚔️ Quest Completed!");
                // CRITICAL: Tell Engine to grant rewards
                gameEngine.playerCompletesTask(t);
            } else {
                System.out.println("Marked as incomplete.");
            }
        } else {
            System.out.println("Invalid Quest Number.");
        }
    }

    // --- 3. CHARACTER SHEET ---
    private void handleCharacterSheet() {
        System.out.println("\n--- 👤 HERO STATUS ---");
        // Uses the Player's toString method for a nice display
        System.out.println(gameEngine.getPlayer());
    }

    // --- 4. STAT ALLOCATION ---
    private void handleStatAllocation() {
        int points = gameEngine.getPlayer().getStatPoints();
        if (points <= 0) {
            System.out.println("You have no Stat Points to spend. Level up to gain more!");
            return;
        }

        System.out.println("\n--- 💪 LEVEL UP! ---");
        System.out.println("Points Available: " + points);
        System.out.println("Which stat to upgrade?");
        System.out.println("[STR] Strength (Damage)");
        System.out.println("[DEF] Defense  (Damage Reduction)");
        System.out.println("[INT] Intel    (EXP Gain)");
        System.out.println("[DEX] Dexterity(Dodge Chance)");
        System.out.print("Enter stat name: ");

        String stat = sc.nextLine();
        gameEngine.upgradePlayerStat(stat);
    }

    // --- 5. END DAY (INTERACTIVE DODGE) ---
    private void handleEndDay() {
        System.out.println("\n--- 🌅 END OF DAY REVIEW ---");

        // 1. Get failed tasks from Engine
        // NOTE: Ensure GameEngine has public List<DailyTask> getFailedTasks() method
        List<DailyTask> failures = gameEngine.getTaskManager().getIncompleteDailyTasks();

        if (failures.isEmpty()) {
            System.out.println("✨ Perfect day! No failed tasks.");
        } else {
            // 2. Loop through failures and offer dodge
            for (DailyTask task : failures) {
                System.out.println("\n⚠️ You failed: " + task.getDescription());

                boolean dodged = false;

                // Check if they CAN dodge
                if (gameEngine.getPlayer().hasDodgeAvailable()) {
                    System.out.println("   You have " + gameEngine.getPlayer().getDodgeCharges() + " Dodge Charge(s).");
                    System.out.print("   Use a dodge to avoid penalty? [Y/N]: ");
                    String choice = sc.nextLine();

                    if (choice.equalsIgnoreCase("Y")) {
                        gameEngine.resolveFailedTask(task, true); // User chose Yes
                        dodged = true;
                    }
                } else {
                    System.out.println("   (No Dodge Charges remaining)");
                }

                if (!dodged) {
                    gameEngine.resolveFailedTask(task, false); // User chose No or has no charges
                }
            }
        }

        // 3. Finalize
        gameEngine.completeDayReset();
    }

    // --- HELPER ---
    private void printHeader() {
        System.out.println("\n========================================");
        System.out.println("      Q U E S T I F Y   V 1.0");
        System.out.println("========================================");
    }

    private int getValidInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
