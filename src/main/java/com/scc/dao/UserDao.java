package com.scc.dao;

import com.scc.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

/** All SQL for the users table. */
public class UserDao {

    private static final String COLUMNS = "id, username, password, roles, status";

    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Saves a new user with the chosen role and default status PENDING. */
    public boolean register(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, roles, status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash(password));
            ps.setString(3, role);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    /** Verifies username and password against the database. */
    public Optional<User> login(String username, String password) {
        String sql = "SELECT " + COLUMNS + " FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<User> findAll() {
        return query("SELECT " + COLUMNS + " FROM users ORDER BY id");
    }

    public List<User> findPending() {
        return query("SELECT " + COLUMNS + " FROM users WHERE status = 'PENDING' ORDER BY id");
    }

    /** Sets an account's status to ACTIVE. */
    public boolean approve(int userId) {
        String sql = "UPDATE users SET status = 'ACTIVE' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    private List<User> query(String sql) {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    private User map(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                rs.getString("roles"), rs.getString("status"));
    }

    private static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
