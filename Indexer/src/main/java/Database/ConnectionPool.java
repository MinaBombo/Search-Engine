package Database;

import Indexer.DynamicIndexer;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private BasicDataSource dataSource;


    private static ConnectionPool ourInstance = new ConnectionPool();

    public static ConnectionPool getInstance() {
        return ourInstance;
    }

    private ConnectionPool() {
        String url = "jdbc:postgresql://localhost/SearchEngineDatabase";
        String user = "SearchEngine";
        String password = "root";
        dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setInitialSize(DynamicIndexer.numThreads+1);
    }
    public Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }
}
