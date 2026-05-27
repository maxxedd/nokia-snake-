# Walkthrough Script: Nokia Snake Game

This script outlines the end-to-end user experience and technical workflow of the application, designed to guide users through the game while explaining the underlying distributed architecture.

## Part 1: User Walkthrough
### 1. Launch & Initialization
* **Action:** Launch the application from the main menu.
* **Under the Hood (Distributed Aspect):** The client application instantly initiates a network connection to the database server. It queries the `tbl_leaderboard` table to retrieve the current top scorers. You will see these rankings populated immediately in the "LIVE RANK" sidebar on the right.

### 2. Preparation & Cheat Phase
* **Action:** When the game menu appears, click "Start Game." A 3-second "GET READY..." countdown begins.
* **Hidden Feature:** During this 3-second window, you can type "pointsss" to increase your score per apple or "snakes" to enable invincibility. 
* **Note:** These inputs are captured via the application's `KeyTyped` event listener, ensuring your movement inputs remain undisturbed once the game starts.

### 3. Gameplay
* **Action:** Use your **Arrow Keys** or **WASD** to control the snake.
* **Functionality:** As you eat apples, your score increases. For every 100 points, the game's internal `Timer` delay is reduced by 10%, causing the snake to accelerate progressively.

### 4. Game Over & Synchronization
* **Action:** Upon colliding with a wall or your own body, the game stops.
* **Distributed Synchronization:** The moment the game triggers `gameOver()`, the application performs a final `INSERT` operation. It pushes your final score to the remote database server.
* **Result:** You can now click "PLAY AGAIN" to reset the state or "LEADERBOARD" to view the updated global rankings.

---

## Part 2: Discussion of Distributed Architecture

The application utilizes a **Client-Server Distributed Model**, which is essential for ensuring data persistence across different sessions and devices.

### End-to-End Distributed Workflow
1. **Client Tier (Snake Game):** This layer resides on the player's local hardware. It is responsible for the **computationally intensive local tasks**: rendering the game, managing the snake's physics, and handling the local keyboard event buffer.
2. **Network Connection (JDBC):** The client acts as a consumer of resources provided by the server. It bridges the gap between the game's logic and the data storage via Java Database Connectivity (JDBC).
3. **Server Tier (MySQL Database):** The database acts as the **Centralized Data Store**. By moving the leaderboard storage off the local machine, we ensure that:
   * **Persistence:** Player scores are not lost when the application is closed.
   * **Distribution:** Any instance of the application pointing to this database will display the same leaderboard, enabling a shared competition experience.

### Why this is Distributed
This architecture is distributed because the **logic** (the game) and the **state** (the scores) are decoupled. In a non-distributed (standalone) version of this game, scores would be stored in a local `.txt` file, making them unreachable by other players. By utilizing a remote database server, we have transformed the game from a local isolated experience into a **networked, collaborative application**.

[Image of client-server network architecture diagram]
