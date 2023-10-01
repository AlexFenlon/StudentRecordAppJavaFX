package ie.mtu.oopassignment.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    static String dbPath = System.getProperty("user.dir") + "/sqlite/OOPdb.db";
    private static Connection conn = null;

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:" + dbPath;
        while (true) {
            try {
                if (conn == null || conn.isClosed()) {
                    conn = DriverManager.getConnection(url);
                }
                return conn;
            } catch (SQLException sqle) {
                System.err.println("Error connecting to database. Retrying in 5 seconds...");
                try {
                    Thread.sleep(5000); // wait for 5 seconds before trying again
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void closeConnection()  {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException  sqle) {
            System.err.println("Error closing connection");
            sqle.printStackTrace();
        }
    }


}
