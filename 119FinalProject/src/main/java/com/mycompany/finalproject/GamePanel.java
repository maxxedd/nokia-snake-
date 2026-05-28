package com.mycompany.finalproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    // 1. Core Geometry & Precise Grid Scaling
    private final int UNIT_SIZE = 20; 
    private final int PLAY_WIDTH = 320; 
    private final int PLAY_HEIGHT = 320; 
    
    // Layout Margins
    private final int MARGIN_X = 20;
    private final int MARGIN_Y = 30;
    
    // Total panel dimensions fitting comfortably within your 500x500 frame
    private final int SCREEN_WIDTH = 500;
    private final int SCREEN_HEIGHT = 500;
    
    // 2. Snake Mechanics Matrix
    private final java.util.ArrayList<Point> snake = new java.util.ArrayList<>();
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    
    // 3. Threaded Synchronization Engines
    private Timer gameTimer; 
    private Timer gameClock; 
    private int score = 0;
    private int timeElapsed = 0;
    private MainFrame parentFrame;
    private boolean isCountingDown = false;
    private int countdownVal = 3;
    private javax.swing.Timer countdownTimer;
    
    private final int INITIAL_DELAY = 160; 
    private int currentDelay = INITIAL_DELAY;
    private int lastSpeedThreshold = 0;    
    
    // 4. Distributed Architecture Simulation Fields
    private int distributedTasksProcessed = 0;
    private String clusterNodeStatus = "SYNCING...";
    private java.util.ArrayList<LeaderboardPlayer> liveLeaderboard = new java.util.ArrayList<>();
    private javax.swing.JButton btnToLeaderboard;
    private javax.swing.JButton btnPlayAgain;

    // Palette Definitions
    private final Color NOKIA_BG = new Color(155, 188, 15);
    private final Color NOKIA_DARK = new Color(15, 56, 15);

    // Cheat Code State Tracking
    private StringBuilder cheatBuffer = new StringBuilder();
    private boolean cheatMorePoints = false;
    private boolean cheatGodMode = false;

    // Small data container for tracking live ranking climbing shifts
    private class LeaderboardPlayer {
        String name;
        int score;
        boolean isCurrentPlayer;

        LeaderboardPlayer(String name, int score, boolean isCurrentPlayer) {
            this.name = name;
            this.score = score;
            this.isCurrentPlayer = isCurrentPlayer;
        }
    }

    public GamePanel(MainFrame frame) {
        this.parentFrame = frame;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(NOKIA_BG);
        this.setFocusable(true);
        this.setLayout(null); 

        // Initialize core gameplay loop timer
        gameTimer = new Timer(INITIAL_DELAY, this);

        // Style and inject the Play Again button
        btnPlayAgain = new JButton("PLAY AGAIN");
        btnPlayAgain.setFont(new Font("Monospaced", Font.BOLD, 12));
        btnPlayAgain.setBackground(NOKIA_DARK);
        btnPlayAgain.setForeground(NOKIA_BG);
        btnPlayAgain.setFocusable(false); 
        btnPlayAgain.setBounds(MARGIN_X + 80, MARGIN_Y + 175, 160, 35); 
        btnPlayAgain.setVisible(false);
        btnPlayAgain.addActionListener(e -> startGame());
        this.add(btnPlayAgain);

        // Style and inject the leaderboard button
        btnToLeaderboard = new JButton("LEADERBOARD");
        btnToLeaderboard.setFont(new Font("Monospaced", Font.BOLD, 12));
        btnToLeaderboard.setBackground(NOKIA_DARK);
        btnToLeaderboard.setForeground(NOKIA_BG);
        btnToLeaderboard.setFocusable(false); 
        btnToLeaderboard.setBounds(MARGIN_X + 80, MARGIN_Y + 220, 160, 35); 
        btnToLeaderboard.setVisible(false);
        
        btnToLeaderboard.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.showLeaderboardCard();
            }
        });
        this.add(btnToLeaderboard);

        // Keyboard event capture framework
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                
                // Classic movement capture (Processed ONLY during active running game loop)
                if (running && !isCountingDown) {
                    if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && direction != 'R') {
                        direction = 'L';
                    }
                    if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && direction != 'L') {
                        direction = 'R';
                    }
                    if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && direction != 'D') {
                        direction = 'U';
                    }
                    if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && direction != 'U') {
                        direction = 'D';
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // CHANGED: Captures actual characters typed reliably during the 3-second countdown
                if (isCountingDown) {
                    char ch = e.getKeyChar();
                    if (Character.isLetter(ch)) {
                        cheatBuffer.append(Character.toLowerCase(ch));
                        
                        // Limit buffer window length to prevent overflow build up
                        if (cheatBuffer.length() > 20) {
                            cheatBuffer.delete(0, cheatBuffer.length() - 20);
                        }
                        
                        checkCheatCodes();
                    }
                }
            }
        });

        // Initialize background metric game clock 
        gameClock = new Timer(1000, e -> {
            if (running && !isCountingDown) {
                timeElapsed++;
                distributedTasksProcessed += new java.util.Random().nextInt(3) + 1;
                clusterNodeStatus = (timeElapsed % 4 == 0) ? "BROKER IDLE" : "PROCESSING QUEUE";
                repaint();
            }
        });
        
        initLiveLeaderboard();
    }

    private void checkCheatCodes() {
        String currentBuffer = cheatBuffer.toString();
        
        if (currentBuffer.endsWith("pointsss")) {
            cheatMorePoints = true;
            cheatBuffer.setLength(0);
            
            // Visual confirmation for the player
            JOptionPane.showMessageDialog(this, 
                "CHEAT ACTIVATED: Point Multiplier Enabled (15 pts/apple)!", 
                "Cheat Code Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            this.requestFocusInWindow(); // Return focus to the game panel
        } 
        else if (currentBuffer.endsWith("snakes")) {
            cheatGodMode = true;
            cheatBuffer.setLength(0);
            
            // Visual confirmation for the player
            JOptionPane.showMessageDialog(this, 
                "CHEAT ACTIVATED: God Mode Enabled (Invincible)!", 
                "Cheat Code Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            this.requestFocusInWindow(); // Return focus to the game panel
        }
    }

    private void initLiveLeaderboard() {
        liveLeaderboard = new ArrayList<>();
        try {
            java.sql.Connection conn = DBConnection.getConnection(); 
            String query = "SELECT player_name, score FROM tbl_leaderboard ORDER BY score DESC LIMIT 4";
            java.sql.PreparedStatement ps = conn.prepareStatement(query);
            java.sql.ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String dbName = rs.getString("player_name");
                int dbScore = rs.getInt("score");
                liveLeaderboard.add(new LeaderboardPlayer(dbName, dbScore, false));
            }
            
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("=== LEADERBOARD DATABASE ERROR ===");
            e.printStackTrace(); 
            System.out.println("==================================");
        }
        liveLeaderboard.add(new LeaderboardPlayer("YOU", score, true));
    }    

    private void sortLeaderboard() {
        liveLeaderboard.sort((p1, p2) -> Integer.compare(p2.score, p1.score));
    }

    private void initSnakeCoordinates() {
        snake.clear();
        direction = 'R'; 
        
        int startX = MARGIN_X + (6 * UNIT_SIZE);
        int startY = MARGIN_Y + (8 * UNIT_SIZE);
        
        snake.add(new Point(startX, startY));             
        snake.add(new Point(startX - UNIT_SIZE, startY));  
        snake.add(new Point(startX - (2 * UNIT_SIZE), startY)); 
    }

    public void startGame() {
        score = 0;
        timeElapsed = 0;
        lastSpeedThreshold = 0;
        currentDelay = INITIAL_DELAY;
        
        // Wipe cheat fields cleanly for the fresh countdown round
        cheatMorePoints = false;
        cheatGodMode = false;
        cheatBuffer.setLength(0);
        
        btnPlayAgain.setVisible(false);
        btnToLeaderboard.setVisible(false);
        
        if (gameTimer != null) {
            gameTimer.setDelay(currentDelay);
            gameTimer.stop();
        }
        if (gameClock != null) {
            gameClock.stop();
        }

        initLiveLeaderboard();
        initSnakeCoordinates();
        spawnFood();

        running = false; 
        isCountingDown = true;
        countdownVal = 3;

        if (countdownTimer != null) {
            countdownTimer.stop();
        }

        countdownTimer = new javax.swing.Timer(1000, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                countdownVal--;

                if (countdownVal <= 0) {
                    countdownTimer.stop(); 
                    launchGameplay(); 
                }
                repaint(); 
            }
        });

        countdownTimer.start();
        repaint();
    }
    
    private void launchGameplay() {
        isCountingDown = false;
        running = true;

        if (gameTimer != null) gameTimer.start(); 
        if (gameClock != null) gameClock.start(); 

        this.requestFocusInWindow();
        repaint();
    }

    private void spawnFood() {
        java.util.Random random = new java.util.Random();
        int fx = random.nextInt(PLAY_WIDTH / UNIT_SIZE) * UNIT_SIZE + MARGIN_X;
        int fy = random.nextInt(PLAY_HEIGHT / UNIT_SIZE) * UNIT_SIZE + MARGIN_Y;
        food = new Point(fx, fy);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !isCountingDown) {
            moveSnake();
            checkFood();
            checkCollisions();
        }
        repaint(); 
    }

    private void moveSnake() {
        for (int i = snake.size() - 1; i > 0; i--) {
            snake.set(i, new Point(snake.get(i - 1)));
        }

        Point head = snake.get(0);
        switch (direction) {
            case 'U' -> head.y -= UNIT_SIZE;
            case 'D' -> head.y += UNIT_SIZE;
            case 'L' -> head.x -= UNIT_SIZE;
            case 'R' -> head.x += UNIT_SIZE;
        }    
    }

    private void checkFood() {
        if (!snake.isEmpty() && snake.get(0).equals(food)) {
            score += cheatMorePoints ? 15 : 10;
            
            snake.add(new Point(snake.get(snake.size() - 1))); 
            spawnFood();
            
            int currentMilestone = score / 100; 
            if (currentMilestone > lastSpeedThreshold) {
                lastSpeedThreshold = currentMilestone;
                currentDelay = (int) (currentDelay * 0.90); 
                
                if (currentDelay < 45) {
                    currentDelay = 45; 
                }
                
                if (gameTimer != null) {
                    gameTimer.setDelay(currentDelay);
                }
            }
            
            for (LeaderboardPlayer player : liveLeaderboard) {
                if (player.isCurrentPlayer) {
                    player.score = score;
                    break;
                }
            }
            sortLeaderboard(); 
        }
    }

    private void checkCollisions() {
        if (snake.isEmpty()) return;
        Point head = snake.get(0);
        
        if (head.x < MARGIN_X || head.x >= MARGIN_X + PLAY_WIDTH || 
            head.y < MARGIN_Y || head.y >= MARGIN_Y + PLAY_HEIGHT) {
            running = false;
        }
        
        // Body self-collision checks: Evaluated only if cheatGodMode is disabled
        if (!cheatGodMode) {
            for (int i = 1; i < snake.size(); i++) {
                if (head.equals(snake.get(i))) {
                    running = false;
                    break;
                }
            }
        }
        
        if (!running) {
            if (head.x >= MARGIN_X + PLAY_WIDTH) head.x = MARGIN_X + PLAY_WIDTH - UNIT_SIZE;
            if (head.x < MARGIN_X) head.x = MARGIN_X;
            if (head.y >= MARGIN_Y + PLAY_HEIGHT) head.y = MARGIN_Y + PLAY_HEIGHT - UNIT_SIZE;
            if (head.y < MARGIN_Y) head.y = MARGIN_Y;
            
            gameOver();
        }
    }

    private void gameOver() {
        running = false;
        if (gameTimer != null) gameTimer.stop();
        if (gameClock != null) gameClock.stop(); 
        
        if (parentFrame != null) {
            parentFrame.handleGameOver(score, timeElapsed);
        }
        
        if (btnPlayAgain != null) {
            btnPlayAgain.setVisible(true);
        }
        if (btnToLeaderboard != null) {
            btnToLeaderboard.setVisible(true); 
        }
        this.repaint();    
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // 1. Draw boundary walls
        g2.setColor(NOKIA_DARK);
        g2.setStroke(new BasicStroke(3)); 
        g2.drawRect(MARGIN_X - 2, MARGIN_Y - 2, PLAY_WIDTH + 4, PLAY_HEIGHT + 4);
        
        g2.setColor(new Color(145, 178, 15));
        g2.fillRect(MARGIN_X, MARGIN_Y, PLAY_WIDTH, PLAY_HEIGHT);
        
        // 2. Render elements if snake coordinates are populated
        if (!snake.isEmpty()) {
            if (food != null) {
                g2.setColor(NOKIA_DARK);
                g2.fillRect(food.x + 2, food.y + 2, UNIT_SIZE - 4, UNIT_SIZE - 4);
            }
            
            for (int i = 0; i < snake.size(); i++) {
                if (i == 0) {
                    g2.setColor(NOKIA_DARK); 
                } else {
                    g2.setColor(new Color(30, 75, 30)); 
                }
                Point p = snake.get(i);
                g2.fillRect(p.x + 1, p.y + 1, UNIT_SIZE - 2, UNIT_SIZE - 2);
            }
        }
        
        // Display game over screen text
        if (!running && !isCountingDown && !snake.isEmpty()) {
            g2.setColor(NOKIA_DARK);
            g2.setFont(new Font("Monospaced", Font.BOLD, 24));
            g2.drawString("GAME OVER", MARGIN_X + 90, MARGIN_Y + 140);
        }
        
        // Render 3-Second Countdown Text Overlays
        if (isCountingDown) {
            g2.setColor(NOKIA_DARK);
            g2.setFont(new Font("Monospaced", Font.BOLD, 60));
            FontMetrics metrics = g2.getFontMetrics();
            String msg = String.valueOf(countdownVal);
            
            int x = MARGIN_X + (PLAY_WIDTH - metrics.stringWidth(msg)) / 2;
            int y = MARGIN_Y + (PLAY_HEIGHT - metrics.getHeight()) / 2 + metrics.getAscent();
            g2.drawString(msg, x, y);
            
            g2.setFont(new Font("Monospaced", Font.BOLD, 14));
            metrics = g2.getFontMetrics();
            String subMsg = "GET READY...";
            int subX = MARGIN_X + (PLAY_WIDTH - metrics.stringWidth(subMsg)) / 2;
            g2.drawString(subMsg, subX, y + 40);
        }
        
        // 3. HUD System Layout
        int hudX = 360;
        
        g2.setColor(NOKIA_DARK);
        g2.setFont(new Font("Monospaced", Font.BOLD, 12));
        g2.drawString("--- TIME ---", hudX, 50);
        
        g2.setFont(new Font("Monospaced", Font.BOLD, 18));
        int mins = timeElapsed / 60;
        int secs = timeElapsed % 60;
        g2.drawString(String.format("%02d:%02d", mins, secs), hudX, 75);
        
        g2.setFont(new Font("Monospaced", Font.BOLD, 12));
        g2.drawString("--- SCORE ---", hudX, 115);
        g2.setFont(new Font("Monospaced", Font.BOLD, 22));
        g2.drawString(String.format("%04d", score), hudX, 142);
        
        g2.setFont(new Font("Monospaced", Font.BOLD, 12));
        g2.drawString("--- LIVE RANK ---", hudX, 185);
        
        int startY = 205; 
        for (int i = 0; i < Math.min(liveLeaderboard.size(), 5); i++) {
            LeaderboardPlayer lp = liveLeaderboard.get(i);
            
            String playerLabel = String.format("%d.%s", (i + 1), lp.name);
            String scoreLabel = String.format("(%d)", lp.score);
            
            if (lp.isCurrentPlayer) {
                g2.setFont(new Font("Monospaced", Font.BOLD, 11));
                g2.drawString(">", hudX - 10, startY); 
            } else {
                g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
            }
            
            g2.drawString(playerLabel, hudX, startY);       
            g2.drawString(scoreLabel, hudX + 110, startY);   
            
            startY += 18; 
        }
    }
}

