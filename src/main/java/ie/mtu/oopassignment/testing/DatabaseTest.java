package ie.mtu.oopassignment.testing;

import ie.mtu.oopassignment.model.Database;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.testng.Assert.assertNotNull;

public class DatabaseTest {

    private static Connection conn;

    @Before
    public void setup() throws SQLException, ClassNotFoundException {
        conn = Database.getConnection();
    }

    @After
    public void cleanup() throws SQLException {
        Database.closeConnection();
    }

    @Test
    public void testGetConnection() throws SQLException, ClassNotFoundException {
        // close the connection to simulate a connection failure
        conn.close();
        // wait for 10 seconds to allow the getConnection() method to retry the connection
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // try to get the connection again
        conn = Database.getConnection();
        assertNotNull(conn, "Connection should not be null");
    }
}
