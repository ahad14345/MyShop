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
                "name TEXT, " +
                "price REAL, " +
                "stock INTEGER DEFAULT 0);";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createProductsTable);

            System.out.println("Database initialization complete: 'myshop.db' is ready.");

        } catch (SQLException e) {
            System.err.println("Database Initialization Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}