package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.Scanner;

/** Menu for users with the STUDENT role. */
public class StudentMenu {

    private final Scanner scanner;
    private final UserDao userDao;

    public StudentMenu(Scanner scanner, UserDao userDao) {
        this.scanner = scanner;
        this.userDao = userDao;
    }

    public void show(User user) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View Profile");
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
