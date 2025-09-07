📘 Expense Tracker (Java + MySQL + Swing)

A desktop-based Expense Tracker built with Java Swing and MySQL.
It allows you to manage your daily expenses, view them in a clean table, and visualize them using charts (Pie + Stacked Bar).

✨ Features

➕ Add, Update, Delete expenses
🔍 Search & Filter records in real time
🌙 Dark & Night Theme toggle
📊 Charts (Pie + Stacked Column) using JFreeChart
📂 CSV Import support (bulk expenses upload)
🎨 Modern UI with gradient welcome screen

🛠️ Tech Stack

Java 17+ (works with Java 11 too)
Swing (UI)
MySQL 8+ (database)
JFreeChart (charts library)
JDBC (DB connection)

⚙️ Setup Instructions
1. Clone the repo
git clone https://github.com/himanshi4144/Expense-Tracker-Elevate-Labs.git
cd Expense

2. Database Setup
CREATE DATABASE expensetracker;
USE expensetracker;

CREATE TABLE expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    amount DOUBLE NOT NULL,
    category VARCHAR(50),
    note VARCHAR(255)
);

3. Add Dependencies

Add mysql-connector-j-9.x.x.jar to your project classpath
Add jfreechart-x.x.x.jar and jcommon-x.x.x.jar

4. Configure DB

In Database.java update:
private static final String USER = "root";       // your MySQL user
private static final String PASSWORD = "root";   // your MySQL password

▶️ Run the Project

Option 1 – From IDE:
Run Main.java to start the app.
Option 2 – Import CSV (optional):
java ImportCSV
Option 3 – Test DB connection:
java TestDB

📊 Charts Preview

Pie Chart → Distribution of expenses by category
Stacked Column Chart → Monthly expenses broken down by category

🚀 Future Enhancements

User authentication
Export reports (CSV/PDF)
Add more chart types (line, area)
Cloud sync

👨‍💻 Author

HIMANSHI DHAKA

💼 Java Developer | 📊 Data Enthusiast | 🎨 UI Designer
