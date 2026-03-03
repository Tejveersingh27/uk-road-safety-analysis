import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    // ⚠️ IMPORTANT: Change 'luongh2' to your actual schema/username if different
    // This variable helps us prefix tables automatically later
    public static final String SCHEMA = "cs3380.luongh2"; 

    public static Connection getConnection() {
        Properties prop = new Properties();
        String fileName = "auth.cfg";
        
        try (FileInputStream configFile = new FileInputStream(fileName)) {
            prop.load(configFile);
        } catch (IOException ex) {
            System.out.println("❌ Error: Could not find auth.cfg");
            return null;
        }

        String username = prop.getProperty("username");
        String password = prop.getProperty("password");

        // Connection String for Uranium
        String connectionUrl = "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                + "database=cs3380;"
                + "user=" + username + ";"
                + "password=" + password + ";"
                + "encrypt=true;"
                + "trustServerCertificate=true;"
                + "loginTimeout=30;";

        try {
            // Load the driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(connectionUrl);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}