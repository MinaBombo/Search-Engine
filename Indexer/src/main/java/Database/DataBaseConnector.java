package Database;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnector implements Closeable {
    private Connection connection;
    public DataBaseConnector() throws SQLException {
        String url = "jdbc:postgresql://localhost/SearchEngineDatabase";
        String user = "SearchEngine";
        String password = "root";
        connection = DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close(){
        try{
            connection.close();
        }
        catch (SQLException exception)
        {
            System.err.println("Couldn't Close Connection");
            System.err.println(exception.getStackTrace());
            System.err.println(exception.getSQLState());

        }
    }
}
