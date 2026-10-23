# Student Payment System (Layered + SQLite)

## Structure
- `com.scc.dao` - DatabaseConnection, UserDao (SQL)
- `com.scc.model` - User record
- `com.scc.ui` - MainMenu (console screens)
- `com.scc.Main` - entry point

## Run
IntelliJ: open `pom.xml` as a project > Maven tool window > Reload > run `Main`.
Terminal: `mvn compile exec:java`

The database file `studentpaymentsystem.db` is created in the project root on first run.

## View the DB in IntelliJ
View > Tool Windows > Database > + > Data Source > SQLite > pick `studentpaymentsystem.db` > download driver if prompted.
