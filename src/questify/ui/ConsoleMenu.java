package ui;

import models.Task;
import models.DailyTask;
import logic.GameEngine;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private GameEngine gameEngine;
    private Scanner sc; 

    // Constructor accepts the engine (Dependency Injection)
    public ConsoleMenu(GameEngine engine) {
        this.gameEngine = engine;
        this.sc = new Scanner(System.in); 
    }

    // --- MAIN LOOP ---
    public void startMenu() {
        boolean running = true;

        while (running) {
            printHeader();
            
            // If defeated, only allow rest or exit
            if (gameEngine.getPlayer().isDefeated()) {
                System.out.println("\n💀 YOU ARE DEFEATED! You must rest to recover.");
                System.out.println("1. 🌅 End Day (Rest & Recover)");
                System.out.println("2. ❌ Exit Game");
                System.out.print("\nChoose an action: ");
                
                String choice = sc.nextLine();
                if (choice.equals("1")) handleEndDay();
                else if (choice.equals("2")) running = false;
                else System.out.println("Invalid command. Only rest or exit allowed.");
                
                System.out.println("\n(Press Enter to continue...)");
                sc.nextLine();
                continue;
            }

            System.out.println("1. 📜 Quest Log (List/Create/Remove)");
            System.out.println("2. ✅ Complete a Quest");
            System.out.println("3. 👤 Character Sheet & Stats");
            System.out.println("4. 💪 Allocate Stat Points (1 point at a time)");
            
            // Check for Boss availability
            if (gameEngine.getCurrentBoss() != null && !gameEngine.getCurrentBoss().isDefeated()) {
                System.out.println("5. ⚔️ FIGHT BOSS (" + gameEngine.getCurrentBoss().getName() + ")");
            } else {
                System.out.println("5. 🛡️ (No active Boss)");
            }
            
            System.out.println("6. 🌅 End Day (Resolve Dailies, Rest & Reset)");
            System.out.println("7. ❌ Exit Game");
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
                    // FIGHT BOSS
                    if (gameEngine.getCurrentBoss() != null && !gameEngine.getCurrentBoss().isDefeated()) {
                        gameEngine.initiateCombat();
                    } else {
                        System.out.println("No boss to fight right now. Focus on your Quests!");
                    }
                    break;
                case "6":
                    // END DAY
                    handleEndDay();
                    break;
                case "7":
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
        // Note: Closing the scanner here as it's initialized in the constructor.
        sc.close(); 
    }

    // --- 1. QUEST LOG ---
    private void handleQuestLog() {
        System.out.println("\n--- 📜 QUEST LOG ---");
        System.out.println("L. List all tasks");
        System.out.println("C. Create new task");
        System.out.println("R. Remove a To-Do task");
        System.out.print("Choice: ");
        String subChoice = sc.nextLine().toUpperCase();

        if (subChoice.equals("L")) {
            listTasks();
        } else if (subChoice.equals("C")) {
            createTask();
        } else if (subChoice.equals("R")) {
             removeTask();
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void listTasks() {
        List<Task> tasks = gameEngine.getTaskManager().getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("Your Quest Log is empty.");
        } else {
            System.out.println("\n--- Active Quests ---");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                String status = task.isCompleted() ? "[X]" : "[ ]";
                String type = (task instanceof DailyTask) ? "DAILY" : "TODO";
                String details = (task instanceof DailyTask) ? " (Streak: " + ((DailyTask) task).getStreak() + ")" : "";
                System.out.printf("%d. %s [%s] %s (EXP: %d)%s\n", 
                    (i + 1), status, type, task.getDescription(), task.getExpReward(), details);
            }
            System.out.println("---------------------");
        }
    }

    private void createTask() {
        System.out.print("\nIs this a Daily Habit? (Y/N): ");
        boolean isDaily = sc.nextLine().equalsIgnoreCase("Y");

        System.out.print("Enter Description: ");
        String desc = sc.nextLine();

        System.out.print("Enter Difficulty (Easy, Medium, Hard): ");
        String diff = sc.nextLine();

        if (isDaily) {
            gameEngine.getTaskManager().addDailyTask(desc, diff);
        } else {
            gameEngine.getTaskManager().addTask(desc, diff);
        }
        System.out.println("✨ Quest Added!");
    }
    
    private void removeTask() {
        listTasks();
        if (gameEngine.getTaskManager().getAllTasks().isEmpty()) return;

        int index = getValidInt("Enter the To-Do Quest Number to remove: ") - 1;
        gameEngine.getTaskManager().removeTask(index);
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
                System.out.println("Marked as incomplete. EXP will be lost.");
            }
        } else {
            System.out.println("Invalid Quest Number or action failed (e.g., player defeated).");
        }
    }

    // --- 3. CHARACTER SHEET ---
    private void handleCharacterSheet() {
        System.out.println("\n--- 👤 HERO STATUS ---");
        // Uses the Player's toString method for a nice display
        System.out.println(gameEngine.getPlayer());
        
        if (gameEngine.getCurrentBoss() != null && !gameEngine.getCurrentBoss().isDefeated()) {
            System.out.println("\n--- 🔥 CURRENT THREAT ---");
            System.out.printf("Boss: %s\nHP: %d/%d\nAttack Power: %d\n", 
                gameEngine.getCurrentBoss().getName(), 
                gameEngine.getCurrentBoss().getCurrHp(), 
                gameEngine.getCurrentBoss().getMaxHp(),
                gameEngine.getCurrentBoss().getAttackPower());
            System.out.println("-------------------------");
        }
    }

    // --- 4. STAT ALLOCATION ---
    private void handleStatAllocation() {
        int points = gameEngine.getPlayer().getStatPoints();
        if (points <= 0) {
            System.out.println("You have no Stat Points to spend. Level up to gain more!");
            return;
        }

        System.out.println("\n--- 💪 ALLOCATE STAT POINTS ---");
        System.out.println("Points Available: " + points);
        System.out.println("Which stat to upgrade? (Enter full name or abbreviation: STR, DEF, INT/INTEL, DEX)");
        System.out.print("Enter stat name: ");

        String stat = sc.nextLine();
        gameEngine.upgradePlayerStat(stat);
    }

    // --- 6. END DAY (INTERACTIVE DODGE) ---
    private void handleEndDay() {
        System.out.println("\n--- 🌅 END OF DAY REVIEW ---");
        
        // 1. Check for defeated status (used to recover)
        if (gameEngine.getPlayer().isDefeated()) {
            System.out.println("⛑️ Critical rest taken. Your hero recovers from their wounds.");
            // Do not apply penalties if resting from being defeated, just heal
            gameEngine.completeDayReset();
            return;
        }

        // 2. Get failed tasks
        List<DailyTask> failures = gameEngine.getTaskManager().getIncompleteDailyTasks();

        if (failures.isEmpty()) {
            System.out.println("✨ Perfect day! All daily obligations met or completed. No penalties applied.");
        } else {
            System.out.println("⚠️ You have " + failures.size() + " failed Daily Tasks. Prepare for penalties!");
            System.out.println("----------------------------------------");
            // 3. Loop through failures and offer dodge
            for (DailyTask task : failures) {
                System.out.println("\n⚠️ Failed: " + task.getDescription());

                boolean dodged = false;

                // Check if they CAN dodge
                if (gameEngine.getPlayer().hasDodgeAvailable()) {
                    System.out.println("   You have " + gameEngine.getPlayer().getDodgeCharges() + " Dodge Charge(s).");
                    System.out.print("   Use a dodge to avoid penalty? [Y/N]: ");
                    String choice = sc.nextLine();

                    if (choice.equalsIgnoreCase("Y")) {
                        gameEngine.resolveFailedTask(task, true); // User chose Yes
                        dodged = true;
                    }
                } else {
                    System.out.println("   (No Dodge Charges remaining. Penalty is unavoidable.)");
                }

                if (!dodged) {
                    gameEngine.resolveFailedTask(task, false); // User chose No or has no charges
                }
                
                // Stop the penalty loop if the player is defeated mid-resolution
                if (gameEngine.getPlayer().isDefeated()) {
                    System.out.println("\n💀 Your hero collapses from the stress of failure! You must rest to survive.");
                    break;
                }
            }
            System.out.println("----------------------------------------");
        }

        // 4. Finalize day: rest and reset tasks/charges
        gameEngine.completeDayReset();
    }

    // --- HELPER ---
    private void printHeader() {
        System.out.println("\n========================================");
        System.out.println("      Q U E S T I F Y   V 1.0");
        System.out.println("========================================");
        System.out.printf("Hero: %s | Lvl: %d | HP: %d/%d (Dodge: %d)\n", 
            gameEngine.getPlayer().getName(), 
            gameEngine.getPlayer().getLevel(), 
            gameEngine.getPlayer().getCurrHp(), 
            gameEngine.getPlayer().getMaxHp(),
            gameEngine.getPlayer().getDodgeCharges());
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