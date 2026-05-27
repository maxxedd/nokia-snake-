# Nokia-Style Snake Game with Distributed Leaderboard

This application is a modern, retro-styled take on the classic Snake game, featuring dynamic speed progression, an integrated SQL-based leaderboard, and a distributed architecture that separates game logic from data persistence.

## 1. Development Overview
The application was developed using Java Swing for the graphical interface and JDBC for database connectivity. It follows a modular design pattern, separating game mechanics (movement, collision, scoring) from data management (database queries and synchronization).

## 2. Distributed Architecture
The application implements a **Client-Server Distributed Model**:
* **Client (Game Application):** Handles all high-performance requirements locally, including frame rendering, user input, physics calculations (collision detection), and cheat code processing.
* **Server (Database):** A remote MySQL instance acting as a centralized "Source of Truth." The game client communicates with this server via the `DBConnection` utility, which establishes a network connection to fetch and push leaderboard data.

This separation ensures that high scores are not stored locally, allowing for a **synchronized leaderboard** accessible to any instance of the game connected to the same database.

[Image of client-server distributed architecture diagram]

## 3. Core Features
* **Nokia Retro Aesthetic:** A high-contrast monochrome color palette (NOKIA_BG and NOKIA_DARK).
* **Live HUD:** Real-time tracking of time, current score, and top 5 rankings.
* **Dynamic Difficulty:** The game begins at a base speed and increases by 10% for every 100 points scored, creating an escalating challenge.
* **Game Lifecycle:** Includes a 3-second "Get Ready" countdown, active gameplay, and a "Game Over" state with immediate post-game options.

## 4. Controls & Functions
* **Movement:** Use the **Arrow Keys** or **WASD** to control the snake.
* **Buttons:**
    * **Play Again:** Restarts the game immediately without returning to the main menu.
    * **Leaderboard:** Navigates to the full leaderboard view.

## 5. Hidden Cheat Codes
During the **3-second preparation phase** (before the snake starts moving), players can type the following keywords to activate secret features:

| Cheat Code | Function |
| :--- | :--- |
| `pointsss` | Increases score per apple from 10 to 15. |
| `snakes` | Activates God Mode (prevents death upon self-collision). |

*Note: Cheats are automatically cleared upon restarting the game.*

## 6. End-to-End Workflow
1. **Launch:** The client establishes a network socket connection to the database server.
2. **Fetch:** The game queries `tbl_leaderboard` for the top-performing records to display on the HUD.
3. **Play:** Input is captured locally for near-zero latency.
4. **Sync:** Upon the "Game Over" trigger, the final score is pushed to the database, updating the distributed dataset globally.
