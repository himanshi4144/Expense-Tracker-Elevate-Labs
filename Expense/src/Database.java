import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database utility class.
 * Provides a reusable connection method for the Expense Tracker application.
 *
 * Make sure MySQL server has:
 *   local_infile = 1   (for CSV import support)
 *
 * Update USER and PASSWORD with your MySQL credentials.
 */
public class Database {

    private static final String URL =
            "jdbc:mysql://localhost:3306/expense_tracker?allowLoadLocalInfile=true";

    private static final String USER = "";      // change if needed
    private static final String PASSWORD = "";  // change if needed

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found. Did you add mysql-connector-j to lib?");
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
