package game.utils;

import javax.swing.*;
import java.awt.*;

public class EndgameScreen extends JFrame {
    public static JLabel infoLabel = new JLabel("Game Stats");
    public static JLabel survivedTime = new JLabel();
    public static JLabel playerHealth = new JLabel();
    public static JLabel enemyCount = new JLabel();
    public static JLabel kills = new JLabel();
    public static JLabel totalScore = new JLabel();
    public static JLabel isVulnerable = new JLabel();
    public static JLabel reloadTime = new JLabel();

    public EndgameScreen() throws HeadlessException {
        setSize(400, 200);
        super.setTitle("Score");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocation(1300, 250);

        add(panel());
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
        panel.add(closeButton());
        return panel;
    }

    public static void updateLabels() {
        survivedTime.setText("Time survived: " + (int) GlobalVariables.time);
        playerHealth.setText("Player health: " + GlobalVariables.player.getHealth());
        enemyCount.setText("Enemies: " + GlobalVariables.enemies.size());
        kills.setText("Kills: " + GlobalVariables.playerKills);
        totalScore.setText("Total score: " + ((int) GlobalVariables.time + GlobalVariables.playerKills));
        isVulnerable.setText("Vulnerable: " + convertVulnerable());
        reloadTime.setText("Reloading: " + GlobalVariables.timeBeforeNextShot);
    }

    private static JButton closeButton() {
        JButton button = new JButton("Exit");
        button.addActionListener(e -> System.exit(0));
        return button;
    }

    private static String convertVulnerable() {
        if (GlobalVariables.isDamageable) return "Yes";
        else return "No";

    }
}
