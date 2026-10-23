package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.List;
import java.util.Scanner;

/** Menu for users with the ADMINISTRATOR role. No SQL here; it only calls UserDao. */
public class AdminMenu {

    // column widths for the table helper
    private static final int W_ID = 5;
    private static final int W_USERNAME = 15;
    private static final int W_ROLE = 14;
    private static final int W_STATUS = 10;

    private final Scanner scanner;
    private final UserDao userDao;

    public AdminMenu(Scanner scanner, UserDao userDao) {
        this.scanner = scanner;
        this.userDao = userDao;
    }

    public void show(User admin) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- Administrator Menu (" + admin.username() + ") ---");
            System.out.println("1. View All System Users");
            System.out.println("2. View Pending Registrations");
            System.out.println("3. Approve User Account");
            System.out.println("4. Logout");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> printUserTable("ALL SYSTEM USERS", userDao.getAllUsers());
                case "2" -> printUserTable("PENDING REGISTRATIONS", userDao.getPendingUsers());
                case "3" -> approveUserAccount();
                case "4" -> {
                    System.out.println("Logged out.");
                    loggedIn = false;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    /** Shows pending accounts, asks for a target ID (0 cancels), checks it is pending, then approves. */
    private void approveUserAccount() {
        List<User> pending = userDao.getPendingUsers();
        if (pending.isEmpty()) {
            System.out.println("\nNo pending registrations to approve.");
            return;
        }
        printUserTable("PENDING REGISTRATIONS", pending);

        while (true) {
            System.out.print("Enter Target User ID to approve (0 to cancel): ");
            Integer targetId = parseId(scanner.nextLine().trim());
            if (targetId == null) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            if (targetId == 0) {
                System.out.println("Approval cancelled.");
                return;
            }

            User target = null;
            for (User u : pending) {
                if (u.id() == targetId) {
                    target = u;
                    break;
                }
            }
            if (target == null) {
                System.out.println("ID " + targetId + " is not in the pending list. Try again.");
                continue;
            }

            if (userDao.approveUser(target.id())) {
                System.out.println("SUCCESS: User ID " + target.id() + " (" + target.username()
                        + ", " + target.roles() + ") status changed from PENDING to APPROVED.");
            } else {
                System.out.println("Approval failed. Please try again.");
            }
            return;
        }
    }

    // ------------------------------------------------- table rendering helper

    /** Prints any list of users as an aligned table with borders. */
    private void printUserTable(String title, List<User> users) {
        String border = border();
        System.out.println("\n" + title);
        System.out.println(border);
        System.out.println(row("ID", "USERNAME", "ROLE", "STATUS"));
        System.out.println(border);
        if (users.isEmpty()) {
            System.out.println(row("-", "(none)", "-", "-"));
        }
        for (User u : users) {
            System.out.println(row(String.valueOf(u.id()), u.username(), u.roles(), u.status()));
        }
        System.out.println(border);
        System.out.println("Total: " + users.size());
    }

    private String row(String id, String username, String role, String status) {
        return String.format("| %-5s | %-15s | %-14s | %-10s |",
                fit(id, W_ID), fit(username, W_USERNAME), fit(role, W_ROLE), fit(status, W_STATUS));
    }

    private String border() {
        return "+-" + "-".repeat(W_ID) + "-+-" + "-".repeat(W_USERNAME) + "-+-"
                + "-".repeat(W_ROLE) + "-+-" + "-".repeat(W_STATUS) + "-+";
    }

    /** Cuts text that is too long so the columns stay aligned. */
    private String fit(String text, int width) {
        if (text == null) {
            return "";
        }
        return text.length() <= width ? text : text.substring(0, width - 3) + "...";
    }

    private Integer parseId(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
