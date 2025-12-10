package driver;

import model.DecisionManager;
import view.DecisionApp;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DecisionManager manager = new DecisionManager();
            DecisionApp app = new DecisionApp(manager);
            app.setVisible(true);
        });
    }
}
