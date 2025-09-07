/**
 * Represents a single expense entry in the system.
 */
public class Expense {
    public int id;
    public String date;
    public double amount;
    public String category;
    public String note;

    public Expense(int id, String date, double amount, String category, String note) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.note = note;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %.2f (%s) : %s",
                id, date, amount, category, note);
    }
}
