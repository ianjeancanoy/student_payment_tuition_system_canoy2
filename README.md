# Student Tuition Payment Reservation System (Layered + SQLite)

## Structure
```
student_payment_system_canoy2f/
├── src/main/java/com/scc/
│   ├── dao/
│   │   ├── DatabaseConnection.java   (PRAGMA foreign_keys = ON)
│   │   ├── DatabaseInitializer.java
│   │   └── UserDao.java              (getAllUsers, getPendingUsers, approveUser)
│   ├── model/
│   │   └── User.java                 (Java Record)
│   ├── ui/
│   │   ├── AdminMenu.java            [NEW]
│   │   ├── Authentication.java
│   │   ├── StudentMenu.java          [NEW]
│   │   └── CashierMenu.java          [NEW]
│   └── Main.java
├── src/main/resources/
├── pom.xml
└── studentpaymentsystem.db
```

## Run
IntelliJ: open `pom.xml` as a project > Maven tool window > Reload > run `Main`.
Terminal: `mvn compile exec:java`
