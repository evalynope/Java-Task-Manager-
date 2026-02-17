package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection conn = null; // single reusable connection

    // This is the method you call from MainTest or other classes
    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "evalynbassett"; // your postgres username
            String password = "";           // your Postgres.app password (blank if none)

            conn = DriverManager.getConnection(url, user, password);
        }
        return conn;
    }

    // Optional main for testing the connection
    public static void main(String[] args) {
        try {
            Connection testConn = DatabaseConnection.getConnection();
            System.out.println("Connected!");
            testConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
