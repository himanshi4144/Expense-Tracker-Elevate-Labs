import java.sql.Connection;
import java.sql.SQLException;

/**
 * Quick tester to verify DB connection.
 * Run this before the app to ensure your DB is configured properly.
 */
public class TestDB {
    public static void main(String[] args) {
        try (Connection conn = Database.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Database connected successfully!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
