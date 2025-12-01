<h1 align="center">⚔️ Questify: Habit Conquest 🏹</h1>
<h3 align="center">A Gamified Productivity & To-Do List Console Application</h3>
<p align="center">
    <b>CS 2103 Final Project</b><br/>
</p>

---

# 1. Overview and Core Concept


**Questify: Habit Conquest** transforms mundane task management into an engaging, turn-based RPG experience. By gamifying daily responsibilities, the application tracks habit streaks, incentivizes consistency, and utilizes core game elements—such as experience, leveling, and combat—to drive real-world productivity.

This project showcases robust **Object-Oriented Programming (OOP)** principles implemented within a Java console application, demonstrating advanced state management, inheritance, and local data persistence.

## Key Features

| Category | Features | Description |
| :--- | :--- | :--- |
| **🖋️ Task Management** | To-dos & Daily Tasks | Differentiated tasks where Daily Tasks track **streaks** and apply penalties for failure at the end of the day. |
| **👾 Character Progression** | Stats & Leveling | Players allocate Stat Points (`STR`, `INTEL`, `DEX`) to customize their hero. `INTEL` directly multiplies EXP gained from tasks. |
| **⚔️ Combat Mechanics** | Boss Fights | Turn-based combat system utilizing **RNG Impact** (hit/miss chance influenced by `DEX`) against powerful Bosses. |
| **📝 Consequence System** | Penalty & Dodge | Failed Daily Tasks deal damage to the player's HP. Players can use a limited **Active Dodge** charge to mitigate a single penalty. |
| **📩 Progress Security** | Save/Load System | Progress (hero stats, tasks, streaks) is securely managed and persists using local file saving (`SaveManager.java`). |

---

# 2. OOP Principles Applied

The architecture of Questify is built on fundamental OOP concepts, ensuring the code is modular, extensible, and maintainable.

| Concept | Implementation in Questify |
| :--- | :--- |
| **Inheritance** | The specialized **`DailyTask`** class seamlessly inherits core properties and methods from the base **`Task`** class, adding unique streak logic via `endDayMaintenance()`. |
| **Encapsulation** | All model properties (`Player`, `Task`, `Boss`) are **private**. State changes are strictly controlled through public accessors and mutators (e.g., `player.takeDamage()`, `task.toggleCompleted()`). |
| **Polymorphism** | The **`TaskManager`** holds a generic `List<Task>`. It can iterate over this list and conditionally invoke specialized methods (like streak maintenance) only on objects confirmed to be instances of the derived **`DailyTask`** class. |
| **Abstraction** | The **`Task`** class serves as the abstraction layer, defining the essential contract for all task behaviors (`isCompleted()`, `getExpReward()`) regardless of whether it is a daily habit or a one-time to-do. |

---

# 3. Project Structure

This project follows a standard Java package structure, separating concerns into logical modules:

```
📂---src 
    \📂---questify
        +📂---logic // Core game mechanics and state controllers
        |       GameEngine.java
        |       SaveManager.java
        |       TaskManager.java
        |
        +📂---main # Entry point
        |       Main.java
        |
        +📂---models // Data classes and object representations
        |       Boss.java
        |       DailyTask.java
        |       Player.java
        |       Task.java
        |
        \📂---ui // Console interaction and I/O handling
                ConsoleMenu.java
                GameUI.java
```

-   `Main.java` serves as the primary application entry point, initializing all core services.

### Class Relationship Overview

| Class | Description | Key Relationships |
| :--- | :--- | :--- |
| **`Main`** | Application Entry Point | Initializes and connects the main components (`GameEngine`, `TaskManager`). |
| **`GameEngine`** | Core Controller / Logic | *Uses* `Player`, `TaskManager`, and `Boss`. Manages combat, EXP, and day resolution. |
| **`TaskManager`** | Data Manager | *Holds* a `List` of `Task` objects. |
| **`ConsoleMenu`** | User Interface | *Interacts with* `GameEngine` to display state and process user commands. |
| **`DailyTask`** | Task Model (Habits) | *Extends* `Task` (Inheritance). |
| **`Task`** | Base Task Model | Abstract definition for all tasks. *Is extended by* `DailyTask`. |
| **`Player`** | Hero Model | *Used by* `GameEngine` to track hero stats, HP, and experience. |
| **`Boss`** | Enemy Model | *Used by* `GameEngine` during combat encounters. |

