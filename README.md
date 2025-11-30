<h1 align = "center">⚔️ Questify: Habit Conquest 🏹</h1>
<h3 align = "center">Embark on a quest towards productivity!</h3>
<p align = "center">
    <b>CS 2103 Final Project</b><br/>
</p>

---

## 🌟 1. Overview and Core Concept

<p>
             />_________________________________<br/>
    |########[]_________________________________><br/>
             \><br/>
</p>

<b>Questify: Habit Conquest</b> is a new take on making a to-do list. It gamifies your tasks, tracks your streaks for your daily tasks, and implements game elements to inspire you to be on track and do more tasks!
<br/><br/>
It showcases the use of Object-Oriented Programming(OOP) through a console-based application that engages with the user by using OOP concepts such as inheritance, encapsulation, polymorphism, and abstraction, with additional features such as saving and loading player data. <br/>

### In Questify, players can do the following:
<p>
🖋️ Add tasks
 └── To-dos
 └── Daily tasks
📝 Task Manager
 └── Mark as done/undone
 └── Active dodge mechanics
👾 Make Character Profile
 └── Choose Avatar and Username
 └── Level up
 └── View stats
 └── Allocate stat points
⚔️ Combat Mechanics
 └── Boss fights
 └── Turn-based Combat
 └── RNG Impact
    └── Passive dodge mechanics
    └── Attack hits/misses
📩 Progress Security
 └── Local file saving
 └── Saving/Loading of Progress and Tasks
</p>

---

## 🧬 2. Object-Oriented Principles (OOP) Applied

The project is structured around strong OOP principles to ensure maintainability and modularity, as demonstrated in the source code:

| Concept | Implementation in Questify (Code Reference) |
| :--- | :--- |
| **Inheritance** | **`DailyTask` extends `Task`**. `DailyTask` inherits all base properties and methods (e.g., `isCompleted()`) and adds its unique logic for managing the **`streak`** counter via `endDayMaintenance()`. |
| **Encapsulation** | All core properties in models (`Player`, `Task`, `Boss`) are declared **private**. State changes are managed through public, controlled access methods (e.g., `player.takeDamage(int rawDamage)`, `task.toggleCompleted()`). |
| **Polymorphism** | The **`TaskManager`** holds a generic `List<Task>`. During the `resetDailyTasks()` method, the `TaskManager` iterates and performs a check (`if (task instanceof DailyTask)`) to correctly invoke the specialized `endDayMaintenance()` method for only the daily tasks. |
| **Abstraction** | The **`Task`** class serves as the abstraction for all tasks, defining the common contract for all task behaviors (e.g., `isCompleted()`, `getExpReward()`). The `GameEngine` interacts with tasks abstractly via `Task` objects. |

---

## ⚙️ 3. Project Structure

This project follows a logical structure, separating core logic, data models, and the console interface:

📂 src ---📂 questify     +---📂 logic     |       GameEngine.java     |       SaveManager.java     |       TaskManager.java     |     +---📂 main     |       Main.java     |     +---📂 models     |       Boss.java     |       DailyTask.java     |       Player.java     |       Task.java     |     ---📂 ui             ConsoleMenu.java             GameUI.java

- 'Main.java' - Entry point of the program

---

## ▶️ 4. Getting Started

### Prerequisites
* **Java Development Kit (JDK) 17 or newer.**
* A Java IDE (e.g., VS Code, IntelliJ IDEA).

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

## 👥 5. Authors to Acknowledgements

This project was developed by the following students for **CS 2103**. **[To insert your picture, use a relative image path like `![Your Name](assets/ritzy.jpg)`. You may need to create an `assets` folder.]**

| Picture | Name | Role / Focus |
| :---: | :--- | :--- |
| **[Insert Picture]** | Celestial, Ritzy Leewis G. | Core Game Logic / Combat System |
| **[Insert Picture]** | Guillo, Rejc C. | Task and Streak Management / OOP Design |
| **[Insert Picture]** | Santiago, Francis D. | Console UI / User Experience (UX) |

### Special Thanks To
* Our instructors for their continuous guidance and support throughout the development of this project.
* Our classmates and peers for their cooperation, encouragement, and valuable insights.

---

## 🚀 6. Future Enhancements

Potential improvements and extensions for Questify include:

* **GUI Implementation:** Developing a graphical user interface instead of a console-based one for improved accessibility and visual engagement.
* **Database Persistence:** Migrating from local file saving to a database (e.g., SQLite) for more robust data integrity and management.
* **Expanded Content:** Adding more Bosses, Items, Spells, and a more complex quest/storyline system.
* **Cloud Sync:** Implementing API integration for synchronization across devices.

---

## 📚 7. References

* Oracle Java Documentation (Specifically the **Java Time API** for date/time handling).
* Principles of **Object-Oriented Programming (OOP)**.
* **Java Collections Framework** (for `List`, `Map`, etc.).
