package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.Optional;
import java.util.Scanner;

/**
 * Handles register, login and role routing.
 * The same Scanner and UserDao instances are passed to every menu.
 * No SQL lives here; all database work goes through UserDao.
 */
public class Authentication {

    private final Scanner scanner;
    private final UserDao userDao;

    private final StudentMenu studentMenu;
    private final CashierMenu cashierMenu;
    private final AdminMenu adminMenu;

    public Authentication(Scanner scanner, UserDao userDao) {
        this.scanner = scanner;
        this.userDao = userDao;
        this.studentMenu = new StudentMenu(scanner, userDao);
        this.cashierMenu = new CashierMenu(scanner, userDao);
        this.adminMenu = new AdminMenu(scanner, userDao);
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
            case "STUDENT" -> studentMenu.show(user);
            case "CASHIER" -> cashierMenu.show(user);
            case "ADMINISTRATOR" -> adminMenu.show(user);
            default -> System.out.println("Unknown role: " + user.roles());
        }
    }
}
