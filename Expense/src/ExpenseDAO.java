import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * DAO (Data Access Object) for managing Expense records in the database.
 */
public class ExpenseDAO {

    /**
     * Inserts a new expense into DB.
     */
    public void insert(String date, double amount, String category, String note) throws SQLException {
        String sql = "INSERT INTO expenses (date, amount, category, note) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, date);
            ps.setDouble(2, amount);
            ps.setString(3, category);
            ps.setString(4, note);
            ps.executeUpdate();
        }
    }

    /**
     * Updates an existing expense.
     */
    public void update(int id, String date, double amount, String category, String note) throws SQLException {
        String sql = "UPDATE expenses SET date=?, amount=?, category=?, note=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, date);
            ps.setDouble(2, amount);
            ps.setString(3, category);
            ps.setString(4, note);
            ps.setInt(5, id);
            ps.executeUpdate();
        }
    }

    /**
     * Deletes an expense by ID.
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM expenses WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Returns all expenses as a list.
     */
    public List<Expense> findAll() throws SQLException {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT * FROM expenses ORDER BY date DESC";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Expense(
                        rs.getInt("id"),
                        rs.getString("date"),
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        rs.getString("note")
                ));
            }
        }
        return list;
    }

    /**
     * Aggregates expenses by category (for Pie Chart).
     */
    public Map<String, Double> getTotalByCategory() throws SQLException {
        Map<String, Double> map = new HashMap<>();
        String sql = "SELECT category, SUM(amount) AS total FROM expenses GROUP BY category";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getString("category"), rs.getDouble("total"));
            }
        }
        return map;
    }

    /**
     * Aggregates expenses by month & category (for Stacked Bar Chart).
     */
    public Map<String, Map<String, Double>> getTotalByMonthAndCategory() throws SQLException {
        Map<String, Map<String, Double>> result = new LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(date, '%b') AS month, category, SUM(amount) AS total " +
                     "FROM expenses GROUP BY month, category ORDER BY MIN(date)";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String month = rs.getString("month");
                String category = rs.getString("category");
                double total = rs.getDouble("total");

                result.putIfAbsent(month, new HashMap<>());
                result.get(month).put(category, total);
            }
        }
        return result;
    }

    /**
     * Imports expenses from a CSV file into the DB.
     * CSV must have: id,date,amount,category,note (header row ignored).
     */
    public void importFromCSV(String filePath) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             Connection conn = Database.getConnection()) {

            br.readLine();

            String sql = "INSERT INTO expenses (date, amount, category, note) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 5) {
                    String date = data[1].trim();
                    double amount = Double.parseDouble(data[2].trim());
                    String category = data[3].trim();
                    String note = data[4].trim();

                    ps.setString(1, date);
                    ps.setDouble(2, amount);
                    ps.setString(3, category);
                    ps.setString(4, note);

                    ps.addBatch();
                }
            }
            ps.executeBatch();
            System.out.println("✅ CSV imported successfully!");
        }
    }
}