---

# 4. How to Run the Program

### 🧰 System Requirements

* **Java Development Kit (JDK) 17 or newer.**
* A command line interface (Terminal or Command Prompt) is required.

### 🚀 Steps to Run the Application

This is a packaged Java application. You must compile the entire project before running the main class.

1.  **Open Terminal:** Open your terminal and navigate to the **root folder** of the project (the folder containing the `src` directory).

2.  **Create Output Folder:** Create a folder named `bin` to store the compiled files:
    ```bash
    mkdir bin
    ```

3.  **Compile Source Files:** Compile all Java files from the `src` directory into the new `bin` folder. This command handles the nested package structure:
    ```bash
    javac -d bin src/questify/*/*.java src/questify/*/*/*.java
    ```

4.  **Execute Program:** Run the application by executing the main class (`questify.main.Main.java`) using the classpath (`-cp`) pointing to your compiled files:
    ```bash
    java -cp bin questify.main.Main
    ```

5.  **Start Quest:** The console application will launch. Use the number-based menu options to create your character and start playing!

### 🚀 Steps to Run

1.  **Open in IDE:** Open the project folder in **VS Code**, **IntelliJ IDEA**, or your preferred Java IDE.
2.  **Locate Main:** Navigate to the entry point file: `src/questify/main/Main.java`.
3.  **Run Program:** Execute the program directly through your IDE's run button or by compiling and running from the terminal:
    ```bash
    # (Assuming you are in the project root)
    javac -d bin src/questify/*/*.java src/questify/*/*/*.java
    java -cp bin questify.main.Main
    ```
4.  **Start Quest:** Use the number-based menu in the console to create your hero, manage your tasks, and embark on your quest!
        ```
3.  **Play:** Follow the console prompts to enter your hero's name and begin your quest!

---

# 5. Sample Console Output

This output illustrates the process of completing a Daily Task and the subsequent End-of-Day resolution:

```bash
======================================
  W E L C O M E  T O  Q U E S T I F Y 
======================================
Enter your Hero's Name: Mr. Banana

Initializing game world...

Starting menu...

========================================
      Q U E S T I F Y   V 1.0
========================================
Hero: Mr. Banana | Lvl: 1 | HP: 100/100 | Exp: 0/30 (Dodge: 1)
========================================
1. ? Quest Log (List/Create/Remove)
2. ? Complete a Quest
3. ? Character Sheet & Stats
4. ? Allocate Stat Points
5. ?? (No active Boss)
6. ? End Day (Resolve Dailies, Rest & Reset)
7. ? Exit Game
```
# 6. Authors and Acknowledgements

This project was developed by the following students for **CS 2103**.

| Name | Role |
| :--- | :--- |
| Celestial, Ritzy Leewis G. | Console UI Designer |
| Guillo, Rejc C. | Task Logic Developer |
| Santiago, Francis D. | Game Logic Developer |

### Special Thanks To
* Our instructors for their continuous guidance and support throughout the development of this project.
* Our classmates and peers for their cooperation, encouragement, and valuable insights.

---

# 7. Future Enhancements

Potential improvements and extensions for Questify include:

* **GUI Implementation:** Developing a graphical user interface instead of a console-based one for improved accessibility and visual engagement.
* **Database Persistence:** Migrating from local file saving to a database (e.g., SQLite) for more robust data integrity and management.
* **Expanded Content:** Adding more Bosses, Items, Spells, and a more complex quest/storyline system.
* **Cloud Sync:** Implementing API integration for synchronization across devices.

---

# 8. References

* Principles of **Object-Oriented Programming (OOP)**.
* **Java Collections Framework** (for `List`, `Map`, etc.).
