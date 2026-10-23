# Student Tuition Payment Reservation System (Layered + SQLite)

## Structure
```
student_payment_system_canoy2f/
├── src/main/java/com/scc/
│   ├── dao/
│   │   ├── DatabaseConnection.java   (PRAGMA foreign_keys = ON)
│   │   ├── DatabaseInitializer.java  (creates the users table)
│   │   └── UserDao.java
│   ├── model/
│   │   └── User.java                 (Java Record)
│   ├── ui/
│   │   └── Authentication.java       (register, login, role menus)
│   └── Main.java                     (entry point)
├── src/main/resources/
├── pom.xml
└── studentpaymentsystem.db           (database file in project root)
```

## Run
IntelliJ: open `pom.xml` as a project > Maven tool window > Reload > run `Main`.
Terminal: `mvn compile exec:java`
