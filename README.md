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

📂 src ---📂 questify +---📂 logic # Core game mechanics and state controllers | GameEngine.java | SaveManager.java | TaskManager.java | +---📂 main # Entry point | Main.java | +---📂 models # Data classes and object representations | Boss.java | DailyTask.java | Player.java | Task.java | ---📂 ui # Console interaction and I/O handling ConsoleMenu.java GameUI.java

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

### Prerequisites
* **Java Development Kit (JDK) 17 or newer.**
* A Java IDE (e.g., VS Code, IntelliJ IDEA) or command line access.

### Running Steps

1.  **Download/Clone:** Ensure all Java files are downloaded and arranged in the corresponding package folders (`questify/logic`, `questify/models`, etc.) within your `src` directory.
2.  **Compile & Run from Command Line (Recommended):**
    * Navigate to your project's `src` folder.
    * Compile the package structure (adapt file names if necessary):
        ```bash
        # 1. Compile all files into a 'bin' directory
        javac -d bin questify/*/*.java questify/*/*/*.java

        # 2. Run the main class
        java -cp bin questify.main.Main
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

| Picture | Name | Role |
| :---: | :--- | :--- |
| **[Insert Picture]** | Celestial, Ritzy Leewis G. |  |
| **[Insert Picture]** | Guillo, Rejc C. |  |
| **[Insert Picture]** | Santiago, Francis D. |  |

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

* Oracle Java Documentation (Specifically the **Java Time API** for date/time handling).
* Principles of **Object-Oriented Programming (OOP)**.
* **Java Collections Framework** (for `List`, `Map`, etc.).
