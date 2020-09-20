package student.adventure;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private final static String DATABASE_URL = "jdbc:sqlite:src/main/resources/adventure.db";
    private final Connection dbConnection = DriverManager.getConnection();
}
