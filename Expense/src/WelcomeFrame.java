import javax.swing.*;
import java.awt.*;

/**
 * Welcome screen for the Expense Tracker app.
 * Provides a simple entry point with gradient background,
 * Start and About buttons.
 */
public class WelcomeFrame extends JFrame {

    public WelcomeFrame() {
        setTitle("Welcome - Expense Tracker");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // custom gradient background panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(70, 130, 180),
                        getWidth(), getHeight(), new Color(25, 25, 112));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridBagLayout());

        JLabel title = new JLabel("Expense Tracker");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);

        JButton startBtn = new JButton("Start");
        JButton aboutBtn = new JButton("About");

        styleButton(startBtn);
        styleButton(aboutBtn);

        startBtn.addActionListener(e -> {
            new MainFrame().setVisible(true);
            dispose(); // close welcome
        });

        aboutBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Expense Tracker\nTrack your daily expenses with ease.\n\nDeveloped by Himanshi Dhaka.",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE)
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        gbc.gridy++;
        panel.add(startBtn, gbc);

        gbc.gridy++;
        panel.add(aboutBtn, gbc);

        add(panel);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 144, 255));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeFrame().setVisible(true));
    }
}
