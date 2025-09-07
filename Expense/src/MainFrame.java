import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Main application window.
 * Allows CRUD operations on expenses, theme switching,
 * search/filter, and navigation to charts.
 */
public class MainFrame extends JFrame {

    private final ExpenseDAO expenseDAO = new ExpenseDAO();
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField dateField, amountField, categoryField, noteField, searchField;
    private JPanel formPanel, buttonPanel;
    private JScrollPane scrollPane;

    private boolean nightMode = false;

    public MainFrame() {
        setTitle("Expense Tracker");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== Top Form =====
        formPanel = new JPanel(new GridLayout(1, 8, 5, 5));
        dateField = new JTextField(LocalDate.now().toString());
        amountField = new JTextField();
        categoryField = new JTextField();
        noteField = new JTextField();

        formPanel.add(new JLabel("Date:"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Amount:"));
        formPanel.add(amountField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Note:"));
        formPanel.add(noteField);

        add(formPanel, BorderLayout.NORTH);

        // ===== Table =====
        tableModel = new DefaultTableModel(new String[]{"ID", "Date", "Amount", "Category", "Note"}, 0);
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Buttons & Search =====
        buttonPanel = new JPanel(new BorderLayout());

        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("➕ Add");
        JButton updateBtn = new JButton("✏️ Update");
        JButton deleteBtn = new JButton("🗑 Delete");
        JButton chartBtn = new JButton("📊 View Charts");

        crudPanel.add(addBtn);
        crudPanel.add(updateBtn);
        crudPanel.add(deleteBtn);
        crudPanel.add(chartBtn);

        addBtn.addActionListener(e -> addExpense());
        updateBtn.addActionListener(e -> updateExpense());
        deleteBtn.addActionListener(e -> deleteExpense());
        chartBtn.addActionListener(e -> new ChartFrame().setVisible(true));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("🔍");
        JButton themeBtn = new JButton("🌙");

        searchBtn.addActionListener(e -> applySearchFilter());
        themeBtn.addActionListener(e -> toggleTheme());

        rightPanel.add(searchField);
        rightPanel.add(searchBtn);
        rightPanel.add(themeBtn);

        buttonPanel.add(crudPanel, BorderLayout.WEST);
        buttonPanel.add(rightPanel, BorderLayout.EAST);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== Load initial data =====
        loadExpenses();
        table.getSelectionModel().addListSelectionListener(e -> fillFormFromRow());

        applyDarkTheme();
    }

    // === DB Methods ===
    private void addExpense() {
        try {
            String date = dateField.getText();
            double amount = Double.parseDouble(amountField.getText());
            expenseDAO.insert(date, amount, categoryField.getText(), noteField.getText());
            loadExpenses();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding: " + ex.getMessage());
        }
    }

    private void updateExpense() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            expenseDAO.update(id, dateField.getText(),
                    Double.parseDouble(amountField.getText()),
                    categoryField.getText(),
                    noteField.getText());
            loadExpenses();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating: " + ex.getMessage());
        }
    }

    private void deleteExpense() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            expenseDAO.delete(id);
            loadExpenses();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting: " + ex.getMessage());
        }
    }

    private void loadExpenses() {
        try {
            tableModel.setRowCount(0);
            for (Expense e : expenseDAO.findAll()) {
                tableModel.addRow(new Object[]{e.id, e.date, e.amount, e.category, e.note});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void fillFormFromRow() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            dateField.setText(tableModel.getValueAt(row, 1).toString());
            amountField.setText(tableModel.getValueAt(row, 2).toString());
            categoryField.setText(tableModel.getValueAt(row, 3).toString());
            noteField.setText(tableModel.getValueAt(row, 4).toString());
        }
    }

    // === Search ===
    private void applySearchFilter() {
        String keyword = searchField.getText().trim();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        if (keyword.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
        }
    }

    // === Themes ===
    private void toggleTheme() {
        nightMode = !nightMode;
        if (nightMode) applyNightTheme();
        else applyDarkTheme();
    }

    private void styleTable(Color bg, Color altRow, Color text, Color headerBg, Color headerText) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(headerBg);
        header.setForeground(headerText);
        header.setFont(new Font("Arial", Font.BOLD, 13));

        table.setSelectionBackground(new Color(100, 149, 237));
        table.setSelectionForeground(Color.WHITE);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? bg : altRow);
                    c.setForeground(text);
                }
                return c;
            }
        });
    }

    private void applyDarkTheme() {
        Color bg = new Color(45, 45, 45);
        Color altRow = new Color(60, 60, 60);
        Color text = Color.WHITE;
        Color border = new Color(70, 130, 180);

        getContentPane().setBackground(bg);
        formPanel.setBackground(bg);
        buttonPanel.setBackground(bg);
        scrollPane.setBorder(new LineBorder(border, 3));

        updateFormLabels(text);
        styleTable(bg, altRow, text, new Color(70, 130, 180), Color.WHITE);

        SwingUtilities.updateComponentTreeUI(this);
    }

    private void applyNightTheme() {
        Color bg = Color.BLACK;
        Color altRow = new Color(25, 25, 25);
        Color text = new Color(0, 255, 255);
        Color border = new Color(0, 255, 255);

        getContentPane().setBackground(bg);
        formPanel.setBackground(bg);
        buttonPanel.setBackground(bg);
        scrollPane.setBorder(new LineBorder(border, 3));

        updateFormLabels(text);
        styleTable(bg, altRow, text, Color.DARK_GRAY, Color.CYAN);

        SwingUtilities.updateComponentTreeUI(this);
    }

    private void updateFormLabels(Color text) {
        for (Component comp : formPanel.getComponents()) {
            if (comp instanceof JLabel) {
                comp.setForeground(text);
            }
        }
    }
}
