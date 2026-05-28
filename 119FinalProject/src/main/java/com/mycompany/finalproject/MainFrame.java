package com.mycompany.finalproject;

import javax.swing.JOptionPane;

public class MainFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainFrame.class.getName());

    // Declare the GamePanel here at the class level
    private GamePanel gamePanel;

/**
     * Creates new form MainFrame
     */
/**
     * Creates new form MainFrame
     */
/**
     * Creates new form MainFrame
     */
/**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        
        // 1. Clear out the default layout design space
        jPanel1.removeAll(); 
        
        // Define our unified retro color palette
        java.awt.Color darkNokia = new java.awt.Color(15, 56, 15);
        java.awt.Color lightNokia = new java.awt.Color(155, 188, 15);
        
        // 2. MAIN MENU CARD SETUP
        javax.swing.JPanel pnlMenu = new javax.swing.JPanel();
        pnlMenu.setBackground(lightNokia);
        pnlMenu.setLayout(new java.awt.GridLayout(3, 1, 0, 25));
        pnlMenu.setBorder(javax.swing.BorderFactory.createEmptyBorder(70, 90, 70, 90));
        
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("NOKIA SNAKE");
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 32));
        lblTitle.setForeground(darkNokia);
        
        javax.swing.JButton btnGameStart = new javax.swing.JButton("START GAME");
        btnGameStart.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 18));
        btnGameStart.setBackground(darkNokia);
        btnGameStart.setForeground(lightNokia);
        btnGameStart.setFocusPainted(false);
        btnGameStart.addActionListener(this::btnStartActionPerformed);
        
        javax.swing.JButton btnViewLeaderboard = new javax.swing.JButton("VIEW LEADERBOARD");
        btnViewLeaderboard.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 14));
        btnViewLeaderboard.setBackground(darkNokia);
        btnViewLeaderboard.setForeground(lightNokia);
        btnViewLeaderboard.setFocusPainted(false);
        btnViewLeaderboard.addActionListener(this::btnLeaderboardActionPerformed);
        
        pnlMenu.add(lblTitle);
        pnlMenu.add(btnGameStart);
        pnlMenu.add(btnViewLeaderboard);
        
        // 3. GAME PANEL CARD SETUP
        gamePanel = new GamePanel(this);
        
// 4. LEADERBOARD WRAPPER CARD SETUP
        javax.swing.JPanel pnlLeaderboardWrapper = new javax.swing.JPanel();
        pnlLeaderboardWrapper.setBackground(lightNokia);
        pnlLeaderboardWrapper.setLayout(new java.awt.BorderLayout(0, 15)); // Vertical 15px gap
        pnlLeaderboardWrapper.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Edge padding
        
        // FIX: Bypass NetBeans' jScrollPane1 and wrap your jtLeaderboard in a fresh, clean container
        javax.swing.JScrollPane cleanScrollPane = new javax.swing.JScrollPane(jtLeaderboard);
        
        // Match the internal viewport background to your retro Nokia green palette
        cleanScrollPane.getViewport().setBackground(lightNokia);
        cleanScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(darkNokia, 2)); // Neat retro border
        
        // Mount our clean scroll pane right into the center layout zone
        pnlLeaderboardWrapper.add(cleanScrollPane, java.awt.BorderLayout.CENTER);
        
        // Create the clean programmatic Back Button
        javax.swing.JButton btnBackToMenu = new javax.swing.JButton("MAIN MENU");
        btnBackToMenu.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 14));
        btnBackToMenu.setBackground(darkNokia);
        btnBackToMenu.setForeground(lightNokia);
        btnBackToMenu.setFocusPainted(false);
        
        // Attach the card switcher to the back button
        btnBackToMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                java.awt.CardLayout cl = (java.awt.CardLayout) jPanel1.getLayout();
                cl.show(jPanel1, "menuCard");
            }
        });
        
        // Mount the back button to the bottom area of your leaderboard panel
        pnlLeaderboardWrapper.add(btnBackToMenu, java.awt.BorderLayout.SOUTH);
        
        // 5. Commit your 3 layout tracks to the parent CardLayout panel
        jPanel1.add(pnlMenu, "menuCard");
        jPanel1.add(gamePanel, "gamePanelCard");
        jPanel1.add(pnlLeaderboardWrapper, "leaderboardCard"); // Wrapped panel card
        
        // 6. Force initial draw of the main menu
        java.awt.CardLayout cl = (java.awt.CardLayout) jPanel1.getLayout();
        cl.show(jPanel1, "menuCard");
        
        this.revalidate();
        this.repaint();
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnStart = new javax.swing.JButton();
        btnLeaderboard = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtLeaderboard = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 500));
        setResizable(false);

        jPanel1.setLayout(new java.awt.CardLayout());

        jLabel1.setText("NOKIA SNAKE");
        jPanel1.add(jLabel1, "card2");

        btnStart.setText("START");
        btnStart.addActionListener(this::btnStartActionPerformed);
        jPanel1.add(btnStart, "card3");

        btnLeaderboard.setText("VIEW LEADER BOARD");
        btnLeaderboard.addActionListener(this::btnLeaderboardActionPerformed);
        jPanel1.add(btnLeaderboard, "card4");

        jtLeaderboard.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Rank", "Player", "Score", "Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtLeaderboard.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jtLeaderboard);
        if (jtLeaderboard.getColumnModel().getColumnCount() > 0) {
            jtLeaderboard.getColumnModel().getColumn(0).setResizable(false);
            jtLeaderboard.getColumnModel().getColumn(1).setResizable(false);
            jtLeaderboard.getColumnModel().getColumn(2).setResizable(false);
            jtLeaderboard.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanel1.add(jScrollPane1, "card5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
    // Fetch the layout from jPanel1 and switch to the Game Panel
        java.awt.CardLayout cl = (java.awt.CardLayout) jPanel1.getLayout();
        cl.show(jPanel1, "gamePanelCard"); 
        
        gamePanel.startGame();
        gamePanel.requestFocusInWindow(); // Give control layout back to keyboard actions
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnLeaderboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaderboardActionPerformed
        java.awt.CardLayout cl = (java.awt.CardLayout) jPanel1.getLayout();
        cl.show(jPanel1, "leaderboardCard");
    }//GEN-LAST:event_btnLeaderboardActionPerformed

    /**
     * @param args the command line arguments
     */
    public void showLeaderboardCard() {
        java.awt.CardLayout cl = (java.awt.CardLayout) jPanel1.getLayout();
        cl.show(jPanel1, "leaderboardCard");
    }
    
    
    
