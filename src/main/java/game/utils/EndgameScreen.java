package game.utils;

import javax.swing.*;
import java.awt.*;

public class EndgameScreen extends JFrame {

    public EndgameScreen() throws HeadlessException {
        setSize(400, 200);
        super.setTitle("Score");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        add(panel());
        setVisible(true);
    }

    private static JPanel panel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Final score is: " + (int) GlobalVariables.time);
        panel.add(label);
        panel.add(closeButton());
        return panel;
    }

    private static JButton closeButton() {
        JButton button = new JButton("Ok");
        button.addActionListener(e -> {
            System.exit(0);
        });
        return button;
    }
}
