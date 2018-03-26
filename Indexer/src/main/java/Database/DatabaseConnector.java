package Database;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector implements Closeable {
    private Connection connection;
    public DatabaseConnector() throws SQLException {
//        String url = "jdbc:postgresql://localhost/SearchEngineDatabase";
//        String user = "SearchEngine";
//        String password = "root";
//        connection = DriverManager.getConnection(url, user, password);
        connection = ConnectionPool.getInstance().getConnection();
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
            exception.printStackTrace();
            System.err.println(exception.getSQLState());

        }
    }
}
