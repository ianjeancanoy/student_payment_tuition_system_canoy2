package com.scc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** Makes sure the users table exists before the CLI menu starts. */
public final class DatabaseInitializer {

    private static final String USERS_TABLE = """
            CREATE TABLE IF NOT EXISTS users (
                id       INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                roles    TEXT NOT NULL CHECK (roles IN ('STUDENT', 'CASHIER', 'ADMINISTRATOR')),
                status   TEXT NOT NULL DEFAULT 'PENDING'
            )
            """;

    private DatabaseInitializer() {
    }

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {
            st.execute(USERS_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Database initialization failed: " + e.getMessage(), e);
        }
    }
}
