package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Handles register, login and role routing.
 * The same Scanner and UserDao instances are used by every menu.
 * No SQL lives here; all database work goes through UserDao.
 */
public class Authentication {

    private final Scanner scanner;
    private final UserDao userDao;

    public Authentication(Scanner scanner, UserDao userDao) {
        this.scanner = scanner;
        this.userDao = userDao;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Student Tuition Payment Reservation System ===");
            System.out.println("1. Register Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ------------------------------------------------------------ register

    private void register() {
        System.out.println("\n--- Register ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }
        if (userDao.usernameExists(username)) {
            System.out.println("Username already taken.");
            return;
        }
        System.out.print("Password: ");
        String password = scanner.nextLine();
        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }

        System.out.println("Select role:");
        System.out.println("1. STUDENT");
        System.out.println("2. CASHIER");
        System.out.println("3. ADMINISTRATOR");
        System.out.print("Choose: ");
        String role = switch (scanner.nextLine().trim()) {
            case "1" -> "STUDENT";
            case "2" -> "CASHIER";
            case "3" -> "ADMINISTRATOR";
            default -> null;
        };
        if (role == null) {
            System.out.println("Invalid role.");
            return;
        }

        if (userDao.register(username, password, role)) {
            System.out.println("Registered successfully! Role: " + role + " | Status: PENDING");
        } else {
            System.out.println("Registration failed.");
        }
    }

    // --------------------------------------------------------------- login

    private void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<User> found = userDao.login(username, password);
        if (found.isEmpty()) {
            System.out.println("Invalid username or password.");
            return;
        }

        User user = found.get();
        System.out.println("\nWelcome, " + user.username() + "! Role: " + user.roles()
                + " | Status: " + user.status());

        // route to the menu that matches the user's role
        switch (user.roles()) {
            case "STUDENT" -> studentMenu(user);
            case "CASHIER" -> cashierMenu(user);
            case "ADMINISTRATOR" -> adminMenu(user);
            default -> System.out.println("Unknown role: " + user.roles());
        }
    }

    // ----------------------------------------------------- role-based menus

    private void studentMenu(User user) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View Profile");
            System.out.println("2. Logout");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> printProfile(user);
                case "2" -> loggedIn = logout();
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void cashierMenu(User user) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- Cashier Menu ---");
            System.out.println("1. View Profile");
            System.out.println("2. Logout");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> printProfile(user);
                case "2" -> loggedIn = logout();
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void adminMenu(User user) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- Administrator Menu ---");
            System.out.println("1. View All Accounts");
            System.out.println("2. Approve Pending Account");
            System.out.println("3. Logout");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> printUsers(userDao.findAll());
                case "2" -> approveAccount();
                case "3" -> loggedIn = logout();
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void approveAccount() {
        List<User> pending = userDao.findPending();
        if (pending.isEmpty()) {
            System.out.println("No pending accounts.");
            return;
        }
        printUsers(pending);
        System.out.print("Enter the ID to approve: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            if (userDao.approve(id)) {
                System.out.println("Account approved (status ACTIVE).");
            } else {
                System.out.println("Could not approve that account.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    // ------------------------------------------------------------- helpers

    private boolean logout() {
        System.out.println("Logged out.");
        return false; // ends the menu loop
    }

    private void printProfile(User user) {
        System.out.println("ID: " + user.id() + "\nUsername: " + user.username()
                + "\nRole: " + user.roles() + "\nStatus: " + user.status());
    }

    private void printUsers(List<User> users) {
        System.out.printf("%-5s %-20s %-15s %-10s%n", "ID", "USERNAME", "ROLE", "STATUS");
        for (User u : users) {
            System.out.printf("%-5d %-20s %-15s %-10s%n", u.id(), u.username(), u.roles(), u.status());
        }
    }
}
