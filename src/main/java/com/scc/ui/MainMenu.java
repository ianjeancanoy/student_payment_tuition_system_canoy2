package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.Optional;
import java.util.Scanner;

/** Console menu screens. */
public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final UserDao userDao = new UserDao();

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
        System.out.println("Select role: 1. TEACHER  2. STUDENT");
        System.out.print("Choose: ");
        String role = switch (scanner.nextLine().trim()) {
            case "1" -> "TEACHER";
            case "2" -> "STUDENT";
            default -> null;
        };
        if (role == null) {
            System.out.println("Invalid role.");
            return;
        }
        if (userDao.register(username, password, role)) {
            System.out.println("Registered successfully! Status: PENDING");
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

        Optional<User> user = userDao.login(username, password);
        if (user.isPresent()) {
            userMenu(user.get());
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private void userMenu(User user) {
        System.out.println("\nWelcome, " + user.username() + "! Role: " + user.roles()
                + " | Status: " + user.status());
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n1. View Profile");
            System.out.println("2. Logout");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> System.out.println("ID: " + user.id() + "\nUsername: " + user.username()
                        + "\nRole: " + user.roles() + "\nStatus: " + user.status());
                case "2" -> {
                    System.out.println("Logged out.");
                    loggedIn = false;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
