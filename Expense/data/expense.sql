CREATE DATABASE IF NOT EXISTS expense_tracker;
USE expense_tracker;

CREATE TABLE expenses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date DATE NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    note VARCHAR(255)
);

select * from expenses;
DROP TABLE IF EXISTS expenses;

LOAD DATA LOCAL INFILE 'C:/expenses.csv'
INTO TABLE expenses
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(id, date, amount, category, note);

