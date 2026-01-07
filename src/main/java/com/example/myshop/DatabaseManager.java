package com.example.myshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:myshop.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fullname TEXT, " +
                "email TEXT UNIQUE, " +
                "username TEXT UNIQUE, " +
                "password TEXT);";

        String createProductsTable = "CREATE TABLE IF NOT EXISTS products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT UNIQUE, " + // Name is unique to prevent duplicate items
                "price REAL NOT NULL, " +
                "stock INTEGER DEFAULT 0);";

        String createBillsTable = "CREATE TABLE IF NOT EXISTS bills (" +
                "bill_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customer_name TEXT, " +
                "total_amount REAL, " +
                "sale_date DATETIME DEFAULT CURRENT_TIMESTAMP);";

        String createBillItemsTable = "CREATE TABLE IF NOT EXISTS bill_items (" +
                "item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "bill_id INTEGER, " +
                "product_name TEXT, " +
                "quantity INTEGER, " +
                "unit_price REAL, " +
                "subtotal REAL, " +
                "FOREIGN KEY (bill_id) REFERENCES bills(bill_id));";



        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.execute(createUsersTable);
            stmt.execute(createProductsTable);
            stmt.execute(createBillsTable);
            stmt.execute(createBillItemsTable);

            System.out.println("Database initialization complete: All tables ready.");

        } catch (SQLException e) {
            System.err.println("Database Initialization Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}