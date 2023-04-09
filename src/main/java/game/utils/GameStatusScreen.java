package game.utils;

import renderEngine.DisplayManager;

import javax.swing.*;
import java.awt.*;

import static game.utils.GlobalVariables.invulnerabilityTimer;

public class GameStatusScreen extends JFrame {
    public static JLabel infoLabel = new JLabel("Game Stats");
    public static JLabel survivedTime = new JLabel();
    public static JLabel playerHealth = new JLabel();
    public static JLabel enemyCount = new JLabel();
    public static JLabel kills = new JLabel();
    public static JLabel totalScore = new JLabel();
    public static JLabel isVulnerable = new JLabel();
    public static JLabel reloadTime = new JLabel();
    public static JLabel enemySpawnerLabel = new JLabel();
    public static JLabel enemiesToSpawn = new JLabel();

    public GameStatusScreen() throws HeadlessException {
        setSize(400, 600);
        super.setTitle("Score");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocation(1300, 250);

        add(panel());
        pack();
        setVisible(true);
    }

    private static JPanel panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        updateLabels();
        panel.add(infoLabel);
        panel.add(survivedTime);
        panel.add(playerHealth);
        panel.add(enemyCount);
        panel.add(kills);
        panel.add(totalScore);
        panel.add(isVulnerable);
        panel.add(reloadTime);
        panel.add(enemySpawnerLabel);
        panel.add(enemiesToSpawn);
        panel.add(closeButton());
        return panel;
    }

    public static void updateLabels() {
        survivedTime.setText("Time survived: " + (int) GlobalVariables.time);
        playerHealth.setText("Player health: " + GlobalVariables.player.getHealth());
        enemyCount.setText("Enemies: " + GlobalVariables.enemies.size());
        kills.setText("Kills: " + GlobalVariables.playerKills);
        totalScore.setText("Total score: " + ((int) GlobalVariables.time + GlobalVariables.playerKills));
        isVulnerable.setText("Vulnerable: " + invulnerabilityTimer);
        reloadTime.setText("Reloading: " + GlobalVariables.timeBeforeNextShot);
        enemySpawnerLabel.setText("Time before next wave: " + (int) GlobalVariables.enemySpawnTimer);
        enemiesToSpawn.setText("Wave strength: " + GlobalVariables.enemiesToSpawn);
    }

    private static JButton closeButton() {
        JButton button = new JButton("Close game");
        button.addActionListener(e -> {
            if (GlobalVariables.isGameRunning) {
                GlobalVariables.isGameRunning = false;
                button.setText("Exit program");
            } else {
                System.exit(0);
            }
        });
        return button;
    }

    public static void invokeGameStatusScreen() {
        GameStatusScreen gameStatusScreen = new GameStatusScreen();
        Runnable runnable = GameStatusScreen::updateLabels;
        Thread thread = new Thread(() -> {
           while (true) {
               runnable.run();
               try {
                   Thread.sleep((int) DisplayManager.getFrameTimeSeconds());
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        });
        thread.start();
    }
}
