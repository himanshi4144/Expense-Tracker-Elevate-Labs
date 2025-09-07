import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Map;

/**
 * Displays charts for expense visualization.
 * - Pie chart: total by category
 * - Stacked bar chart: monthly breakdown
 */
public class ChartFrame extends JFrame {

    private final ExpenseDAO expenseDAO = new ExpenseDAO();

    public ChartFrame() {
        setTitle("Expense Charts");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Pie Chart", new ChartPanel(buildPieChart()));
        tabs.add("Stacked Chart", new ChartPanel(buildStackedChart()));

        add(tabs, BorderLayout.CENTER);
    }

    private JFreeChart buildPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        try {
            Map<String, Double> data = expenseDAO.getTotalByCategory();
            data.forEach(dataset::setValue);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ChartFactory.createPieChart("Expenses by Category", dataset, true, true, false);
    }

    private JFreeChart buildStackedChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            Map<String, Map<String, Double>> data = expenseDAO.getTotalByMonthAndCategory();
            for (String month : data.keySet()) {
                Map<String, Double> cats = data.get(month);
                for (String category : cats.keySet()) {
                    dataset.addValue(cats.get(category), category, month);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ChartFactory.createStackedBarChart(
                "Monthly Expenses",
                "Month",
                "Amount",
                dataset
        );
    }
}
