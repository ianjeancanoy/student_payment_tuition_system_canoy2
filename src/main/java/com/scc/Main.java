package com.scc;

import com.scc.dao.DatabaseInitializer;
import com.scc.dao.UserDao;
import com.scc.ui.Authentication;

import java.util.Scanner;

/** Single entry point: initialize database -> create DAO and Scanner -> hand over to Authentication. */
public class Main {

    public static void main(String[] args) {
        // 1. Initialize database (creates tables if missing)
        DatabaseInitializer.initialize();

        // 2. Instantiate the shared DAO and utilities (one instance each)
        Scanner scanner = new Scanner(System.in);
        UserDao userDao = new UserDao();

        // 3. Delegate execution to Authentication
        new Authentication(scanner, userDao).start();

        scanner.close();
    }
}
