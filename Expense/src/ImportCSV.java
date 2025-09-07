import java.io.IOException;
import java.sql.SQLException;

/**
 * Utility class to import expenses from a CSV file.
 * CSV Format: id,date,amount,category,note
 */
public class ImportCSV {
    public static void main(String[] args) {
        ExpenseDAO dao = new ExpenseDAO();
        String path = "C:/expenses.csv";  // update with your actual CSV path

        try {
            dao.importFromCSV(path);
            System.out.println("✅ Import complete!");
        } catch (IOException | SQLException e) {
            System.err.println("❌ Import failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
