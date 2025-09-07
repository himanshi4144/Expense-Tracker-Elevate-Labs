/**
 * Entry point of the Expense Tracker application.
 * Launches the Welcome screen.
 */
public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new WelcomeFrame().setVisible(true);
        });
    }
}
