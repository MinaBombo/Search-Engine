package Database;


import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector implements Closeable {
    private Connection connection;
    public static final String url = "jdbc:postgresql://localhost/SearchEngineDatabase";
    public static final String user = "SearchEngine";
    public static final String password = "root";
    public DatabaseConnector() throws SQLException {
        connection = ConnectionPool.getInstance().getConnection();
    }

    public Connection getPooledConnection() {
        return connection;
    }
    public Connection getNonPooledConnection() throws  SQLException{
            return DriverManager.getConnection(url,user,password);
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
