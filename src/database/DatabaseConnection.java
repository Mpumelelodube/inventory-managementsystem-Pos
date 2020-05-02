package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String REAPER_SQLITE = "jdbc:sqlite:reaper.sqlite";

    public static Connection connection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("connected");

            return DriverManager.getConnection(REAPER_SQLITE);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(" not connected");

        return null;
    }
}
