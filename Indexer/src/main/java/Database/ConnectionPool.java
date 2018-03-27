package Database;

import Indexer.DynamicIndexer;
import org.apache.commons.dbcp2.BasicDataSource;
import org.postgresql.core.BaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool implements AutoCloseable {
    private BasicDataSource dataSource;


    private static ConnectionPool ourInstance = new ConnectionPool();

    public static ConnectionPool getInstance() {
        return ourInstance;
    }

    private ConnectionPool() {
        dataSource = new BasicDataSource();
        dataSource.setUrl(DatabaseConnector.url);
        dataSource.setUsername(DatabaseConnector.user);
        dataSource.setPassword(DatabaseConnector.password);
        dataSource.setInitialSize(DynamicIndexer.numThreads+1);
    }
    public Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }

    @Override
    public void close() throws Exception {
        dataSource.close();
    }
}