public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }
    
    public void handleGameOver(int finalScore, int totalSeconds) {
        String name = JOptionPane.showInputDialog(this, "Game Over!\nScore: " + finalScore + "\nEnter Name:");
        if (name != null && !name.trim().isEmpty()) {
            saveScoreToDatabase(name.trim(), finalScore, totalSeconds);
        }
        showLeaderboardData();
        
        // Switch back to the leaderboard view after saving
        java.awt.CardLayout cl = (java.awt.CardLayout) jPanel1.getLayout();
        cl.show(jPanel1, "card5");
    }

    private void saveScoreToDatabase(String name, int score, int seconds) {
        String query = "INSERT INTO tbl_leaderboard (player_name, score, survival_time) VALUES (?, ?, ?);";
        try (java.sql.Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, score);
            pstmt.setInt(3, seconds);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Score saved to Server!");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save score. Check database connection.");
        }
    }
    

    public void showLeaderboardData() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jtLeaderboard.getModel();
        model.setRowCount(0); 

        String query = "SELECT player_name, score, survival_time FROM tbl_leaderboard ORDER BY score DESC, survival_time ASC;";
        try (java.sql.Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(query);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            int rank = 1;
            while(rs.next()) {
                java.util.Vector<Object> row = new java.util.Vector<>();
                row.add(rank++);
                row.add(rs.getString("player_name"));
                row.add(rs.getInt("score"));
                row.add(rs.getInt("survival_time") + "s");
                model.addRow(row);
            }
        } catch(java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLeaderboard;
    private javax.swing.JButton btnStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtLeaderboard;
    // End of variables declaration//GEN-END:variables
}
